package com.zwonline.top28.fragment;


import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.bean.ZxingConfig;
import com.xys.libzxing.zxing.common.Constant;
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
import com.zwonline.top28.nim.main.reminder.ReminderId;
import com.zwonline.top28.nim.main.reminder.ReminderItem;
import com.zwonline.top28.nim.main.reminder.ReminderManager;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.badge.InfoBadgeView;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.MyQrCodePopwindow;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import butterknife.Unbinder;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;
import static com.zwonline.top28.adapter.InfoFragmentPageAdapter.formatBadgeNumber;

/**
 * 1.消息界面
 * 2.@authorDell
 * 3.@date2017/12/6 14:27
 */

public class InformationFragment extends BasesFragment {
    Unbinder unbinder;
    private TabLayout infoTab;
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
    private int count;
    private List<InfoBadgeView> mBadgeViews;
    private InfoFragmentPageAdapter mPagerAdapter;
    //修改后的Tablayout
    private MagicIndicator magicIndicator;
    private String[] titles = new String[]{"消息", "我的"};
    private ViewPager infoPager;
    private List<Fragment> fList = new ArrayList<>();
    private MyAdapter adapter;
    private SimplePagerTitleView simplePagerTitleView;
    private BadgePagerTitleView badgePagerTitleView;
    private TextView badgeTextView;
    private int REQUEST_CODE_SCAN = 111;
    private String mPermissions[] = {Manifest.permission.CAMERA};
    private static final int Permissions_CAMERA_KEY = 2;

    @Override
    protected void init(View view) {
//        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        setHasOptionsMenu(true);
        sp = SharedPreferencesUtils.getUtil();
        searchMessage = view.findViewById(R.id.search_message);
        accid = (String) sp.getKey(getActivity(), "account", "");
        nickname = (String) sp.getKey(getActivity(), "nickname", "");
        avatar = (String) sp.getKey(getActivity(), "avatar", "");
        uid = (String) sp.getKey(getActivity(), "uid", "");
        sign = (String) sp.getKey(getActivity(), "sign", "");
        bean = new MyQRCodeBean();
//        infoTab = view.findViewById(R.id.info_tab);
        addFuction = view.findViewById(R.id.add_fuction);
        magicIndicator = view.findViewById(R.id.friend_cicler_indicator);
        infoPager = view.findViewById(R.id.info_viewpager);
        initControls();
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountBlock();
        updateUnreadNum(unread);
        initFragments();
    }

    private void updateUnreadNum(int unreadCount) {
        if (unreadCount == 0) {
            badgePagerTitleView.setBadgeView(null);
        } else {
            if (unreadCount < 100) {
                badgeTextView.setText(unreadCount + "");
            } else {
                badgeTextView.setText("99+");
            }
            badgePagerTitleView.setBadgeView(badgeTextView);
        }
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
        fList.add(sessionListFragment);
        fList.add(myFragment);
        adapter = new MyAdapter(getActivity().getSupportFragmentManager());
        infoPager.setAdapter(adapter);

        friendCircIndicator();
    }

    private void friendCircIndicator() {
        magicIndicator.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
                        infoPager.setCurrentItem(index);
//                        badgePagerTitleView.setBadgeView(null);
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);
                if (index == 1) {
                    badgeTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.simple_count_badge_layout, null);
                    badgePagerTitleView.setBadgeView(null);

                }
                if (index == 1) {
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UIUtil.dip2px(context, 4)));
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
                indicator.setLineWidth(UIUtil.dip2px(context, 30));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#FF2B2B"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, infoPager);

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
        Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
            @Override
            public void onEvent(Integer unreadCount) {
                updateUnreadNum(unreadCount);
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

        }

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
                                if (Build.VERSION.SDK_INT >= 23) {
                                    setPermissions(Permissions_CAMERA_KEY);
                                } else {
                                    saoData();
                                }

//                                startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
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

    /**
     * fragment适配器
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

    /**
     * 相机权限开启
     * @param mPermissions_KEY
     */
    public void setPermissions(int mPermissions_KEY) {
        /*
        要添加List原因是想判断数组里如果有个别已经授权的权限，就不需要再添加到List中。添加到List中的权限后续将转成数组去申请权限
         */
        List<String> permissionsList = new ArrayList<>();
        //判断系统版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < mPermissions.length; i++) {
                //判断一个权限是否已经允许授权，如果没有授权就会将单个未授权的权限添加到List里面
                if (ContextCompat.checkSelfPermission(getActivity(), mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(mPermissions[i]);
                }
            }
            //判断List不是空的，如果有内容就运行获取权限
            if (!permissionsList.isEmpty()) {
                String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
                for (int j = 0; j < permissions.length; j++) {
                }
                //执行授权的代码。此处执行后会弹窗授权
                InformationFragment.this.requestPermissions(permissions, mPermissions_KEY);
            } else { //如果是空的说明全部权限都已经授权了，就不授权了,直接执行进入相机或者图库
                saoData();
            }
        } else {
            saoData();
        }
    }

    /**
     * 跳转扫一扫界面
     */
    public void saoData() {
        Intent saoIntent = new Intent(getActivity(), CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.white);//设置扫描框四个角的颜色 默认为淡蓝色
        config.setFrameLineColor(R.color.transparent);//设置扫描框边框颜色 默认无色
        config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        saoIntent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(saoIntent, REQUEST_CODE_SCAN);
    }

    /**
     * 权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permissions_CAMERA_KEY) {
            if (grantResults.length > 0) { //安全写法，如果小于0，肯定会出错了
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        saoData();
                    } else {
                        saoData();
                    }
                }
            }
        }

    }

}