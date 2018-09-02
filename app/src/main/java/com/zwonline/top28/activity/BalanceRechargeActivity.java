package com.zwonline.top28.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;
import com.xgr.easypay.EasyPay;
import com.xgr.easypay.alipay.AliPay;
import com.xgr.easypay.alipay.AlipayInfoImpli;
import com.xgr.easypay.callback.IPayCallback;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BalanceRechargeBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.BalanceRechargePresenter;
import com.zwonline.top28.utils.NumberOperateUtil;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IBalanceRechargeActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.OnClick;

/**
 * 描述：余额充值
 *
 * @author YSG
 * @date 2018/3/14
 */
public class BalanceRechargeActivity extends BaseActivity<IBalanceRechargeActivity, BalanceRechargePresenter>
        implements IBalanceRechargeActivity {

    private RadioGroup payRadioGroup;
    private String payMethodType = BizConstant.ALIPAY_RECHARGE_METHOD;
    private EditText payAmountEditText, payOrderStr;
    private Button balanceRechargeBtn;
    private String orderId;
    private String orderStr;
    //Activity最外层的Layout视图
    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private String orderCode;
    private Double unitPrice;
    private TextView integralCount;
    private RadioButton alipayBtn, unionpayBtn;

    @Override
    protected void init() {
        initWeight();
        presenter.getUnitPrice(this);//订单单价请求
        final LinearLayout activityRootView = (LinearLayout) findViewById(R.id.integtal_pay_linear);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(BalanceRechargeActivity.this, 150)) { // if more than 200 dp, it's probably a keyboard...
                    // ... do something here

                } else {
                    String payAmount = payAmountEditText.getText().toString();
                    if (!StringUtil.isEmpty(payAmount)) {
                        sendBalanceRecharge(Double.parseDouble(payAmount));
                    }
                }
            }
        });
        payAmountEditText.addTextChangedListener(textWatcher);
//        payAmountEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);

    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    //初始化控件
    void initWeight() {
        payRadioGroup = (RadioGroup) findViewById(R.id.pay_radiogroup);
        payRadioGroup.setOnCheckedChangeListener(rechPayMethodRadioListener);
        payAmountEditText = (EditText) findViewById(R.id.points_edittext);
        balanceRechargeBtn = (Button) findViewById(R.id.balance_recharge);
        integralCount = (TextView) findViewById(R.id.integral_count);
        alipayBtn = (RadioButton) findViewById(R.id.banlance_alipay);
        unionpayBtn = (RadioButton) findViewById(R.id.balance_unionpay);
    }

    /**
     * 支付方式group
     */
    private RadioGroup.OnCheckedChangeListener rechPayMethodRadioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id = group.getCheckedRadioButtonId();
            String payAmount = payAmountEditText.getText().toString();
            switch (id) {
                case R.id.banlance_alipay:
                    payMethodType = BizConstant.ALIPAY_RECHARGE_METHOD;
                    if (!StringUtils.isEmpty(payAmount)) {
//                        presenter.walletBalanceRecharge(BalanceRechargeActivity.this, Double.valueOf(payAmount), payMethodType);
                    }
                    break;
                case R.id.balance_unionpay:
                    payMethodType = BizConstant.UNIONPAY_RECHARGE_METHOD;
                    if (!StringUtils.isEmpty(payAmount)) {
//                        presenter.walletBalanceRecharges(BalanceRechargeActivity.this, Double.valueOf(payAmount), payMethodType);
                    }
                    break;
            }
        }
    };


    @OnClick({R.id.integtal_pay_relative, R.id.balance_recharge, R.id.recharge_record})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.integtal_pay_relative:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.balance_recharge:
                String payAmount = payAmountEditText.getText().toString();
                int payCheckedId = payRadioGroup.getCheckedRadioButtonId();
                if (checkAmount(payAmount)) {
                    if (!StringUtil.isEmpty(payAmount)) {
                        sendBalanceRecharge(Double.parseDouble(payAmount));
                    } else {
                        ToastUtils.showToast(BalanceRechargeActivity.this, getString(R.string.common_amount_empty));
                        return;
                    }

                    if (payCheckedId == alipayBtn.getId()) {  //支付宝支付
                        presenter.walletBalanceRecharge(BalanceRechargeActivity.this, Double.valueOf(payAmount), payMethodType);

                    } else if (payCheckedId == unionpayBtn.getId()) {   //银行卡支付
                        presenter.walletBalanceRecharges(BalanceRechargeActivity.this, Double.valueOf(payAmount), payMethodType);

                    } else {
                        ToastUtils.showToast(BalanceRechargeActivity.this, getString(R.string.common_pay_method_empty));
                        return;
                    }
                }
                break;
            case R.id.recharge_record:
