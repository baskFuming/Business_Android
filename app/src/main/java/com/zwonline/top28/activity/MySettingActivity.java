package com.zwonline.top28.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
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
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.CacheDataManager;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.BindView;
import butterknife.OnClick;

import static io.reactivex.schedulers.Schedulers.newThread;

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
    private RelativeLayout exitLogin;
    private RelativeLayout amend;
    private RelativeLayout amentpossword;
    private String avatar;
    private String nicknames;
    private String mobile;
    private TextView bind;
    private RelativeLayout bindPhone;
    private ImageView bindImag;
    @BindView(R.id.text_cash)
    TextView tv_Cash;
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

        ;

    };
    private ProgressBar clearProgress;
    private LinearLayout clearDialog;

    @Override
    protected void init() {
        settingPassword = (TextView) findViewById(R.id.setting_password);
        sp = SharedPreferencesUtils.getUtil();
        mobile = (String) sp.getKey(this, "mobile", "");
        initData();
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
        try {

            tv_Cash.setText(CacheDataManager.getTotalCacheSize(this));

        } catch (Exception e) {

            e.printStackTrace();

        }
        //获取缓存
//        tv_Cash.setText(DataClearUtil.getTotalCacheSize(this));
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
        TextView clearTv= (TextView) findViewById(R.id.clear_tv);
        clearTv.setSelected(true);
        if (StringUtil.isNotEmpty(mobile)) {
            bind.setText("已绑定");
            bindPhone.setClickable(false);
            bindImag.setVisibility(View.GONE);
            bind.setTextColor(Color.parseColor("#d1d1d1"));
        } else {
            bind.setText("未绑定");
            bind.setTextColor(Color.parseColor("#ff2b2b"));
            bindImag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_setting;
    }


    @OnClick({R.id.back, R.id.amend, R.id.amentpossword, R.id.exit_login, R.id.feedback, R.id.shield_settting, R.id.bind_phone, R.id.about_owen, R.id.lin_discash})
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
            case R.id.about_owen:
                startActivity(new Intent(MySettingActivity.this, AboutUsActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            //清除缓存功能
            case R.id.lin_discash:
                //清除缓存
//                DataClearUtil.cleanAllCache(this);
//                tv_Cash.setText("0.0MB");
//                ToastUtils.showToast(this,"清除缓存成功");
                clearCache clearCache = new clearCache();
                new Thread(clearCache).start();
                clearDialog.setVisibility(View.VISIBLE);
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

    class clearCache implements Runnable {

        @Override

        public void run() {

            try {

                CacheDataManager.clearAllCache(MySettingActivity.this);

                Thread.sleep(3000);

                if (CacheDataManager.getTotalCacheSize(MySettingActivity.this).startsWith("0")) {

                    handler.sendEmptyMessage(0);


                }

            } catch (Exception e) {

                return;

            }

        }

    }


}
