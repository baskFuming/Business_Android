package com.zwonline.top28.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.WithdrawRecordAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.WithdrawRecordBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.BalancePresenter;
import com.zwonline.top28.utils.ItemDecoration;
import com.zwonline.top28.view.IBalanceActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：提现记录
 *
 * @author YSG
 * @date 2018/3/15
 */
public class WithdrawRecordActivity extends BaseActivity<IBalanceActivity, BalancePresenter> implements IBalanceActivity {

    private RelativeLayout withdrawBack;
    private RecyclerView withdrawRecy;
    private SwipeRefreshLayout withdrawswipe;
    private WithdrawRecordAdapter recordAdapter;
    private TextView withdrawNo;

    @Override
    protected void init() {
        initView();
        presenter.mWithdrawRecord(this, String.valueOf(BizConstant.PAGE));
    }

    @Override
    protected BalancePresenter getPresenter() {
        return new BalancePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_withdraw_record;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        withdrawBack = (RelativeLayout) findViewById(R.id.withdraw_back);
        withdrawRecy = (RecyclerView) findViewById(R.id.withdraw_recy);
        withdrawswipe = (SwipeRefreshLayout) findViewById(R.id.withdraw_swipe);
        withdrawNo = (TextView) findViewById(R.id.withdraw_no);
    }

    @Override
    public void showWithdrawRecord(List<WithdrawRecordBean.DataBean> withdrawList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        withdrawRecy.setLayoutManager(linearLayoutManager);
        recordAdapter = new WithdrawRecordAdapter(withdrawList, this);
        withdrawRecy.setAdapter(recordAdapter);
        withdrawRecy.addItemDecoration(new ItemDecoration(this));
        recordAdapter.notifyDataSetChanged();
        withdrawswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.mWithdrawRecord(WithdrawRecordActivity.this, String.valueOf(BizConstant.PAGE));
                withdrawswipe.setRefreshing(false);
                recordAdapter.notifyDataSetChanged();
            }
        });
    }

    //判断是否有提现记录
    @Override
    public void showWithdrawRecordData(boolean flag) {
        if (flag) {
            withdrawNo.setVisibility(View.GONE);
            withdrawRecy.setVisibility(View.VISIBLE);
        } else {
            withdrawRecy.setVisibility(View.GONE);
            withdrawNo.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.withdraw_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
