package com.zwonline.top28.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.presenter.AddBankPresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IAddBankActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：添加银行卡
 *
 * @author YSG
 * @date 2017/12/27
 */
public class AddBankActivity extends BaseActivity<IAddBankActivity, AddBankPresenter> implements IAddBankActivity {

    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.et_chicar)
    EditText etChicar;
    @BindView(R.id.card_num)
    EditText cardNum;
    @BindView(R.id.open_bank)
    EditText openBank;
    @BindView(R.id.commit)
    Button commit;
    private String addCard;

    /**
     * 初始化
     */
    @Override
    protected void init() {
        Intent intent = getIntent();
        addCard = intent.getStringExtra("add_card");
    }

    @Override
    protected AddBankPresenter getPresenter() {
        return new AddBankPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_bank;
    }


    @OnClick({R.id.back, R.id.commit})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.commit:
                if (!StringUtil.isEmpty(addCard) && addCard.equals("1")){
                    addBankCard();
                    startActivity(new Intent(AddBankActivity.this, BalanceWithdrawActivity.class));
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }else {
                    addBankCard();
//                    startActivity(new Intent(AddBankActivity.this, BankActivity.class));
                    finish();
                    overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                }

                break;
        }
    }

    @Override
    public void isSuccess(AddBankBean addBankBean) {
        if (addBankBean.status==1) {
            ToastUtils.showToast(this,getString(R.string.add_suc_tip));
//            if (!StringUtil.isEmpty(addCard) && addCard.equals("1")){
//                startActivity(new Intent(AddBankActivity.this, BalanceWithdrawActivity.class));
//                finish();
//                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//            }else {
//                startActivity(new Intent(AddBankActivity.this, BankActivity.class));
//                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                finish();
//            }
//            Toast.makeText(this, R.string.add_suc_tip, Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(AddBankActivity.this, BankActivity.class));
//            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else {
            Toast.makeText(this, addBankBean.msg, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onErro() {
        Toast.makeText(this, R.string.add_fail_tip, Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加银行卡请求网络
     */
    public void addBankCard() {
        if (!etChicar.getText().toString().trim().equals("")) {
            if (!cardNum.getText().toString().trim().equals("")) {
                if (!openBank.getText().toString().trim().equals("")) {
                    presenter.mAddBank(this,
                            etChicar.getText().toString().trim(),
                            cardNum.getText().toString().trim(),
                            openBank.getText().toString().trim());
                    Toast.makeText(this, R.string.add_suc_tip, Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(AddBankActivity.this, BankActivity.class));
//                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    Toast.makeText(this, R.string.bank_account_name, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.bank_account_no, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.bank_customer_name, Toast.LENGTH_SHORT).show();
        }
    }

}
