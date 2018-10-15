package com.zwonline.top28.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.carlos.notificatoinbutton.library.NotificationButton;
import com.jaeger.library.StatusBarUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AeosActivity;
import com.zwonline.top28.activity.ArticleActivity;
import com.zwonline.top28.activity.EnsurePoolActivity;
import com.zwonline.top28.activity.HashrateActivity;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.activity.InsuranceActivity;
import com.zwonline.top28.activity.MyAttentionsActivity;
import com.zwonline.top28.activity.MyCollectActivity;
import com.zwonline.top28.activity.MyExamineActivity;
import com.zwonline.top28.activity.MyFansesActivity;
import com.zwonline.top28.activity.MyIssueActivity;
import com.zwonline.top28.activity.MyProjectActivity;
import com.zwonline.top28.activity.MySettingActivity;
import com.zwonline.top28.activity.MyShareActivity;
import com.zwonline.top28.activity.RecommendUserActivity;
import com.zwonline.top28.activity.SettingActivity;
import com.zwonline.top28.activity.TransmitActivity;
import com.zwonline.top28.activity.WalletActivity;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.adapter.MyOneMunuAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.main.AnnouncementActivity;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.presenter.UserInfoPresenter;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.ObservableScrollView;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IUserInfo;

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

public class MyFragment extends BaseFragment<IUserInfo, UserInfoPresenter> implements IUserInfo {

    private static Bundle bundle;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.setting_relat)
    RelativeLayout settingRelat;
    @BindView(R.id.my_scrollview)
    ObservableScrollView myScrollview;
    @BindView(R.id.user_tou)
    ImageViewPlus userTou;
    @BindView(R.id.approve)
    ImageView approve;
    @BindView(R.id.title_user_name)
    TextView titleUserName;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.attestation)
    TextView attestation;
    @BindView(R.id.relative)
    RelativeLayout relative;
    @BindView(R.id.company)
    TextView company;
    @BindView(R.id.tv_guanzhu_num)
    TextView tvGuanzhuNum;
    @BindView(R.id.tv_guanzhu)
    TextView tvGuanzhu;
    @BindView(R.id.tv_fensi_num)
    TextView tvFensiNum;
    @BindView(R.id.tv_fensi)
    TextView tvFensi;
    @BindView(R.id.tv_shoucang_num)
    TextView tvShoucangNum;
    @BindView(R.id.tv_shoucang)
    TextView tvShoucang;
    @BindView(R.id.article)
    TextView article;
    @BindView(R.id.transmit)
    TextView transmit;
    @BindView(R.id.video)
    TextView video;
    @BindView(R.id.wallet)
    TextView wallet;
    @BindView(R.id.my_business)
    TextView myBusiness;
    @BindView(R.id.insurance)
    TextView insurance;
    @BindView(R.id.recommend_user)
    TextView recommendUser;
    @BindView(R.id.my_issue)
    TextView myIssue;
    @BindView(R.id.my_share)
    TextView myShare;
    @BindView(R.id.my_inspect)
    TextView myInspect;
    @BindView(R.id.aeo)
    TextView aeo;
    @BindView(R.id.management)
    TextView management;
    @BindView(R.id.ensure_pool)
    TextView ensurePool;
    @BindView(R.id.exit)
    TextView exit;
    Unbinder unbinder;
    @BindView(R.id.tv_guanzhu_linear)
    LinearLayout tvGuanzhuLinear;
    @BindView(R.id.tv_fensi_linear)
    LinearLayout tvFensiLinear;//my_relative
    @BindView(R.id.tv_shoucang_linear)
    LinearLayout tvShoucangLinear;
    @BindView(R.id.my_relative)
    RelativeLayout myRelative;
    @BindView(R.id.native_menu)
    LinearLayout nativeMenu;
    @BindView(R.id.menu_recy)
    RecyclerView menuRecy;
    Unbinder unbinder1;
    @BindView(R.id.notice_img)
    NotificationButton noticeImg;
    private SharedPreferencesUtils sp;
    private String nickName;
    private String username;
    private String signature;
    private String residence;
    private String uid;
    private String age;
    private String avatar;
    private String isDefaultPassword;
    private int imageHeight = 150; //设置渐变高度，一般为导航图片高度，自己控制
    private boolean islogins;
    private String nicknames;
    private String avatars;
    private List<MyPageBean.DataBean> menuList;
    private MyOneMunuAdapter myOneMenuAdapter;
    @Override
    protected void init(View view) {
        menuList = new ArrayList<>();
//        NavigationBar.Statedata(getActivity());
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.reded), 0);
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(getActivity(), "islogin", false);
        EventBus.getDefault().register(this);
        if (islogins) {
            presenter.mNoticeNotReadCount(getActivity());
            presenter.mUserInfo(getActivity());
            presenter.PersonCenterMenu(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.user_not_login, Toast.LENGTH_SHORT).show();
        }
        userName.setText((String) sp.getKey(getActivity(), "nickname", ""));
        titleUserName.setText((String) sp.getKey(getActivity(), "nickname", ""));
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
        Glide.with(getActivity()).load(sp.getKey(getActivity(), "avatar", "")).apply(options).into(userTou);
