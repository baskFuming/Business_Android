package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zwonline.top28.APP;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BindWechatBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.BindWechatPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.CacheDataManager;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.BindWechatPopWindow;
import com.zwonline.top28.utils.popwindow.SuccessPopWindow;
import com.zwonline.top28.view.IbindWechatActivity;
import com.zwonline.top28.web.BaseWebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：设置页面
 *
 * @author YSG
 * @date 2018/1/23
 */
public class MySettingActivity extends BaseActivity<IbindWechatActivity, BindWechatPresenter> implements IbindWechatActivity {
    private SharedPreferencesUtils sp;
    private String isDefaultPassword;
    private TextView settingPassword;
    private RelativeLayout back;
    private ImageViewPlus imagHead;
    private TextView nickname;
    private RelativeLayout exitLogin;
    private RelativeLayout amend;
    private RelativeLayout amentpossword;
    private String avatar;
    private String nicknames;
    private String mobile;//判断是否有手机号绑定手机号
    private TextView bind;
    private RelativeLayout bindPhone;
    private ImageView bindImag;
    private ProgressBar clearProgress;
    private LinearLayout clearDialog;
    private TextView bindWechat;//绑定微信未绑定显示
    private String weChatUnionId;//微信union_id
    @BindView(R.id.text_cash)
    TextView tv_Cash;
    @BindView(R.id.bind_wechat_relative)
    RelativeLayout bindWechatRelative;
    private String invitation_uids;
    private String invitation_nicknames;
    private RelativeLayout reayoutRecommned;//我的推荐
    private TextView textMyRecond;
    private List<BindWechatBean.DataBean> list;
    private BindWechatPopWindow bindWechatPopWindow;
    private RelativeLayout re_Recommned;
    private ImageView imageRe;
    private ImageView bindWechatImag;
    private String invitation_nickname;
    private String invitation_uid;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 0:
                    clearDialog.setVisibility(View.GONE);
                    Toast.makeText(MySettingActivity.this, "清理完成", Toast.LENGTH_SHORT).show();
                    try {
                        tv_Cash.setText(CacheDataManager.getTotalCacheSize(MySettingActivity.this));
                    } catch (Exception e) {

                        e.printStackTrace();

                    }

            }
        }
    };

    @Override
    protected void init() {
        list = new ArrayList<>();
        settingPassword = (TextView) findViewById(R.id.setting_password);
        sp = SharedPreferencesUtils.getUtil();
        mobile = (String) sp.getKey(this, "mobile", "");
        weChatUnionId = (String) sp.getKey(this, "union_id", "");
        initData();
        Intent intent = getIntent();
        avatar = (String) sp.getKey(getApplicationContext(), "avatar", "");
        nicknames = (String) sp.getKey(getApplicationContext(), "nickname", "");
        invitation_nickname = intent.getStringExtra("invitation_nickname");
        invitation_uid = intent.getStringExtra("invitation_uid");
        nickname.setText(nicknames);
        isDefaultPassword = intent.getStringExtra("is_default_password");
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                .error(R.mipmap.no_photo_male);
        Glide.with(MySettingActivity.this).load(avatar).apply(options).into(imagHead);
        //判别是设置密码还是修改密码
        if (StringUtil.isNotEmpty(isDefaultPassword) && isDefaultPassword.equals("1")) {
            settingPassword.setText("设置密码");
        } else if (StringUtil.isNotEmpty(isDefaultPassword) && isDefaultPassword.equals("0")) {
            settingPassword.setText(this.getString(R.string.user_update_password));
        }
        try {
            //显示缓存的内存
            tv_Cash.setText(CacheDataManager.getTotalCacheSize(this));

        } catch (Exception e) {
            e.printStackTrace();
        }
        invitationData(invitation_nickname,invitation_uid);
    }

    /**
     * 判断有没有绑定上级
     */
    public void invitationData(String invitation_nickname,String invitation_uid) {

        if (StringUtil.isNotEmpty(invitation_uid)) {
            textMyRecond.setText(invitation_nickname);
            imageRe.setVisibility(View.GONE);
            re_Recommned.setClickable(false);
            textMyRecond.setTextColor(Color.parseColor("#d1d1d1"));
        } else {
            re_Recommned.setClickable(true);
            textMyRecond.setText("未绑定");
            imageRe.setVisibility(View.VISIBLE);
            textMyRecond.setTextColor(Color.parseColor("#ff2b2b"));
        }
    }

    @Override
    protected BindWechatPresenter getPresenter() {
        return new BindWechatPresenter(this, this);
    }

    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        imagHead = (ImageViewPlus) findViewById(R.id.imag_head);
        nickname = (TextView) findViewById(R.id.nickname);
        exitLogin = (RelativeLayout) findViewById(R.id.exit_login);
        amend = (RelativeLayout) findViewById(R.id.amend);
        bindPhone = (RelativeLayout) findViewById(R.id.bind_phone);
        amentpossword = (RelativeLayout) findViewById(R.id.amentpossword);
        bindImag = (ImageView) findViewById(R.id.bind_imag);
        clearProgress = (ProgressBar) findViewById(R.id.clear_progress);
        bind = (TextView) findViewById(R.id.bind);
        clearDialog = (LinearLayout) findViewById(R.id.clear_dialog);
        TextView clearTv = (TextView) findViewById(R.id.clear_tv);
        bindWechat = (TextView) findViewById(R.id.bind_wechat);
        textMyRecond = (TextView) findViewById(R.id.bind_myrecommd);
        re_Recommned = (RelativeLayout) findViewById(R.id.bind_remyrecommd);
        imageRe = (ImageView) findViewById(R.id.recommd_img);
        bindWechatImag = (ImageView) findViewById(R.id.bind_wechat_imag);
        clearTv.setSelected(true);
        //判别是否绑定微信
        if (StringUtil.isNotEmpty(weChatUnionId)) {
            bindWechat.setText("已绑定");
            bindWechatRelative.setClickable(false);
            bindWechatImag.setVisibility(View.GONE);
            bindWechat.setTextColor(Color.parseColor("#d1d1d1"));
        } else {
            presenter.Dialogs(this, BizConstant.WX_BIND, BizConstant.TYPE_ONE);//绑定微信
            bindWechat.setText("未绑定");
            bindWechatRelative.setClickable(true);
            bindWechat.setTextColor(Color.parseColor("#ff2b2b"));
            bindWechatImag.setVisibility(View.VISIBLE);
        }
        //判别是否绑定手机号
        if (StringUtil.isNotEmpty(mobile)) {
            bind.setText("已绑定");
            bindPhone.setClickable(false);
            bindImag.setVisibility(View.GONE);
            bind.setTextColor(Color.parseColor("#d1d1d1"));
        } else {
            presenter.Dialogs(this, BizConstant.MOBILE_BIND, BizConstant.TYPE_THREE);//绑定手机号
            bind.setText("未绑定");
            bind.setTextColor(Color.parseColor("#ff2b2b"));
            bindImag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_setting;
    }


    @OnClick({R.id.back, R.id.amend, R.id.amentpossword, R.id.exit_login, R.id.feedback, R.id.shield_settting, R.id.bind_phone
            , R.id.about_owen, R.id.lin_discash, R.id.look_playing, R.id.bind_wechat_relative, R.id.bind_remyrecommd})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                Intent backIntent = new Intent(MySettingActivity.this, MainActivity.class);
                backIntent.putExtra("loginType", BizConstant.MYLOGIN);
                startActivity(backIntent);
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.amend:
                startActivity(new Intent(MySettingActivity.this, SettingActivity.class));
