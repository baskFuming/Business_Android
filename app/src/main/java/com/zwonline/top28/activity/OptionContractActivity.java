package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.OptionContractAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.OptionContractBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.OptionContractPresenter;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.view.IOptionContractActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：选择合同
 *
 * @author YSG
 * @date 2018/4/6
 */
public class OptionContractActivity extends BaseActivity<IOptionContractActivity, OptionContractPresenter> implements IOptionContractActivity {

    private RelativeLayout optionBack;
    private RecyclerView optionXrecy;
    private LinearLayout haveContract;
    private LinearLayout noContract;
    private Button addContract;
    private String projectId;
    private String enterprise_name;//项目名字

    @Override
    protected void init() {
        initView();
        projectId = getIntent().getStringExtra("projectId");
        enterprise_name = getIntent().getStringExtra("gathering_project");
        presenter.mOptionContractModel(this);
    }

    @Override
    protected OptionContractPresenter getPresenter() {
        return new OptionContractPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_option_contract;
    }

    private void initView() {
        optionBack = (RelativeLayout) findViewById(R.id.option_back);
        optionXrecy = (RecyclerView) findViewById(R.id.option_xrecy);
        haveContract = (LinearLayout) findViewById(R.id.have_contract);
        noContract = (LinearLayout) findViewById(R.id.no_contract);
        addContract = (Button) findViewById(R.id.add_contract);

    }

    /**
     * 显示选择合同列表
     *
     * @param optionContractBean
     */
    @Override
    public void showOptionContract(final List<OptionContractBean.DataBean> optionContractBean) {

        ScrollLinearLayoutManager scrollLinearLayoutManager=new ScrollLinearLayoutManager(this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        optionXrecy.setLayoutManager(scrollLinearLayoutManager);
        OptionContractAdapter optionContractAdapter = new OptionContractAdapter(optionContractBean, this);
        optionXrecy.setAdapter(optionContractAdapter);
        optionContractAdapter.notifyDataSetChanged();
        optionContractAdapter.setOnClickItemListener(new OptionContractAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                Intent intent = new Intent(OptionContractActivity.this, CustomContractActivity.class);
                intent.putExtra("contractId", optionContractBean.get(position).contract_id);
                intent.putExtra("projectId",projectId);
                intent.putExtra("cid", BizConstant.MOBAN);
                intent.putExtra("enterprise_name",enterprise_name);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    /**
     * 判别是否有合同
     *
     * @param flag
     */
    @Override
    public void noContract(boolean flag) {
        if (flag) {
            haveContract.setVisibility(View.VISIBLE);
            noContract.setVisibility(View.GONE);
        } else {
            haveContract.setVisibility(View.GONE);
            noContract.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.option_back, R.id.add_contract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.option_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.add_contract:
                Intent intent = new Intent(OptionContractActivity.this, AddContractActivity.class);
                intent.putExtra("projectId",projectId);
                intent.putExtra("enterprise_name",enterprise_name);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }
}
