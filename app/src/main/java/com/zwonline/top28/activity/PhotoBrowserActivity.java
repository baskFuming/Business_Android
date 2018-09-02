package com.zwonline.top28.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.zwonline.top28.R;
import com.zwonline.top28.utils.FloatViewPager;

/**
 * 文章详情点击图片查看图片
 */
public class PhotoBrowserActivity extends AppCompatActivity {
    private String curImgUrl;
    private String imgUrls[];
    private FloatViewPager viewPager;
    private TextView textView;
    private int curPosition;
    //    private PhotoView img;
    private View mBackground;

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
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoBrowserActivity.this.onBackPressed();
            }
        });
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
        viewPager.setPositionListener(new FloatViewPager.OnPositionChangeListener() {
            @Override
            public void onPositionChange(int initTop, int nowTop, float ratio) {
                float alpha = 1 - Math.min(1, ratio * 5);
//                mYfBottomLayout.setAlpha(alpha);
//                mYfTitleLayout.setAlpha(alpha);
                mBackground.setAlpha(Math.max(0, 1 - ratio));
            }

            @Override
            public void onFlingOutFinish() {
                finish();
            }
        });
    }

    private void initView() {
        viewPager = (FloatViewPager) findViewById(R.id.vp_photo);
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
        for (int i = 0; i < imgUrls.length; i++) {
            if (curImgUrl.equals(imgUrls[i])) {
                return i;
            }
        }
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
            if (imgUrls[position] != null) {
                PhotoView photoView = new PhotoView(getApplicationContext());
                photoView.enable();
                photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(PhotoBrowserActivity.this)
                        .load(imgUrls[position])
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
}

