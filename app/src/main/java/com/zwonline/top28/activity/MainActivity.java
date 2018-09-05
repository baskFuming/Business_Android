package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.nimlib.sdk.team.model.Team;
import com.umeng.socialize.UMShareAPI;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseMainActivity;
import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UpdateCodeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.fragment.ExamineFragment;
import com.zwonline.top28.fragment.FriendCircleFragment;
import com.zwonline.top28.fragment.HomeFragment;
import com.zwonline.top28.fragment.InformationFragment;
import com.zwonline.top28.fragment.MyFragment;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.nim.team.TeamCreateHelper;
import com.zwonline.top28.presenter.MainPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.CustomPopuWindow;
import com.zwonline.top28.utils.popwindow.YangFenUnclaimedWindow;
import com.zwonline.top28.view.IMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class MainActivity extends BaseMainActivity<IMainActivity, MainPresenter> implements IMainActivity, BottomNavigationBar.OnTabSelectedListener {
    public SharedPreferencesUtils sp;
    private HomeFragment homeFragment;
    private FriendCircleFragment businessFragment;
    private InformationFragment informationFragment;
    private MyFragment myFragment;
    private ExamineFragment examineFragment;
    private TextBadgeItem mBadgeItem;
    private List<RecentContact> items;
    private int unreadMsgsCount;
    //退出时的时间
    private long mExitTime;
    private String platform = "1";
    private String version;
    private CustomPopuWindow customPopuWindow;
    private FrameLayout framelayout;
    private BottomNavigationBar bottomBar;
    private String description;
    private String forceUpdate;
    private Context context;
    private boolean appInstalled;
    private LinearLayout mian;
    private boolean isUpdata;
    private boolean xiaomi;
    private boolean oppo;
    private String loginType;
    private String package_download_url;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private boolean isfer;
    private YangFenUnclaimedWindow yangFenUnclaimedWindow;
    private boolean islogine;
    private String token;
    private String mbpCount;


    @Override
    protected void init() {
        new RecentContactsFragment();
        initView();
//        ToastUtils.showToast(this,LanguageUitils.getVersionName(this)+"");
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        Intent intent = getIntent();
        loginType = intent.getStringExtra("loginType");//登录后返回界面
        context = MainActivity.this.getApplicationContext();
        sp = SharedPreferencesUtils.getUtil();
        islogine = (boolean) sp.getKey(this, "islogin", false);
        token = (String) sp.getKey(getApplicationContext(), "dialog", "");
        presenter.HongBaoPermission(getApplicationContext());//红包权限

//        try {
//            String token= (String) sp.getKey(context,"dialog","");
//            long timestamp = new Date().getTime() / 1000;//时间戳
//            Map<String, String> map = new HashMap<>();
//            map.put("token", token);
//            map.put("hongbao_id", "125");
//            map.put("timestamp", String.valueOf(timestamp));
//            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
//            System.out.print("timestamp==" + timestamp + "sign==" + sign + "token==" + token);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        appInstalled = LanguageUitils.isAppInstalled(context, BizConstant.YINGYONGBAO);
        xiaomi = LanguageUitils.isAppInstalled(context, BizConstant.XIAOMI);
        oppo = LanguageUitils.isAppInstalled(context, BizConstant.OPPO);
        try {
            EventBus.getDefault().register(this);
            items = new ArrayList<>();

            String token = (String) sp.getKey(getApplicationContext(), "dialog", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assignViews();
        updateUnreadCount();//更新消息数据
        initMessageListener();
        int code = getVersionCode();//获取版本号
        if (NetUtils.isConnected(getApplicationContext())) {
            presenter.UpdataVersion(getApplicationContext(), platform, String.valueOf(code));
        } else {
            ToastUtils.showToast(getApplicationContext(), "请检查网络");
        }


    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter(this);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void initView() {
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        bottomBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        mian = (LinearLayout) findViewById(R.id.main);
    }

    //检测本程序的版本，这里假设从服务器中获取到最新的版本号为3
    public void checkVersion() {
        //如果检测本程序的版本号小于服务器的版本号，那么提示用户更新
        if (!StringUtil.isEmpty(version) && version.equals("1")) {
//            showDialogUpdate();//弹出提示版本更新的对话框
            customPopuWindow = new CustomPopuWindow(MainActivity.this, description, forceUpdate, listener);
            customPopuWindow.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            if (islogine) {
                //yangFenLingQu();//鞅分领取
                presenter.UnclaimedMbpCount(getApplicationContext());
            }

            //否则吐司，说现在是最新的版本
//            Toast.makeText(this, "当前已经是最新的版本", Toast.LENGTH_SHORT).show();
            Log.v("updata", "当前已经是最新的版本");
        }
    }

    //版本更新
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.no:
                    customPopuWindow.dismiss();
                    customPopuWindow.backgroundAlpha(MainActivity.this, 1f);
                    break;
                case R.id.sure:
                    LanguageUitils.gotoBrowserDownload(context, package_download_url);//直接跳浏览器
                    break;
                case R.id.coerce_sure:
                    LanguageUitils.gotoBrowserDownload(context, package_download_url);//直接跳浏览器
                    break;
            }
        }
    };


    //E.接收Application中监听到的未读消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(boolean flag) {
        if (flag) {
            mBadgeItem.setText(unreadMsgsCount + "");
        }
    }

    //E.更新未读消息的数量
    public void updateUnreadCount() {
        int unreadNum = 0;
        for (RecentContact r : items) {
            unreadNum += r.getUnreadCount();
        }
        //获取所有的未读消息

        unreadMsgsCount = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        if (unreadMsgsCount > 99) {
            mBadgeItem.setText("99+");//底部使用的自定义控件显示未读消息99d
            mBadgeItem.show(true);//true,使用默认动画显示
        } else if (unreadMsgsCount > 0) {//unreadMsgsCount
            mBadgeItem.setText(String.valueOf(unreadMsgsCount));
            mBadgeItem.show(true);
        } else {
            mBadgeItem.hide(true);
        }
        //mBadgeItem.setText(unreadMsgsCount+"");

    }

    //重新获取焦点,着再次刷新一下未读消息
    @Override
    protected void onResume() {
        super.onResume();
//        updateUnreadCount();
    }

    //添加底部布局
    private void assignViews() {

        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar.setTabSelectedListener(this);//设置监听
        setDefaultFragment();

        BottomNavigationItem conversationItem = new BottomNavigationItem(R.mipmap.menubar_message1, "消息").setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menubar_message2));
        mBadgeItem = new TextBadgeItem();
        mBadgeItem.setGravity(Gravity.RIGHT);
        mBadgeItem.setTextColor("#ffffff");
        mBadgeItem.setBackgroundColor("#ff0000");
