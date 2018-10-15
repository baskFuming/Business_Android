package com.zwonline.top28.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.edittextutils.VerifyCodeView;
import com.zwonline.top28.presenter.LoginPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ILoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：验证码登录
 *
 * @author YSG
 * @date 2018/4/3
 */
public class CodeLoginActivity extends BaseActivity<ILoginActivity, LoginPresenter> implements ILoginActivity {

    private RelativeLayout codeBack;
    private TextView tvCodeRetrieve;
    private TextView verificationCode;
    private VerifyCodeView verifyCodeView;
    private Button codeLogins;
    private TextView cellPhone;
    private TimeCount mTimeCount;//计时器
    private String iphoneNumber;
    private String withoutRetrieve;
    private SharedPreferencesUtils sp;
    private String LOGIN_KEY = "login";
    String type = BizConstant.TYPE_LOGIN;
    private ProgressBar loginProgress;
    private String login;
    private String invitationCode;
    private String dialogs;
    private String tokens;
    private String login_type;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String seesionid;
    private String geetest_seccodes;
    private String geetest_validates;
    private String geetest_challenges;
    private String quhao;

    @Override
    protected void init() {
        initView();
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        Intent intent = getIntent();
        iphoneNumber = intent.getStringExtra("iphoneNumber");
        //邀请码
        invitationCode = intent.getStringExtra("invitationCode");
        login = intent.getStringExtra("login");
        login_type = intent.getStringExtra("login_type");
        quhao = intent.getStringExtra("quhao");
        sp = SharedPreferencesUtils.getUtil();

        sp.insertKey(CodeLoginActivity.this, LOGIN_KEY, true);
        StringBuffer sb = new StringBuffer();
        sb.append(iphoneNumber).insert(3, " ").insert(8, " ");
        withoutRetrieve = intent.getStringExtra("without_retrieve");//判断是短信验证登录还是找回密码
        getwithoutRetrieve();
        cellPhone.setText(sb.toString());
        mTimeCount.start();

    }

    /**
     * 初始化组件
     */
    private void initView() {
        //定时器
        mTimeCount = new TimeCount(60000, 1000);
        codeLogins = (Button) findViewById(R.id.code_logins);
        codeBack = (RelativeLayout) findViewById(R.id.code_back);
        cellPhone = (TextView) findViewById(R.id.cell_phone);
        tvCodeRetrieve = (TextView) findViewById(R.id.tv_code_retrieve);
        verificationCode = (TextView) findViewById(R.id.verification_code);
        verifyCodeView = (VerifyCodeView) findViewById(R.id.verify_code_view);
        loginProgress = (ProgressBar) findViewById(R.id.codelogin_progress);
        /**
         * 监听输入框输入状态
         */
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                //输入完成
                codeLogins.setBackgroundResource(R.drawable.btn_red_shape);
                codeLogins.setEnabled(true);
            }

