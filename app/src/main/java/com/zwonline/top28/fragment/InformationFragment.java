package com.zwonline.top28.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
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
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AddFriendsActivity;
import com.zwonline.top28.activity.SearchGroupActivity;
import com.zwonline.top28.adapter.InfoFragmentPageAdapter;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.MyQRCodeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.main.activity.GlobalSearchActivity;
import com.zwonline.top28.nim.main.activity.GroupTagsActivity;
import com.zwonline.top28.nim.main.fragment.ContactListFragment;
import com.zwonline.top28.nim.main.fragment.SessionListFragment;
import com.zwonline.top28.nim.main.helper.SystemMessageUnreadManager;
import com.zwonline.top28.nim.main.reminder.ReminderId;
import com.zwonline.top28.nim.main.reminder.ReminderItem;
import com.zwonline.top28.nim.main.reminder.ReminderManager;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.TabUtils;
import com.zwonline.top28.utils.badge.InfoBadgeView;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.MyQrCodePopwindow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zwonline.top28.adapter.InfoFragmentPageAdapter.formatBadgeNumber;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 14:27
 */

public class InformationFragment extends BasesFragment {
    Unbinder unbinder;
    private TabLayout infoTab;
    private ViewPager infoPager;
    private List<Fragment> fList = new ArrayList<>();
    private List<String> mPageTitleList = new ArrayList<String>();
    private List<Integer> mBadgeCountList = new ArrayList<Integer>();
    private SessionListFragment sessionListFragment;
    private ContactListFragment myFragment;
    private FragmentPagerAdapter fAdapter;
    private RelativeLayout searchMessage;
    private PopupWindow mCurPopupWindow;
    private RelativeLayout addFuction;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private SharedPreferencesUtils sp;
    private String accid;
    private String nickname;
    private String avatar;
    private String uid;
    private String sign;
    private List<MyQRCodeBean> qrlist;
    private MyQrCodePopwindow myQrCodePopwindow;
    private MyQRCodeBean bean;
    private int count = 0;
    private List<InfoBadgeView> mBadgeViews;
    private InfoFragmentPageAdapter mPagerAdapter;

    @Override
    protected void init(View view) {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        setHasOptionsMenu(true);
        sp = SharedPreferencesUtils.getUtil();
        accid = (String) sp.getKey(getActivity(), "account", "");
        nickname = (String) sp.getKey(getActivity(), "nickname", "");
        avatar = (String) sp.getKey(getActivity(), "avatar", "");
        uid = (String) sp.getKey(getActivity(), "uid", "");
        sign = (String) sp.getKey(getActivity(), "sign", "");
        bean = new MyQRCodeBean();
        infoTab = view.findViewById(R.id.info_tab);
        addFuction = view.findViewById(R.id.add_fuction);
        infoPager = view.findViewById(R.id.info_viewpager);
        searchMessage = view.findViewById(R.id.search_message);
        initControls();
    }

    private void updateUnreadNum(int unreadCount) {
//        count += unreadCount;
        mBadgeCountList.set(1, unreadCount);
    }

    @Override
    protected BasePresenter setPresenter() {
        return null;
    }

    @Override
    protected int setLayouId() {
        return R.layout.informationfragment;
    }


