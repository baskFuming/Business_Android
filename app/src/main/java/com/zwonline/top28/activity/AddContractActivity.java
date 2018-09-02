package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.TemplateContractAdater;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.OptionContractBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.OptionContractPresenter;
import com.zwonline.top28.view.IOptionContractActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：添加合同
 *
 * @author YSG
 * @date 2018/4/6
 */
public class AddContractActivity extends BaseActivity<IOptionContractActivity, OptionContractPresenter> implements IOptionContractActivity {

    private RelativeLayout addContractBack;
    private RecyclerView addContractXrecy;
    private Button addContract;
    private String projectId;
    private String enterprise_name;


    @Override
    protected void init() {
        initView();
        projectId = getIntent().getStringExtra("projectId");
        presenter.mOptionContractModels(this);
        enterprise_name = getIntent().getStringExtra("enterprise_name");
    }

    @Override
    protected OptionContractPresenter getPresenter() {
        return new OptionContractPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_contract;
    }

    private void initView() {
        addContractBack = (RelativeLayout) findViewById(R.id.add_contract_back);
        addContractXrecy = (RecyclerView) findViewById(R.id.add_contract_xrecy);
        addContract = (Button) findViewById(R.id.add_contract);

    }


    /**
     * 显示模板的数据
     *
     * @param optionContractBean
     */
    @Override
    public void showOptionContract(final List<OptionContractBean.DataBean> optionContractBean) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        addContractXrecy.setLayoutManager(linearLayoutManager);
        TemplateContractAdater templateContractAdater = new TemplateContractAdater(optionContractBean, this);
        addContractXrecy.setAdapter(templateContractAdater);
        templateContractAdater.notifyDataSetChanged();
        templateContractAdater.setOnClickItemListener(new TemplateContractAdater.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                Intent intent = new Intent(AddContractActivity.this, CustomContractActivity.class);
                intent.putExtra("contractId", optionContractBean.get(position).contract_id);
                intent.putExtra("projectId",projectId);
                intent.putExtra("enterprise_name",enterprise_name);
                intent.putExtra("cid", BizConstant.MOBAN);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    @Override
    public void noContract(boolean flag) {

    }

    /**
     * 点击事件
     *
     * @param view
     */

    @OnClick({R.id.add_contract_back, R.id.add_contract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_contract_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.add_contract:
                Intent intent = new Intent(AddContractActivity.this, CustomContractActivity.class);
                intent.putExtra("cid", BizConstant.CUSTOM_CONTRACT);
                intent.putExtra("projectId",projectId);
                intent.putExtra("enterprise_name",enterprise_name);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }
}
