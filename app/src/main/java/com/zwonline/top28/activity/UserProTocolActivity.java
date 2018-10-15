package com.zwonline.top28.activity;

import android.os.Bundle;
import android.view.View;

import com.zwonline.top28.R;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.forward.androids.base.BaseActivity;

public class UserProTocolActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pro_tocol);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back})
    public void onCliceListen(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }
}