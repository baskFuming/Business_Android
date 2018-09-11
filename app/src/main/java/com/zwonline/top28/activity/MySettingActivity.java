package com.zwonline.top28.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.OnClick;

/**
 * 描述：设置页面
 *
 * @author YSG
 * @date 2018/1/23
 */
public class MySettingActivity extends BaseActivity {


    private SharedPreferencesUtils sp;
    private String isDefaultPassword;
    private TextView settingPassword;
    private RelativeLayout back;
    private ImageViewPlus imagHead;
    private TextView nickname;
    private TextView exitLogin;
    private RelativeLayout amend;
    private RelativeLayout amentpossword;
    private String avatar;
    private String nicknames;

    @Override
    protected void init() {
        initData();
        settingPassword = (TextView) findViewById(R.id.setting_password);
        sp = SharedPreferencesUtils.getUtil();
        Intent intent = getIntent();
        avatar = (String) sp.getKey(getApplicationContext(), "avatar", "");
        nicknames = (String) sp.getKey(getApplicationContext(), "nickname", "");
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
    }

    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        imagHead = (ImageViewPlus) findViewById(R.id.imag_head);
        nickname = (TextView) findViewById(R.id.nickname);
        exitLogin = (TextView) findViewById(R.id.exit_login);
        amend = (RelativeLayout) findViewById(R.id.amend);
        amentpossword = (RelativeLayout) findViewById(R.id.amentpossword);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_setting;
    }


    @OnClick({R.id.back, R.id.amend, R.id.amentpossword, R.id.exit_login, R.id.feedback, R.id.shield_settting, R.id.join_group})
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
            case R.id.join_group:
                startActivity(new Intent(MySettingActivity.this, GroupActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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
        String avatars = (String) sp.getKey(getApplicationContext(), "avatar", "");
        String name = (String) sp.getKey(getApplicationContext(), "nickname", "");
        nickname.setText(name);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male)
                .error(R.mipmap.no_photo_male);
        Glide.with(MySettingActivity.this).load(avatars).apply(options).into(imagHead);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
