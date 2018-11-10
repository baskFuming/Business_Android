package com.zwonline.top28.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.umeng.socialize.UMShareAPI;
import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShortMessage;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.main.AnnouncementActivity;
import com.zwonline.top28.presenter.RegisterPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.country.CityActivity;
import com.zwonline.top28.utils.popwindow.SuccessPopWindow;
import com.zwonline.top28.view.IRegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 绑定手机号
 */
public class BindPhoneActivity extends BaseActivity<IRegisterActivity, RegisterPresenter> implements IRegisterActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView tvFunction;
    private TextView area;
    private ImageView bindImag;
    private RelativeLayout bindCountries;
    private TextView quhao;
    private EditText iphone;
    private EditText etYanzheng;
    private TextView tvYanzheng;
    private TextView btnBind;
    private TimeCount mTimeCount;//计时器
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String seesionid;
    private String geetest_seccodes;
    private String geetest_validates;
    private String geetest_challenges;
    String type = BizConstant.TYPE_BIND;
    private SharedPreferencesUtils sp;
    private String unionId;

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        unionId = (String) sp.getKey(this, "union_id", "");
        initView();
    }


    @Override
    protected RegisterPresenter getPresenter() {
        return new RegisterPresenter(this, this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_phone;
    }

    private void initView() {
        //定时器
        mTimeCount = new TimeCount(60000, 1000);
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        tvFunction = (TextView) findViewById(R.id.tv_function);
        area = (TextView) findViewById(R.id.area);
        bindImag = (ImageView) findViewById(R.id.bind_imag);
        bindCountries = (RelativeLayout) findViewById(R.id.bind_countries);
        quhao = (TextView) findViewById(R.id.quhao);
        iphone = (EditText) findViewById(R.id.iphone);
        etYanzheng = (EditText) findViewById(R.id.et_yanzheng);
        tvYanzheng = (TextView) findViewById(R.id.tv_yanzheng);
        btnBind = (TextView) findViewById(R.id.btn_bind);
        title.setText("绑定手机号");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                area.setText(data.getStringExtra("country"));
                quhao.setText(data.getStringExtra("area"));
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.back, R.id.bind_countries, R.id.btn_bind, R.id.tv_yanzheng})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.bind_countries:
                startActivityForResult(new Intent(BindPhoneActivity.this, CityActivity.class), 2);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.btn_bind:
                String phone = iphone.getText().toString().trim();
                String code = etYanzheng.getText().toString().trim();
                if (StringUtil.isNotEmpty(phone)) {
                    if (StringUtil.isNotEmpty(code)) {
                        presenter.VerifySmsCode(BindPhoneActivity.this, phone, code, seesionid);
                    } else {
                        ToastUtils.showToast(getApplicationContext(), "验证码不能为空！");
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "手机号不能为空！");
                }
                break;
            case R.id.tv_yanzheng:
                initGt3GeetestUtils();
                break;
        }
    }

    @Override
    public void isSuccess(int status, String dialog, String token, String account) {

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

    /**
     * 发送验证码
     *
     * @param shortMessage
     */
    @Override
    public void showStatus(ShortMessage shortMessage) {
        ToastUtils.showToast(getApplicationContext(), shortMessage.getMsg());
    }

    @Override
    public void loginShowWechat(LoginWechatBean bean) {

    }

    /**
     * 验证短信验证码是否正确
     *
     * @param attentionBean
     */
    @Override
    public void showVerifySmsCode(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            presenter.BindMobile(this, iphone.getText().toString().trim(), unionId, seesionid);
        } else {
            ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
        }
    }

    /**
     * 绑定手机号
     *
     * @param attentionBean
     */
    @Override
    public void showBindMobile(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
//            Intent intent = new Intent(this, BindSuccessActivity.class);
//            intent.putExtra("phone_num", iphone.getText().toString().trim());
//            startActivity(intent);
//            finish();
//            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            presenter.Dialogs(this, BizConstant.MOBILE_BIND_SUCCESS);//绑定手机号成功
        } else {
            ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
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
        bindWechatPopWindow.showAtLocation(BindPhoneActivity.this.findViewById(R.id.setting_layout), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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
            tvYanzheng.setClickable(false);
            tvYanzheng.setText(l / 1000 + getString(R.string.user_count_down));
            tvYanzheng.setTextColor(Color.parseColor("#A0B0AD"));
        }

        @Override
        public void onFinish() {
            tvYanzheng.setClickable(true);
            tvYanzheng.setText(R.string.user_get_code);
            tvYanzheng.setTextColor(Color.parseColor("##228FFE"));
        }
    }

    public void initGt3GeetestUtils() {
        /**
         * 初始化
         * 务必放在onCreate方法里面执行
         */
        gt3GeetestUtils = new GT3GeetestUtilsBind(BindPhoneActivity.this);
        String iphoneNumber = iphone.getText().toString().trim();
        if (StringUtil.isNotEmpty(iphoneNumber)) {
            if (iphoneNumber.length() >= 8 && iphoneNumber.length() <= 15) {
                gt3GeetestUtils.getGeetest(BindPhoneActivity.this, BizConstant.CAPTCHAURL, BizConstant.VALIDATEURL, null, new GT3GeetestBindListener() {
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
                            if (yanzheng.status == 1) {
                                gt3GeetestUtils.gt3TestFinish();
                                mTimeCount.start();
                                presenter.getPhoneCode(iphone.getText().toString().trim(), type, seesionid, quhao.getText().toString());
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
}
