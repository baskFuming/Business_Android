package com.zwonline.top28.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.zwonline.top28.R;
import com.zwonline.top28.fragment.All_AreaFragment;
import com.zwonline.top28.fragment.All_CirclerFragment;
import com.zwonline.top28.fragment.All_IndustryFragment;

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

/**
 *  全部圈子
 */
public class AllBusinessCirclerActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fList;
    private String[] titles = new String[]{"圈子", "地区", "行业"};
    //修改当前的Tablayout
    private MagicIndicator magicIndicator;
    private MyAdapter adapter;
    private SimplePagerTitleView simplePagerTitleView;
    private BadgePagerTitleView badgePagerTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_business_circler);
        tabLayout = (TabLayout) findViewById(R.id.all_bCircler_Tab);
        viewPager = (ViewPager) findViewById(R.id.all_bCircler_Viewpager);
        magicIndicator = (MagicIndicator) findViewById(R.id.fried_magic_dicator);
        fList = new ArrayList<>();
        fList.add(new All_CirclerFragment());
        fList.add(new All_AreaFragment());
        fList.add(new All_IndustryFragment());
        adapter = new MyAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        initMagicIndicator();


    }

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

}
