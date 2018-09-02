package com.zwonline.top28.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.IndustryAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.presenter.Settingpresenter;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ISettingView;

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

import butterknife.OnClick;

/**
 * 描述：个人设置页
 *
 * @author YSG
 * @date 2017/12/19
 */
public class SettingActivity extends BaseActivity<ISettingView, Settingpresenter> implements ISettingView {

    private SharedPreferencesUtils sp;
    private int sex;
    private List<IndustryBean.DataBean> industry_list;
    private String cate_id;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int SELECT_PIC_KITKAT = 3;
    protected Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private BufferedOutputStream bos;
    //    private String data;
    private String sex_cn;
    private int count;
    private LinearLayout amendPicture;
    private Spinner spinnerSex;
    private Spinner spinnerIndustry;
    private Button btnSetting;
    private EditText nickName;
    private EditText realName;
    private EditText age;
    private EditText address;
    private EditText bio;
    private ImageViewPlus imagHead;
    private RelativeLayout back;
    private Uri mUritempFile;

    @Override
    protected void init() {
        initView();
        sp = SharedPreferencesUtils.getUtil();
        presenter.mUserInfo(this);
//        String avatar = (String) sp.getKey(SettingActivity.this, "avatar", "");
        Intent intent = getIntent();
        presenter.mIndustryBean(getApplicationContext());
        industry_list = new ArrayList<>();
        spinnerDate();

    }

    private void initView() {
        amendPicture = (LinearLayout) findViewById(R.id.amend_picture);
        spinnerSex = (Spinner) findViewById(R.id.spinner_sex);
        spinnerIndustry = (Spinner) findViewById(R.id.spinner_industry);
        btnSetting = (Button) findViewById(R.id.btn_setting);
        nickName = (EditText) findViewById(R.id.nick_name);
        realName = (EditText) findViewById(R.id.real_name);
        age = (EditText) findViewById(R.id.age);
        address = (EditText) findViewById(R.id.address);
        bio = (EditText) findViewById(R.id.bio);
        imagHead = (ImageViewPlus) findViewById(R.id.imag_head);
        back = (RelativeLayout) findViewById(R.id.back);
    }

    @Override
    protected Settingpresenter getPresenter() {
        return new Settingpresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }


    //展示感兴趣的行业
    @Override
    public void showIndustry(final List<IndustryBean.DataBean> beanList) {
        IndustryAdapter adapter = new IndustryAdapter(beanList, this);
        if (adapter != null) {
            boolean b = spinnerIndustry == null;
            spinnerIndustry.setAdapter(adapter);
        } else {
            ToastUtils.showToast(this, getString(R.string.network_error));
        }
        spinnerIndustry.setPrompt(getString(R.string.center_car));
        spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cate_id = beanList.get(position).cate_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 上传头像
     *
     * @param headBean
     */
    @Override
    public void showSettingHead(HeadBean headBean) {
        if (headBean.status == 1) {
            Toast.makeText(getApplicationContext(), R.string.update_suc_tip, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.update_fail_tip, Toast.LENGTH_SHORT).show();
        }
    }

    //获取用户信息
    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean.status == 1) {
            boolean b = userInfoBean == null;
            boolean b1 = age == null;
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                    .error(R.mipmap.no_photo_male);
            Glide.with(getApplicationContext()).load(userInfoBean.data.user.avatar).apply(options).into(imagHead);
            age.setText(userInfoBean.data.user.age);
            nickName.setText(userInfoBean.data.user.nickname);
            bio.setText(userInfoBean.data.user.signature);
            realName.setText(userInfoBean.data.user.realname);
            address.setText(userInfoBean.data.user.residence);
            presenter.mIndustryBean(this);
            sex_cn = userInfoBean.data.user.sex;
            String catePid = userInfoBean.data.user.cate_pid;
            if (StringUtil.isNotEmpty(sex_cn)) {
                spinnerSex.setSelection(Integer.parseInt(sex_cn) - 1, true);
            } else {
                spinnerSex.setSelection(0, true);
            }
        } else if (userInfoBean.status == -1) {
            sp.insertKey(getApplicationContext(), "islogin", false);
            sp.insertKey(getApplicationContext(), "isUpdata", false);
            RecordUserBehavior.recordUserBehavior(getApplicationContext(), BizConstant.SIGN_OUT);
            startActivity(new Intent(getApplicationContext(), WithoutCodeLoginActivity.class));
            SharedPreferences settings = getApplicationContext().getSharedPreferences("SP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("diaog");
            editor.remove("avatar");
            editor.remove("uid");
            editor.remove("nickname");
            editor.clear();
            editor.commit();
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            ToastUtils.showToast(getApplicationContext(), "登录异常，重新登录！");
        } else {
            ToastUtils.showToast(getApplicationContext(), userInfoBean.msg);
        }
    }