//        UserDatas();
        myOneMenuAdapter = new MyOneMunuAdapter(menuList,getActivity());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        scrollLinearLayoutManager.setScrollEnabled(false);
        menuRecy.setLayoutManager(scrollLinearLayoutManager);
        menuRecy.setNestedScrollingEnabled(false);
        menuRecy.setAdapter(myOneMenuAdapter);
        ScrollVieListener();

    }


    @Override
    protected UserInfoPresenter setPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.myfragment;
    }

    //获取个人信息
    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean.status == 1) {
            isDefaultPassword = userInfoBean.data.user.is_default_password;
            nickName = userInfoBean.data.user.nickname;
            age = userInfoBean.data.user.age;
            uid = userInfoBean.data.user.uid;
            if (sp != null) {
                sp.insertKey(getActivity(), "avatar", userInfoBean.data.user.avatar);
                sp.insertKey(getActivity(), "uid", userInfoBean.data.user.uid);
                sp.insertKey(getActivity(), "nickname", userInfoBean.data.user.nickname);
                sp.insertKey(getActivity(), "sign", userInfoBean.data.user.signature);
                sp.insertKey(getActivity(), "follow", userInfoBean.data.user.follow);
                sp.insertKey(getActivity(), "fans", userInfoBean.data.user.fans);
                sp.insertKey(getActivity(), "favorite", userInfoBean.data.user.favorite);
                sp.insertKey(getActivity(), "job_cate_pid", userInfoBean.data.user.job_cate_pid);
                sp.insertKey(getActivity(), "wx_page_type", userInfoBean.data.user.wx_page_type);
                sp.insertKey(getActivity(), "enterprise", userInfoBean.data.user.enterprise);
                sp.insertKey(getActivity(), "position", userInfoBean.data.user.position);
            }

            avatar = userInfoBean.data.user.avatar;
//        sp.insertKey(getActivity(), "avatar", avatar);
            if (sp != null) {
                sp.insertKey(getActivity(), "sharedUid", uid);
                tvGuanzhuNum.setText((String) sp.getKey(getActivity(), "follow", ""));
                tvFensiNum.setText((String) sp.getKey(getActivity(), "fans", ""));
                tvShoucangNum.setText((String) sp.getKey(getActivity(), "favorite", ""));
            }
            if (StringUtil.isNotEmpty(userInfoBean.data.user.nickname)) {
                username = userInfoBean.data.user.nickname;
//                userName.setText(userInfoBean.data.user.nickname+"");
            }

            signature = userInfoBean.data.user.signature;
            residence = userInfoBean.data.user.residence;

            String is_enterprise_user = userInfoBean.data.user.is_enterprise_auth_user;

            try {
                if (!StringUtil.isEmpty(is_enterprise_user) && is_enterprise_user.equals(BizConstant.ENTERPRISE_tRUE)) {
                    if (sp != null) {
                        sp.insertKey(getActivity(), "is_enterprise", BizConstant.ENTERPRISE_tRUE);
                    }
//                attestation.setVisibility(View.GONE);
                    management.setVisibility(View.GONE);
                } else {
                    if (sp != null) {
                        sp.insertKey(getActivity(), "is_enterprise", BizConstant.ENTERPRISE_FALSE);
                    }
//                attestation.setVisibility(View.VISIBLE);
                    company.setVisibility(View.VISIBLE);
                    approve.setVisibility(View.VISIBLE);
                    aeo.setVisibility(View.GONE);
                    management.setVisibility(View.VISIBLE);
                    company.setText(userInfoBean.data.user.company.enterprise_name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (userInfoBean.status == -1) {
            if (sp != null) {
                sp.insertKey(getActivity(), "islogin", false);
                sp.insertKey(getActivity(), "isUpdata", false);
            }

            RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.SIGN_OUT);
            startActivity(new Intent(getActivity(), WithoutCodeLoginActivity.class));
            SharedPreferences settings = getActivity().getSharedPreferences("SP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("diaog");
            editor.remove("avatar");
            editor.remove("uid");
            editor.remove("has_permission");
            editor.remove("nickname");
            editor.remove("token");
            editor.remove("account");
            editor.clear();
            editor.commit();
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        } else {
            ToastUtils.showToast(getActivity(), userInfoBean.msg);
        }
    }

    @OnClick({R.id.user_tou, R.id.tv_guanzhu_linear, R.id.tv_fensi_linear, R.id.tv_shoucang_linear, R.id.article, R.id.transmit, R.id.video, R.id.wallet, R.id.my_inspect, R.id.aeo, R.id.recommend_user, R.id.insurance, R.id.my_issue, R.id.my_share, R.id.my_business, R.id.exit, R.id.setting_relat, R.id.management, R.id.ensure_pool, R.id.notice_img})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }

        switch (view.getId()) {
            case R.id.user_tou://修改头像
                presenter.mUserInfo(getActivity());
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_AVATAR);
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("nickName", nickName);
                intent.putExtra("age", age);
                intent.putExtra("avatar", avatar);
                intent.putExtra("username", username);
                intent.putExtra("signature", signature);
                intent.putExtra("residence", residence);
                startActivityForResult(intent, 100);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.tv_guanzhu_linear://我的关注
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_FOLLOW);
                Intent guan = new Intent(getActivity(), MyAttentionsActivity.class);
                guan.putExtra("uid", uid);
                guan.putExtra("attention", R.string.center_my_followed);
                startActivity(guan);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.tv_fensi_linear://我的fans
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_FANS);
                Intent intent_fen = new Intent(getActivity(), MyFansesActivity.class);
                intent_fen.putExtra("uid", uid);
                intent_fen.putExtra("fans", R.string.center_my_fans);
                startActivity(intent_fen);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.tv_shoucang_linear://我的收藏
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_FAVORITE);
                Intent shou = new Intent(getActivity(), MyCollectActivity.class);
                shou.putExtra("uid", uid);
                shou.putExtra("collect", getString(R.string.center_my_favorite));
                startActivity(shou);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.article://发布文章
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PUBLISH_ARTICLE);
                Intent article_intent = new Intent(getActivity(), ArticleActivity.class);
                startActivity(article_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.transmit://转载文章
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_TRANSFER_ARTICLE);
                Intent transmit_intent = new Intent(getActivity(), TransmitActivity.class);
                startActivity(transmit_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.video://我的主页
//                if (StringUtil.isNotEmpty(uid)){
                Intent myintent = new Intent(getActivity(), HomePageActivity.class);
                myintent.putExtra("uid", uid);
                startActivity(myintent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                }else {
//                }
//
                break;
            case R.id.wallet://钱包
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_WALLET);
                Intent WalletIntent = new Intent(getActivity(), WalletActivity.class);
                WalletIntent.putExtra("back", BizConstant.ALREADY_FAVORITE);
                startActivity(WalletIntent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.my_inspect://我的考察
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_INVESTIGATION);
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_WALLET);
                startActivity(new Intent(getActivity(), MyExamineActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.aeo://企业认证
                Intent aeoIntent = new Intent(getActivity(), AeosActivity.class);
                aeoIntent.putExtra("uid", uid);
                aeoIntent.putExtra("project", BizConstant.ALIPAY_METHOD);
                startActivity(aeoIntent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.recommend_user://推荐用户
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_RECOMMEND_USER);
                Intent intent1 = new Intent(getActivity(), RecommendUserActivity.class);
                intent1.putExtra("jumPath", BizConstant.RECOMMENTUSER);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.insurance://守约宝
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_SHOUYUEBAO);
                String isEnter = (String) SharedPreferencesUtils.getUtil().getKey(getActivity(),
                        "is_enterprise", "");
                //个人 企业区分
                if (!StringUtil.isEmpty(isEnter) && BizConstant.IS_SUC.equals(isEnter)) {
                    Intent insuranceIntent = new Intent(getActivity(), MyProjectActivity.class);
                    insuranceIntent.putExtra("clickSource", BizConstant.CLICK_SYB);
                    startActivity(insuranceIntent);
                } else {
                    startActivity(new Intent(getActivity(), InsuranceActivity.class));
                }
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.my_issue://我的发布
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_PUBLISH);
                Intent issue_intent = new Intent(getActivity(), MyIssueActivity.class);
                issue_intent.putExtra("issue", getString(R.string.center_my_publishs));
                startActivity(issue_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.my_share://我的分享
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_PERSONAL_SHARE);
                Intent share_intent = new Intent(getActivity(), MyShareActivity.class);
                share_intent.putExtra("uid", uid);
                startActivity(share_intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.my_business://我的积分
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_POINT);
                startActivity(new Intent(getActivity(), HashrateActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.exit://联系客服
                NimUIKit.startP2PSession(getActivity(), BizConstant.SEVERCEUid);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.setting_relat://设置
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_SETTING_ICON);
                Intent settingIntent = new Intent(getActivity(), MySettingActivity.class);
                settingIntent.putExtra("nickname", nickName);
                settingIntent.putExtra("avatar", avatar);
                settingIntent.putExtra("is_default_password", isDefaultPassword);
                startActivity(settingIntent);
//                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.management://企业管理
                RecordUserBehavior.recordUserBehavior(getActivity(), BizConstant.CLICK_ENTERPRISE_MANAGE);
                startActivity(new Intent(getActivity(), MyProjectActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

            case R.id.ensure_pool://保障池
                startActivity(new Intent(getActivity(), EnsurePoolActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.notice_img://公告

                startActivity(new Intent(getActivity(), AnnouncementActivity.class));
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            userName.setText(data.getStringExtra("nickname"));
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                    .error(R.mipmap.no_photo_male);
            Glide.with(getActivity()).load(data.getStringExtra("avatar")).apply(options).into(userTou);
        } else {
            userName.setText(nickName);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                    .error(R.mipmap.no_photo_male);
            if (sp != null) {
                String avatared = (String) sp.getKey(getActivity(), "avatar", "");
                Glide.with(getActivity()).load(avatared).apply(options).into(userTou);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageFollow event) {
        nicknames = event.nickname;
        avatars = event.avatar;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (StringUtil.isNotEmpty(nicknames)) {
            titleUserName.setText(nicknames);
            userName.setText(nicknames);
        }
        if (StringUtil.isNotEmpty(avatars)) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                    .error(R.mipmap.no_photo_male);

            Glide.with(getActivity()).load(avatars).apply(options).into(userTou);
        }
        if (islogins) {
            presenter.mNoticeNotReadCount(getActivity());
            presenter.mUserInfo(getActivity());
        } else {
            Toast.makeText(getActivity(), R.string.user_not_login, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        if (getActivity()!=null){
//            getActivity().finish();
//        }
        if (presenter != null) {
            presenter = null;
        }
        if (sp != null) {
            sp = null;
        }
    }


    @Override
    public void showErro() {
        Toast.makeText(getActivity(), R.string.network_content_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 公告未读数量
     *
     * @param noticeNotReadCountBean
     */
    @Override
    public void showNoticeNoRead(NoticeNotReadCountBean noticeNotReadCountBean) {
        if (StringUtil.isEmpty(noticeNotReadCountBean.data.noticeNotReadCount) || "".equals(noticeNotReadCountBean.data.noticeNotReadCount)) {
            noticeImg.setNotificationNumber(0);
        } else if (StringUtil.isNotEmpty(noticeNotReadCountBean.data.noticeNotReadCount)) {
            noticeImg.setNotificationNumber(Integer.parseInt(noticeNotReadCountBean.data.noticeNotReadCount));
        }

    }

    /**
     * 菜单一级列表
     *
     * @param menuLists
     */
    @Override
    public void showPersonCenterMenu(List<MyPageBean.DataBean> menuLists) {
        nativeMenu.setVisibility(View.GONE);
        menuRecy.setVisibility(View.VISIBLE);
        menuList.clear();
        menuList.addAll(menuLists);
        myOneMenuAdapter.notifyDataSetChanged();
    }


    /**
     * 滑动监听隐藏title
     */
    public void ScrollVieListener() {
        //搜索框在布局最上面
        myRelative.bringToFront();
        //滑动监听
        myScrollview.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y <= 0) {
//                    myRelative.setBackgroundColor(Color.argb((int) 0, 111, 29, 26));//AGB由相关工具获得，或者美工提供
                } else if (y > 0 && y <= imageHeight) {
                    float scale = (float) y / imageHeight;
                    float alpha = (255 * scale);
                    titleUserName.setVisibility(View.GONE);
                } else {
                    titleUserName.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
