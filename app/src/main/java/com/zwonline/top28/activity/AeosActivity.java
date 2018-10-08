package com.zwonline.top28.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ta.utdid2.android.utils.StringUtils;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.IndustryAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.EnterpriseStatusBean;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.AeoPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.BitmapUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IAeoActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：认证企业
 *
 * @author YSG
 * @date 2017/12/21
 */
public class AeosActivity extends BaseActivity<IAeoActivity, AeoPresenter> implements IAeoActivity {


    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.enterprise_name)
    EditText enterpriseName;
    @BindView(R.id.company_name)
    EditText companyName;
    @BindView(R.id.signs)
    EditText signs;
    @BindView(R.id.spinner_type)
    Spinner spinnerType;
    @BindView(R.id.enterprise_contacts)
    EditText enterpriseContacts;
    @BindView(R.id.enterprise_contact_tel)
    EditText enterpriseContactTel;
    @BindView(R.id.enterprise_contact_address)
    EditText enterpriseContactAddress;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.add_aeo)
    ImageView addAeo;
    @BindView(R.id.add_btn_layout)
    RelativeLayout addBtnLayout;
    @BindView(R.id.aeo_linear)
    LinearLayout aeoLinear;
    @BindView(R.id.aeo_image)
    LinearLayout aeoImage;
    @BindView(R.id.complete)
    Button complete;
    private String cate_id;
    private int num = 0;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int SELECT_PIC_KITKAT = 3;
    protected  Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private BufferedOutputStream bos;
    private SharedPreferencesUtils sp;
    private List<String> addPics;
    private EnterpriseStatusBean.DataBean enterData;
    private String checkStatus;
    private IndustryAdapter industryAdapter;
    private LinearLayout aeoImageLinear;
    private TextView aeoStatusTv;
    private String project;
    private Uri mUritempFile;

    @Override
    protected void init() {
        aeoStatusTv = (TextView) findViewById(R.id.aeo_status_tv);
        aeoImageLinear = (LinearLayout) findViewById(R.id.aeo_image);
        addPics = new ArrayList<>();
        Intent intent = getIntent();
        //判断是从项目列表过来还是个人中心
        project = intent.getStringExtra("project");
        presenter.mAeoClass(AeosActivity.this);
        checkStatus = intent.getStringExtra("checkStatus");
        if (StringUtil.isNotEmpty(project)&&project.equals(BizConstant.ALIPAY_METHOD)){
            if (StringUtil.isNotEmpty(intent.getStringExtra("uid"))){
                presenter.showMyProjectList(this, intent.getStringExtra("uid"));
            }
        }else if (StringUtil.isNotEmpty(project)&&project.equals(BizConstant.ALREADY_FAVORITE)){

            if (StringUtil.isNotEmpty(checkStatus)&& StringUtil.isNotEmpty(intent.getStringExtra("projectId"))) {
                String projectId = intent.getStringExtra("projectId");
                presenter.getEnterpriseDetail(AeosActivity.this, projectId);
            }else {
                Log.d("top","没有申请过");
            }
        }

    }

    @Override
    protected AeoPresenter getPresenter() {
        return new AeoPresenter(AeosActivity.this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_aeos;
    }

    @Override
    public void showAeo(HeadBean headBean) {
        if (headBean.status == 1) {
            Toast.makeText(this, getString(R.string.enterprise_apply_commit), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ToastUtils.showToast(this, headBean.msg);
        }

    }

    /**
     * 感兴趣的行业
     * @param classList
     */
    @Override
    public void showAeoClass(final List<IndustryBean.DataBean> classList) {
        industryAdapter = new IndustryAdapter(classList, AeosActivity.this);
        spinnerType.setAdapter(industryAdapter);
//        spinnerType.setPrompt("餐饮小吃");
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate_id = classList.get(position).cate_id;
//                Toast.makeText(AeosActivity.this, "cate_id=" + classList.get(position).cate_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showAeoImage(PicturBean headBean) {
        addPics.add(headBean.url);
    }


    @OnClick({R.id.add_aeo, R.id.complete, R.id.back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.add_aeo:
                showChoosePicDialog();
                break;
            case R.id.complete:
                onclickComplete();
                RecordUserBehavior.recordUserBehavior(AeosActivity.this, BizConstant.CLICK_ENTERPRISE_AUTH);
                break;
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加图片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
//                        Intent openAlbumIntent = new Intent(
//                                Intent.ACTION_GET_CONTENT);
//                        openAlbumIntent.setType("image/*");
//                        openAlbumIntent.setAction(Intent.ACTION_PICK);
//                        openAlbumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//使用以上这种模式，并添加以上两句
//                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        pickPhoto();
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//                        try {
//                            tempUri = TakePhotoUtils.takePhoto(AeosActivity.this, TAKE_PICTURE);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        break;
                }
            }
        });
        builder.show();
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent openAlbumIntent = new Intent(
                Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setType("image/*");
        openAlbumIntent.setAction(Intent.ACTION_PICK);
        openAlbumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//使用以上这种模式，并添加以上两句
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            Uri photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PICTURE);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//         outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    /**
     * 裁剪过之后
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
//                    cutImage(tempUri); // 对图片进行裁剪处理
//                    doPhoto(requestCode, tempUri);
                    cropRawPhoto(tempUri);
                    break;
                case CHOOSE_PICTURE:
