package com.zwonline.top28.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;

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
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 新版商机圈
 */
public class BusinessiCrcleFragment extends BaseFragment {
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_function)
    TextView tvFunction;
    @BindView(R.id.business_recyler)
    RecyclerView businessRecyler;
    @BindView(R.id.business_tab)
    MagicIndicator businessTab;
    @BindView(R.id.business_page)
    ViewPager businessPage;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    Unbinder unbinder;
    private SimplePagerTitleView simplePagerTitleView;
    private BadgePagerTitleView badgePagerTitleView;
    private String[] titles = new String[]{"精选", "关注", "我的"};
    private TextView badgeTextView;
    private List<Fragment> fList;
    private AttentionCotentFragment attentionCotentFragment;
    private MyDynamicFragment myDynamicFragment;
    private RecommendFragment recommendFragment;
    private boolean isLogin;
    private SharedPreferencesUtils sp;
    private MyAdapter adapter;

    @Override
    protected void init(View view) {
        sp = SharedPreferencesUtils.getUtil();
        if (sp != null) {
            isLogin = (boolean) sp.getKey(getActivity(), "islogin", false);
        }
        back.setVisibility(View.GONE);
        title.setText("商机圈");
        tvFunction.setTextColor(Color.parseColor("#228FFE"));
        tvFunction.setText("全部圈子");
        //初始化各fragment
        attentionCotentFragment = new AttentionCotentFragment();
        myDynamicFragment = new MyDynamicFragment();
        recommendFragment = new RecommendFragment();
        //将fragment装进列表中
        fList = new ArrayList<>();
        fList.add(recommendFragment);
        fList.add(attentionCotentFragment);
        fList.add(myDynamicFragment);
        adapter = new MyAdapter(getActivity().getSupportFragmentManager());
        businessPage.setAdapter(adapter);
        if (isLogin) {
            businessPage.setOffscreenPageLimit(fList.size());
        } else {
            businessPage.setOffscreenPageLimit(1);
        }
        initMagicIndicator();
    }

    private void initMagicIndicator() {

        businessTab.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
//        commonNavigator.setAdjustMode(true);  //ture 即标题平分屏幕宽度的模式
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
                        businessPage.setCurrentItem(index);
//                        badgePagerTitleView.setBadgeView(null);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                if (index == 3) {
                    badgeTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                    badgePagerTitleView.setBadgeView(null);
                }
                if (index == 3) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 6)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, -UIUtil.dip2px(context, 4)));
                }
                badgePagerTitleView.setAutoCancelBadge(false);
                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 40));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF2B2B"));
                return indicator;
            }
        });
        businessTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(businessTab, businessPage);
        businessPage.setCurrentItem(1);

    }

    @Override
    protected BasePresenter setPresenter() {
        return null;
    }

    @Override
    protected int setLayouId() {
        return R.layout.businessi_crcle_fragment;
    }


    @OnClick({R.id.back, R.id.title, R.id.tv_function})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_function:
                ToastUtils.showToast(getActivity(),"全部圈子");
                break;
        }
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
