package com.zwonline.top28.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.presenter.RetPosswordPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IRetPossword;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：重设密码
 *
 * @author YSG
 * @date 2018/2/6
 */
public class RetPosswordActivity extends BaseActivity<IRetPossword, RetPosswordPresenter> implements IRetPossword, View.OnClickListener {

    private RelativeLayout retBack;
    private EditText newPossword;
    private EditText confirmPassword;
    private Button btnRetpossword;
    private SharedPreferencesUtils sp;
    private TextView settingRetPassword;
    private TextView tvYanZheng;
    private String ispassword;

    @Override
    protected void init() {
        initView();
        sp = SharedPreferencesUtils.getUtil();
        boolean islogins = (boolean) sp.getKey(this, "islogin", false);
        ispassword = getIntent().getStringExtra("ispassword");
        //判别是验证密码还是修改密码
        if (StringUtil.isNotEmpty(ispassword) && ispassword.equals("1")) {
            settingRetPassword.setText("设置密码");
            tvYanZheng.setVisibility(View.GONE);
        } else if (StringUtil.isNotEmpty(ispassword) && ispassword.equals("0")) {
            settingRetPassword.setText(this.getString(R.string.user_update_password));
            tvYanZheng.setVisibility(View.VISIBLE);
        }else if (StringUtil.isNotEmpty(ispassword) && ispassword.equals("3")){
            settingRetPassword.setText("找回密码");
            tvYanZheng.setVisibility(View.GONE);
        }
    }

    @Override
    protected RetPosswordPresenter getPresenter() {
        return new RetPosswordPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_ret_possword;
    }

    //查找控件
    private void initView() {
        retBack = (RelativeLayout) findViewById(R.id.ret_back);
        newPossword = (EditText) findViewById(R.id.new_possword);
        confirmPassword = (EditText) findViewById(R.id.confirm_password);
        btnRetpossword = (Button) findViewById(R.id.btn_retpossword);
        settingRetPassword = (TextView) findViewById(R.id.setting_ret_password);
        tvYanZheng = (TextView) findViewById(R.id.tv_yanzheng);
        btnRetpossword.setOnClickListener(this);
        retBack.setOnClickListener(this);
        setEdNoChinaese(newPossword);
        setEdNoChinaese(confirmPassword);
    }

    @Override
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.btn_retpossword://点击修改密码
                RecordUserBehavior.recordUserBehavior(this, BizConstant.EDITED_PASSWORD);
                if (!newPossword.getText().toString().trim().equals("")) {
                    if (!confirmPassword.getText().toString().trim().equals("")) {
                        if (newPossword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                            presenter.mRetPossWord(this, newPossword.getText().toString().trim());
//                            Toast.makeText(this, R.string.user_suc_updatepass, Toast.LENGTH_SHORT).show();
//                            sp.insertKey(this, "islogin", false);
//                            startActivity(new Intent(RetPosswordActivity.this, PasswordLoginActivity.class));
////                            startActivity(new Intent(RetPosswordActivity.this, WithoutCodeLoginActivity.class));
//                            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);

                        } else {
                            ToastUtils.showToast(this, getString(R.string.user_inconformity_password));
                        }
                    } else {
                        ToastUtils.showToast(this, getString(R.string.user_confirm_newpassword));
                    }
                } else {
                    ToastUtils.showToast(this, getString(R.string.user_empty_newpassword));
                }
                break;
            case R.id.ret_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    @Override
    public void showRetPossWord(SettingBean settingBean) {
        if (settingBean.status==1){
            sp.insertKey(this, "islogin", false);
            sp.insertKey(this, "isUpdata", false);
            RecordUserBehavior.recordUserBehavior(this, BizConstant.SIGN_OUT);
            startActivity(new Intent(this, WithoutCodeLoginActivity.class));
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
            Toast.makeText(this, R.string.user_suc_resetpass, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RetPosswordActivity.this, PasswordLoginActivity.class));
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//            finish();
        }else {
            ToastUtils.showToast(this,settingBean.msg);
        }

    }

    @Override
    public void onErro() {
        ToastUtils.showToast(this, getString(R.string.user_fail_resetpass));
    }

    @Override
    public void Success(LoginBean loginBean) {

    }

    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {

    }
    /**
     * 限制edittext 不能输入中文
     * @param editText
     */
    public  void setEdNoChinaese(final EditText editText){
        TextWatcher textWatcher=new TextWatcher() {
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