//                Intent bankIntent = new Intent(BalanceRechargeActivity.this, BankPayActivity.class);
//                bankIntent.putExtra("orderCode", orderId);
                startActivity(new Intent(BalanceRechargeActivity.this, RechargeRecordActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    /**
     * 判断输入金额是否为空
     *
     * @param payAmount
     * @return
     */
    boolean checkAmount(String payAmount) {
        if (!StringUtil.isEmpty(payAmount)) {
            return true;
        } else {
            ToastUtils.showToast(BalanceRechargeActivity.this, getString(R.string.common_amount_empty));
            return false;
        }
    }

    void sendBalanceRecharge(Double amount) {


//        presenter.getPayOrderInfoByOrderId(BalanceRechargeActivity.this, orderId);
    }

    private void toAlipay(String orderStr) {
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
                ToastUtils.showToast(BalanceRechargeActivity.this, "支付成功");
            }

            @Override
            public void failed() {
                ToastUtils.showToast(BalanceRechargeActivity.this, "支付失败");
            }

            @Override
            public void cancel() {
                ToastUtils.showToast(BalanceRechargeActivity.this, "支付取消");
            }
        });
    }


    @Override
    protected BalanceRechargePresenter getPresenter() {
        return new BalanceRechargePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_balabce_recharge;
    }

    /**
     * 支付宝支付
     *
     * @param dataBean
     */
    @Override
    public void walletBalanceRecharge(BalanceRechargeBean dataBean) {
        orderStr = dataBean.data.order_str;
        if (dataBean.status == 1) {
            toAlipay(orderStr);
        } else {
            ToastUtils.showToast(this, dataBean.msg);
        }

    }

    /**
     * 银行卡支付
     *
     * @param amountPointsBean
     */
    @Override
    public void walletBalanceRecharges(AmountPointsBean amountPointsBean) {
        orderId = amountPointsBean.data;
//        ToastUtils.showToast(this,"orderId=="+orderId);
        if (amountPointsBean.status == 1) {
            Intent bankIntent = new Intent(BalanceRechargeActivity.this, BankPayActivity.class);
            bankIntent.putExtra("orderCode", orderId);
            startActivity(bankIntent);
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else {
            ToastUtils.showToast(this, amountPointsBean.msg);
        }
    }

    @Override
    public void showOrderInfo(OrderInfoBean.DataBean paymentData) {
        orderCode = paymentData.oid;
    }

    @Override
    public void IUnitPriceId(AmountPointsBean dataBean) {
        unitPrice = Double.parseDouble(dataBean.data);
    }


    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            String pointsEditT = payAmountEditText.getText().toString();
            if (!StringUtil.isEmpty(pointsEditT)) {
                integralCount.setText(getString(R.string.balance_give_boc) + pointsEditT);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            String pointsEditT = payAmountEditText.getText().toString();

            if (!StringUtil.isEmpty(pointsEditT)) {
                Double parseDouble = Double.parseDouble(pointsEditT) / 100;
//                ToastUtils.showToast(BalanceRechargeActivity.this,"parseDouble="+parseDouble);
                integralCount.setText(getString(R.string.balance_give_boc) + " " + (int) NumberOperateUtil.div(parseDouble, unitPrice, 100) + getString(R.string.coin_bole_coin));
            } else {
                integralCount.setText(getString(R.string.balance_give_boc) + "0" + getString(R.string.coin_bole_coin));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            String pointsEditT = payAmountEditText.getText().toString();
            if (!StringUtil.isEmpty(pointsEditT)) {
                Double parseDouble = Double.parseDouble(pointsEditT) / 100;
                integralCount.setText(getString(R.string.balance_give_boc) + " " + (int) NumberOperateUtil.div(parseDouble, unitPrice, 100) + getString(R.string.coin_bole_coin));
            } else {
                integralCount.setText(getString(R.string.balance_give_boc) + getString(R.string.coin_bole_coin));
            }

        }
    };

    /**
     * 保留两位小数正则
     *
     * @param number
     * @return
     */
    public static boolean isOnlyPointNumber(String number) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
}
