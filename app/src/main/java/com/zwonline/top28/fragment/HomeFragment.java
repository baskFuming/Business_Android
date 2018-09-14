package com.zwonline.top28.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AdvertisingActivity;
import com.zwonline.top28.activity.HomeSearchActivity;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomeClassPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.YangFenUnclaimedWindow;
import com.zwonline.top28.view.IHomeClassFrag;

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

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 14:27
 */

public class HomeFragment extends BasesFragment<IHomeClassFrag, HomeClassPresenter> implements IHomeClassFrag {


    @BindView(R.id.et_home)
    EditText etHome;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    private SharedPreferencesUtils sp;
    private boolean isfer;
    private ImageView hotBusiness;
    private RelativeLayout hot_business_relat;
    private LinearLayout home_linear;
    private YangFenUnclaimedWindow yangFenUnclaimedWindow;


    //二次修改
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    private SimplePagerTitleView simplePagerTitleView;

    private List<HomeBean.DataBean> hlist;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    protected void init(View view) {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        sp = SharedPreferencesUtils.getUtil();
        hotBusiness = (ImageView) view.findViewById(R.id.hot_business);
        hot_business_relat = (RelativeLayout) view.findViewById(R.id.hot_business_relat);
        home_linear = (LinearLayout) view.findViewById(R.id.home_linear);
        date(view);
        if (NetUtils.isConnected(getActivity())) {
            presenter.mHomeClass(getActivity());
        }
        etHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHome.requestFocus();//获取焦点
                etHome.setCursorVisible(true);//光标出现
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_SEARCH_BAR);
            }
        });
        etHome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtil.isEmpty(etHome.getText().toString().trim())) {
                        RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.DO_SEARCH);
                        Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
                        intent.putExtra("title", etHome.getText().toString().trim());
                        startActivity(intent);
                        etHome.setText("");
                        etHome.setCursorVisible(false);//光标隐藏
                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else {
                        ToastUtil.showToast(getActivity(), "请输入内容！");
                    }
                    return true;
                }
                return false;
            }
        });
        //触摸监听
        touchListener();

    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected HomeClassPresenter setPresenter() {
        return new HomeClassPresenter(this, getActivity());
    }

    @Override
    protected int setLayouId() {
        return R.layout.homefragment;
    }

    @Override
    public void showHomeClass(final List<HomeBean.DataBean> classList) {
        loadingTablayout(classList);

//        for (int i = 0; i < classList.size(); i++) {
//            tablayout.newTab().setText(classList.get(i).cate_name);
//        }
//
//        tablayout.newTab().setText(R.string.center_recommend);
//
//        if (classList.size() > 5) {
//
//            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        } else {
//            tablayout.setTabMode(TabLayout.MODE_FIXED);
//        }
//
//        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), classList);
//        viewpager.setAdapter(myFragmentAdapter);
//        viewpager.setOffscreenPageLimit(1);
//        viewpager.setCurrentItem(1);
//        StringUtil.reflex(tablayout);
//        tablayout.setTabsFromPagerAdapter(myFragmentAdapter);
//        tablayout.setupWithViewPager(viewpager);
    }

    private void loadingTablayout(final List<HomeBean.DataBean> classList) {
        HomeBean.DataBean bean = new HomeBean.DataBean("300", getString(R.string.center_recommend));
        classList.add(1, bean);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), classList);
        viewpager.setAdapter(myFragmentAdapter);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setCurrentItem(1);
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdjustMode(true);  //ture 即标题平分屏幕宽度的模式
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return classList == null ? 0 : classList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(16);
                for (int i = 0; i < classList.size(); i++) {
                    simplePagerTitleView.setText(classList.get(index).cate_name);
                }
                simplePagerTitleView.setSelectedColor(Color.parseColor("#000000"));
                simplePagerTitleView.setNormalColor(Color.parseColor("#9e9e9e"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 30));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF2B2B"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewpager);
    }

    @Override
    public void showHomesClass(HomeBean homeBean) {
    }

    @Override
    public void showErro() {
        Toast.makeText(getActivity(), R.string.get_content_fail_tip, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btn_search, R.id.hot_business})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.btn_search://搜索
                if (!StringUtil.isEmpty(etHome.getText().toString().trim())) {
                    RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.DO_SEARCH);
                    Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
                    intent.putExtra("title", etHome.getText().toString().trim());
                    startActivity(intent);
                    etHome.setText("");
                    etHome.setCursorVisible(false);//光标隐藏
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    ToastUtil.showToast(getActivity(), "请输入内容！");
                }
                break;
            case R.id.hot_business://热商机
                Intent intent = new Intent(getActivity(), AdvertisingActivity.class);
                intent.putExtra("jump_path", BizConstant.HOT_BUSINESS);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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
            return HomeClass.getInstance(hlist.get(position));
        }

        @Override
        public int getCount() {
            return hlist.size();
        }
    }


    //判断应用第一次启动
    private void date(View view) {
        isfer = (boolean) sp.getKey(getActivity(), "isfer", false);
        if (isfer) {
            hot_business_relat.setVisibility(View.VISIBLE);
        } else {
            //第二次进入跳转
            hot_business_relat.setVisibility(View.GONE);
        }
    }


    //触摸监听方法
    private void touchListener() {
        //触摸监听
        MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                // 处理手势事件
                hot_business_relat.setVisibility(View.GONE);
            }
        };

        // 将myTouchListener注册到分发列表
        ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
    }


}
