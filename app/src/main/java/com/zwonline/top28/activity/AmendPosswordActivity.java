package com.zwonline.top28.activity;

import android.content.Intent;
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
import com.zwonline.top28.presenter.RetPosswordPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IRetPossword;

/**
 * 描述：修改密码
 * @author YSG
 * @date 2018/1/12
 */
public class AmendPosswordActivity extends BaseActivity<IRetPossword, RetPosswordPresenter> implements IRetPossword, View.OnClickListener {
    private RelativeLayout back;
    private TextView collect;
    private EditText amnet_password;
    private Button btn_register;
    private SharedPreferencesUtils sp;
    private String phone;

    //查找控件
    private void initView() {

        sp = SharedPreferencesUtils.getUtil();
        back = (RelativeLayout) findViewById(R.id.back);
        collect = (TextView) findViewById(R.id.collect);
        amnet_password = (EditText) findViewById(R.id.amnet_password);
        btn_register = (Button) findViewById(R.id.btn_yanzheng);
        back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    protected void init() {
        initView();
        presenter.mUserInfo(this);
    }
    @Override
    protected RetPosswordPresenter getPresenter() {
        return new RetPosswordPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_amend_possword;
    }
    //点击事件
    @Override
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.btn_yanzheng:
                if (!amnet_password.getText().toString().trim().equals("")) {
                    presenter.loginNumber(//验证旧密码
                            this,phone,
                            amnet_password.getText().toString().trim()
                    );
                }else {
                    ToastUtils.showToast(this, getString(R.string.mycenter_old_password));
                }

                break;
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }


    @Override
    public void Success(LoginBean loginBean) {
        if (loginBean.getStatus() == 1) {
            Intent intent = new Intent(this, RetPosswordActivity.class);
            intent.putExtra("ispassword","0");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else {
            Toast.makeText(this, R.string.mycenter_fail_verify, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showUserInfo(UserInfoBean userInfoBean) {
        phone = userInfoBean.data.user.phone;
    }


    @Override
    public void showRetPossWord(SettingBean settingBean) {

    }

    @Override
    public void onErro() {
        Toast.makeText(AmendPosswordActivity.this, R.string.mycenter_fail_verify, Toast.LENGTH_SHORT).show();
    }



}