//                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.amentpossword:
                if (StringUtil.isNotEmpty(isDefaultPassword) && isDefaultPassword.equals("1")) {
                    Intent intent = new Intent(MySettingActivity.this, RetPosswordActivity.class);
                    intent.putExtra("ispassword", "1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else if (StringUtil.isNotEmpty(isDefaultPassword) && isDefaultPassword.equals("0")) {
                    startActivity(new Intent(MySettingActivity.this, AmendPosswordActivity.class));
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
                break;
            case R.id.exit_login:
                sp.insertKey(this, "islogin", false);
                sp.insertKey(this, "isUpdata", false);
                RecordUserBehavior.recordUserBehavior(this, BizConstant.SIGN_OUT);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("loginType", BizConstant.NEW);
                startActivity(intent);
                SharedPreferences settings = this.getSharedPreferences("SP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("diaog");
                editor.remove("has_permission");
                editor.remove("avatar");
                editor.remove("uid");
                editor.remove("token");
                editor.remove("account");
                editor.remove("nickname");
                editor.clear();
                editor.commit();
                NIMClient.getService(AuthService.class).logout();//退出网易云信
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.feedback:
                startActivity(new Intent(MySettingActivity.this, FeedBackActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;

            case R.id.shield_settting:
                startActivity(new Intent(MySettingActivity.this, ShieldUserActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.bind_phone:
                startActivity(new Intent(MySettingActivity.this, BindPhoneActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.bind_wechat_relative://绑定微信2
                if (!APP.mWxApi.isWXAppInstalled()) {
                    ToastUtils.showToast(getApplicationContext(), "绑定微信");
                } else {
                    authorization(SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.about_owen:
                startActivity(new Intent(MySettingActivity.this, AboutUsActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            //清除缓存功能
            case R.id.lin_discash:
                String cash = tv_Cash.getText().toString();
                if (StringUtil.isNotEmpty(cash) && cash.equals("0.0KB")) {
                    ToastUtils.showToast(getApplicationContext(), "清理完成");
                } else {
                    clearCache clearCache = new clearCache();
                    new Thread(clearCache).start();
                    clearDialog.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.look_playing://查看玩法
                Intent lookIntent = new Intent(MySettingActivity.this, LookPlayActivity.class);
                lookIntent.putExtra("type", BizConstant.RECOMMEND);
                startActivity(lookIntent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.bind_remyrecommd:
                Intent cardIntent = new Intent(MySettingActivity.this, BaseWebViewActivity.class);
                cardIntent.putExtra("weburl", Api.baseUrl() + "/Members/referrer.html");
                startActivity(cardIntent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            default:
                break;
        }

    }


    //系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent backIntent = new Intent(MySettingActivity.this, MainActivity.class);
            backIntent.putExtra("loginType", BizConstant.MYLOGIN);
            startActivity(backIntent);
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp != null) {
            String avatars = (String) sp.getKey(getApplicationContext(), "avatar", "");
            String name = (String) sp.getKey(getApplicationContext(), "nickname", "");
            nickname.setText(name);
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                    .error(R.mipmap.no_photo_male);
            Glide.with(MySettingActivity.this).load(avatars).apply(options).into(imagHead);
            String invitation_uids = (String) sp.getKey(getApplicationContext(), "invitation_uid", "");
            String invitation_nicknames = (String) sp.getKey(getApplicationContext(), "invitation_nickname", "");
            invitationData(invitation_nicknames,invitation_uids);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //绑定微信
    @Override
    public void bindWechat(BindWechatBean bindWechatBean) {
        if (bindWechatBean.status == 1) {
            list.add(bindWechatBean.data);
            presenter.Dialogs(this, BizConstant.WX_BIND_SUCCESS, BizConstant.TYPE_TWO);
        } else {
            ToastUtils.showToast(getApplicationContext(), bindWechatBean.msg);
        }
    }

    /**
     * 绑定微信弹窗
     *
     * @param bindWechatBean
     */
    @Override
    public void showBindWechatPop(RegisterRedPacketsBean.DataBean.DialogItemBean.WXBind bindWechatBean) {
        bindWechatPopWindow = new BindWechatPopWindow(this, listener);
        bindWechatPopWindow.showAtLocation(MySettingActivity.this.findViewById(R.id.setting_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        View bindView = bindWechatPopWindow.getContentView();
        TextView bind_information = bindView.findViewById(R.id.bind_information);
        TextView bind_force = bindView.findViewById(R.id.bind_force);
        TextView bind_money = bindView.findViewById(R.id.bind_money);
        TextView bind_type = bindView.findViewById(R.id.bind_type);
        ImageView type_image = bindView.findViewById(R.id.type_image);
        type_image.setImageResource(R.mipmap.wechat_img);
        if (StringUtil.isNotEmpty(bindWechatBean.content1)) {
            bind_type.setText(bindWechatBean.content1 + "");
        }
        if (StringUtil.isNotEmpty(bindWechatBean.content2)) {
            bind_information.setText(bindWechatBean.content2 + "");
        }
        if (StringUtil.isNotEmpty(bindWechatBean.content3)) {
            bind_money.setText(bindWechatBean.content3 + "");
        }
        if (StringUtil.isNotEmpty(bindWechatBean.content4)) {
            bind_money.setText(bindWechatBean.content4 + "");
        }
    }

    /**
     * 绑定微信成功
     *
     * @param bindSuccess
     */
    @Override
    public void showBindWechatSuccess(RegisterRedPacketsBean.DataBean.DialogItemBean.WXBindSuccess bindSuccess) {
        SuccessPopWindow bindWechatPopWindow = new SuccessPopWindow(this);
        bindWechatPopWindow.showAtLocation(MySettingActivity.this.findViewById(R.id.setting_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        View bindViewSuccess = bindWechatPopWindow.getContentView();
        TextView bind_title = bindViewSuccess.findViewById(R.id.bind_title);
        TextView bind_content = bindViewSuccess.findViewById(R.id.bind_content);
        TextView bind_search = bindViewSuccess.findViewById(R.id.bind_search);
        if (StringUtil.isNotEmpty(bindSuccess.content1)) {
            bind_title.setText(bindSuccess.content1 + "");
        }
        if (StringUtil.isNotEmpty(bindSuccess.content2)) {
            bind_content.setText(bindSuccess.content2 + "");
        }
        if (StringUtil.isNotEmpty(bindSuccess.content3)) {
            bind_search.setText(bindSuccess.content3 + "");
        }

        bindWechat.setText("已绑定");
        bindWechatRelative.setClickable(false);
        bindWechat.setTextColor(Color.parseColor("#d1d1d1"));
    }

    /**
     * 绑定手机号弹窗
     *
     * @param mobileBind
     */
    @Override
    public void showBindMobile(RegisterRedPacketsBean.DataBean.DialogItemBean.MobileBind mobileBind) {
        bindWechatPopWindow = new BindWechatPopWindow(this, listener);
        bindWechatPopWindow.showAtLocation(MySettingActivity.this.findViewById(R.id.setting_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        View bindView = bindWechatPopWindow.getContentView();
        TextView bind_information = bindView.findViewById(R.id.bind_information);
        TextView bind_force = bindView.findViewById(R.id.bind_force);
        TextView bind_money = bindView.findViewById(R.id.bind_money);
        TextView bind_type = bindView.findViewById(R.id.bind_type);
        ImageView type_image = bindView.findViewById(R.id.type_image);
        type_image.setImageResource(R.mipmap.phome_img);
        if (StringUtil.isNotEmpty(mobileBind.content1)) {
            bind_type.setText(mobileBind.content1 + "");
        }
        if (StringUtil.isNotEmpty(mobileBind.content2)) {
            bind_information.setText(mobileBind.content2 + "");
        }
        if (StringUtil.isNotEmpty(mobileBind.content3)) {
            bind_money.setText(mobileBind.content3 + "");
        }
        if (StringUtil.isNotEmpty(mobileBind.content4)) {
            bind_money.setText(mobileBind.content4 + "");
        }
    }

    /**
     * 绑定手机号成功弹窗
     *
     * @param mobileBindSuccess
     */
    @Override
    public void showBindMobileSuccess(RegisterRedPacketsBean.DataBean.DialogItemBean.MobileBindSuccess mobileBindSuccess) {
        SuccessPopWindow bindWechatPopWindow = new SuccessPopWindow(this);
        bindWechatPopWindow.showAtLocation(MySettingActivity.this.findViewById(R.id.setting_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        View bindViewSuccess = bindWechatPopWindow.getContentView();
        TextView bind_title = bindViewSuccess.findViewById(R.id.bind_title);
        TextView bind_content = bindViewSuccess.findViewById(R.id.bind_content);
        TextView bind_search = bindViewSuccess.findViewById(R.id.bind_search);
        if (StringUtil.isNotEmpty(mobileBindSuccess.content1)) {
            bind_title.setText(mobileBindSuccess.content1 + "");
        }
        if (StringUtil.isNotEmpty(mobileBindSuccess.content2)) {
            bind_content.setText(mobileBindSuccess.content2 + "");
        }
        if (StringUtil.isNotEmpty(mobileBindSuccess.content3)) {
            bind_search.setText(mobileBindSuccess.content3 + "");
        }
        bind.setText("已绑定");
        bindPhone.setClickable(false);
        bindImag.setVisibility(View.VISIBLE);
        bind.setTextColor(Color.parseColor("#d1d1d1"));
    }


    /**
     * 绑定手机号或者绑定微信弹窗
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lin_close:
                    bindWechatPopWindow.dismiss();
                    bindWechatPopWindow.backgroundAlpha(MySettingActivity.this, 1f);
                    break;
                case R.id.bind_wechat:
                    if (StringUtil.isEmpty(weChatUnionId)) {//微信授权
                        if (!APP.mWxApi.isWXAppInstalled()) {
                            ToastUtils.showToast(getApplicationContext(), "绑定微信");
                        } else {
                            authorization(SHARE_MEDIA.WEIXIN);
                        }
                    } else if (StringUtil.isEmpty(mobile)) {//跳转帮手机号页面
                        Intent intent = new Intent(MySettingActivity.this, BindPhoneActivity.class);
                        startActivityForResult(intent, 1);
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    }

                    bindWechatPopWindow.dismiss();
                    bindWechatPopWindow.backgroundAlpha(MySettingActivity.this, 1f);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onErro() {

    }

    class clearCache implements Runnable {

        @Override

        public void run() {

            try {

                CacheDataManager.clearAllCache(MySettingActivity.this);

                Thread.sleep(2000);

                if (CacheDataManager.getTotalCacheSize(MySettingActivity.this).startsWith("0")) {

                    handler.sendEmptyMessage(0);


                }

            } catch (Exception e) {

                return;

            }

        }

    }

    private void authorization(SHARE_MEDIA weixin) {
        UMShareAPI.get(this).getPlatformInfo(this, weixin, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
//                ToastUtils.showToast(WithoutCodeLoginActivity.this, "微信授权登录");
            }

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param map 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> map) {
                sp.insertKey(MySettingActivity.this, "islogin", true);
                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                final String open_id = map.get("openid");//微博没有
                final String union_id = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                final String name = map.get("name");
                final String gender = map.get("gender");
                final String iconurl = map.get("iconurl");
                //city province country language
                final String city = map.get("city");
                final String province = map.get("province");
                final String country = map.get("country");
                final String language = map.get("language");
                String countrycode = map.get("countrycode");
                //添加微信登录接口
                //拿到信息去请求登录接口。。。差一个接口
                presenter.bindWechatNumber(MySettingActivity.this, union_id, open_id, gender, name, iconurl, "", city, province, country, language);
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param throwable 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable throwable) {
                ToastUtils.showToast(MySettingActivity.this, "授权失败");
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                ToastUtils.showToast(MySettingActivity.this, "授权取消");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String bind_phone_success = data.getStringExtra("bind_phone_success");
                if (StringUtil.isNotEmpty(bind_phone_success) && bind_phone_success.equals(BizConstant.TYPE_ONE)) {
                    presenter.Dialogs(this, BizConstant.MOBILE_BIND_SUCCESS, BizConstant.TYPE_FOUR);//绑定手机号成功
                }

            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
