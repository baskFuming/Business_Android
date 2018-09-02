package com.zwonline.top28.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShortMessage;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RegisterPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.country.CityActivity;
import com.zwonline.top28.view.IRegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：免密登录
 *
 * @author YSG
 * @date 2018/4/3
 */
public class WithoutCodeLoginActivity extends BaseActivity<IRegisterActivity, RegisterPresenter> implements IRegisterActivity, View.OnClickListener {

    private ImageView signinLogo;
    private EditText iphone;
    private EditText invitationCode;
    private Button next;
    private LinearLayout posswodLinear;
    private RelativeLayout withoutBack;
    String type = BizConstant.TYPE_LOGIN;
    private String login;
    private SharedPreferencesUtils sp;
    private String login_type;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String seesionid;
    private String geetest_seccodes;
    private String geetest_validates;
    private String geetest_challenges;
    private ToggleButton mswitch;
    private TextView text_fenge;
    private TextView countryCity;
    private TextView quHao;

    @Override
    protected void init() {
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        initView();
        Intent intent = getIntent();
        login_type = intent.getStringExtra("login_type");
        sp = SharedPreferencesUtils.getUtil();
        login = getIntent().getStringExtra("login");
        iphone.addTextChangedListener(textWatcher);
//        initGt3GeetestUtils();
    }

    @Override
    protected RegisterPresenter getPresenter() {
        return new RegisterPresenter(this, this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_without_code_login;
    }

    private void initView() {
        signinLogo = (ImageView) findViewById(R.id.signin_logo);
        iphone = (EditText) findViewById(R.id.iphone);
        invitationCode = (EditText) findViewById(R.id.invitation_code);
        next = (Button) findViewById(R.id.next);
        text_fenge = (TextView) findViewById(R.id.text_fenge);
        mswitch = (ToggleButton) findViewById(R.id.switch_of);
        posswodLinear = (LinearLayout) findViewById(R.id.posswod_linear);
        withoutBack = (RelativeLayout) findViewById(R.id.without_back);
        countryCity = (TextView) findViewById(R.id.country_city);
        quHao = (TextView) findViewById(R.id.quhao);
        next.setOnClickListener(this);
        posswodLinear.setOnClickListener(this);
        withoutBack.setOnClickListener(this);
        countryCity.setOnClickListener(this);
        // 添加监听开关
        mswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    mText.setText("开启");
                    text_fenge.setVisibility(View.VISIBLE);
                    invitationCode.setVisibility(View.VISIBLE);
                } else {
                    text_fenge.setVisibility(View.GONE);
                    invitationCode.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.next://下一步
                initGt3GeetestUtils();
                break;
            case R.id.posswod_linear:
                Intent passwordIntent = new Intent(WithoutCodeLoginActivity.this, PasswordLoginActivity.class);
                passwordIntent.putExtra("login_type", login_type);
                startActivity(passwordIntent);
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.without_back:
                Intent backIntent=new Intent(WithoutCodeLoginActivity.this, MainActivity.class);
                if (StringUtil.isNotEmpty(login_type)&&login_type.equals(BizConstant.ALIPAY_METHOD)){
                    backIntent.putExtra("loginType", login_type);
                }

                startActivity(backIntent);
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.country_city:
                startActivityForResult(new Intent(WithoutCodeLoginActivity.this, CityActivity.class), 1);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }


    @Override
    public void getToken(String token) {

    }

    @Override
    public void isSuccess(int status) {

    }

    @Override
    public void onErro() {

    }

    @Override
    public void showStatus(ShortMessage shortMessage) {
        String token = shortMessage.getDialog();
        sp.insertKey(this, "dialog", seesionid);
//        ToastUtils.showToast(this,"token=="+shortMessage.getDialog());
        if (shortMessage.getStatus() == 1) {
            Intent intent = new Intent(WithoutCodeLoginActivity.this, CodeLoginActivity.class);
            intent.putExtra("login", login);
            intent.putExtra("login_type", login_type);
            intent.putExtra("istoken", "1");
            intent.putExtra("invitationCode", invitationCode.getText().toString());
            intent.putExtra("without_retrieve", "0");
            intent.putExtra("quhao", quHao.getText().toString());
            intent.putExtra("iphoneNumber", iphone.getText().toString().trim());
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else {
            ToastUtils.showToast(WithoutCodeLoginActivity.this, shortMessage.getMsg());
        }
    }

    //系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            startActivity(new Intent(WithoutCodeLoginActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
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

    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
//            ToastUtils.showToast(WithoutCodeLoginActivity.this,"iphone="+iphone.getText().toString().length());
            if (iphone.getText().toString().length() == 0) {
                next.setBackgroundResource(R.drawable.btn_gray_shape);
            } else if (iphone.getText().toString().length() > 0) {
                next.setBackgroundResource(R.drawable.btn_red_shape);

            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (iphone.getText().toString().length() == 0) {
                next.setBackgroundResource(R.drawable.btn_gray_shape);
                next.setFocusable(false);
            } else if (iphone.getText().toString().length() > 0) {
                next.setFocusable(true);
                next.setBackgroundResource(R.drawable.btn_red_shape);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (iphone.getText().toString().length() == 0) {
                next.setBackgroundResource(R.drawable.btn_gray_shape);
                next.setFocusable(false);
            } else if (iphone.getText().toString().length() > 0) {
                next.setFocusable(true);
                next.setBackgroundResource(R.drawable.btn_red_shape);
            }

        }
    };


    public void initGt3GeetestUtils() {
        /**
         * 初始化
         * 务必放在onCreate方法里面执行
         */
        gt3GeetestUtils = new GT3GeetestUtilsBind(WithoutCodeLoginActivity.this);
        String iphoneNumber = iphone.getText().toString().trim();
        if (StringUtil.isNotEmpty(iphoneNumber)) {
            if (iphoneNumber.length() >= 8 && iphoneNumber.length() <= 15) {
                gt3GeetestUtils.getGeetest(WithoutCodeLoginActivity.this, BizConstant.CAPTCHAURL, BizConstant.VALIDATEURL, null, new GT3GeetestBindListener() {
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
                            if (jsonObject != null) {
//                                ToastUtils.showToast(WithoutCodeLoginActivity.this, jsonObject.toString());
                                JSONObject jobj = new JSONObject(jsonObject.toString());
                                seesionid = jobj.getString("sessionid");
                            } else {
                                ToastUtils.showToast(getApplicationContext(), "验证失败！");
                            }
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
//                            Log.d("test====", result);
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
            } else {
                ToastUtils.showToast(getApplicationContext(), getString(R.string.user_phone_error));
            }
        } else {
            ToastUtils.showToast(getApplicationContext(), getString(R.string.user_empty_phone));
        }

        //设置是否可以点击屏幕边缘关闭验证码
        gt3GeetestUtils.setDialogTouch(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 页面关闭时释放资源
         */
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.cancelUtils();
        }
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
                                String iphoneNumber = iphone.getText().toString().trim();
                                String quhao = quHao.getText().toString().trim();

                                if (StringUtil.isNotEmpty(iphone.getText().toString().trim())) {
                                    if (iphoneNumber.length() >= 8 && iphoneNumber.length() <= 15) {
                                        presenter.getPhoneCode(iphoneNumber, type, seesionid, quhao);
                                    } else {
                                        ToastUtils.showToast(getApplicationContext(), getString(R.string.user_phone_error));
                                    }
                                } else {
                                    ToastUtils.showToast(getApplicationContext(), getString(R.string.user_empty_phone));
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                countryCity.setText(data.getStringExtra("country"));
                quHao.setText(data.getStringExtra("area"));
            }
        }


    }
}
