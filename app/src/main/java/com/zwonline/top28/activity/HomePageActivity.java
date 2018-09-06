package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.MyIssueAdapter;
import com.zwonline.top28.adapter.MyShareAdapter;
import com.zwonline.top28.base.BaseMainActivity;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.RealBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.fragment.ArticleFragmnet;
import com.zwonline.top28.fragment.OthersHomePageFrag;
import com.zwonline.top28.nim.DemoCache;
import com.zwonline.top28.presenter.HomePagePresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.NavigationBar;
import com.zwonline.top28.utils.NomalActionBarView;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.WXUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.MySharePopwindow;
import com.zwonline.top28.view.IHomePageActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

import static com.liaoinstan.springview.utils.DensityUtil.dip2px;

/**
 * 描述：个人主页
 *
 * @author YSG
 * @date 2018/1/16
 */
public class HomePageActivity extends BaseMainActivity<IHomePageActivity, HomePagePresenter> implements IHomePageActivity {
    private String uid;
    private SharedPreferencesUtils sp;
    private String nickname;
    private MessageFollow messageFollow;
    private int currentNum;
    private String is_attention;
    private String follow_count;
    private String fans_count;
    private String favorite_count;
    private TextView chat;
    private TextView guanzhu;
    private TextView shareNo;
    private TextView homepageNo;
    private TextView shareLine;
    private TextView articleLine;
    private TextView share;
    private TextView article;
    private LinearLayout tvShoucang;
    private TextView tvShoucangNum;
    private LinearLayout tvFensi;
    private TextView homepageTvFensiNum;
    private LinearLayout tvGuanzhu;
    private TextView tvGuanzhuNum;
    private TextView userName;
    private LinearLayout linear;
    private LinearLayout shareLinear;
    private LinearLayout articleLinear;
    private XRecyclerView shareRecy;
    private XRecyclerView articleRecy;
    private RelativeLayout relative;
    private RelativeLayout back;
    private RelativeLayout relativeLayout;
    private ImageViewPlus userTou;
    private String sex;
    private String chatted;//是否聊过天
    private String costPoint;//消耗积分数量
    private List<MyShareBean.DataBean> sList;
    private List<MyIssueBean.DataBean> iList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;
    private MyIssueAdapter issueAdapter;
    private MyShareAdapter shareAdapter;
    private boolean islogins;
    private String userUid;
    /**
     * 这里个人主页修改界面
     * 关注：将原来的关注ID跟换
     */
    private TextView text_singnature;
    private String name_Singnature;
    private TabLayout tablayout;
    private ViewPager viewPager;
    private NomalActionBarView toolbar;
    private ImageView img_left_after, img_left_befor;
    private ImageViewPlus add_user_after, imageUser;
    private TextView add_name_after, add_fr_befor, add_foll_befor, add_foll_after;
    private AppBarLayout appBarLayout;
    private LinearLayout add_after;
    private RelativeLayout add_befor;
    private String image_user;
    private ImageView daV_user;
    private ImageView user_dev;
    private MySharePopwindow myQrCodePopwindow;

    private IWXAPI api;
    private String realname;
    private String phone;
    private String wexinnumber;
    private String address;
    private String telphone;
    private String email;
    private String job_cate_pid;


    private String sharerealname;
    private String sharephone;
    private String sharewexinnumber;
    private String shareaddress;
    private String sharemail;
    private String share_job_cate_pid;


    private String realnames;
    private String phones;
    private String wexinnumbers;
    private String addresss;
    private String telphones;
    private String sex_cn;

    @Override
    protected void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
        NavigationBar.Statedata(this);
        api = WXAPIFactory.createWXAPI(this, "wx979d60eb9639eb65");
        initData();//查找控件
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(this, "islogin", false);
        userUid = (String) sp.getKey(this, "uid", "");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        sex = intent.getStringExtra("sex");

        sharewexinnumber = intent.getStringExtra("weixin");
        Log.e("weixin", sharewexinnumber + "");

