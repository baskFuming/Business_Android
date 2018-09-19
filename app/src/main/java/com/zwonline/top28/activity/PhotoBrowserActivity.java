package com.zwonline.top28.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.zwonline.top28.R;
import com.zwonline.top28.utils.FloatViewPager;
import com.zwonline.top28.utils.GlideImageLoader;
import com.zwonline.top28.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.OnTouch;

/**
 * 文章详情点击图片查看图片
 */
public class PhotoBrowserActivity extends AppCompatActivity  {
    private String curImgUrl;
    private String imgUrls[];
    private String curImgUrls;
    private String imgUrl[];
    private ViewPager viewPager;
    private TextView textView;
    private int curPosition;
    //    private PhotoView img;
    private View mBackground;
    private PhotoView photoView;
    private long startTime = 0;
    private long endTime = 0;
    private boolean isclick;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browser);
        initView();
        getLastIntent();
        loadPhoto();
    }

    private void loadPhoto() {
        viewPager.setAdapter(new MyPageAdapter());
        curPosition = returnClickedPosition() == -1 ? 0 : returnClickedPosition();
        viewPager.setCurrentItem(curPosition);//设置当前显示的图片
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PhotoBrowserActivity.this.onBackPressed();
//            }
//        });
        textView.setText((curPosition + 1) + "/" + imgUrls.length);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                textView.setText((curPosition + 1) + "/" + imgUrls.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewPager.setPositionListener(new FloatViewPager.OnPositionChangeListener() {
//            @Override
//            public void onPositionChange(int initTop, int nowTop, float ratio) {
//                float alpha = 1 - Math.min(1, ratio * 5);
////                mYfBottomLayout.setAlpha(alpha);
////                mYfTitleLayout.setAlpha(alpha);
//                mBackground.setAlpha(Math.max(0, 1 - ratio));
//            }
//
//            @Override
//            public void onFlingOutFinish() {
//                finish();
//            }
//        });
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp_photo);
        progressBar = (ProgressBar) findViewById(R.id.progress);
//        img = (PhotoView)findViewById(R.id.img);

        textView = (TextView) findViewById(R.id.tv_photo_order);
        mBackground = findViewById(R.id.background_view);
    }

    public void getLastIntent() {
        curImgUrl = getIntent().getStringExtra("curImg");

        imgUrls = getIntent().getStringArrayExtra("imageUrls");
    }

    private int returnClickedPosition() {
        if (imgUrls == null || curImgUrl == null) {
            return -1;
        }
//        if (imgUrl == null || curImgUrls == null) {
//            return -1;
//        }
        for (int i = 0; i < imgUrls.length; i++) {
            if (curImgUrl.equals(imgUrls[i])) {
                return i;
            }
        }
//        for (int i = 0; i < imgUrl.length; i++) {
//            if (curImgUrls.equals(imgUrl[i])) {
//                return i;
//            }
//        }
        return -1;
    }

    private class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imgUrls.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final Animation scale = AnimationUtils.loadAnimation(PhotoBrowserActivity.this, R.anim.anim_small);
            if (imgUrls[position] != null) {

                photoView = new PhotoView(getApplicationContext());
                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBackground.setAlpha(Math.max(0, 0));
                        finish();
                        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    }
                });
                photoView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showNormalDialogs();
                        return true;
                    }
                });
                photoView.enable();
                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                Glide.with(PhotoBrowserActivity.this)
//                        .load(imgUrl[position])
//                        .into(new Glide());
//                RequestOptions requestOption = new RequestOptions().placeholder(R.color.backgroud_zanwei).error(R.color.backgroud_zanwei);
                Glide.with(PhotoBrowserActivity.this)
                        .load(imgUrls[position])
                        .listener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                ProgressInterceptor.removeListener(url);
//                                bar.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                ProgressInterceptor.removeListener(url);
//                                bar.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(photoView);

                container.addView(photoView);
                return photoView;
            }
            return null;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }


    }

    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 1);
            }
        }
    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            ToastUtils.showToast(PhotoBrowserActivity.this, "保存成功");
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        requestPower();
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));

    }

    //Dialog弹窗
    private void showNormalDialogs() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PhotoBrowserActivity.this);
        normalDialog.setMessage("是否保存图片");
        normalDialog.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveImageToGallery(PhotoBrowserActivity.this, ((BitmapDrawable) photoView.getDrawable()).getBitmap());
                    }
                });
        normalDialog.setNegativeButton(getString(R.string.common_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }


}