            @Override
            public void invalidContent() {
                //正在输入compile.setEnabled(false);
                codeLogins.setBackgroundResource(R.drawable.btn_gray_shape);
                codeLogins.setEnabled(false);
            }
        });
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter(this, this);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_code_login;
    }


    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @OnClick({R.id.code_back, R.id.verification_code, R.id.code_logins})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.code_back:
                startActivity(new Intent(CodeLoginActivity.this, WithoutCodeLoginActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.verification_code://获取验证码
                initGt3GeetestUtils();
                break;

            case R.id.code_logins:
                String content = verifyCodeView.getEditContent();
                tokens = (String) sp.getKey(getApplicationContext(), "dialog", "");
                if (StringUtil.isNotEmpty(withoutRetrieve) && withoutRetrieve.equals("0")) {
                    if (StringUtil.isNotEmpty(content)) {
                        loginProgress.setVisibility(View.VISIBLE);
                        codeLogins.setEnabled(false);
                        if (StringUtil.isNotEmpty(invitationCode)) {
                            presenter.loginVerifys(iphoneNumber, content, tokens, invitationCode);
                        } else {
                            presenter.loginVerify(iphoneNumber, content, tokens);
                        }
                    } else {
                        ToastUtils.showToast(getApplicationContext(), getString(R.string.user_empty_verifycode));
                    }
                } else if (StringUtil.isNotEmpty(withoutRetrieve) && withoutRetrieve.equals("1")) {
                    if (StringUtil.isNotEmpty(content)) {
//                        loginProgress.setVisibility(View.VISIBLE);
//                        codeLogins.setEnabled(false);
                        presenter.mforGetPossword(getApplicationContext(), iphoneNumber, content, tokens);
                    } else {
                        ToastUtils.showToast(getApplicationContext(), getString(R.string.user_empty_verifycode));
                    }

                }

                break;
        }
    }

    /**
     * 验证码登录
     *
     * @param status
     * @param dialog
     * @param token
     * @param account
     */
    @Override
    public void isSuccess(int status, String dialog, String token, String account) {
        LoginBean loginBean = new LoginBean();
        if (status == 1) {
//            if (login_type.equals(BizConstant.ALIPAY_METHOD)) {
//                finish();
//                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//            } else {
                Intent intent = new Intent(CodeLoginActivity.this, MainActivity.class);
                intent.putExtra("loginType", login_type);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//            }
            sp.insertKey(CodeLoginActivity.this, "islogin", true);
            RecordUserBehavior.recordUserBehavior(getApplicationContext(), BizConstant.SIGN_IN);

            loginProgress.setVisibility(View.GONE);
            codeLogins.setEnabled(true);
            ToastUtils.showToast(this, getString(R.string.user_suc_login));
        } else {
            Toast.makeText(CodeLoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 验证忘记密码
     *
     * @param headBean
     */
    @Override
    public void showForgetPossword(LoginBean headBean) {

        if (headBean.getStatus() == 1) {
            Intent intent = new Intent(CodeLoginActivity.this, RetPosswordActivity.class);
            intent.putExtra("ispassword", "3");
            intent.putExtra("tokens", tokens);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            loginProgress.setVisibility(View.GONE);
            codeLogins.setEnabled(true);
        } else {
            ToastUtils.showToast(this, headBean.getMsg());
            loginProgress.setVisibility(View.GONE);
            codeLogins.setEnabled(true);
        }
    }

    @Override
    public void isWechatSuccess(int status, String dialog, String token, String account) {

    }

    @Override
    public void wecahtSuccess(LoginWechatBean loginWechatBean) {

    }

    /**
     * 获取Token
     *
     * @param dialog
     */
    @Override
    public void getToken(String dialog) {
//        ToastUtils.showToast(this,"dialog"+dialog);
        sp.insertKey(CodeLoginActivity.this, "dialog", seesionid);
    }

    @Override
    public void Success(LoginBean loginBean) {
        if (loginBean.getStatus() == 1) {
            sp.insertKey(getApplicationContext(), "avatar", loginBean.getData().getUser().getAvatar());
            sp.insertKey(getApplicationContext(), "uid", loginBean.getData().getUser().getUid());
            sp.insertKey(getApplicationContext(), "nickname", loginBean.getData().getUser().getNickname());
            sp.insertKey(getApplicationContext(), "sign", loginBean.getData().getUser().getSignature());
            sp.insertKey(getApplicationContext(), "follow", loginBean.getData().getUser().getFollow());
            sp.insertKey(getApplicationContext(), "fans", loginBean.getData().getUser().getFans());
            sp.insertKey(getApplicationContext(), "favorite", loginBean.getData().getUser().getFavorite());
            Toast.makeText(getApplicationContext(), R.string.user_suc_login, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
        }
    }

    //判断错误信息
    @Override
    public void onErro() {
        loginProgress.setVisibility(View.GONE);
        codeLogins.setEnabled(true);
        Toast.makeText(CodeLoginActivity.this, R.string.user_verifycode_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErro() {
        loginProgress.setVisibility(View.GONE);
        codeLogins.setEnabled(true);
        Toast.makeText(CodeLoginActivity.this, R.string.user_verifycode_error, Toast.LENGTH_SHORT).show();
    }


    /**
     * 正则匹配手机号码
     *
     * @param tel
     * @return
     */
    public boolean checkTel(String tel) {
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
        /**
         * 页面关闭时释放资源
         */
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.cancelUtils();
        }
    }

    //判断是短信登录还是找回密码
    public void getwithoutRetrieve() {
        if (StringUtil.isNotEmpty(withoutRetrieve) && withoutRetrieve.equals("0")) {
            codeLogins.setText(getText(R.string.login_btn_login));
            tvCodeRetrieve.setText(getText(R.string.without_empty_send));
        } else if (StringUtil.isNotEmpty(withoutRetrieve) && withoutRetrieve.equals("1")) {
            codeLogins.setText(getText(R.string.without_next_step));
            tvCodeRetrieve.setText(getText(R.string.retrieve_password));
        }
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            verificationCode.setClickable(false);
            verificationCode.setText(l / 1000 + getString(R.string.user_count_down));
            verificationCode.setTextColor(Color.parseColor("#A0B0AD"));
        }

        @Override
        public void onFinish() {
            verificationCode.setClickable(true);
            verificationCode.setText(R.string.user_get_code);
            verificationCode.setTextColor(Color.parseColor("#FF2B2B"));
        }
    }

    //系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            startActivity(new Intent(CodeLoginActivity.this, WithoutCodeLoginActivity.class));
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 极验验证验证码
     */
    public void initGt3GeetestUtils() {
        /**
         * 初始化
         * 务必放在onCreate方法里面执行
         */
        gt3GeetestUtils = new GT3GeetestUtilsBind(CodeLoginActivity.this);
        gt3GeetestUtils.getGeetest(CodeLoginActivity.this, BizConstant.CAPTCHAURL, BizConstant.VALIDATEURL, null, new GT3GeetestBindListener() {
            /**
             * num 1 点击验证码的关闭按钮来关闭验证码
             * num 2 点击屏幕关闭验证码
             * num 3 点击返回键关闭验证码
             */
            @Override
            public void gt3CloseDialog(int num) {
            }

            /**
             * 验证码加载准备完成
             * 此时弹出验证码
             */
            @Override
            public void gt3DialogReady() {
            }

            /**
             * 拿到第一个url（API1）返回的数据
             * 该方法只适用于不使用自定义api1时使用
             */
            @Override
            public void gt3FirstResult(JSONObject jsonObject) {
                try {
                    JSONObject jobj = new JSONObject(jsonObject.toString());
                    seesionid = jobj.getString("sessionid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 设置是否自定义第二次验证ture为是 默认为false(不自定义)
             * 如果为false后续会走gt3GetDialogResult(String result)拿到api2需要的参数
             * 如果为true后续会走gt3GetDialogResult(boolean a, String result)拿到api2需要的参数
             * result为二次验证所需要的数据
             */
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            /**
             * 拿到第二个url（API2）需要的数据
             * 该方法只适用于不使用自定义api2时使用
             */
            @Override
            public void gt3GetDialogResult(String result) {
                try {
                    Log.d("test====", result);
                    JSONObject jobj = new JSONObject(result);
                    geetest_challenges = jobj.getString("geetest_challenge");
                    geetest_validates = jobj.getString("geetest_validate");
                    geetest_seccodes = jobj.getString("geetest_seccode");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 自定义二次验证，也就是当gtSetIsCustom为ture时才执行
             * 拿到第二个url（API2）需要的数据
             * 在该回调里面自行请求api2
             * 对api2的结果进行处理
             */
            @Override
            public void gt3GetDialogResult(boolean status, String result) {
//                        ToastUtils.showToast(WithoutCodeLoginActivity.this,result);
                try {
                    Log.d("test====", result);
                    JSONObject jobj = new JSONObject(result);
                    geetest_challenges = jobj.getString("geetest_challenge");
                    geetest_validates = jobj.getString("geetest_validate");
                    geetest_seccodes = jobj.getString("geetest_seccode");
                    yanZhengApi2(geetest_challenges, geetest_validates, geetest_seccodes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (status) {


                }
            }

            /**
             * 需要做验证统计的可以打印此处的JSON数据
             * JSON数据包含了极验每一步的运行状态和结果
             */
            @Override
            public void gt3GeetestStatisticsJson(JSONObject jsonObject) {
            }

            /**
             * 验证过程错误
             * 返回的错误码为判断错误类型的依据
             */

            @Override
            public void gt3DialogOnError(String error) {
                Log.i("dsd", error);

            }
        });

        //设置是否可以点击屏幕边缘关闭验证码
        gt3GeetestUtils.setDialogTouch(false);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /**
         * 设置后，界面横竖屏不会关闭验证码，推荐设置
         */
        gt3GeetestUtils.changeDialogLayout();
    }

    //验证验证码
    public void yanZhengApi2(String geetest_challenge, String geetest_validate, String geetest_seccode) {
        try {
            long timestamp = new Date().getTime() / 1000;//获取时间戳
            Map<String, String> map = new HashMap<>();
            map.put("timestamp", String.valueOf(timestamp));
            map.put("geetest_challenges", geetest_challenge);
            map.put("geetest_validates", geetest_validate);
            map.put("geetest_seccodes", geetest_seccode);
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<SettingBean> flowable = ApiRetrofit.getInstance()
                    .getClientApis(PayService.class, Api.url, seesionid)
                    .iGtValidate(String.valueOf(timestamp), geetest_challenge, geetest_validate, geetest_seccode, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SettingBean>() {

                        @Override
                        public void onNext(SettingBean yanzheng) {
//                            ToastUtils.showToast(WithoutCodeLoginActivity.this,yanzheng.msg+yanzheng.status);
                            if (yanzheng.status == 1) {
                                gt3GeetestUtils.gt3TestFinish();
                                mTimeCount.start();
                                presenter.getPhoneCode(iphoneNumber, type, seesionid, quhao);
                            } else {
                                gt3GeetestUtils.gt3TestClose();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {


                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