        sharerealname = intent.getStringExtra("realname");
        sharephone = intent.getStringExtra("phone");
        sharemail = intent.getStringExtra("email");
        share_job_cate_pid = intent.getStringExtra("job_cate_pid");
        shareaddress = intent.getStringExtra("residence");
        sex_cn = intent.getStringExtra("sex_cn");
        updateUserOperatorView();
        iList = new ArrayList<>();
        sList = new ArrayList<>();
        presenter.mMyissue(this, uid, page);
        presenter.mMyShare(this, uid, page);
        presenter.mUserInfo(this);
        messageFollow = new MessageFollow();
        issueRecyclerViewData();
        shareRecyclerViewData();
        initView();
        presenter.mSetting(this, "", realname, 0, "", address, "", "", wexinnumber, "", phone, "");
    }

    /**
     * xRecyclerview发布文章配置
     */
    private void issueRecyclerViewData() {
        articleRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        articleRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        articleRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        articleRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        articleRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        articleRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(HomePageActivity.this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        articleRecy.setLayoutManager(scrollLinearLayoutManager);
        issueAdapter = new MyIssueAdapter(iList, this);
        articleRecy.setAdapter(issueAdapter);
    }

    /**
     * xRecyclerview分享文章配置
     */
    private void shareRecyclerViewData() {
        shareRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        shareRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        shareRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        shareRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        shareRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        shareRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(HomePageActivity.this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        shareRecy.setLayoutManager(scrollLinearLayoutManager);
        shareAdapter = new MyShareAdapter(sList, HomePageActivity.this);
        shareRecy.setAdapter(shareAdapter);
    }

    //查找控件
    private void initData() {
        text_singnature = (TextView) findViewById(R.id.text_sing_nature);
        userTou = (ImageViewPlus) findViewById(R.id.user_tou);
        tablayout = (TabLayout) findViewById(R.id.art_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.art_view_pager);
        imageUser = (ImageViewPlus) findViewById(R.id.user_head);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        toolbar = (NomalActionBarView) findViewById(R.id.toolbar);

        daV_user = (ImageView) findViewById(R.id.dv_user);
        View view = toolbar.getChildAt(0);
        //未折叠显示
        img_left_befor = view.findViewById(R.id.img_left_befor);
        add_fr_befor = view.findViewById(R.id.add_fr_befor);
        add_foll_befor = view.findViewById(R.id.add_foll_befor);
        add_befor = view.findViewById(R.id.add_befor);
        //折叠后显示
        img_left_after = view.findViewById(R.id.img_left_after);
        add_user_after = view.findViewById(R.id.add_user_after);
        add_name_after = view.findViewById(R.id.add_name_after);
        add_after = view.findViewById(R.id.add_after);

        user_dev = view.findViewById(R.id.user_dev);

        //加载Tobla
        initTol();
        img_left_befor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_left_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        chat = (TextView) findViewById(R.id.chat);
        guanzhu = (TextView) findViewById(R.id.guanzhu);
        shareNo = (TextView) findViewById(R.id.share_no);
        homepageNo = (TextView) findViewById(R.id.homepage_no);
        shareLine = (TextView) findViewById(R.id.share_line);
        articleLine = (TextView) findViewById(R.id.article_line);
        share = (TextView) findViewById(R.id.share);
        article = (TextView) findViewById(R.id.article);
        tvShoucang = (LinearLayout) findViewById(R.id.tv_shoucang);
        tvShoucangNum = (TextView) findViewById(R.id.tv_shoucang_num);
        tvFensi = (LinearLayout) findViewById(R.id.tv_fensi);
        homepageTvFensiNum = (TextView) findViewById(R.id.homepage_tv_fensi_num);
        tvGuanzhu = (LinearLayout) findViewById(R.id.tv_guanzhu);
        tvGuanzhuNum = (TextView) findViewById(R.id.tv_guanzhu_num);
        userName = (TextView) findViewById(R.id.user_name);
        linear = (LinearLayout) findViewById(R.id.linear);
        shareLinear = (LinearLayout) findViewById(R.id.share_linear);
        articleLinear = (LinearLayout) findViewById(R.id.article_linear);
        shareRecy = (XRecyclerView) findViewById(R.id.share_recy);
        articleRecy = (XRecyclerView) findViewById(R.id.article_recy);
        relative = (RelativeLayout) findViewById(R.id.relative);
        back = (RelativeLayout) findViewById(R.id.back);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        userTou = (ImageViewPlus) findViewById(R.id.user_tou);
        if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && userUid.equals(uid)) {
            guanzhu.setVisibility(View.GONE);
        }
    }

    //获取用户信息
    @Override
    public void showCompany(PersonageInfoBean companyBean) {
        image_user = companyBean.data.avatar;
        name_Singnature = companyBean.data.signature;
        is_attention = companyBean.data.did_i_follow;
        follow_count = companyBean.data.follow_count;
        fans_count = companyBean.data.fans_count;
        favorite_count = companyBean.data.article_count;
        homepageTvFensiNum.setText(fans_count);
        tvGuanzhuNum.setText(follow_count);
        tvShoucangNum.setText(favorite_count);
        nickname = companyBean.data.nickname;
        userName.setText(nickname);
        //大v显示与隐藏
        if (companyBean.data.identity_type.equals(BizConstant.IS_FAIL)) {
            daV_user.setVisibility(View.GONE);
            user_dev.setVisibility(View.GONE);
        } else {
            user_dev.setVisibility(View.VISIBLE);
            daV_user.setVisibility(View.VISIBLE);
        }
        if (StringUtil.isNotEmpty(name_Singnature)) {
            text_singnature.setText(name_Singnature);
        } else {
            text_singnature.setText("这个人很懒，什么也没有留下!");
        }
        if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && !uid.equals(userUid)) {
            if (companyBean.data.did_i_follow.equals("0")) {
                add_foll_befor.setText(R.string.common_btn_add_focus);
                add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            } else if (companyBean.data.did_i_follow.equals("1")) {
                add_foll_befor.setText(R.string.common_followed);
                add_foll_befor.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            }
        } else {
            if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && !companyBean.data.did_i_follow.equals("0")) {
                add_foll_befor.setText("分享");
                add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            } else if (!companyBean.data.did_i_follow.equals("1")) {
                add_foll_befor.setText("分享");
                add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            }
        }

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
        if (Util.isOnMainThread()) {
            Glide.with(getApplicationContext()).load(companyBean.data.avatar).apply(options)
                    .into(userTou);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.mExamineChat(this, uid);//检查是否聊过天
        presenter.mCompany(this, uid);
    }

    @Override
    protected HomePagePresenter getPresenter() {
        return new HomePagePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home_page;
    }

    @OnClick({R.id.tv_guanzhu, R.id.tv_fensi, R.id.tv_shoucang, R.id.back, R.id.guanzhu, R.id.chat, R.id.article, R.id.share, R.id.add_foll_befor})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.tv_guanzhu://关注
                Intent intent_guan = new Intent(HomePageActivity.this, MyAttentionActivity.class);
                intent_guan.putExtra("uid", uid);
                intent_guan.putExtra("attention", getString(R.string.myis_followed, nickname));
                startActivity(intent_guan);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.tv_fensi:
                Intent intent_fans = new Intent(HomePageActivity.this, MyFansActivity.class);
                intent_fans.putExtra("uid", uid);
                intent_fans.putExtra("fans", getString(R.string.myis_fans, nickname));
                startActivity(intent_fans);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.tv_shoucang:
                Intent intent_shou = new Intent(HomePageActivity.this, MyIssueActivity.class);
                intent_shou.putExtra("uid", uid);
                intent_shou.putExtra("collect", getString(R.string.myis_article, nickname));
                startActivity(intent_shou);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.add_foll_befor:
                if (islogins) {
                    if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && !uid.equals(userUid)) {
                        if (add_foll_befor.getText().toString().equals(getString(R.string.common_btn_add_focus))) {
                            presenter.mAttention(HomePageActivity.this, "follow", uid, BizConstant.ALREADY_FAVORITE);
                            add_foll_befor.setText(R.string.common_followed);
                            currentNum = Integer.parseInt((String) sp.getKey(HomePageActivity.this, "follow", "0")) + 1;
                            messageFollow.followNum = currentNum + "";
                            sp.insertKey(HomePageActivity.this, "follow", messageFollow.followNum);
                            EventBus.getDefault().post(messageFollow);
                            add_foll_befor.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
                            add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
                        } else {
                            presenter.mUnAttention(this, "un_follow", uid);
                            add_foll_befor.setText(R.string.common_btn_add_focus);
                            currentNum = Integer.parseInt((String) sp.getKey(HomePageActivity.this, "follow", "0")) - 1;
                            messageFollow.followNum = currentNum + "";
                            sp.insertKey(HomePageActivity.this, "follow", messageFollow.followNum);
                            EventBus.getDefault().post(messageFollow);
                            add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                            add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
                        }
                    } else if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && uid.equals(userUid)) {
                        if (add_foll_befor.getText().toString().equals("分享")) {
                            add_foll_befor.setText("分享");
                            add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                            add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
                            myQrCodePopwindow = new MySharePopwindow(this);
                            myQrCodePopwindow.showAtLocation(view, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                            View contentView = myQrCodePopwindow.getContentView();
                            //查找  布局控件
                            TextView editTextshare = contentView.findViewById(R.id.textshare);
                            final EditText editTextname = contentView.findViewById(R.id.share_name);
                            final EditText editTextphone = contentView.findViewById(R.id.share_phone);
                            final EditText editTexewxin = contentView.findViewById(R.id.share_wxin);
                            final EditText editTextaddress = contentView.findViewById(R.id.share_address);

                            if (StringUtil.isNotEmpty(nickname)) {
                                editTextname.setText(nickname);
                            }
                            if (StringUtil.isNotEmpty(phone)) {
                                editTextphone.setText(phone);
                            } else {
                                editTextphone.setText("请输入电话");
                            }
                            if (StringUtil.isNotEmpty(wexinnumber)) {
                                editTexewxin.setText(wexinnumber);
                            } else {
                                editTexewxin.setText("请输入微信号");
                            }
                            if (StringUtil.isNotEmpty(address)) {
                                editTextaddress.setText(address);
                            } else {
                                editTextaddress.setText("请输入地址");
                            }
                            final CheckBox checkBoxrealname = contentView.findViewById(R.id.share_check1);
                            checkBoxrealname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (checkBoxrealname.isChecked()) {
                                        editTextname.setText(realname);
                                    } else {
                                        editTextname.setText(nickname);
                                    }
                                }
                            });
                            final CheckBox checkBoxsharephone = contentView.findViewById(R.id.share_check2);
