package com.zwonline.top28.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.PaymentBean;
import com.zwonline.top28.presenter.WallletPresenter;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IPayMentView;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：提现
 *
 * @author YSG
 * @date 2018/1/21
 */
public class BalanceActivity extends BaseActivity<IPayMentView, WallletPresenter> implements IPayMentView {


    private TextView balanceNums;
    private Button recharge;
    private Button withdraw;

    @Override
    protected void init() {
        presenter.mBalance(this);
        initView();
    }

    //初始化组件
    private void initView() {
        balanceNums = (TextView) findViewById(R.id.balance_nums);
        recharge = (Button) findViewById(R.id.recharge);
        withdraw = (Button) findViewById(R.id.withdraw);
    }

    @Override
    protected WallletPresenter getPresenter() {
        return new WallletPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    public void showBalance(BalanceBean balanceBean) {
        balanceNums.setText(balanceBean.data);
    }


    @OnClick({R.id.back, R.id.recharge, R.id.withdraw})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.recharge:
                startActivity(new Intent(BalanceActivity.this, BalanceRechargeActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.withdraw:
                startActivity(new Intent(BalanceActivity.this, BalanceWithdrawActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    @Override
    public void showPayMent(boolean flag) {

    }

    @Override
    public void showOnErro() {

    }

    @Override
    public void showPayMentData(List<PaymentBean.DataBean> payList) {

    }


}
