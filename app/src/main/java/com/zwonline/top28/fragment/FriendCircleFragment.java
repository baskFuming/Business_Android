package com.zwonline.top28.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AddFriendsActivity;
import com.zwonline.top28.activity.ArticleActivity;
import com.zwonline.top28.activity.SearchGroupActivity;
import com.zwonline.top28.activity.SendFriendActivity;
import com.zwonline.top28.activity.TransmitActivity;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.adapter.InfoFragmentPageAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.main.activity.GlobalSearchActivity;
import com.zwonline.top28.nim.main.fragment.ContactListFragment;
import com.zwonline.top28.nim.main.fragment.SessionListFragment;
import com.zwonline.top28.nim.main.helper.SystemMessageUnreadManager;
import com.zwonline.top28.nim.main.reminder.ReminderId;
import com.zwonline.top28.nim.main.reminder.ReminderItem;
import com.zwonline.top28.nim.main.reminder.ReminderManager;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.badge.InfoBadgeView;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.MyQrCodePopwindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

import static com.liaoinstan.springview.utils.DensityUtil.dip2px;
import static com.zwonline.top28.adapter.InfoFragmentPageAdapter.formatBadgeNumber;

/**
 * 商机圈页面
 */
public class FriendCircleFragment extends BasesFragment {
    private TabLayout friendTab;
    private ViewPager friendPager;
    private List<Fragment> fList;
    private NewContentFragment newContentFragment;
    private AttentionCotentFragment attentionCotentFragment;
    private MyDynamicFragment myDynamicFragment;
    private RecommendFragment recommendFragment;
    private PopupWindow mCurPopupWindow;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private RelativeLayout sendFriend;
    private SharedPreferencesUtils sp;
    private boolean isLogin;
    private String uid;
    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();
    private List<InfoBadgeView> mBadgeViews;
    private InfoFragmentPageAdapter mPagerAdapter;
    private int count = 0;
    private int notifyCount;

    @Subscribe
    @Override
    protected void init(View view) {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        sp = SharedPreferencesUtils.getUtil();
        if (sp != null) {
            isLogin = (boolean) sp.getKey(getActivity(), "islogin", false);
            uid = (String) sp.getKey(getActivity(), "uid", "");
        }
        EventBus.getDefault().register(this);
        initView(view);
    }


    @Override
    protected BasePresenter setPresenter() {
        return null;
    }

    @Override
    protected int setLayouId() {
        return R.layout.friend_circlef_ragment;
    }

