package com.zwonline.top28.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xgr.easypay.EasyPay;
import com.xgr.easypay.alipay.AliPay;
import com.xgr.easypay.alipay.AlipayInfoImpli;
import com.xgr.easypay.callback.IPayCallback;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.PaymentPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IPaymentActivity;

import butterknife.OnClick;

/**
 * 确认支付activity
 */

public class PaymentActivity extends BaseActivity<IPaymentActivity, PaymentPresenter> implements IPaymentActivity {

    private RadioGroup payRadioGroup;
    private RadioButton posRadioBut, alipayRadioBut, bankCarkRadioBut;
    private TextView payAmountText;
    private Button surePayBut;
    private String orderStr;
    private String orderId;
    private String orderAmount;
    private String orderCode;
    private String projectName;


    @Override
    protected void init() {
        initWidget();
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderinfo_id");

        presenter.getPayOrderInfoByOrder(this, orderId);

//        presenter.getPayOrderInfoByOrderId(this, "1");
    }

    private void initWidget() {
        payRadioGroup = (RadioGroup) findViewById(R.id.pay_radiogroup);
        //init widget event
        payRadioGroup.setOnCheckedChangeListener(payRadioListen);
        payAmountText = (TextView) findViewById(R.id.pay_sum);
        posRadioBut = (RadioButton) findViewById(R.id.pos_pay_btn);
        alipayRadioBut = (RadioButton) findViewById(R.id.alipay_pay_btn);
        bankCarkRadioBut = (RadioButton) findViewById(R.id.bankcard_pay_btn);
        surePayBut = (Button) findViewById(R.id.sure_pay_btn);
    }

    private RadioGroup.OnCheckedChangeListener payRadioListen = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            switch (id) {
                case R.id.alipay_pay_btn:
//                    ToastUtils.showToast(PaymentActivity.this, "我选择的是:"+alipayRadioBut.getText());
                    presenter.getPayOrderInfoByOrderId(PaymentActivity.this, orderId);
                    break;
                case R.id.bankcard_pay_btn:
//                    ToastUtils.showToast(PaymentActivity.this, "我选择的是:"+bankCarkRadioBut.getText());
                    break;
                case R.id.pos_pay_btn:
//                    ToastUtils.showToast(PaymentActivity.this, "我选择的是:"+posRadioBut.getText());
                    break;
            }

        }
    };

    @Override
    protected PaymentPresenter getPresenter() {
        return new PaymentPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_payment;
    }

    @OnClick({R.id.payment_sure_back, R.id.sure_pay_btn})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.payment_sure_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.sure_pay_btn:
                int checkedId = payRadioGroup.getCheckedRadioButtonId();  //获取选中按钮的id
                if (checkedId == posRadioBut.getId()) {  //pos 支付
//                    ToastUtils.showToast(PaymentActivity.this, "我选择的是: POS");
                    Intent intent = new Intent(PaymentActivity.this, PosOrderCodeActivity.class);
                    intent.putExtra("amount", orderAmount);
                    intent.putExtra("order_code", orderCode);
                    intent.putExtra("order_id", orderId);
                    intent.putExtra("project_name", projectName);
                    intent.putExtra("pos_recharge", BizConstant.POS_GATHERING);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else if (checkedId == alipayRadioBut.getId()) {  //支付宝支付
//                    ToastUtils.showToast(PaymentActivity.this, "我选择的是: Alipay");
                    toAlipay();
                } else if (checkedId == bankCarkRadioBut.getId()) {  //银行卡支付
//                    ToastUtils.showToast(PaymentActivity.this, "我选择的是: bankcard");
                    Intent bankIntent = new Intent(PaymentActivity.this, BankPayActivity.class);
                    bankIntent.putExtra("order_code", orderCode);
                    startActivity(bankIntent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
        }
    }

    private void toAlipay() {
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
//        ToastUtils.showToast(PaymentActivity.this, "返回的订单支付信息" + orderStr);
        alipayInfoImpli.setOrderInfo(orderStr);
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(aliPay, this, alipayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
//                ToastUtils.showToast(PaymentActivity.this, "支付成功");
                showNormalDialogFollow();
            }

            @Override
            public void failed() {
                ToastUtils.showToast(PaymentActivity.this, "支付失败");
            }

            @Override
            public void cancel() {
                ToastUtils.showToast(PaymentActivity.this, "支付取消");
            }
        });
    }

    //获取支付宝orderStr
    @Override
    public void getOrderInfoByOrderId(PrepayPayBean.DataBean paymentData) {
        orderStr = paymentData.order_str;
    }

    //获取订单详情
    @Override
    public void showOrderInfo(OrderInfoBean paymentData) {
        orderAmount = paymentData.data.amount;
        orderCode = paymentData.data.oid;
        projectName = paymentData.data.project_name;
        payAmountText.setText(orderAmount + getString(R.string.amount_unit));
        if (!StringUtil.isEmpty(String.valueOf(paymentData.status)) && !(paymentData.status == 1)) {
            ToastUtil.showToast(this, paymentData.msg);
            finish();
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        }
    }

    @Override
    public void showGetOrderPayStatus(AmountPointsBean amountPointsBean) {

    }

    @Override
    public void pollingOrderPayStatus() {

    }

    @Override
    public void stopPollingOrder() {

    }
    //删除条款的弹框
    private void showNormalDialogFollow() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("普通的对话框的标题");
        builder.setMessage("付款成功");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PaymentActivity.this,WalletActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
