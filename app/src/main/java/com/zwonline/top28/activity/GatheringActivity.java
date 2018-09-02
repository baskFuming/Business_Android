package com.zwonline.top28.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.NumberOperateUtil;
import com.zwonline.top28.utils.click.AntiShake;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：收款
 * @author YSG
 * @date 2018/1/3
 */
public class GatheringActivity extends BaseActivity {

    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.qian)
    ImageView qian;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.gathering_project)
    TextView gatheringProject;
    @BindView(R.id.withdraw)
    Button withdraw;
    private String projectId;
    private TextView gatheringProjectName;
    private String contractId;

    @Override
    protected void init() {
        gatheringProjectName = (TextView) findViewById(R.id.gathering_project);
        projectId = getIntent().getStringExtra("projectId");
        gatheringProjectName.setText(getString(R.string.pay_gathering_project) + getIntent().getStringExtra("enterprise_name"));
        contractId = getIntent().getStringExtra("contract_id");
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gathering;
    }


    @OnClick({R.id.back, R.id.withdraw})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.withdraw:
                if (TextUtils.isEmpty(amount.getText().toString().trim())) {
                    Toast.makeText(GatheringActivity.this, R.string.pay_not_amount, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(GatheringActivity.this, BusinessGatheringActivity.class);
                    intent.putExtra("amount", NumberOperateUtil.getBigDecimalForStrReturnDouble(
                            amount.getText().toString().trim(), 2));
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("contractId", contractId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
                break;
        }
    }
}
