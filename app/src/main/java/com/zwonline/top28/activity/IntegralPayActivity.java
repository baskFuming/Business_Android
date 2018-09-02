package com.zwonline.top28.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.BalancePayBean;
import com.zwonline.top28.bean.IntegralPayBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.IntegralPayPresenter;
import com.zwonline.top28.utils.NumberOperateUtil;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.PaySucPopuWindow;
import com.zwonline.top28.view.IIntegralPayActivity;

import butterknife.OnClick;

/**
 * 描述：积分充值操作类
 *
 * @author YSG
 * @date 2018/3/12
 */
public class IntegralPayActivity extends BaseActivity<IIntegralPayActivity, IntegralPayPresenter> implements IIntegralPayActivity {

    private RadioGroup pointsRadioGroup, payMethodRadioGroup;
    private EditText pointsEditText;
    private Button paySureBtn;
    private TextView pointsMonney;
    private String amountPoints;
    private RadioButton onePointsBtn, twoPointsBtn, threePointsBtn;
    private String payMethodType = BizConstant.ALIPAY_METHOD;
    private RadioButton alipayBtn, unionpayBtn, posBtn;
    private String orderStr;
    private String orderId;
    private Double unitPrice;//单价
    private String orderAmount;
    private String orderCode;
    private String projectName;
    private Double balance;
    private int payCheckedId;
    private PaySucPopuWindow paySucPopuWindow;
    @Override
    protected void init() {
        initWeight(); //初始化控件
        presenter.getUnitPrice(this);//订单单价请求
        presenter.mBalances(this);//余额
        sendAmountByPoints("100"); //初化化数据
        pointsEditText.addTextChangedListener(textWatcher);
        pointsEditText.setSelection(pointsEditText.getText().length());//设置光标在文本末尾
    }

    void initWeight() {
        pointsMonney = (TextView) findViewById(R.id.points_monney);
        pointsRadioGroup = (RadioGroup) findViewById(R.id.points_radioGroup);
        pointsRadioGroup.setOnCheckedChangeListener(pointsRadioListener);
        onePointsBtn = (RadioButton) findViewById(R.id.one_points);
        twoPointsBtn = (RadioButton) findViewById(R.id.two_points);
        threePointsBtn = (RadioButton) findViewById(R.id.three_points);
        pointsEditText = (EditText) findViewById(R.id.points_edittext);
        payMethodRadioGroup = (RadioGroup) findViewById(R.id.pay_radiogroup);
        payMethodRadioGroup.setOnCheckedChangeListener(payMethodRadioListener);
        alipayBtn = (RadioButton) findViewById(R.id.alipay_btn);
        unionpayBtn = (RadioButton) findViewById(R.id.unionpay_btn);
        posBtn = (RadioButton) findViewById(R.id.pos_btn);
        paySureBtn = (Button) findViewById(R.id.pay_sure_btn);
        onePointsBtn.setText("100" + getString(R.string.coin_bole_coin));
        twoPointsBtn.setText("500" + getString(R.string.coin_bole_coin));
        threePointsBtn.setText("1000" + getString(R.string.coin_bole_coin));
    }


