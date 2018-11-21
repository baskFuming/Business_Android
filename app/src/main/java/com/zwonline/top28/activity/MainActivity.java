package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
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
import com.xys.libzxing.zxing.common.Constant;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseMainActivity;
import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UpdateCodeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.fragment.ExamineFragment;
import com.zwonline.top28.fragment.FriendCircleFragment;
import com.zwonline.top28.fragment.HomeFragment;
import com.zwonline.top28.fragment.InformationFragment;
import com.zwonline.top28.fragment.MyFragment;
import com.zwonline.top28.fragment.YangShiFragment;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.nim.team.TeamCreateHelper;
import com.zwonline.top28.presenter.MainPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.CacheDataManager;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.MyYAnimation;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.badge.MainBadgeView;
import com.zwonline.top28.utils.popwindow.CustomPopuWindow;
import com.zwonline.top28.utils.popwindow.RedacketPopWindow;
import com.zwonline.top28.utils.popwindow.YangFenUnclaimedWindow;
import com.zwonline.top28.view.IMainActivity;
import com.zwonline.top28.web.BaseWebViewActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindArray;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class MainActivity extends BaseMainActivity<IMainActivity, MainPresenter> implements RadioGroup.OnCheckedChangeListener, IMainActivity {
    public SharedPreferencesUtils sp;
    private HomeFragment homeFragment;
    private FriendCircleFragment businessFragment;
    private InformationFragment informationFragment;
    private MyFragment myFragment;
    private YangShiFragment yangShiFragment;
    private ExamineFragment examineFragment;
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
    private Fragment mFragment;
    private int page = 1;
    private RadioButton rbHome;
    private RadioButton rbYangShi;
    private RadioButton rbBusinessCircle;
    private RadioButton rbInfo;
    private RadioButton rbMy;
    private MainBadgeView badgeView;
    private ImageView infoGuide;
    private SharedPreferences fristInfo;
    private boolean isFristInfo;
    private RedacketPopWindow redacketPopWindow;
    private ImageView receive;
    private LinearLayout readbackground1;
    private LinearLayout readbackground2;
    private TextView recriveCode;
    private String redStatus;//是否是新人判断
    private String yangfenCheatsUrl;//鞅分秘籍URL
    private TextView introduc_user;
    private String content1;
    private String content2;
    private String content3;
    private String btnName;
    private TextView describeOne;
    private TextView describeTwo;
    private TextView describeThree;
    private TextView text_busin;
    private DownloadManager mDownloadManager;
    private long mId;
    private ProgressBar mProgressBar;
    private TextView coerceSure;

    @Override
    protected void init() {
        new RecentContactsFragment();
        homeFragment = new HomeFragment();
        businessFragment = new FriendCircleFragment();
        informationFragment = new InformationFragment();
        myFragment = new MyFragment();
        yangShiFragment = new YangShiFragment();
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
        initFragmentManager();
        presenter.HongBaoPermission(getApplicationContext());//红包权限

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

//        assignViews();
        updateUnreadCount();//更新消息数据
        initMessageListener();
        int code = getVersionCode();//获取版本号
        if (NetUtils.isConnected(getApplicationContext())) {
            if (islogine) {
                presenter.RegisterRedPacketDialogs(this, BizConstant.REDPACKETDIALOG, BizConstant.TYPE_ONE);//新人注册红包弹窗
            }
            presenter.UpdataVersion(getApplicationContext(), platform, String.valueOf(code));
        } else {
            ToastUtils.showToast(getApplicationContext(), "请检查网络");
        }


    }

    /**
     *
     */
    private void initFragmentManager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.MYLOGIN)) {
            fragmentManager.beginTransaction().add(R.id.framelayout, myFragment).commit();
            mFragment = myFragment;
            rbMy.setChecked(true);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.INFO_LOGIN)) {
            fragmentManager.beginTransaction().add(R.id.framelayout, informationFragment).commit();
            mFragment = informationFragment;
            rbInfo.setChecked(true);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.TYPE_TWO)) {
            fragmentManager.beginTransaction().add(R.id.framelayout, businessFragment).commit();
            mFragment = businessFragment;
            rbBusinessCircle.setChecked(true);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.TYPE_ONE)) {
            fragmentManager.beginTransaction().add(R.id.framelayout, homeFragment).commit();
            mFragment = homeFragment;
            rbHome.setChecked(true);
        } else if (StringUtil.isNotEmpty(loginType) && loginType.equals(BizConstant.IS_FAIL)) {
            fragmentManager.beginTransaction().add(R.id.framelayout, homeFragment).commit();
            mFragment = homeFragment;
            rbHome.setChecked(true);
        } else {
            fragmentManager.beginTransaction().add(R.id.framelayout, homeFragment).commit();
            mFragment = homeFragment;
            rbHome.setChecked(true);
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
        RadioGroup navigationBar = (RadioGroup) findViewById(R.id.rg_main);
        rbHome = (RadioButton) findViewById(R.id.rb_home);
        infoGuide = findView(R.id.info_guide);
        infoGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = fristInfo.edit();//创建状态储存文件
                edit.putBoolean("isFristInfo", false);//将参数put，改变其状态
                edit.commit();//保证文件的创建和编辑
                infoGuide.setVisibility(View.GONE);
            }
        });
        rbYangShi = (RadioButton) findViewById(R.id.rb_yangshi);
        rbBusinessCircle = (RadioButton) findViewById(R.id.rb_business_circle);
        rbInfo = (RadioButton) findViewById(R.id.rb_info);
        rbMy = (RadioButton) findViewById(R.id.rb_my);
        Button btn_msg = (Button) findViewById(R.id.btn_msg);
        badgeView = new MainBadgeView(this);
        badgeView.setTargetView(btn_msg);
        badgeView.setTextSize(10);
        badgeView.setBadgeMargin(0, 0, 12, 10);
        navigationBar.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        sp.insertKey(getApplicationContext(), "isfer", false);
        switch (checkedId) {
            case R.id.rb_home:
                rbHome.setChecked(true);
                switchFragment(homeFragment);
                StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                page = 1;
                break;
            case R.id.rb_yangshi:
                if (islogine) {
//                    if (page == 1) {
//                        rbHome.setChecked(true);
//                        switchFragment(homeFragment);
//                        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    } else if (page == 3) {
//                        rbBusinessCircle.setChecked(true);
//                        switchFragment(businessFragment);
//                        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    } else if (page == 4) {
//                        rbInfo.setChecked(true);
//                        switchFragment(informationFragment);
//                        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    } else if (page == 5) {
//                        rbMy.setChecked(true);
//                        switchFragment(myFragment);
//                        StatusBarUtil.setColor(this, getResources().getColor(R.color.reded), 0);
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
//                    }
//
//                    startActivity(new Intent(this, YangShiActivity.class));
//                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    rbInfo.setChecked(false);
                    rbHome.setChecked(false);
                    rbMy.setChecked(false);
                    rbBusinessCircle.setChecked(false);
                    rbYangShi.setChecked(true);
                    switchFragment(yangShiFragment);
                    StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    Intent infoIntent = new Intent(this, WithoutCodeLoginActivity.class);
//                    infoIntent.putExtra("login_type", BizConstant.BUSINESS_LOGIN);
                    startActivity(infoIntent);
                    //activity切换动画效果
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
                break;
            case R.id.rb_business_circle:
                rbBusinessCircle.setChecked(true);
                switchFragment(businessFragment);
                StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                page = 3;
                break;
            case R.id.rb_info:
                if (islogine) {
                    fristInfo = getSharedPreferences("fristInfo", 0);
                    //这个文件里面的布尔常量名，和它的初始状态，状态为是，则触发下面的方法
                    isFristInfo = fristInfo.getBoolean("isFristInfo", true);
                    rbInfo.setChecked(true);
                    rbHome.setChecked(false);
                    rbMy.setChecked(false);
                    rbBusinessCircle.setChecked(false);
                    rbYangShi.setChecked(false);
                    if (isFristInfo) {
                        infoGuide.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor edit = fristInfo.edit();//创建状态储存文件
                        edit.putBoolean("isFristInfo", false);//将参数put，改变其状态
                        edit.commit();//保证文件的创建和编辑
                    } else {
                        infoGuide.setVisibility(View.GONE);
                    }
                    switchFragment(informationFragment);
                    StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    page = 4;
                } else {
                    Intent infoIntent = new Intent(this, WithoutCodeLoginActivity.class);
//                    infoIntent.putExtra("login_type", BizConstant.BUSINESS_LOGIN);
                    infoIntent.putExtra("login_type", BizConstant.INFO_LOGIN);
                    startActivity(infoIntent);
                    //activity切换动画效果
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    startActivity(infoIntent);
                    //activity切换动画效果
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

                break;
            case R.id.rb_my:
                if (islogine) {
                    rbMy.setChecked(true);
                    switchFragment(myFragment);
                    StatusBarUtil.setColor(this, getResources().getColor(R.color.reded), 0);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
                    page = 5;
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
    }

    private void switchFragment(Fragment fragment) {
        //判断当前显示的Fragment是不是切换的Fragment
        if (mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.framelayout, fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    //检测本程序的版本，这里假设从服务器中获取到最新的版本号为3
    public void checkVersion() {
        //如果检测本程序的版本号小于服务器的版本号，那么提示用户更新
        if (!StringUtil.isEmpty(version) && version.equals("1")) {
//            showDialogUpdate();//弹出提示版本更新的对话框
            customPopuWindow = new CustomPopuWindow(MainActivity.this, description, forceUpdate, listener);
            mian.post(new Runnable() {
                @Override
                public void run() {
                    customPopuWindow.showAtLocation(mian, Gravity.CENTER, 0, 0);
                    View customView = customPopuWindow.getContentView();
                    mProgressBar = customView.findViewById(R.id.pb_progressbar_bar);
                    coerceSure = customView.findViewById(R.id.coerce_sure);
                }
            });

//            customPopuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            if (islogine) {//判断是否登录
                /**
                 * 前提是是否登录状态
                 * 判断是否是新人，redStatus是1是新人弹出新人领商机币红包，其他调查看鞅分接口
                 */
                if (StringUtil.isNotEmpty(redStatus) && redStatus.equals(BizConstant.NEW)) {
                    //新用户随机红包
                    redacketPopWindow = new RedacketPopWindow(MainActivity.this, reaListener);
                    View redacketView = redacketPopWindow.getContentView();
                    receive = redacketView.findViewById(R.id.receive);
                    introduc_user = redacketView.findViewById(R.id.introduc_user);
                    if (StringUtil.isNotEmpty(content1)) {
                        introduc_user.setText(content1);
                    }
                    //红包取值范围
                    TextView get_boc_scope = redacketView.findViewById(R.id.get_boc_scope);
                    if (StringUtil.isNotEmpty(content2)) {
                        get_boc_scope.setText(content2);
                    }
                    //价值
                    TextView get_value = redacketView.findViewById(R.id.get_value);
                    if (StringUtil.isNotEmpty(content3)) {
                        get_value.setText(content3);
                    }
                    TextView click_get = redacketView.findViewById(R.id.click_get);
//                    if (StringUtil.isNotEmpty(btnName)) {
//                        click_get.setText(btnName);
//                    }
                    describeOne = redacketView.findViewById(R.id.describe_one);
                    describeTwo = redacketView.findViewById(R.id.describe_two);
                    describeThree = redacketView.findViewById(R.id.describe_three);
                    recriveCode = redacketView.findViewById(R.id.recrive_code);
                    readbackground1 = redacketView.findViewById(R.id.readbackground1);
                    readbackground2 = redacketView.findViewById(R.id.readbackground2);
                    text_busin = redacketView.findViewById(R.id.text_busin);
                    mian.post(new Runnable() {
                        @Override
                        public void run() {
                            redacketPopWindow.showAtLocation(mian, Gravity.CENTER, 0, 0);
                        }
                    });
                } else {
                    //yangFenLingQu();//鞅分领取
                    presenter.UnclaimedMbpCount(getApplicationContext());
                }


            }

            //否则吐司，说现在是最新的版本
//            Toast.makeText(this, "当前已经是最新的版本", Toast.LENGTH_SHORT).show();
//            Log.v("updata", "当前已经是最新的版本");
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
//                    listener(package_download_url);
                    LanguageUitils.gotoBrowserDownload(context, package_download_url);//直接跳浏览器
                    break;
                case R.id.coerce_sure:
//                    listener(package_download_url);
//                    coerceSure.setVisibility(View.GONE);
//                    mProgressBar.setVisibility(View.VISIBLE);
                    LanguageUitils.gotoBrowserDownload(context, package_download_url);//直接跳浏览器
                    break;
            }
        }
    };
    /***
     * 首页新用户随机红包
     */
    private View.OnClickListener reaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.receive://打开红包
                    presenter.RegisterRedPacketDialogs(MainActivity.this, BizConstant.SHOWREDPACKETDIALOG, BizConstant.TYPE_TWO);
                    MyYAnimation myYAnimation = new MyYAnimation();
                    myYAnimation.setRepeatCount(Animation.INFINITE); //旋转的次数（无数次）
                    receive.startAnimation(myYAnimation);
                    break;
                case R.id.text_busin://查看商机币
                    Intent intent = new Intent(MainActivity.this, IntegralActivity.class);
                    intent.putExtra("type", BizConstant.RECOMMEND);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                    redacketPopWindow.dismiss();
//                    redacketPopWindow.backgroundAlpha(MainActivity.this, 1f);
                    break;
                case R.id.text_cheats://前往秘籍
                    Intent intent1 = new Intent(MainActivity.this, BaseWebViewActivity.class);
                    intent1.putExtra("weburl", yangfenCheatsUrl);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                    redacketPopWindow.dismiss();
//                    redacketPopWindow.backgroundAlpha(MainActivity.this, 1f);
                    break;
            }
        }
    };


    //E.接收Application中监听到的未读消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(boolean flag) {
        if (flag) {
            badgeView.setBadgeCount(unreadMsgsCount);
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
        badgeView.setBadgeCount(unreadMsgsCount);
    }

    //重新获取焦点,着再次刷新一下未读消息
    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadCount();

    }


    private boolean mIsExit;

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (redacketPopWindow != null) {
            redacketPopWindow.dismiss();
            redacketPopWindow.backgroundAlpha(MainActivity.this, 1f);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mIsExit) {
                MainActivity.this.finish();

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
//                            mBadgeItem.hide();
                        }
                    }
                }, true);
    }


    /*
     * 获取当前程序的版本号
     */
    private int getVersionCode() {
        try {
            //获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return 1;
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

                String result = data.getStringExtra(Constant.CODED_CONTENT);

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
                if (businessFragment != null) {
                    businessFragment.onActivityResult(requestCode & 0xffff, requestCode, data);
                }
                if (myFragment != null) {
                    myFragment.onActivityResult(requestCode & 0xffff, requestCode, data);
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
        if (NetUtils.isConnected(this)) {
            //判断是否更新版本
            version = updateCodeBean.data.have_new_version;
            //更新版本
            description = updateCodeBean.data.description;
            //强制更新
            forceUpdate = updateCodeBean.data.force_update;

            package_download_url = updateCodeBean.data.package_download_url;
            String flush_cache = updateCodeBean.data.flush_cache;
            //清理缓存
            if (StringUtil.isNotEmpty(flush_cache) && flush_cache.equals(BizConstant.TYPE_ONE)) {
                CacheDataManager.clearAllCache(MainActivity.this);
            }
            checkVersion();
        }
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

    /**
     * 新人注册红包弹窗
     *
     * @param registerRedPacketBean
     */
    @Override
    public void showRedPacketDialog(RegisterRedPacketsBean.DataBean.DialogItemBean.RegisterRedPacketBean registerRedPacketBean) {
        if (StringUtil.isNotEmpty(registerRedPacketBean.status)) {
            redStatus = registerRedPacketBean.status;
            content1 = registerRedPacketBean.content1;
            content2 = registerRedPacketBean.content2;
            content3 = registerRedPacketBean.content3;
//            btnName = registerRedPacketBean.btn1.name;
        }

    }

    /**
     * 点击领取新人红包
     *
     * @param registerRedPacketBean
     */
    @Override
    public void showGetRedPacketDialog(RegisterRedPacketsBean.DataBean.DialogItemBean.ShowRegisterRedPacketBean registerRedPacketBean) {
        receive.clearAnimation();
        readbackground1.setVisibility(View.GONE);
        readbackground2.setVisibility(View.VISIBLE);
        if (StringUtil.isNotEmpty(registerRedPacketBean.content2)) {
            recriveCode.setText(registerRedPacketBean.content2);//获取多少商机币
        }
        describeOne.setText(registerRedPacketBean.content1);
        describeTwo.setText(registerRedPacketBean.content3);
        describeThree.setText(registerRedPacketBean.content4);
        if (StringUtil.isNotEmpty(registerRedPacketBean.btn2.action)) {
            yangfenCheatsUrl = Api.baseUrl() + registerRedPacketBean.btn2.action;
        }
    }

    @Override
    public void Erro() {
        receive.clearAnimation();
        redacketPopWindow.dismiss();
        redacketPopWindow.backgroundAlpha(MainActivity.this, 1f);
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
            View v = getCurrentFocus();
//            if (LanguageUitils.isShouldHideInput(v, ev)) {
//                if (LanguageUitils.hideInputMethod(this, v)) {
//                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
//                }
//            }
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

    public static String formatBadgeNumber(int value) {
        if (value <= 0) {
            return null;
        }

        if (value < 100) {
            // equivalent to String#valueOf(int);
            return Integer.toString(value);
        }

        // my own policy
        return "99+";
    }


    private void listenerDownApk(String apkUrl) {
        //此处使用DownLoadManager开启下载任务
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        // 下载过程和下载完成后通知栏有通知消息。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载");
        request.setDescription("apk正在下载");
        //设置保存目录  /storage/emulated/0/Android/包名/files/Download
        request.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS, "jiaogeyi.apk");
        mId = mDownloadManager.enqueue(request);

        //注册内容观察者，实时显示进度
        MyContentObserver downloadChangeObserver = new MyContentObserver(null);
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, downloadChangeObserver);
        //Toast.makeText(MainActivity.this,"XXXX",Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long longExtra = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (mId == longExtra) {
//                    Uri downloadUri = mDownloadManager.getUriForDownloadedFile(id);
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    File apkFile = getExternalFilesDir("DownLoad/shangjitoutiao.apk");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri uriForFile = FileProvider.getUriForFile(context, "com.zwonline.top28.fileprovider", apkFile);
                        install.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                    } else {
                        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }

                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(install);
                    Toast.makeText(MainActivity.this, "ZZZZ", Toast.LENGTH_SHORT).show();
                }
            }

        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    class MyContentObserver extends ContentObserver {

        public MyContentObserver(Handler handler) {
            super(handler);
        }


        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mId);
            DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                float progress = (float) Math.floor(percent * 100);
//                mPrecent.setText(progress+"%");
                mProgressBar.setProgress((int) progress, true);
                if (progress == 100) {
                    customPopuWindow.dismiss();
                    customPopuWindow.backgroundAlpha(MainActivity.this, 1f);
                }
            }
        }

    }

}