//                            checkBoxsharephone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    if (checkBoxsharephone.isChecked()) {
//                                        editTextphone.setText(phone);
//                                    } else {
//                                        editTextphone.setText("请输入电话");
//                                    }
//                                }
//                            });
                            final CheckBox checkBoxwxin = contentView.findViewById(R.id.share_check3);
//                            checkBoxwxin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    if (checkBoxwxin.isChecked()) {
//                                        editTexewxin.setText(wexinnumber);
//                                    } else {
//                                        editTexewxin.setText("请输入微信号");
//                                    }
//                                }
//                            });
                            final CheckBox checkBoxaddress = contentView.findViewById(R.id.share_check4);
//                            checkBoxaddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    if (checkBoxaddress.isChecked()) {
//                                        editTextaddress.setText(address);
//                                    } else {
//                                        editTextaddress.setText("请输入地址");
//                                    }
//                                }
//                            });
                            editTextshare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {  // 1 0
                                    if (checkBoxrealname.isChecked()) {
                                        realnames = BizConstant.ALREADY_FAVORITE;
                                    } else {
                                        realnames = BizConstant.NO_FAVORITE;
                                    }
                                    if (checkBoxsharephone.isChecked()) {
                                        phones = BizConstant.ALREADY_FAVORITE;
                                    } else {
                                        phones = BizConstant.NO_FAVORITE;
                                    }
                                    if (checkBoxwxin.isChecked()) {
                                        wexinnumbers = BizConstant.ALREADY_FAVORITE;
                                    } else {
                                        wexinnumbers = BizConstant.NO_FAVORITE;
                                    }
                                    if (checkBoxaddress.isChecked()) {
                                        addresss = BizConstant.ALREADY_FAVORITE;
                                    } else {
                                        addresss = BizConstant.NO_FAVORITE;
                                    }
                                    presenter.cardShareWXin(HomePageActivity.this, realnames, phones, wexinnumbers, addresss);
                                    presenter.mSetting(HomePageActivity.this, "", editTextname.getText().toString().trim(), Integer.parseInt(sex_cn), "", editTextaddress.getText().toString().trim(),
                                            "", "", editTexewxin.getText().toString().trim(),
                                            "", editTextphone.getText().toString().trim(), "");
                                    shareWXin(uid);
                                }

                            });

                        }
                    } else {
                        ToastUtils.showToast(HomePageActivity.this, "自己不能关注自己！");
                    }
                } else {
                    ToastUtils.showToast(this, "请先登录！");
                }
                break;
            case R.id.chat:
                if (islogins) {
                    if (chatted.equals("1")) {
                        presenter.mOnLineChat(this, uid);
                        // 打开单聊界面
                        NimUIKit.startP2PSession(this, uid);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else if (chatted.equals("0")) {
                        showNormalDialog();
                    }
                } else {
                    ToastUtils.showToast(this, "请先登录！");
                }
                break;
            case R.id.article:
                article.setTextColor(Color.RED);
                share.setTextColor(Color.BLACK);
                articleLinear.setVisibility(View.VISIBLE);
                shareLine.setBackgroundColor(Color.WHITE);
                articleLine.setBackgroundColor(Color.RED);
                shareLinear.setVisibility(View.GONE);
                break;
            case R.id.share:
                article.setTextColor(Color.BLACK);
                share.setTextColor(Color.RED);
                articleLinear.setVisibility(View.GONE);
                shareLine.setVisibility(View.VISIBLE);
                shareLinear.setVisibility(View.VISIBLE);
                articleLine.setBackgroundColor(Color.WHITE);
                shareLine.setBackgroundColor(Color.RED);
                break;
        }
    }
    //调起微信小程序
    private void shareWXin(String uid) {
        WXMiniProgramObject miniProgramObject = new WXMiniProgramObject();
        miniProgramObject.webpageUrl = "https://toutiao.28.com";//小程序网页地址
        miniProgramObject.userName = "gh_2c9958d8253e";//小程序ID
        miniProgramObject.path = "pages/article/article?user_id=" + uid;//小程序路径
        // 0.正式版本  1.测试版本  2.测试版本
        miniProgramObject.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgramObject);
        mediaMessage.title = "商机头条";//自定标题
        mediaMessage.description = "商机头条和会赚钱的人在一起";//描述
        Bitmap bitmap = BitmapFactory.decodeResource(HomePageActivity.this.getResources(), R.mipmap.id_card);
        Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        bitmap.recycle();
        mediaMessage.thumbData = WXUtils.bmpToByteArray(sendBitmap, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.message = mediaMessage;
        api.sendReq(req);
    }


    @Override
    public void showHomeDetails(HomeDetailsBean homeDetails) {
        if (!uid.equals(userUid)) {
            if (homeDetails.data.is_attention.equals("0")) {
                add_foll_befor.setText(R.string.common_btn_add_focus);
                add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            } else if (homeDetails.data.is_attention.equals("1")) {
                add_foll_befor.setText(R.string.common_followed);
                add_foll_befor.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            }
        } else {
            if (!homeDetails.data.is_attention.equals("0")) {
                add_foll_befor.setText("分享");
                add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            } else if (!homeDetails.data.is_attention.equals("0")) {
                add_foll_befor.setText("分享");
                add_foll_befor.setBackgroundResource(R.drawable.btn_ganzhu_red);
                add_foll_befor.setTextColor(Color.parseColor("#FDFDFD"));
            }
        }

    }

    /**
     * 判断是否是好友
     *
     * @param isFriend
     */
    private void updateAlias(final boolean isFriend) {
        if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && !uid.equals(userUid)) {
            if (isFriend) {
                add_fr_befor.setText("聊天");
            } else {
                add_fr_befor.setText("+好友");
            }
        } else {
            add_fr_befor.setText("数据分析");
        }
        add_fr_befor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isNotEmpty(uid) && StringUtil.isNotEmpty(userUid) && !uid.equals(userUid)) {
                    if (isFriend) {
                        NimUIKit.startP2PSession(HomePageActivity.this, uid);
                    } else {
                        onAddFriendByVerify(uid);
                    }
                } else {
                    Intent intent = new Intent(HomePageActivity.this, DataAnalysisActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 判断我的发布是否有数据
     *
     * @param flag
     */
    @Override
    public void showMyIssue(boolean flag) {
        if (flag) {
            homepageNo.setVisibility(View.GONE);
            articleRecy.setVisibility(View.VISIBLE);
        } else {
            homepageNo.setVisibility(View.VISIBLE);
            articleRecy.setVisibility(View.GONE);
        }
    }

    //发布文章
    @Override
    public void showMyIssueDate(List<MyIssueBean.DataBean> issueList) {
        if (page == 1) {
            iList.clear();
        }
        iList.addAll(issueList);
        issueAdapter.notifyDataSetChanged();
        issueAdapter.setOnClickItemListener(new MyIssueAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(HomePageActivity.this, HomeDetailsActivity.class);
                intent.putExtra("id", iList.get(position - 1).id + "");
                intent.putExtra("title", iList.get(position - 1).title);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        IssueLoadMore();
    }


    /**
     * 没有更多数据
     */
    @Override
    public void issueNoLoadMore() {
        if (this == null) {
            return;
        }
        if (page == 1) {
            iList.clear();
            issueAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }


    /**
     * 上拉刷新下拉加载
     */
    public void IssueLoadMore() {
        articleRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mMyissueLoad(HomePageActivity.this, uid, page);
                        if (articleRecy != null)
                            articleRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mMyissueLoad(HomePageActivity.this, uid, page);
                        if (articleRecy != null) {
                            articleRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }


    /**
     * 判别分享文章是否有数据
     *
     * @param flag
     */
    @Override
    public void showMyShare(boolean flag) {
        if (flag) {
            shareNo.setVisibility(View.GONE);
            shareRecy.setVisibility(View.VISIBLE);
        } else {
            shareNo.setVisibility(View.VISIBLE);
            shareRecy.setVisibility(View.GONE);
        }
    }

    //分享的文章
    @Override
    public void showMyShareDte(List<MyShareBean.DataBean> shareList) {
        if (page == 1) {
            sList.clear();
        }
        sList.addAll(shareList);
        shareAdapter.notifyDataSetChanged();
        shareAdapter.setOnClickItemListener(new MyShareAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(HomePageActivity.this, HomeDetailsActivity.class);
                intent.putExtra("id", sList.get(position - 1).id + "");
                intent.putExtra("title", sList.get(position - 1).title);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        shareLoadMore();
    }


    /**
     * 没有更多数据
     */
    @Override
    public void shareNoLoadMore() {
        if (this == null) {
            return;
        }
        if (page == 1) {
            sList.clear();
            shareAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }


    /**
     * 上拉刷新下拉加载
     */
    public void shareLoadMore() {
        articleRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mMyShareLoad(HomePageActivity.this, uid, page);
                        if (shareRecy != null)
                            shareRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mMyShareLoad(HomePageActivity.this, uid, page);
                        if (shareRecy != null) {
                            shareRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }


    @Override
    public void onErro() {
        if (HomePageActivity.this != null) {
            return;
        }
    }

    //检查是否聊过天
    @Override
    public void showExamineChat(ExamineChatBean.DataBean examineList) {
        chatted = examineList.chatted;
        costPoint = examineList.cost_point;
    }

    //在线聊天
    @Override
    public void showOnLineChat(AmountPointsBean amountPointsBean) {

    }

    @Override
    public void showShareWXin(RealBean realbean) {

    }

    //获取微信分享的内容
    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean.status == 1) {
            realname = userInfoBean.data.user.realname;
            phone = userInfoBean.data.user.phone;
            telphone = userInfoBean.data.user.telephone;
            wexinnumber = userInfoBean.data.user.weixin;
            address = userInfoBean.data.user.residence;
            sex_cn = userInfoBean.data.user.sex_cn;
        } else {
            ToastUtil.showToast(this, "请重新登录");
        }

    }

    @Override
    public void isSucceed() {

    }

    @Override
    public void showSetting(SettingBean headBean) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread()) {
            Glide.with(getApplicationContext()).pauseRequests();
        }
    }

    //Dialog弹窗
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(HomePageActivity.this);
        normalDialog.setMessage(getString(R.string.consume_integral) + costPoint + getString(R.string.coin_bole_coin));
        normalDialog.setPositiveButton(R.string.chat,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mOnLineChat(HomePageActivity.this, uid);
                        // 打开单聊界面
                        NimUIKit.startP2PSession(HomePageActivity.this, uid);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    }
                });
        normalDialog.setNegativeButton(R.string.common_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    //Dialog弹窗关注
    private void showNormalDialogs() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(HomePageActivity.this);
        normalDialog.setMessage(getString(R.string.is_willing_answer_calls));
        normalDialog.setPositiveButton(getString(R.string.willing),
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mAttention(HomePageActivity.this, "follow", uid, BizConstant.ALREADY_FAVORITE);

                    }
                });
        normalDialog.setNegativeButton(getString(R.string.unwillingness),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mAttention(HomePageActivity.this, "follow", String.valueOf(uid), BizConstant.NO_FAVORITE);
                    }
                });
        // 显示
        normalDialog.show();
    }

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, HomePageActivity.class);
        intent.putExtra("uid", account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    /**
     * --------------
     */
    //加载当Tolbar在折叠前和折叠后的样式
    private void initTol() {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    NavigationBar.Statedata(HomePageActivity.this);
                    //张开状态
                    add_befor.setVisibility(View.VISIBLE);
                    add_after.setVisibility(View.GONE);
                    StatusBarUtil.setColor(HomePageActivity.this, getResources().getColor(R.color.black), 0);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //收缩状态
                    add_befor.setVisibility(View.GONE);
                    add_after.setVisibility(View.VISIBLE);
                    StatusBarUtil.setColor(HomePageActivity.this, getResources().getColor(R.color.white), 0);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
                    if (Util.isOnMainThread()) {
                        Glide.with(getApplicationContext()).load(image_user).apply(options)
                                .into(add_user_after);
                        add_name_after.setText(nickname);
                    }
                } else {
                    int alpha = 255 - Math.abs(verticalOffset);
                    if (alpha < 0) {
                        //收缩toolbar
                        add_befor.setVisibility(View.GONE);
                        add_after.setVisibility(View.VISIBLE);
                    } else {
                        //张开toolbar
                        add_befor.setVisibility(View.VISIBLE);
                        add_after.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    //这里用于处理Tablayout的
    @Override
    protected void onStart() {
        super.onStart();
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tablayout.getChildAt(0);

                    int dp10 = dip2px(tablayout.getContext(), 70);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //加载数据Tablayout标题
    private void initView() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new OthersHomePageFrag();
                    case 1:
                        return new ArticleFragmnet();
                    default:
                        return new OthersHomePageFrag();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "动态";
                    case 1:
                        return "文章";
                    default:
                        return "未知";
                }
            }
        });
        tablayout.setupWithViewPager(viewPager);
    }

    /**
     * 判断是否是好友
     */
    private void updateUserOperatorView() {

        if (NIMClient.getService(FriendService.class).isMyFriend(uid)) {
            updateAlias(true);
        } else {
            updateAlias(false);
        }
    }


    /**
     * 通过验证方式添加好友
     */
    private void onAddFriendByVerify(final String account) {
        final EasyEditDialog requestDialog = new EasyEditDialog(HomePageActivity.this);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setTitle(HomePageActivity.this.getString(R.string.add_friend_verify_tip));
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                doAddFriend(account, msg, false);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();
    }

    /**
     * 添加好友
     *
     * @param account
     * @param msg
     * @param addDirectly
     */
    private void doAddFriend(String account, String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            Toast.makeText(getApplicationContext(), "不能加自己为好友", Toast.LENGTH_SHORT).show();
            return;
        }
        final VerifyType verifyType = addDirectly ? VerifyType.DIRECT_ADD : VerifyType.VERIFY_REQUEST;
        DialogMaker.showProgressDialog(HomePageActivity.this, "", true);
        NIMClient.getService(FriendService.class).addFriend(new AddFriendData(account, verifyType, msg))
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
//                        updateUserOperatorView();
                        if (VerifyType.DIRECT_ADD == verifyType) {
                            Toast.makeText(getApplicationContext(), "添加好友成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "添加好友请求发送成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        if (code == 408) {
                            Toast.makeText(getApplicationContext(), R.string.network_is_not_available, Toast
                                    .LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "自己不能添加自己", Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
