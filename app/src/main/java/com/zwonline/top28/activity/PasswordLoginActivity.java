package com.zwonline.top28.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.LoginPresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ILoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;

/**
 * 描述：账号密码登录
 *
 * @author YSG
 * @date 2018/4/4
 */
public class PasswordLoginActivity extends BaseActivity<ILoginActivity, LoginPresenter> implements ILoginActivity {

    private ImageView signinLogo;
    private RelativeLayout passwordBack;
    private EditText iphoneNum;
    private EditText iphonePassword;
    private TextView forgetPassword;
    private Button login;
    private LinearLayout withoutLogin;
    private ProgressBar loginProgress;
    private SharedPreferencesUtils sp;
    private String msg;

    @Override
    protected void init() {
        initView();
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        sp = SharedPreferencesUtils.getUtil();
        iphonePassword.addTextChangedListener(textWatcher);
        setEdNoChinaese(iphonePassword);
    }

    @Override
    protected LoginPresenter getPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_password_login;
    }

    private void initView() {
        signinLogo = (ImageView) findViewById(R.id.signin_logo);
        passwordBack = (RelativeLayout) findViewById(R.id.password_back);
        iphoneNum = (EditText) findViewById(R.id.iphone_num);
        iphonePassword = (EditText) findViewById(R.id.iphone_password);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        login = (Button) findViewById(R.id.login);
        withoutLogin = (LinearLayout) findViewById(R.id.without_login);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
    }


    @OnClick({R.id.password_back, R.id.forget_password, R.id.login, R.id.without_login})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.password_back:
                startActivity(new Intent(PasswordLoginActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.forget_password:
                Intent intent = new Intent(PasswordLoginActivity.this, RetrievePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.login:
                String iphone = iphoneNum.getText().toString().trim();
                String password = iphonePassword.getText().toString().trim();
                if (StringUtil.isNotEmpty(iphone)) {
                    if (iphone.length() >= 8 && iphone.length() <= 15) {
                        if (StringUtil.isNotEmpty(password)) {
                            presenter.loginNumber(iphone, password);
                            loginProgress.setVisibility(View.VISIBLE);
                            login.setEnabled(false);
                        } else {
                            ToastUtils.showToast(this, getString(R.string.user_empty_password));
                        }
                    } else {
                        ToastUtils.showToast(this, getString(R.string.user_phone_error));
                    }
                } else {
                    ToastUtils.showToast(this, getString(R.string.user_empty_phone));
                }
                break;
            case R.id.without_login:
                Intent withoutIntent = new Intent(PasswordLoginActivity.this, WithoutCodeLoginActivity.class);
                withoutIntent.putExtra("login_type", getIntent().getStringExtra("login_type"));
                startActivity(withoutIntent);
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    @Override
    public void isSuccess(int status, String dialog, String token, String account) {
        LoginBean loginBean = new LoginBean();
        msg = loginBean.getMsg();
        if (status == 1) {
            loginProgress.setVisibility(View.GONE);
            sp.insertKey(PasswordLoginActivity.this, "islogin", true);
            sp.insertKey(PasswordLoginActivity.this, "dialog", loginBean.getDialog());

            RecordUserBehavior.recordUserBehavior(this, BizConstant.SIGN_IN);
            Intent intent = new Intent(PasswordLoginActivity.this, MainActivity.class);

            intent.putExtra("loginType", getIntent().getStringExtra("login_type"));
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else if (status == -1) {
            Toast.makeText(PasswordLoginActivity.this, R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PasswordLoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getToken(String dialog) {

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

    @Override
    public void onErro() {
        loginProgress.setVisibility(View.GONE);
        login.setEnabled(true);
        Toast.makeText(PasswordLoginActivity.this, R.string.user_account_pass_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErro() {

    }

    @Override
    public void showForgetPossword(LoginBean settingBean) {

    }


    //系统返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            startActivity(new Intent(PasswordLoginActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
//            ToastUtils.showToast(WithoutCodeLoginActivity.this,"iphone="+iphone.getText().toString().length());
            if (StringUtil.isNotEmpty(iphoneNum.getText().toString()) && StringUtil.isNotEmpty(iphonePassword.getText().toString())) {
                login.setBackgroundResource(R.drawable.btn_red_shape);
            } else {
                login.setBackgroundResource(R.drawable.btn_gray_shape);
            }
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (StringUtil.isNotEmpty(iphoneNum.getText().toString()) && StringUtil.isNotEmpty(iphonePassword.getText().toString())) {
                login.setBackgroundResource(R.drawable.btn_red_shape);
            } else {
                login.setBackgroundResource(R.drawable.btn_gray_shape);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtil.isNotEmpty(iphoneNum.getText().toString()) && StringUtil.isNotEmpty(iphonePassword.getText().toString())) {
                login.setBackgroundResource(R.drawable.btn_red_shape);
            } else {
                login.setBackgroundResource(R.drawable.btn_gray_shape);
            }
        }
    };

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
     * 点击输入框其他地方软件盘隐藏
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (LanguageUitils.isShouldHideInput(v, ev)) {
                if (LanguageUitils.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 限制edittext 不能输入中文
     *
     * @param editText
     */
    public void setEdNoChinaese(final EditText editText) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                //注意返回值是char数组
                char[] stringArr = txt.toCharArray();
                for (int i = 0; i < stringArr.length; i++) {
                    //转化为string
                    String value = new String(String.valueOf(stringArr[i]));
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(value);
                    if (m.matches()) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        };
        editText.addTextChangedListener(textWatcher);
    }

}