//                    cutImage(data.getData()); // 对图片进行裁剪处理
//                    doPhoto(requestCode, data.getData());
                    cropRawPhoto(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        //setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(mUritempFile));
                            settingImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
            }
        }
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param photoUri
     */
    private void doPhoto(int requestCode, Uri photoUri) {
        // 从相册取图片，有些手机有异常情况，请注意
        if (requestCode == CHOOSE_PICTURE) {
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
//            Uri photoUri = data.getData();
//            if (photoUri == null) {
//                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
//                return;
//            }

            ContentResolver resolver = getContentResolver();
            try {
                // 获取圖片URI
                String picPath = "";
                // 将URI转换为路径：
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(photoUri, proj, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(proj[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                    // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
                    if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                        cursor.close();
                    }
                }

                // 如果图片符合要求将其上传到服务器
                if (picPath != null && (picPath.endsWith(".png") ||
                        picPath.endsWith(".PNG") ||
                        picPath.endsWith(".jpg") ||
                        picPath.endsWith(".JPG"))) {

                    // 这个是获得用户选择的图片的索引值
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    // 最后根据索引值获取图片路径
                    String photoPath = cursor.getString(column_index);
                    // 压缩成800*480
                    Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(photoPath, 800, 480);
                    settingImage(bitmap);
                } else {
                    Toast.makeText(this, "选择图片文件格式不正确", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == TAKE_PICTURE) {

            // 压缩成800*480
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeFile(tempUri.getPath(), getOptions(tempUri.getPath()));

            //            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(photoUri.getPath(), 800, 480);
            settingImage(bitmap);
        }
    }

    /**
     * 获取压缩图片的options
     *
     * @return
     */
    public static BitmapFactory.Options getOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 4;      //此项参数可以根据需求进行计算
        options.inJustDecodeBounds = false;

        return options;
    }

    /**
     * 上传图片
     *
     * @param bitmap
     */
    protected void settingImage(Bitmap bitmap) {
        if (bitmap != null) {
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            num++;
            if (num == 1) {
                image1.setVisibility(View.VISIBLE);
                image1.setImageBitmap(bitmap);
            } else if (num == 2) {
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image2.setImageBitmap(bitmap);
            } else {
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image3.setImageBitmap(bitmap);
                addBtnLayout.setVisibility(View.GONE);
            }
            //将Bitmap质量压缩写入File文件中
            File file = compressImage(bitmap);
            presenter.mSettingHead(this, file, BizConstant.BUSINESS_LOGIN);
        }
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            num++;
            if (num == 1) {
                image1.setVisibility(View.VISIBLE);
                image1.setImageBitmap(mBitmap);
            } else if (num == 2) {
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image2.setImageBitmap(mBitmap);
            } else {
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image3.setImageBitmap(mBitmap);
                addBtnLayout.setVisibility(View.GONE);
            }
            //将Bitmap质量压缩写入File文件中
            File file = compressImage(mBitmap);
            presenter.mSettingHead(this, file, BizConstant.BUSINESS_LOGIN);
        }
    }
    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

//        // 设置裁剪
//        intent.putExtra("crop", "true");
//
//        // aspectX , aspectY :宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//
//        // outputX , outputY : 裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);      //原本的裁剪方式

        //uritempFile为Uri类变量，实例化uritempFile，转化为uri方式解决问题
        mUritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    /**
     * 压缩图片（质量压缩）
     *
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //recycleBitmap(bitmap);
        return file;
    }

    //释放
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

    /**
     * 获取认证状态
     * @param enterpriseData
     */
    @Override
    public void getEnterpriseDetail(EnterpriseStatusBean.DataBean enterpriseData) {
        enterData = enterpriseData;
        setIsDisable(enterpriseName, false);
        setIsDisable(companyName, false);
        setIsDisable(enterpriseContacts, false);
        setIsDisable(enterpriseContactTel, false);
        setIsDisable(enterpriseContactAddress, false);
        setIsDisable(signs, false);
        spinnerType.setFocusable(false);
        enterpriseName.setText(enterpriseData.company_name);
        companyName.setText(enterpriseData.company_name);
        enterpriseContacts.setText(enterpriseData.enterprise_contacts);
        enterpriseContactTel.setText(enterpriseData.enterprise_contact_tel);
        signs.setText(enterpriseData.sign);
        enterpriseContactAddress.setText(enterpriseData.enterprise_contact_address);
        String check_status = enterpriseData.check_status;
        if (BizConstant.PROJECT_FAIL_CHECK_STATUS.equals(check_status)) { //审核拒绝
            complete.setText(R.string.project_apply_again);
            complete.setVisibility(View.VISIBLE);
            complete.setFocusable(true);
            aeoStatusTv.setVisibility(View.VISIBLE);
            aeoStatusTv.setText("审核结果未通过原因：" + enterData.check_comment);
            addImageDynamically();
        } else {
            aeoStatusTv.setVisibility(View.VISIBLE);
            aeoStatusTv.setText("认证申请已在[" + enterData.add_time + "]提交请耐心等待审核结果");
            complete.setVisibility(View.GONE);
            addImageDynamically();
        }


    }

    @Override
    public void initEnterpriseDetail() {
        if (!StringUtils.isEmpty(checkStatus)) {
//            if (BizConstant.PROJECT_BEGIN_CHECK_STATUS.equals(checkStatus)) {  //审核中
            setWidget(enterData.enterprise_name, enterData.company_name, enterData.enterprise_contacts,
                    enterData.enterprise_contact_tel, enterData.enterprise_contact_address,
                    enterData.sign, enterData.cate_id);
//            } else if (BizConstant.PROJECT_SUC_CHECK_STATUS.equals(checkStatus)) { //审核通过
//
//            } else if (BizConstant.PROJECT_FAIL_CHECK_STATUS.equals(checkStatus)) { //审核拒绝
//
//            } else if (BizConstant.PROJECT_CANCEL_CHECK_STATUS.equals(checkStatus)) { //注销
//
//            }
        }
    }

    /**
     * 项目列表
     *
     * @param myProjectList
     */
    @Override
    public void showMyProjectList(List<ProjectBean.DataBean> myProjectList) {
        String pid = myProjectList.get(0).id;
        if (StringUtil.isNotEmpty(pid)){
        presenter.getEnterpriseDetail(this, pid);
        }
    }

    /**
     * 设置值
     *
     * @param enterName
     * @param cName
     * @param enterContacts
     * @param enterTel
     * @param enterAddress
     * @param signsStr
     * @param cateId
     */
    void setWidget(String enterName, String cName, String enterContacts, String enterTel,
                   String enterAddress, String signsStr, String cateId) {
        setIsDisable(enterpriseName, false);
        setIsDisable(companyName, false);
        setIsDisable(enterpriseContacts, false);
        setIsDisable(enterpriseContactTel, false);
        setIsDisable(enterpriseContactAddress, false);
        setIsDisable(signs, false);
        spinnerType.setFocusable(false);
        enterpriseName.setText(enterName);
        companyName.setText(cName);
        enterpriseContacts.setText(enterContacts);
        enterpriseContactTel.setText(enterTel);
        signs.setText(signsStr);
        enterpriseContactAddress.setText(enterAddress);
        if (BizConstant.PROJECT_FAIL_CHECK_STATUS.equals(checkStatus)) { //审核拒绝
            complete.setText(R.string.project_apply_again);
            complete.setVisibility(View.VISIBLE);
            complete.setFocusable(true);
            aeoStatusTv.setVisibility(View.VISIBLE);
            aeoStatusTv.setText("审核结果未通过原因：" + enterData.check_comment);
            addImageDynamically();
        } else {
            aeoStatusTv.setVisibility(View.VISIBLE);
            aeoStatusTv.setText("认证申请已在[" + enterData.add_time + "]提交请耐心等待审核结果");
            complete.setVisibility(View.GONE);
            addImageDynamically();
        }

    }

    //审核认证和未通过认证显示申请的图片
    public void addImageDynamically() {

        addBtnLayout.setVisibility(View.GONE);
        //动态添加imageview
        for (int i = 0; i < enterData.enterprise_license.size(); i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
            lp.setMargins(12, 0, 12, 0);
            imageView.setLayoutParams(lp);  //设置图片宽高
            Glide.with(this).load(enterData.enterprise_license.get(i)).into(imageView); //图片资源
            aeoImageLinear.addView(imageView); //动态添加图片
        }
    }

    //点击完成提交企业认证审核
    public void onclickComplete() {
        if (complete.getText().toString().equals(getString(R.string.project_apply_again))) { //审核拒绝
//            finish();
//            startActivity(new Intent(AeosActivity.this, AgainAeoActivity.class));
            setIsDisable(enterpriseName, true);
            setIsDisable(companyName, true);
            setIsDisable(enterpriseContacts, true);
            setIsDisable(enterpriseContactTel, true);
            setIsDisable(enterpriseContactAddress, true);
            spinnerType.setFocusable(true);
            setIsDisable(signs, true);
            addBtnLayout.setVisibility(View.VISIBLE);
            aeoImageLinear.setVisibility(View.GONE);
            complete.setText(R.string.common_btn_complete);
            aeoStatusTv.setVisibility(View.GONE);

        } else {
            if (!StringUtil.isEmpty(enterpriseName.getText().toString())) {
                if (!StringUtil.isEmpty(companyName.getText().toString())) {
                    if (!StringUtil.isEmpty(signs.getText().toString())) {
                        if (!StringUtil.isEmpty(enterpriseContacts.getText().toString())) {
                            if (!StringUtil.isEmpty(enterpriseContactTel.getText().toString())) {
                                if (!StringUtil.isEmpty(enterpriseContactAddress.getText().toString())) {
                                    if (addPics.size() > 0) {
                                        String[] pics = (String[]) addPics.toArray(new String[addPics.size()]);
                                        Log.i("testlog", "pic=" + pics);
                                        presenter.mAeo(AeosActivity.this, enterpriseName.getText().toString().trim(), companyName.getText().toString().trim(),
                                                signs.getText().toString().trim(), cate_id, enterpriseContacts.getText().toString().trim(),
                                                enterpriseContactTel.getText().toString().trim(), enterpriseContactAddress.getText().toString().trim(),
                                                pics);
                                    }
                                } else {
                                    ToastUtils.showToast(this, getString(R.string.enter_enterprise_address));
                                }
                            } else {
                                ToastUtils.showToast(this, getString(R.string.enter_enterprise_contacts_phone));
                            }
                        } else {
                            ToastUtils.showToast(this, getString(R.string.enter_enterprise_contacts));
                        }
                    } else {
                        ToastUtils.showToast(this, getString(R.string.enter_enterprise_sign));
                    }
                } else {
                    ToastUtils.showToast(this, getString(R.string.enter_enterprise_name));
                }
            } else {
                ToastUtils.showToast(this, getString(R.string.enter_enterprise_abstract));
            }

        }
    }

    /**
     * 設置文本是否可用
     *
     * @param editText
     * @param flag
     */
    void setIsDisable(EditText editText, boolean flag) {
        editText.setFocusable(flag);
        editText.setFocusableInTouchMode(flag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (sp != null) {
            sp = null;
        }
    }
}
