package com.zwonline.top28.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.back)
    RelativeLayout re_back;
    @BindView(R.id.te_visioncode)
    TextView te_VisionCode;
    private PackageInfo packInfo;


    @Override
    protected void init() {
        //获取packagemanager的实例
        PackageManager packageManager = this.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        te_VisionCode.setText(packInfo.versionName + "");
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about_us;
    }

    @OnClick({R.id.back, R.id.user_protocol, R.id.privacy_policy})
    public void onClickListen(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                Intent backIntent = new Intent(AboutUsActivity.this, MySettingActivity.class);
                startActivity(backIntent);
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out );
                break;
            case R.id.user_protocol:
                Intent intent = new Intent(AboutUsActivity.this, UserProTocolActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.privacy_policy:
                Intent bintent = new Intent(AboutUsActivity.this, PrivacyPolicyActivity.class);
                startActivity(bintent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }
}