//        mBadgeItem.setText("5");
        mBadgeItem.show();
        conversationItem.setBadgeItem(mBadgeItem);
        bottomBar.addItem(new BottomNavigationItem(R.mipmap.menubar_home1, R.string.tabbar_home_page).setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menubar_home2)))
//                .addItem(new BottomNavigationItem(R.mipmap.menubar_jifen1, R.string.tabbar_biz_page).setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menuba_jifen2)))
//                .addItem(new BottomNavigationItem(R.mipmap.menubar_video1, R.string.tabbar_video_page).setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menubar_video2)))
                .addItem(new BottomNavigationItem(R.mipmap.menubar_ys, R.string.tabbar_yangshi).setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menubar_ys)))
                .addItem(new BottomNavigationItem(R.mipmap.menubar_sjq1, R.string.tabbar_business_circle).setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menubar_sjq2)))
                .addItem(conversationItem)
                .addItem(new BottomNavigationItem(R.mipmap.menubar_wode1, R.string.tabbar_my_page).setActiveColorResource(R.color.reded).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.menubar_wode2)))
                .setActiveColor(R.color.red)
                .setInActiveColor(R.color.tabhost_text)
                .setActiveColor(R.color.tabhost_text)
                .setBarBackgroundColor(R.color.white)
                .initialise();
    }

    //设置默认的选项
    private void setDefaultFragment() {
        if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.MYLOGIN)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            myFragment = new MyFragment();
            fragmentTransaction.add(R.id.framelayout, myFragment);
            fragmentTransaction.commit();
            bottomBar.setFirstSelectedPosition(4);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.INFO_LOGIN)) {
            informationFragment = new InformationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.framelayout, informationFragment);
            fragmentTransaction.commit();
            bottomBar.setFirstSelectedPosition(3);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.TYPE_TWO)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            businessFragment = new FriendCircleFragment();
            fragmentTransaction.add(R.id.framelayout, businessFragment);
            fragmentTransaction.commit();
            bottomBar.setFirstSelectedPosition(2);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.TYPE_ONE)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            examineFragment = new ExamineFragment();
            fragmentTransaction.add(R.id.framelayout, examineFragment);
            fragmentTransaction.commit();
            bottomBar.setFirstSelectedPosition(1);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.IS_FAIL)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.framelayout, homeFragment);
            fragmentTransaction.commit();
            bottomBar.setFirstSelectedPosition(0);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.framelayout, homeFragment);
            fragmentTransaction.commit();
            bottomBar.setFirstSelectedPosition(0);
        }
    }
    //控制Fragment显隐

    private void hideFragments(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (businessFragment != null) {
            transaction.hide(businessFragment);
        }
        if (examineFragment != null) {
            transaction.hide(examineFragment);
        }
        if (informationFragment != null) {
            transaction.hide(informationFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    //底部点击
    @Override
    public void onTabSelected(int position) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        sp.insertKey(getApplicationContext(), "isfer", false);
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.framelayout, homeFragment);
                } else {
                    fragmentTransaction.show(homeFragment);
//                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为黑色
                    StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);

                }
                break;
            case 2:
                if (businessFragment == null) {
                    businessFragment = new FriendCircleFragment();
                    fragmentTransaction.add(R.id.framelayout, businessFragment);
                    StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
//                    NavigationBar.Statedata(this);
                } else {
                    fragmentTransaction.show(businessFragment);
                    StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
//                    NavigationBar.Statedata(this);
                }
                break;
            case 1:
                if (islogine) {
//                if (examineFragment == null) {
//                    examineFragment = new ExamineFragment();
//                    fragmentTransaction.add(R.id.framelayout, examineFragment);
//                    StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
//                } else {
//                    fragmentTransaction.show(examineFragment);
////                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏字体为黑色
//                    StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
//                }
                    startActivity(new Intent(this, YangShiActivity.class));
                    finish();
//                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    Intent infoIntent = new Intent(this, WithoutCodeLoginActivity.class);
//                    infoIntent.putExtra("login_type", BizConstant.BUSINESS_LOGIN);
                    startActivity(infoIntent);
                    //activity切换动画效果
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

                break;
            case 3:
                sp = SharedPreferencesUtils.getUtil();
                boolean islogins = (boolean) sp.getKey(this, "islogin", false);
                if (islogins) {
                    if (informationFragment == null) {
                        informationFragment = new InformationFragment();
                        fragmentTransaction.add(R.id.framelayout, informationFragment);
                        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
                    } else {
                        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
                        fragmentTransaction.show(informationFragment);
                    }
                } else {
                    Intent infoIntent = new Intent(this, WithoutCodeLoginActivity.class);
                    infoIntent.putExtra("login_type", BizConstant.INFO_LOGIN);
                    startActivity(infoIntent);
                    //activity切换动画效果
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
                break;
            case 4:
                //判断是否登录
                sp = SharedPreferencesUtils.getUtil();
                boolean islogin = (boolean) sp.getKey(this, "islogin", false);
                if (islogin) {
                    if (myFragment == null) {
                        myFragment = new MyFragment();
                        fragmentTransaction.add(R.id.framelayout, myFragment);
                    } else {
                        fragmentTransaction.show(myFragment);
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
                        StatusBarUtil.setColor(this, getResources().getColor(R.color.reded), 0);
                    }
                } else {
                    Intent myIntent = new Intent(this, WithoutCodeLoginActivity.class);
                    myIntent.putExtra("login_type", BizConstant.MYLOGIN);
                    startActivity(myIntent);
                    //activity切换动画效果
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
                break;
        }
        fragmentTransaction.commit();
    }


    /**
     * 设置tab数字提示加缩放动画
     */
    private void setBadgeNum(int num) {
        mBadgeItem.setText(String.valueOf(num));
        if (num == 5) {
            mBadgeItem.hide();
        } else {
            mBadgeItem.show();
        }
    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }


    private boolean mIsExit;

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();

            } else {
                Toast.makeText(getApplicationContext(), getText(R.string.enter_exit_app), Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                        RecordUserBehavior.recordUserBehavior(getApplicationContext(), BizConstant.CLOSE_APP);
                        //关闭整个程序
//                        SysApplication.getInstance().exit();
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //会话列表变更
    private void initMessageListener() {
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(new Observer<List<RecentContact>>() {
                    @Override
                    public void onEvent(List<RecentContact> recentContacts) {
                        if (recentContacts != null && recentContacts.size() > 0) {
                            //EventBus.getDefault().post(true);
                            updateUnreadCount();//更新消息数据
                        } else {
                            mBadgeItem.hide();
                        }
                    }
                }, true);
    }

    /*
     * 获取当前程序的版本名
     */
    private String getVersionName() {
        PackageInfo packInfo = null;
        try {
            //获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packInfo.versionName;

    }


    /*
     * 获取当前程序的版本号
     */
    private int getVersionCode() {
        try {
            //获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return 1;
    }


    /**
     * 当activity销毁时不保存其内部的view的状态
     *
     * @paramoutState
     */

    @Override

    public void onSaveInstanceState(Bundle outState) {

//将super调用取消即可，表明当意外(比如系统内存吃紧将应用杀死)发生我不需要保存Fragmentde状态和数据等

//super.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //E.取消EventBus的注册
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {

            }
            if (resultCode == Activity.RESULT_OK) {

                String result = data.getExtras().getString("result");
                if (businessFragment != null) {
                    businessFragment.onActivityResult(requestCode & 0xffff, requestCode, data);
                }
                if (myFragment != null) {
                    myFragment.onActivityResult(requestCode & 0xffff, requestCode, data);
                }
                if (StringUtil.isNotEmpty(result)) {
                    JSONObject jobj = new JSONObject(result.toString());
                    String qrType = jobj.getString("qr_Type");
                    String qrCode = jobj.getString("qr_Code");
                    //扫一扫加好友，加群回调
                    if (StringUtil.isNotEmpty(qrType) && qrType.equals(BizConstant.ALREADY_FAVORITE)) {
                        SessionHelper.query(getApplicationContext(), qrCode);
                    } else if (StringUtil.isNotEmpty(qrType) && qrType.equals(BizConstant.ALIPAY_METHOD)) {
                        SessionHelper.queryTeamById(getApplicationContext(), qrCode);
                    } else {
                        ToastUtils.showToast(getApplicationContext(), result);
                    }
                }

                //创建群回传
                if (requestCode == REQUEST_CODE_ADVANCED) {
                    final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                    TeamCreateHelper.createAdvancedTeam(MainActivity.this, selected, new RequestCallback<CreateTeamResult>() {
                        @Override
                        public void onSuccess(CreateTeamResult createTeamResult) {
                            final Team team = createTeamResult.getTeam();
                            NimUIKit.startTeamSession(MainActivity.this, team.getId());
                        }

                        @Override
                        public void onFailed(int i) {
                        }

                        @Override
                        public void onException(Throwable throwable) {
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 版本更新
     *
     * @param updateCodeBean
     */
    @Override
    public void showUpdataVersion(UpdateCodeBean updateCodeBean) {
        if (NetUtils.isConnected(getApplicationContext())) {
            //判断是否更新版本
            version = updateCodeBean.data.have_new_version;
            //更新版本
            description = updateCodeBean.data.description;
            //强制更新
            forceUpdate = updateCodeBean.data.force_update;
            package_download_url = updateCodeBean.data.package_download_url;
        }
        checkVersion();
    }

    /**
     * 鞅分未领取
     *
     * @param unclaimedMbpCountBean
     */
    @Override
    public void showUnclaimedMbpCount(UnclaimedMbpCountBean unclaimedMbpCountBean) {
        if (unclaimedMbpCountBean.status == 1) {
            mbpCount = unclaimedMbpCountBean.data.unclaimed_mbp_count;//鞅分数量
            date();
        }

    }

    /**
     * 红包权限
     *
     * @param hongbaoPermissionBean
     */
    @Override
    public void showHongBaoPermission(HongbaoPermissionBean hongbaoPermissionBean) {
        if (hongbaoPermissionBean.status == 1) {
            String has_permission = hongbaoPermissionBean.data.has_permission;
            if (sp != null)
                sp.insertKey(context, "has_permission", has_permission);
        }
    }

    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    //判断应用第一次启动
    private void date() {
        isfer = (boolean) sp.getKey(getApplicationContext(), "isfer", false);
        if (isfer) {
            if (StringUtil.isNotEmpty(mbpCount)) {
                if (Double.valueOf(mbpCount) > 0) {
                    yangFenUnclaimedWindow = new YangFenUnclaimedWindow(MainActivity.this, listeners);
                    yangFenUnclaimedWindow.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    View yangFenView = yangFenUnclaimedWindow.getContentView();//send_head
                    TextView yangFenNum = (TextView) yangFenView.findViewById(R.id.yangfen_num);
                    yangFenNum.setText(mbpCount);
                }
            }

        } else {
            //第二次进入跳转
        }
    }

    private View.OnClickListener listeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close:
                    yangFenUnclaimedWindow.dismiss();
                    yangFenUnclaimedWindow.backgroundAlpha(MainActivity.this, 1f);
                    break;
                case R.id.go_yangfen:
                    startActivity(new Intent(getApplicationContext(), HashrateActivity.class));
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    yangFenUnclaimedWindow.dismiss();
                    yangFenUnclaimedWindow.backgroundAlpha(MainActivity.this, 1f);

                    break;

            }
        }
    };
}