    /**
     * 积分group
     */
    private RadioGroup.OnCheckedChangeListener pointsRadioListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            switch (id) {
                case R.id.one_points:
                    sendAmountByPoints("100");
                    pointsMonney.setText((Double) NumberOperateUtil.mul(100, unitPrice) + "");
                    pointsEditText.setSelection(pointsEditText.getText().length());//设置光标在文本末尾
                    break;
                case R.id.two_points:
                    sendAmountByPoints("500");
                    pointsMonney.setText((Double) NumberOperateUtil.mul(500, unitPrice) + "");
                    pointsEditText.setSelection(pointsEditText.getText().length());//设置光标在文本末尾
                    break;
                case R.id.three_points:
                    sendAmountByPoints("1000");
                    pointsMonney.setText((Double) NumberOperateUtil.mul(1000, unitPrice) + "");
                    pointsEditText.setSelection(pointsEditText.getText().length());//设置光标在文本末尾
                    break;
            }
        }
    };


    private void sendAmountByPoints(String amount) {
        pointsEditText.setText(amount);
    }

    /**
     * 支付方式group
     */
    private RadioGroup.OnCheckedChangeListener payMethodRadioListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            payCheckedId = payMethodRadioGroup.getCheckedRadioButtonId();
            switch (id) {
                case R.id.alipay_btn:
                    payMethodType = BizConstant.ALIPAY_METHOD;
                    paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
                    paySureBtn.setEnabled(true);
                    break;
                case R.id.unionpay_btn:
                    payMethodType = BizConstant.UNIONPAY_METHOD;
//                    Double Monney= Double.valueOf(pointsMonney.getText().toString());
//                    if (Monney>balance){
//                        paySureBtn.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
//                        paySureBtn.setEnabled(false);
//                    }else {
//                        paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
////                    presenter.mBalancesPay(IntegralPayActivity.this,pointsMonney.getText().toString());
//                    }
                    isBalance();
                    break;

                case R.id.pos_btn:
                    payMethodType = BizConstant.POS_METHOD;
                    paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
                    paySureBtn.setEnabled(true);
                    break;
            }
        }
    };

    @OnClick({R.id.points_pay_back, R.id.pay_sure_btn})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.points_pay_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.pay_sure_btn:

                int pointsCheckedId = pointsRadioGroup.getCheckedRadioButtonId();  //获取选中按钮的id
                int payCheckedId = payMethodRadioGroup.getCheckedRadioButtonId();
                String payAmount = pointsEditText.getText().toString();
                if (checkAmount(payAmount)) {
                    if (payCheckedId == alipayBtn.getId()) {  //支付宝支付
                        presenter.pointRecharge(IntegralPayActivity.this, payMethodType, pointsMonney.getText().toString());
                    } else if (payCheckedId == unionpayBtn.getId()) {
//                        presenter.pointRecharge(IntegralPayActivity.this, payMethodType, pointsMonney.getText().toString());
//                        ToastUtils.showToast(IntegralPayActivity.this, "选着的是银行卡" + orderCode);
                      Double Monney= Double.valueOf(pointsMonney.getText().toString());
                        if (Monney>balance){
                            paySureBtn.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
                        }else {
                            paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
                            showNormalDialogFollow();

                        }
                    } else if (payCheckedId == posBtn.getId()) {
                        presenter.pointRecharge(IntegralPayActivity.this, payMethodType, pointsMonney.getText().toString());
//                        ToastUtils.showToast(IntegralPayActivity.this, "选着的pos机");

                    } else {
                        ToastUtils.showToast(IntegralPayActivity.this, getString(R.string.common_pay_method_empty));
                        return;
                    }
                }
        }
    }

    boolean checkAmount(String payAmount) {
        if (!StringUtil.isEmpty(payAmount)) {
            return true;
        } else {
            ToastUtils.showToast(IntegralPayActivity.this, getString(R.string.common_amount_empty));
            return false;
        }
    }

    @Override
    public void initAlipayOrderStr() {
        if (!StringUtil.isEmpty(orderId)) {
            presenter.getPayOrderInfoByOrderId(IntegralPayActivity.this, orderId);
        } else {
            ToastUtils.showToast(IntegralPayActivity.this, getString(R.string.common_orderId_empty));
            return;
        }
    }

    //获取订单详情
    @Override
    public void showOrderInfo(OrderInfoBean paymentData) {
        int payCheckedId = payMethodRadioGroup.getCheckedRadioButtonId();
        orderAmount = paymentData.data.amount;
        orderCode = paymentData.data.oid;
        projectName = paymentData.data.project_name;
        if (paymentData.status == 1) {
            if (payCheckedId == unionpayBtn.getId()) {
                Intent bankIntent = new Intent(IntegralPayActivity.this, BankPayActivity.class);//银行卡支付
                //                    intent.putExtra("orderid", orderId);
                bankIntent.putExtra("orderCode", orderId);
                startActivity(bankIntent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            } else if (payCheckedId == posBtn.getId()) {
                Intent posIntent = new Intent(IntegralPayActivity.this, PosOrderCodeActivity.class);
                posIntent.putExtra("amount", orderAmount);
                posIntent.putExtra("order_code", orderCode);
                posIntent.putExtra("project_name", projectName);
                posIntent.putExtra("order_id", orderId);
                posIntent.putExtra("pos_recharge", BizConstant.POS_INTEGRAL);
                startActivity(posIntent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            } else {
                ToastUtils.showToast(this, paymentData.msg);
            }
        }

    }

    //余额
    @Override
    public void showBalance(BalanceBean balanceBean) {
       balance = Double.valueOf(balanceBean.data);
        unionpayBtn.setText(getText(R.string.bank_balance_payments)+"("+ balance +"元)");

    }

    /**
     * 余额充值
     * @param balancePayBean
     */
    @Override
    public void showBalancePay(BalancePayBean balancePayBean) {
        if (balancePayBean.status.equals("1")){
            paySucPopuWindow = new PaySucPopuWindow(IntegralPayActivity.this, listener,pointsEditText.getText().toString().trim(),pointsMonney.getText().toString().trim(),balancePayBean.data.balance);
            paySucPopuWindow.showAtLocation(IntegralPayActivity.this.findViewById(R.id.integral_pay_linear), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        }else {
            ToastUtils.showToast(this,balancePayBean.msg);
        }
    }

    //返回积分单价接口
    @Override
    public void IUnitPriceId(AmountPointsBean dataBean) {
        unitPrice = Double.parseDouble(dataBean.data);
        Double parseDouble = Double.parseDouble(pointsEditText.getText().toString());
        Double cp = NumberOperateUtil.mul(parseDouble, unitPrice);
        pointsMonney.setText(cp + "");
//        presenter.pointRecharge(IntegralPayActivity.this, payMethodType, cp+"");
    }

    private void toAlipay(String orderStrs) {
        //实例化支付宝支付策略
        AliPay aliPay = new AliPay();
        //构造支付宝订单实体。一般都是由服务端直接返回。
        AlipayInfoImpli alipayInfoImpli = new AlipayInfoImpli();
        //        ToastUtils.showToast(PaymentActivity.this, "返回的订单支付信息" + orderStr);
        alipayInfoImpli.setOrderInfo(orderStrs);
        //策略场景类调起支付方法开始支付，以及接收回调。
        EasyPay.pay(aliPay, this, alipayInfoImpli, new IPayCallback() {
            @Override
            public void success() {
                ToastUtils.showToast(IntegralPayActivity.this, "支付成功");
            }

            @Override
            public void failed() {
                ToastUtils.showToast(IntegralPayActivity.this, "支付失败");
            }

            @Override
            public void cancel() {
                ToastUtils.showToast(IntegralPayActivity.this, "支付取消");
            }
        });
    }

    @Override
    protected IntegralPayPresenter getPresenter() {
        return new IntegralPayPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_integral_pay;
    }

    @Override
    public void pointsRecharge(IntegralPayBean dataBean) {
        orderId = dataBean.data;
        int payCheckedId = payMethodRadioGroup.getCheckedRadioButtonId();
        if (dataBean.status == 1) {
            if (payCheckedId == alipayBtn.getId()) {
                presenter.getPayOrderInfoByOrderId(IntegralPayActivity.this, orderId);
            } else {
                presenter.getPayOrderInfoByOrder(IntegralPayActivity.this, orderId);
            }
        } else {
            ToastUtils.showToast(this, dataBean.msg);
        }

    }

    /**
     * 支付宝支付
     *
     * @param paymentData
     */
    @Override
    public void getOrderInfoByOrderId(PrepayPayBean paymentData) {
        orderStr = paymentData.data.order_str;
        if (paymentData.status == 1) {
            toAlipay(orderStr);
        } else {
            ToastUtils.showToast(this, paymentData.msg);
        }

    }

    @Override
    public void getPointsRechargeBackAmount(AmountPointsBean dataBean) {
        amountPoints = dataBean.data;
    }


    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String pointsEditT = pointsEditText.getText().toString();
            if (pointsEditT.contains(getString(R.string.coin_bole_coin))) {
                String replacepointsEditT = pointsEditT.replace(getString(R.string.coin_bole_coin), "");
                if (!StringUtil.isEmpty(replacepointsEditT)) {
                    pointsMonney.setText(pointsEditT);
                }
            }
            if (payCheckedId == unionpayBtn.getId()){
//                Double Monney= Double.valueOf(pointsMonney.getText().toString());
//                if (Monney>balance){
//                    paySureBtn.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
//                    paySureBtn.setEnabled(false);
//                }else {
//                    paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
////                    presenter.mBalancesPay(IntegralPayActivity.this,pointsMonney.getText().toString());
//                }
                isBalance();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            showIntegralBackGround();
            String pointsEditT = pointsEditText.getText().toString();
            if (pointsEditT.contains(getString(R.string.coin_bole_coin))) {
                String replacepointsEditT = pointsEditT.replace(getString(R.string.coin_bole_coin), "");
                if (!StringUtil.isEmpty(replacepointsEditT)) {
                    Double parseDouble = Double.parseDouble(replacepointsEditT);
                    pointsMonney.setText((Double) NumberOperateUtil.mul(parseDouble, unitPrice) + "");
                }
            } else {
                if (!StringUtil.isEmpty(pointsEditT)) {
                    Double parseDouble = Double.parseDouble(pointsEditT);
                    Double cp = NumberOperateUtil.mul(parseDouble, unitPrice);
                    pointsMonney.setText(cp + "");
                }
            }
            if (payCheckedId == unionpayBtn.getId()){
//                Double Monney= Double.valueOf(pointsMonney.getText().toString());
//                if (Monney>balance){
//                    paySureBtn.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
//                    paySureBtn.setEnabled(false);
//                }else {
//                    paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
////                    presenter.mBalancesPay(IntegralPayActivity.this,pointsMonney.getText().toString());
//                }
                isBalance();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            showIntegralBackGround();
            String pointsEditT = pointsEditText.getText().toString();
            if (pointsEditT.contains(getString(R.string.coin_bole_coin))) {
                String replacepointsEditT = pointsEditT.replace(getString(R.string.coin_bole_coin), "");
                if (!StringUtil.isEmpty(replacepointsEditT)) {
                    Double parseDouble = Double.parseDouble(replacepointsEditT);
                    Double cp = NumberOperateUtil.mul(parseDouble, unitPrice);
                    pointsMonney.setText(cp + "");
//                    presenter.pointRecharge(IntegralPayActivity.this, payMethodType, cp+"");
                } else {
                    pointsMonney.setText("0");
                }
            } else {
                if (!StringUtil.isEmpty(pointsEditT)) {
                    Double parseDouble = Double.parseDouble(pointsEditT);
                    pointsMonney.setText((Double) NumberOperateUtil.mul(parseDouble, unitPrice) + "");
                    Double cp = NumberOperateUtil.mul(parseDouble, unitPrice);
                    pointsMonney.setText(cp + "");
//                    presenter.pointRecharge(IntegralPayActivity.this, payMethodType, cp+"");
                } else {
                    pointsMonney.setText("0");
                }
            }

            if (payCheckedId == unionpayBtn.getId()){
//                Double Monney= Double.valueOf(pointsMonney.getText().toString());
//                if (Monney>balance){
//                    paySureBtn.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
//                    paySureBtn.setEnabled(false);
//                }else {
//                    paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
////                    presenter.mBalancesPay(IntegralPayActivity.this,pointsMonney.getText().toString());
//                }
                isBalance();
            }
        }

    };

    //积分按钮的背景颜色判断
    public void showIntegralBackGround() {
        String pointsEdit = pointsEditText.getText().toString();
        if (pointsEdit.equals("100")) {
            onePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_pink);
            onePointsBtn.setTextColor(Color.parseColor("#ffffff"));
            twoPointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            twoPointsBtn.setTextColor(Color.parseColor("#FF0000"));
            threePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            threePointsBtn.setTextColor(Color.parseColor("#FF0000"));
        } else if (pointsEdit.equals("500")) {
            twoPointsBtn.setBackgroundResource(R.drawable.rectangle_shape_pink);
            twoPointsBtn.setTextColor(Color.parseColor("#ffffff"));
            onePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            onePointsBtn.setTextColor(Color.parseColor("#FF0000"));
            threePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            threePointsBtn.setTextColor(Color.parseColor("#FF0000"));
        } else if (pointsEdit.equals("1000")) {
            threePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_pink);
            threePointsBtn.setTextColor(Color.parseColor("#ffffff"));
            twoPointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            twoPointsBtn.setTextColor(Color.parseColor("#FF0000"));
            onePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            onePointsBtn.setTextColor(Color.parseColor("#FF0000"));
        } else {
            onePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            onePointsBtn.setTextColor(Color.parseColor("#FF0000"));
            twoPointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            twoPointsBtn.setTextColor(Color.parseColor("#FF0000"));
            threePointsBtn.setBackgroundResource(R.drawable.rectangle_shape_red);
            threePointsBtn.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public void initOrderInfo() {
//         presenter.getPayOrderInfoByOrder(IntegralPayActivity.this, orderId);
    }

    /**
     * 确认充值弹窗
     */
    private void showNormalDialogFollow() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("您确认消费"+pointsMonney.getText().toString().trim()+"元充值"+pointsEditText.getText().toString().trim()+"算力");
//        normalDialog.setMessage(R.string.is_willing_answer_calls);pointsEditText.getText().toString().trim(),pointsMonney.getText().toString().trim()
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.mBalancesPay(IntegralPayActivity.this,pointsMonney.getText().toString());
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * popwindow弹窗
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {//返回
                case R.id.back_hashrate:
                    startActivity(new Intent(IntegralPayActivity.this,IntegralActivity.class));
                    finish();
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    paySucPopuWindow.dismiss();
                    paySucPopuWindow.backgroundAlpha(IntegralPayActivity.this, 1f);
                    break;
                case R.id.continue_pay://继续充值
                    paySucPopuWindow.dismiss();
                    paySucPopuWindow.backgroundAlpha(IntegralPayActivity.this, 1f);
                    break;
            }
        }
    };



    /**
     * 判断余额是否够充值
     */
    public void isBalance(){
        Double Monney= Double.valueOf(pointsMonney.getText().toString());
        if (Monney>balance){
            paySureBtn.setBackgroundResource(R.drawable.btn_noguanzhu_gray);
            paySureBtn.setEnabled(false);
        }else {
            paySureBtn.setEnabled(true);
            paySureBtn.setBackgroundResource(R.drawable.btn_register_shape);
//                    presenter.mBalancesPay(IntegralPayActivity.this,pointsMonney.getText().toString());
        }
    }
}
