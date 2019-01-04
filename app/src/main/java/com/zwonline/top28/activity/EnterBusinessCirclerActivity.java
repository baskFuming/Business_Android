package com.zwonline.top28.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zwonline.top28.R;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.fragment.All_AreaFragment;
import com.zwonline.top28.fragment.All_CirclerFragment;
import com.zwonline.top28.fragment.All_IndustryFragment;
import com.zwonline.top28.utils.popwindow.FloatButtonPopWindow;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 创业商机圈
 */
public class EnterBusinessCirclerActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Fragment> fList;
    private String[] titles = new String[]{"最新", "精华", "品牌"};
    //修改当前的Tablayout
    private MagicIndicator magicIndicator;
    private MyAdapter adapter;
    private SimplePagerTitleView simplePagerTitleView;
    private BadgePagerTitleView badgePagerTitleView;
    private FloatingActionButton floatingActionButton;
    private FloatButtonPopWindow floatButtonPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_business_circler);
        viewPager = (ViewPager) findViewById(R.id.busin_viewpage);
        magicIndicator = (MagicIndicator) findViewById(R.id.fried_magic_dicator);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.m_main_fab);
        fList = new ArrayList<>();
        fList.add(new All_CirclerFragment());
        fList.add(new All_AreaFragment());
        fList.add(new All_IndustryFragment());
        adapter = new MyAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        initMagicIndicator();
        initFloatButton();
    }

    /**
     * Tab导航标题
     */
    private void initMagicIndicator() {
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                badgePagerTitleView = new BadgePagerTitleView(context);
                simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setSelectedColor(Color.parseColor("#2F2F2F"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#807F81"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
//                        badgePagerTitleView.setBadgeView(null);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                badgePagerTitleView.setAutoCancelBadge(false);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 30));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.0f));
                indicator.setColors(Color.parseColor("#FF2B2B"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
        viewPager.setCurrentItem(0);
    }

    /**
     * 适配器
     */
    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fList.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    /**
     * 设置状态栏以及毛玻璃效果
     */
    private void intLoadBlurAndSetStatusBar(){

        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .apply(bitmapTransform(new BlurTransformation( 25, 4)))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {

                    }
                });

//        Glide.with(this)
//                .load(url)
//                .bitmapTransform(new BlurTransformation(25,4))
//                .into(imageView);

    }
    /**
     * 悬浮按钮点击事件
     */
    private void initFloatButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatButtonPopWindow = new FloatButtonPopWindow(EnterBusinessCirclerActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        floatButtonPopWindow.dismiss();
                        floatButtonPopWindow.backgroundAlpha(EnterBusinessCirclerActivity.this, 1f);
                        switch (v.getId()) {
                            case R.id.weixinghaoyou://发图文
                                Intent pictureIntent = new Intent(EnterBusinessCirclerActivity.this, SendFriendActivity.class);
                                pictureIntent.putExtra("picture_text", BizConstant.ALREADY_FAVORITE);
                                startActivityForResult(pictureIntent, 10);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                floatButtonPopWindow.dismiss();
                                break;
                            case R.id.pengyouquan://发文字
                                Intent textIntent = new Intent(EnterBusinessCirclerActivity.this, SendFriendActivity.class);
                                textIntent.putExtra("picture_text", BizConstant.ALIPAY_METHOD);
                                startActivityForResult(textIntent, 10);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                floatButtonPopWindow.dismiss();
                                break;
                            case R.id.qqkongjian://发布文章
                                Intent sendIntent = new Intent(EnterBusinessCirclerActivity.this, ArticleActivity.class);
                                startActivityForResult(sendIntent, 10);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                floatButtonPopWindow.dismiss();
                                break;
                            case R.id.copyurl://转载文章
                                Intent reprintedIntent = new Intent(EnterBusinessCirclerActivity.this, TransmitActivity.class);
                                startActivityForResult(reprintedIntent, 10);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                floatButtonPopWindow.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
                floatButtonPopWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

}
