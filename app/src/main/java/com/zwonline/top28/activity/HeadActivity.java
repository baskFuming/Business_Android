package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.presenter.Settingpresenter;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ISettingView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：头像设置页
 *
 * @author YSG
 * @date 2017/12/19
 */
public class HeadActivity extends BaseActivity<ISettingView, Settingpresenter> implements View.OnClickListener, ISettingView {

    private ImageViewPlus old_head;
    private ImageView add_head;
    private Button complete_head;
    private Bitmap mBitmap;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int SELECT_PIC_KITKAT = 3;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private BufferedOutputStream bos;
    private SharedPreferencesUtils sp;
    @Override
    protected void init() {
        initView();
    }

    @Override
    protected Settingpresenter getPresenter() {
        return new Settingpresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_head;
    }

    private void initView() {
        sp= SharedPreferencesUtils.getUtil();
        String avatar= (String) sp.getKey(HeadActivity.this,"avatar","");
        old_head = (ImageViewPlus) findViewById(R.id.old_head);
        add_head = (ImageView) findViewById(R.id.add_head);
        complete_head = (Button) findViewById(R.id.complete_head);
        complete_head.setOnClickListener(this);
        add_head.setOnClickListener(this);
        RequestOptions options=new RequestOptions().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(HeadActivity.this).load(avatar).apply(options).into(old_head);
    }

    @Override
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.complete_head:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.add_head:
                showChoosePicDialog();
                break;
        }
    }

    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HeadActivity.this);
        builder.setTitle(R.string.add_photo);
//        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton(R.string.common_cancel, null);
        builder.setItems(R.array.photo_step, new DialogInterface.OnClickListener() {

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
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     */
//    protected void cutImage(Uri uri) {
//        if (uri == null) {
//            Log.i("alanjet", "The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        //com.android.camera.action.CROP这个action是用来裁剪图片用的
//        intent.setDataAndType(uri, "image/*");
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);
//    }
    /**
     * 裁剪图片
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);

        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        Uri uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            old_head.setImageBitmap(mBitmap);//显示图片
            //将Bitmap质量压缩写入File文件中
            File file = compressImage(mBitmap);
            presenter.mSettingHead(this, file);//上传图片
        }
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



    @Override
    public void showIndustry(List<IndustryBean.DataBean> beanList) {

    }

    @Override
    public void showIndustrys(List<UserInfoBean.DataBean.UserBean> bbeanList) {

    }

    @Override
    public void showSetting(SettingBean headBean) {
        if(headBean.status==1){
            Toast.makeText(this, R.string.update_suc_tip,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, R.string.update_fail_tip,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSettingHead(HeadBean headBean) {

    }

    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {

    }

    @Override
    public void onErro() {

    }

    @Override
    public void isSucceed() {

    }
}
