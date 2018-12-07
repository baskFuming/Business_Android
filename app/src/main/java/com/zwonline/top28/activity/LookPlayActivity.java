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
import com.zwonline.top28.constants.BizConstant;

import butterknife.OnClick;

/**
 * 查看玩法
 */
public class LookPlayActivity extends BaseActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView tvFunction;
    private RelativeLayout enterpriseUser;//企业用户
    private RelativeLayout entrepreneur;//创业者
    private RelativeLayout blockchainEnthusiast;//区块链爱好者
    private RelativeLayout blockchainInvestor;//区块链投资者

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
        return R.layout.activity_look;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        title.setText("查看玩法");
        tvFunction = (TextView) findViewById(R.id.tv_function);
        enterpriseUser = (RelativeLayout) findViewById(R.id.enterprise_user);
        entrepreneur = (RelativeLayout) findViewById(R.id.entrepreneur);
        blockchainEnthusiast = (RelativeLayout) findViewById(R.id.blockchain_enthusiast);
        blockchainInvestor = (RelativeLayout) findViewById(R.id.blockchain_investor);
    }

    @OnClick({R.id.back, R.id.enterprise_user, R.id.entrepreneur, R.id.blockchain_enthusiast, R.id.blockchain_investor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.enterprise_user://企业用户
                Intent intent = new Intent(LookPlayActivity.this, GuideActivity.class);
                intent.putExtra("imageArray", BizConstant.ENTERPRISEIMAGEARRAY);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.entrepreneur://创业者
                Intent entrepreneur_intent = new Intent(LookPlayActivity.this, GuideActivity.class);
                entrepreneur_intent.putExtra("imageArray", BizConstant.ENTREPRENEURIMAGEARRAY);
                startActivity(entrepreneur_intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.blockchain_enthusiast://区块链爱好者
                Intent enthusiast_intent = new Intent(LookPlayActivity.this, GuideActivity.class);
                enthusiast_intent.putExtra("imageArray", BizConstant.ENTHUSIASTIMAGEARRAY);
                startActivity(enthusiast_intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.blockchain_investor://区块链投资者
                Intent investor_intent = new Intent(LookPlayActivity.this, GuideActivity.class);
                investor_intent.putExtra("imageArray", BizConstant.INVESTORIMAGEARRAY);
                startActivity(investor_intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            default:
                break;
        }

    }
}
