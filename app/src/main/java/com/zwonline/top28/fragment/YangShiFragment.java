package com.zwonline.top28.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomeSearchActivity;
import com.zwonline.top28.activity.InformationNoticeActivity;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.activity.YangShiActivity;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.bean.YSBannerBean;
import com.zwonline.top28.bean.YSListBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.presenter.YangShiPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.BannerImage;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IYangShiActivity;

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

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 原生鞅市
 */
public class YangShiFragment extends BasesFragment<IYangShiActivity, YangShiPresenter> implements IYangShiActivity, View.OnClickListener {

    private Banner ysBnner;
    private RelativeLayout ysSearch;
    private RelativeLayout ysMy;
    private MagicIndicator ysTab;
    private ViewPager ysViewPager;
    private List<HomeBean.DataBean> hlist;
    private List<HomeBean.DataBean> list;
    private SimplePagerTitleView simplePagerTitleView;
    private RelativeLayout search_relative;
    private EditText search;
    private TextView cancel;

    @Override
    protected void init(View view) {
        initData(view);
        list = new ArrayList<>();
        presenter.BannerList(getActivity());

        ysBnner.setImageLoader(new BannerImage());
        //设置banner动画效果
        ysBnner.setBannerAnimation(Transformer.DepthPage);
        //设置banner样式
        ysBnner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        loadingTablayout();
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initData(View view) {
        ysBnner = view.findViewById(R.id.yangshi_banner);
        ysSearch = view.findViewById(R.id.ys_search);
        ysMy = view.findViewById(R.id.ys_my);
        ysTab = view.findViewById(R.id.ys_indicator);
        search_relative = view.findViewById(R.id.search_relative);
        search = view.findViewById(R.id.search);
        cancel = view.findViewById(R.id.cancel);
        search_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_relative.setVisibility(View.GONE);
                search.setText("");
                LanguageUitils.showKeyboard(getActivity(), false);
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (!StringUtil.isEmpty(search.getText().toString().trim())) {
                        RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.DO_SEARCH);
                        try {
                            Intent intent = new Intent(getActivity(), YangShiActivity.class);
                            intent.putExtra("content", URLEncoder.encode(search.getText().toString().trim(), "utf-8"));
                            intent.putExtra("jump_url", BizConstant.YSSEARCH);
                            startActivity(intent);
                            search.setText("");
                            search_relative.setVisibility(View.GONE);
                            getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ToastUtil.showToast(getActivity(), "请输入内容！");
                    }
                    return true;
                }
                return false;
            }
        });
        ysViewPager = view.findViewById(R.id.ys_view_pager);
        ysMy.setOnClickListener(this);
        ysSearch.setOnClickListener(this);
    }

    @Override
    protected YangShiPresenter setPresenter() {
        return new YangShiPresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.yangshi_fragment;
    }

    /**
     * banner轮播图
     *
     * @param ysBannerBeanList
     */
    @Override
    public void showBannerList(final List<YSBannerBean.DataBean> ysBannerBeanList) {
        List<String> image = new ArrayList<>();
        for (int i = 0; i < ysBannerBeanList.size(); i++) {
            image.add(ysBannerBeanList.get(i).img);
        }
        ysBnner.setImages(image);
        ysBnner.start();
        ysBnner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getActivity(), YangShiActivity.class);
                intent.putExtra("jump_url", ysBannerBeanList.get(position).jump_url);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    /**
     * 鞅分拍卖列表
     *
     * @param ysList
     */
    @Override
    public void showAuctionList(List<YSListBean.DataBean.ListBean> ysList) {

    }

    private void loadingTablayout() {
        HomeBean.DataBean bean1 = new HomeBean.DataBean("1", "最新上线");
        HomeBean.DataBean bean2 = new HomeBean.DataBean("2", "支持更多");
        HomeBean.DataBean bean3 = new HomeBean.DataBean("3", "即将结束");
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
//        classList.add(bean1);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), list);
//        ysViewPager.setOffscreenPageLimit(1);
        ysViewPager.setAdapter(myFragmentAdapter);
//        ysViewPager.setCurrentItem(1);
        ysTab.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        if (list.size() > 6) {
            commonNavigator.setAdjustMode(false);
        } else {
            commonNavigator.setAdjustMode(true);  //ture 即标题平分屏幕宽度的模式
        }
        commonNavigator.setScrollPivotX(0.65f);

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(16);
                for (int i = 0; i < list.size(); i++) {
                    simplePagerTitleView.setText(list.get(index).cate_name);
                }
                simplePagerTitleView.setSelectedColor(Color.parseColor("#2F2F2F"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#807F81"));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //第二次进入跳转
                        ysViewPager.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 60));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF2B2B"));
                return indicator;
            }
        });
        ysTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(ysTab, ysViewPager);
    }

    @Override
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {

            case R.id.ys_my:
                Intent intent = new Intent(getActivity(), YangShiActivity.class);
                intent.putExtra("jump_url", BizConstant.YSMY);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

            case R.id.ys_search:
                search_relative.setVisibility(View.VISIBLE);
                search.setFocusable(true);
                search.setFocusableInTouchMode(true);
                search.requestFocus();
                search.findFocus();
                LanguageUitils.showKeyboard(getActivity(), true);
                break;
        }
    }

    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm, List<HomeBean.DataBean> list) {
            super(fm);
            if (hlist == null) {
                hlist = new ArrayList<>();
            }
            hlist.addAll(list);
//            HomeBean.DataBean bean = new HomeBean.DataBean("300", getString(R.string.center_recommend));
//            hlist.add(1, bean);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object); //避免多出销毁Fragment
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return hlist.get(position).cate_name;
        }

        @Override
        public Fragment getItem(int position) {
            return YangShiListFragment.getInstance(hlist.get(position));
        }

        @Override
        public int getCount() {
            return hlist.size();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    //判断是否展示—与RadioGroup等连用，进行点击切换
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            search_relative.setVisibility(View.GONE);
            search.setText("");
        }
    }


}
