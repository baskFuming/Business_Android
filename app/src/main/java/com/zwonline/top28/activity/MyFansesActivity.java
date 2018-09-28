package com.zwonline.top28.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.IntegralRecordBean;
import com.zwonline.top28.fragment.FansListFragment;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class MyFansesActivity extends BaseActivity {

    private RelativeLayout fansBack;
    private TabLayout fansTab;
    private ViewPager fansView;
    private List<IntegralRecordBean> fansList = null;


    //二次修改
    private MagicIndicator magicIndicator;
    private SimplePagerTitleView simplePagerTitleView;

    @Override
    protected void init() {
        initView();

        fansList = new ArrayList<>();
        fansList.add(new IntegralRecordBean("全部粉丝", 1));
        fansList.add(new IntegralRecordBean("已关注", 2));
        fansList.add(new IntegralRecordBean("未关注", 3));
//        fansList.add(new IntegralRecordBean("已沟通", 4));
//        fansList.add(new IntegralRecordBean("未沟通", 5));
//        for (int i = 0; i < fansList.size(); i++) {
//            fansTab.newTab().setText(fansList.get(i).record_name);
//        }
//        fansTab.setTabMode(TabLayout.MODE_FIXED);
//        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fansList);
//        fansView.setAdapter(myFragmentAdapter);
//        fansTab.setupWithViewPager(fansView);
//        fansTab.setTabsFromPagerAdapter(myFragmentAdapter);
//        StringUtil.dynamicReflexs(fansTab);
        initTablayout();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_fanses;
    }

    private void initView() {
        fansBack = (RelativeLayout) findViewById(R.id.fans_back);
        fansTab = (TabLayout) findViewById(R.id.fans_tab);
        //-------
        magicIndicator = (MagicIndicator) findViewById(R.id.fans_magicindicator);
        fansView = (ViewPager) findViewById(R.id.fans_view);

    }

    private void initTablayout() {
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fansList);
        fansView.setAdapter(myFragmentAdapter);
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);  //ture 即标题平分屏幕宽度的模式
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fansList == null ? 0 : fansList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(16);
                for (int i = 0; i < fansList.size(); i++) {
                    simplePagerTitleView.setText(fansList.get(index).record_name);
                }
                simplePagerTitleView.setSelectedColor(Color.parseColor("#2F2F2F"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#807F81"));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //第二次进入跳转
                        fansView.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 50));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
//                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF2B2B"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, fansView);
    }

    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<IntegralRecordBean> hlist;

        public MyFragmentAdapter(FragmentManager fm, List<IntegralRecordBean> list) {
            super(fm);
            this.hlist = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return hlist.get(position).record_name;
        }

        @Override
        public Fragment getItem(int position) {
            return FansListFragment.getInstance(hlist.get(position));
        }

        @Override
        public int getCount() {
            return hlist.size();
        }

    }

    @OnClick(R.id.fans_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