    /**
     * 初始化各控件
     */
    private void initControls() {
        //初始化各fragment
        sessionListFragment = new SessionListFragment();
        myFragment = new ContactListFragment();
        //将fragment装进列表中
        fList = new ArrayList<>();
        initFragments();
        mPagerAdapter = new InfoFragmentPageAdapter(getActivity(), getActivity().getSupportFragmentManager(), fList, mPageTitleList, mBadgeCountList);
        infoPager.setAdapter(mPagerAdapter);
        setIndicator(infoTab, 5, 5);
        infoTab.setupWithViewPager(infoPager);
        initBadgeViews();
        setUpTabBadge();
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }

    }

    private void initFragments() {
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountBlock();
        count = unread;

        mPageTitleList.add("消息");
        mPageTitleList.add("好友");
        mBadgeCountList.add(0);
        mBadgeCountList.add(count++);
        fList.add(SessionListFragment.getInstance(mPageTitleList.get(0)));
        fList.add(ContactListFragment.getInstance(mPageTitleList.get(1)));
        int unreadCount = SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
        Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
            @Override
            public void onEvent(Integer unreadCount) {
                count += unreadCount;
                mBadgeCountList.set(1, unreadCount);
                setUpTabBadge();
            }
        };
        NIMClient.getService(SystemMessageObserver.class)
                .observeUnreadCountChange(sysMsgUnreadCountChangedObserver, true);

        ReminderManager.getInstance().registerUnreadNumChangedCallback(new ReminderManager.UnreadNumChangedCallback() {
            @Override
            public void onUnreadNumChanged(ReminderItem item) {
                if (item.getId() != ReminderId.CONTACT) {
                    return;
                }
                updateUnreadNum(item.getUnread());
                setUpTabBadge();
            }
        });
    }

    private void initBadgeViews() {
        if (mBadgeViews == null) {
            mBadgeViews = new ArrayList<InfoBadgeView>();
            InfoBadgeView tmp = new InfoBadgeView(getActivity());
            tmp.setBadgeMargin(36, 11, 0, 0);
            tmp.setTextSize(6);
            mBadgeViews.add(tmp);
        }
    }

    /**
     * 设置Tablayout上的标题的角标
     */
    private void setUpTabBadge() {
        // 1. 最简单
        for (int i = 0; i < fList.size(); i++) {
            mBadgeViews.get(0).setTargetView(((ViewGroup) infoTab.getChildAt(0)).getChildAt(i));
            mBadgeViews.get(0).setText(formatBadgeNumber(mBadgeCountList.get(i)));
        }

        // 2. 最实用
        for (int i = 0; i < fList.size(); i++) {
            TabLayout.Tab tab = infoTab.getTabAt(i);

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

    @OnClick({R.id.add_fuction, R.id.search_message})
    public void onViewClicked(final View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.add_fuction:
                mCurPopupWindow = showTipPopupWindow(addFuction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.saosao://扫一扫二维码
                                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
                                mCurPopupWindow.dismiss();
                                break;
                            case R.id.add_friend://添加好友
                                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                                mCurPopupWindow.dismiss();
                                break;
                            case R.id.create_group://创建群组
                                Intent intent = new Intent(getActivity(), GroupTagsActivity.class);
                                intent.putExtra("account", accid);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                mCurPopupWindow.dismiss();
                                break;
                            case R.id.add_group://搜索群
                                startActivity(new Intent(getActivity(), SearchGroupActivity.class));
                                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                                mCurPopupWindow.dismiss();
                                break;
                            case R.id.my_qr://我的二维码
                                myQrCodePopwindow = new MyQrCodePopwindow(getActivity());
                                myQrCodePopwindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                                View contentView = myQrCodePopwindow.getContentView();
                                ImageViewPlus myHead = contentView.findViewById(R.id.my_head);
                                TextView myName = contentView.findViewById(R.id.my_name);

                                TextView mySign = contentView.findViewById(R.id.my_sign);
                                if (StringUtil.isNotEmpty(nickname)) {
                                    myName.setText(nickname);
                                }
                                if (StringUtil.isNotEmpty(sign)) {
                                    mySign.setText(sign);
                                }
                                RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
                                Glide.with(getActivity()).load(avatar).apply(options).into(myHead);
                                ImageView myQrCode = contentView.findViewById(R.id.my_qrcode);
                                // 位图
                                try {
                                    qrlist = new ArrayList<>();
                                    bean.setQr_Type(BizConstant.TYPE_ONE);
                                    bean.setQr_Code(uid);
                                    qrlist.add(bean);
                                    Gson gson = new Gson();
                                    String s = gson.toJson(qrlist);
                                    /**
                                     * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
                                     */
                                    Bitmap bitmap = EncodingUtils.createQRCode(s.substring(1, s.length() - 1), 1000, 1000,
                                            null);
                                    // 设置图片
                                    myQrCode.setImageBitmap(bitmap);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                mCurPopupWindow.dismiss();
                                break;
                        }
                    }
                });
                break;
            case R.id.search_message:
                GlobalSearchActivity.start(getActivity());
                break;
        }
    }

    //fragment适配器
    class Find_tab_Adapter extends FragmentPagerAdapter {
        private String[] titles = new String[]{"消息", "好友"};

        public Find_tab_Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public View getTabItemView(int position) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_add_red_dot, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_tab_title);
            textView.setText(titles[position]);

            return view;
        }

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
        contentView.findViewById(R.id.message_fuction).setVisibility(View.VISIBLE);
        contentView.findViewById(R.id.friend_fuction).setVisibility(View.GONE);
        contentView.findViewById(R.id.saosao).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.add_friend).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.create_group).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.add_group).setOnClickListener(onClickListener);
        contentView.findViewById(R.id.my_qr).setOnClickListener(onClickListener);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        new RecentContactsFragment();
    }
}