    @Override
    public void onErro() {
        if (this == null) {
            return;
        }
        ToastUtils.showToast(this, getString(R.string.data_loading));
    }


    //设置性别
    private void spinnerDate() {

        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);

        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @OnClick({R.id.amend_picture, R.id.btn_setting, R.id.back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.amend_picture:
                showChoosePicDialog();
                break;
            case R.id.btn_setting:
                presenter.mSetting(getApplicationContext(), nickName.getText().toString().trim(),
                        realName.getText().toString().trim(), sex, age.getText().toString().trim(),
                        address.getText().toString().trim(), cate_id, bio.getText().toString().trim());
                RecordUserBehavior.recordUserBehavior(SettingActivity.this, BizConstant.EDITED_PROFILE);

//                Intent intent = new Intent();
//                intent.putExtra("nickname", nickName.getText().toString().trim());
//                intent.putExtra("avatar", data);
//                // 设置结果，并进行传送
//                this.setResult(0, intent);

                break;
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    //判断是否保存成功
    @Override
    public void showSetting(SettingBean headBean) {
//        data = headBean.data;
        if (headBean.status == 1) {
            sp.insertKey(getApplicationContext(), "nickname", nickName.getText().toString().trim());
            sp.insertKey(getApplicationContext(), "sign", bio.getText().toString().trim());
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            Toast.makeText(this, R.string.update_suc_tip, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, headBean.msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断用户是否保存成功
     */
    @Override
    public void isSucceed() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        Toast.makeText(this, R.string.update_suc_tip, Toast.LENGTH_SHORT).show();
    }


    /**
     * 显示修改头像的对话框
     */
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.center_set_photo);
//        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton(R.string.common_cancel, null);
        builder.setItems(R.array.photo_step, new DialogInterface.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        openAlbumIntent.setAction(Intent.ACTION_PICK);
                        openAlbumIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//使用以上这种模式，并添加以上两句
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);

                        break;
                    case TAKE_PICTURE: // 拍照
                        if (Build.VERSION.SDK_INT >= 23) {
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                                return;
                            } else {

                                Intent openCameraIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                tempUri = Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), "image.jpg"));
                                // 将拍照所得的相片保存到SD卡根目录
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                            }
                        }else {
                            Intent openCameraIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            tempUri = Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), "image.jpg"));
                            // 将 拍照所得的相片保存到SD卡根目录
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                            startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        }


                        break;
                }
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    cropRawPhoto(tempUri);
                    break;

                case CHOOSE_PICTURE:
                    cropRawPhoto(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        //将Uri图片转换为Bitmap
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(mUritempFile));
                            //TODO，将裁剪的bitmap显示在imageview控件上
                            imagHead.setImageBitmap(bitmap);
                            sp.insertKey(getApplicationContext(), "avatar", mUritempFile);
//                            compressImage(bitmap);
                            File file = compressImage(bitmap);
                            presenter.mSettingHead(this, file);//上传图片
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
//                    setImageToHeadView(intent);
                    } else {
                        Log.e("ffl", "onActivityResult: -------------intent为null------------");
                    }
                    break;
            }
        }
    }


    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imagHead.setImageBitmap(photo);

//            uploadPic(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, R.string.get_storage_perm, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
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

}