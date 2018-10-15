package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.LoginPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ILoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页
 */
public class LoginActivity extends BaseActivity<ILoginActivity, LoginPresenter> implements ILoginActivity {

    @BindView(R.id.tv_zhanghao)
    TextView tvZhanghao;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_zhanghao_line)
    TextView tvZhanghaoLine;
    @BindView(R.id.tv_phone_line)
    TextView tvPhoneLine;
    @BindView(R.id.et_zhang)
    EditText etZhang;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.ll_zhanghao)
    LinearLayout llZhanghao;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_yanzheng)
    EditText etYanzheng;
    @BindView(R.id.tv_yz)
    TextView tvYz;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.tv_find_password)
    TextView tvFindPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.hotline)
    LinearLayout hotline;
    private String LOGIN_KEY = "login";
    private int time = 60;
    private Handler handler = new Handler();
    private SharedPreferencesUtils sp;
    private String dialog;
    private String token;
    private String account;
    public static String TOKEN = "token";
    private TimeCount mTimeCount;//计时器
    private LinearLayout hotLine;
    private String tel = "(010)60846615";


    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            tvYz.setClickable(false);
            tvYz.setText(l / 1000 + getString(R.string.user_count_down));
        }

        @Override
        public void onFinish() {
            tvYz.setClickable(true);
            tvYz.setText(R.string.user_get_code);
        }
    }

    @Override
    protected void init() {
        hotLine = (LinearLayout) findViewById(R.id.hotline);
        sp = SharedPreferencesUtils.getUtil();
        sp.insertKey(LoginActivity.this, LOGIN_KEY, true);
        mTimeCount = new TimeCount(60000, 1000);

    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @OnClick({R.id.tv_zhanghao, R.id.tv_phone, R.id.btn_login, R.id.tv_yz, R.id.btn_register, R.id.tv_find_password, R.id.hotline})
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.tv_yz:
                //获取手机验证码
                String phone = etPhone.getText().toString().trim();
                boolean cellphone = isCellphone(phone);
                if (!etPhone.getText().toString().trim().equals("")) {
                    if (checkTel(etPhone.getText().toString().trim())) {
                        mTimeCount.start();
//                        presenter.getPhoneCode(etPhone.getText().toString().trim());
                        presenter.getPhoneCode(etPhone.getText().toString().trim(), BizConstant.TYPE_LOGIN,"","");
                    } else {
                        ToastUtils.showToast(this, this.getString(R.string.user_phone_error));
                    }
                } else {
                    Toast.makeText(this, R.string.user_empty_phone, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_zhanghao:
                tvZhanghao.setTextColor(Color.RED);
                tvPhone.setTextColor(Color.BLACK);
                llZhanghao.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.GONE);
                tvZhanghaoLine.setVisibility(View.VISIBLE);
                tvPhoneLine.setVisibility(View.INVISIBLE);
                sp.insertKey(LoginActivity.this, LOGIN_KEY, true);
                break;
            case R.id.tv_phone:
                tvZhanghao.setTextColor(Color.BLACK);
                tvPhone.setTextColor(Color.RED);
                llZhanghao.setVisibility(View.GONE);
                llPhone.setVisibility(View.VISIBLE);
                tvZhanghaoLine.setVisibility(View.INVISIBLE);
                tvPhoneLine.setVisibility(View.VISIBLE);
                sp.insertKey(LoginActivity.this, LOGIN_KEY, false);
                if (tvYz.equals("获取验证码")) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
                break;
            case R.id.btn_login:
                Boolean key = (Boolean) sp.getKey(LoginActivity.this, LOGIN_KEY, false);
                if (key) {
                    if (!etZhang.getText().toString().trim().equals("")) {
                        if (checkTel(etZhang.getText().toString().trim())) {
                            if (!etPassword.getText().toString().trim().equals("")) {
                                //账号密码登录
                                presenter.loginNumber(
                                        etZhang.getText().toString().trim(),
                                        etPassword.getText().toString().trim()
                                );
                                RecordUserBehavior.recordUserBehavior(LoginActivity.this, BizConstant.SIGN_IN);
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.user_empty_password, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.user_phone_error, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.user_empty_phone, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (!etPhone.getText().toString().trim().equals("")) {
                        if (checkTel(etPhone.getText().toString().trim())) {
                            if (!etYanzheng.getText().toString().equals("")) {
                                //获取手机验证码登录
                                presenter.loginVerify(
                                        etPhone.getText().toString().trim(),
                                        etYanzheng.getText().toString().trim(),
                                        dialog
                                );
                                RecordUserBehavior.recordUserBehavior(LoginActivity.this, BizConstant.SIGN_IN);
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.user_empty_verifycode, Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            ToastUtils.showToast(this, getString(R.string.user_phone_error));
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.user_empty_phone, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_register:
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                finish();
                break;
            case R.id.tv_find_password:
                startActivity(new Intent(LoginActivity.this, RetrievePasswordActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.hotline:
                showNormalDialog();
                break;
        }
    }

    //系统返回键
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //判断是否登录成功
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void isSuccess(int status, String dialog, String token, String account) {
        LoginBean loginBean = new LoginBean();
        if (status == 1) {
            sp.insertKey(LoginActivity.this, "islogin", true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else if (status == -1) {
            Toast.makeText(LoginActivity.this, R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getToken(String dialog) {
        this.dialog = dialog;
        sp.insertKey(this, "dialog", dialog);
    }

    @Override
    public void Success(LoginBean loginBean) {
        if (loginBean.getStatus() == 1) {
            Toast.makeText(LoginActivity.this, R.string.user_suc_login, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErro() {
        Toast.makeText(LoginActivity.this, R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErro() {
        ToastUtils.showToast(LoginActivity.this, getString(R.string.user_verifycode_error));
    }

    @Override
    public void showForgetPossword(LoginBean settingBean) {

    }

    @Override
    public void isWechatSuccess(int status, String dialog, String token, String account) {

    }

    @Override
    public void wecahtSuccess(LoginWechatBean loginWechatBean) {

    }


    //手机号格式验证
    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
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
    }
    //呼叫电话
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(LoginActivity.this);
        normalDialog.setMessage(tel);
        normalDialog.setPositiveButton(R.string.mycenter_call,
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
                        startActivity(intentPhone);
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
}
