package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.country.CityActivity;

import butterknife.OnClick;

public class BindSuccessActivity extends BaseActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView phone;
    private TextView tvBack;
    private String phoneNum;


    @Override
    protected void init() {
        initView();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_success;
    }

    private void initView() {
        phoneNum = getIntent().getStringExtra("phone_num");
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        phone = (TextView) findViewById(R.id.phone);
        tvBack = (TextView) findViewById(R.id.tv_back);
        if (StringUtil.isNotEmpty(phoneNum)) {
            phone.setText(phoneNum);
        }
        title.setText("绑定成功");
    }

    @OnClick({R.id.back, R.id.tv_back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.tv_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;

        }
    }

}
