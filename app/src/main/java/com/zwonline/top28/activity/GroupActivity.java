package com.zwonline.top28.activity;

import android.view.View;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.OnClick;

public class GroupActivity extends BaseActivity {
    private RelativeLayout back;
    @Override
    protected void init() {
       initView();
    }

    private void initView() {
        back = (RelativeLayout)findViewById(R.id.back);
    }
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_group;
    }
    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}