    private void initView(View view) {
        sendFriend = view.findViewById(R.id.send_friend);
        friendTab = view.findViewById(R.id.friend_tab);
        friendPager = view.findViewById(R.id.friend_viewpager);
        //初始化各fragment
        newContentFragment = new NewContentFragment();
        attentionCotentFragment = new AttentionCotentFragment();
        myDynamicFragment = new MyDynamicFragment();
        recommendFragment = new RecommendFragment();
        //将fragment装进列表中
        fList = new ArrayList<>();

        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用

        //设置TabLayout的模式
//        infoTab.setTabMode(TabLayout.MODE_FIXED);
//        friendTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        //为TabLayout添加tab名称
//        friendTab.addTab(friendTab.newTab().setText("最新"));
//        friendTab.addTab(friendTab.newTab().setText("推荐"));
//        friendTab.addTab(friendTab.newTab().setText("关注"));
//        friendTab.addTab(friendTab.newTab().setText("我的"));
        initFragments();
//        FrinedAdapter fAdapter = new FrinedAdapter(getChildFragmentManager());
        mPagerAdapter = new InfoFragmentPageAdapter(getActivity(), getActivity().getSupportFragmentManager(), fList, mPageTitleList, mBadgeCountList);
        //viewpager加载adapter
        friendPager.setAdapter(mPagerAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
//        StringUtil.dynamicReflexs(friendTab);

        friendTab.setupWithViewPager(friendPager);
        initBadgeViews();
        setUpTabBadge();
    }

    private void initFragments() {

        mPageTitleList.add("最新");
        mPageTitleList.add("推荐");
        mPageTitleList.add("关注");
        mPageTitleList.add("我的");
        mBadgeCountList.add(0);
        mBadgeCountList.add(0);
        mBadgeCountList.add(0);
        mBadgeCountList.add(count++);
        fList.add(NewContentFragment.getInstance(mPageTitleList.get(0)));
        fList.add(RecommendFragment.getInstance(mPageTitleList.get(1)));
        fList.add(AttentionCotentFragment.getInstance(mPageTitleList.get(2)));
        fList.add(myDynamicFragment.getInstance(mPageTitleList.get(3)));

    }

    private void initBadgeViews() {
        if (mBadgeViews == null) {
            mBadgeViews = new ArrayList<InfoBadgeView>();
            InfoBadgeView tmp = new InfoBadgeView(getActivity());
            tmp.setBadgeMargin(40, 6, 0, 0);
            tmp.setTextSize(10);
            mBadgeViews.add(tmp);
        }
    }

    /**
     * 设置Tablayout上的标题的角标
     */
    private void setUpTabBadge() {
        // 1. 最简单
        for (int i = 0; i < fList.size(); i++) {
            mBadgeViews.get(0).setTargetView(((ViewGroup) friendTab.getChildAt(0)).getChildAt(i));
            mBadgeViews.get(0).setText(formatBadgeNumber(mBadgeCountList.get(i)));
        }

        // 2. 最实用
        for (int i = 0; i < fList.size(); i++) {
            TabLayout.Tab tab = friendTab.getTabAt(i);

            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
//            tab.setCustomView(mPagerAdapter.getTabItemView(i));
        }

        // 需加上以下代码,不然会出现更新Tab角标后,选中的Tab字体颜色不是选中状态的颜色
//        mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }

    //fragment适配器
    class FrinedAdapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{"最新", "推荐", "关注", "我的"};

        public FrinedAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fList.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

    @OnClick({R.id.send_friend, R.id.search_friend})
    public void onViewClicked(final View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.send_friend://发图文
                if (sp != null) {
                    if (isLogin) {
                        mCurPopupWindow = showTipPopupWindow(sendFriend, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    case R.id.send_picture://发送图文
                                        Intent pictureIntent = new Intent(getActivity(), SendFriendActivity.class);
                                        pictureIntent.putExtra("picture_text", BizConstant.ALREADY_FAVORITE);
                                        startActivityForResult(pictureIntent, 10);
                                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                        mCurPopupWindow.dismiss();
                                        break;
                                    case R.id.send_text://发文字
                                        Intent textIntent = new Intent(getActivity(), SendFriendActivity.class);
                                        textIntent.putExtra("picture_text", BizConstant.ALIPAY_METHOD);
                                        startActivityForResult(textIntent, 10);
                                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                        mCurPopupWindow.dismiss();
                                        break;

                                    case R.id.send_article://发布文章
                                        Intent sendIntent = new Intent(getActivity(), ArticleActivity.class);
                                        startActivityForResult(sendIntent, 10);
                                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                        mCurPopupWindow.dismiss();
                                        break;
                                    case R.id.reprinted_article://转载文章
                                        Intent reprintedIntent = new Intent(getActivity(), TransmitActivity.class);
                                        startActivityForResult(reprintedIntent, 10);
                                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                        mCurPopupWindow.dismiss();
                                        break;
                                }
                            }
                        });
                    } else {
                        Intent intent = new Intent(getActivity(), WithoutCodeLoginActivity.class);
                        intent.putExtra("login_type", BizConstant.TYPE_TWO);
                        startActivity(intent);
                        getActivity().finish();
                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        ToastUtils.showToast(getActivity(), "请先登录");
                    }
                }


                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLogin == false) {
            friendPager.setCurrentItem(0);//没有登录默认选中第一项
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageFollow event) {
        if (StringUtil.isNotEmpty(event.notifyCount)){
            notifyCount = Integer.parseInt(event.notifyCount);
            mBadgeCountList.set(3, notifyCount);
            setUpTabBadge();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 加载popwindowt弹窗
     * 点击事件
     *
     * @param anchorView
     * @param onClickListener
     * @return
     */
    public PopupWindow showTipPopupWindow(final View anchorView, final View.OnClickListener onClickListener) {
        final View contentView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.add_function_pop, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        final PopupWindow popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);
        contentView.findViewById(R.id.message_fuction).setVisibility(View.GONE);
        contentView.findViewById(R.id.friend_fuction).setVisibility(View.VISIBLE);

        contentView.findViewById(R.id.send_picture).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.send_text).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.send_article).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.reprinted_article).setOnClickListener(onClickListener);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
        popupWindow.setOutsideTouchable(true);

        // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
        popupWindow.setTouchable(true);

        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键
        popupWindow.setFocusable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // 这里面拦截不到返回键
            }
        });
        // 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
        // 必须在创建PopupWindow的时候指定高度，不能用wrap_content
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (newContentFragment != null) {
            newContentFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (attentionCotentFragment != null) {
            attentionCotentFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (myDynamicFragment != null) {
            myDynamicFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}