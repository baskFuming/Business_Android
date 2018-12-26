package com.zwonline.top28.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.ImageViewPlu;
import com.zwonline.top28.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商机圈编辑页面
 */
public class CreatorBusinessCircleActivity extends BaseActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView tvFunction;
    private ImageViewPlu businessCiclerCover;
    private RelativeLayout uploadPicture;
    private TextView businessCiclerName;
    private RelativeLayout businessCiclerNameRelaytive;
    private TextView businessCiclerNotice;


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
        return R.layout.activity_creator_businesscircle;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        title.setText("创业商机圈");
        tvFunction = (TextView) findViewById(R.id.tv_function);
        businessCiclerCover = (ImageViewPlu) findViewById(R.id.business_cicler_cover);
        uploadPicture = (RelativeLayout) findViewById(R.id.upload_picture);
        businessCiclerName = (TextView) findViewById(R.id.business_cicler_name);
        businessCiclerNameRelaytive = (RelativeLayout) findViewById(R.id.business_cicler_name_relaytive);
        businessCiclerNotice = (TextView) findViewById(R.id.business_cicler_notice);
    }


    @OnClick({R.id.back, R.id.upload_picture, R.id.business_cicler_name_relaytive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                ToastUtils.showToast(getApplicationContext(), "返回");
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.upload_picture://上传封面
                ToastUtils.showToast(getApplicationContext(), "上传封面");
                break;
            case R.id.business_cicler_name_relaytive://商机圈名称
                ToastUtils.showToast(getApplicationContext(), "商机圈名称");
                break;
        }
    }
}
