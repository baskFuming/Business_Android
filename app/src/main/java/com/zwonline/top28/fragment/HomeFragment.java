package com.zwonline.top28.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.VpnService;
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
import com.zwonline.top28.activity.ChannelActivity;
import com.zwonline.top28.activity.HomeSearchActivity;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomeClassPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.more_channel)
    RelativeLayout moreChannel;
    Unbinder unbinder;
    private SharedPreferencesUtils sp;
    private boolean isfer;
    private ImageView hotBusiness;
    private RelativeLayout hot_business_relat;
    private LinearLayout home_linear;
    private List<HomeBean.DataBean> list;

    //二次修改
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    private SimplePagerTitleView simplePagerTitleView;

    private List<HomeBean.DataBean> hlist;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Subscribe
    @Override
    protected void init(View view) {
        EventBus.getDefault().register(this);
//        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        sp = SharedPreferencesUtils.getUtil();
        list = new ArrayList<>();
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

        moreChannel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AntiShake.check(v.getId())) {    //判断是否多次点击
                    return;
                }

                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                intent.putExtra("weburl", BizConstant.MORE_CHANNEL);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });


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
//        viewpager.setCurrentItem(1);
        list.clear();
        list.addAll(classList);
        loadingTablayout();
    }

    private void loadingTablayout() {
        if (isAdded()) {
            HomeBean.DataBean bean = new HomeBean.DataBean("300", getString(R.string.center_recommend));
            list.add(1, bean);
        }
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), list);
//        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(myFragmentAdapter);
//        viewpager.setCurrentItem(1);
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        if (list.size() > 6) {
            commonNavigator.setAdjustMode(false);  //ture 即标题平分屏幕宽度的模式
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
        viewpager.setCurrentItem(1);
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
            hot_business_relat.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageFollow messageFollow) {
        if (StringUtil.isNotEmpty(String.valueOf(messageFollow.homeTag))) {
            int homeTag = messageFollow.homeTag;
            viewpager.setCurrentItem(homeTag);
        }
    }


}
