package com.zwonline.top28.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.PaymentPresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IPaymentActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;

/**
 * 描述：pos机订单号
 *
 * @author YSG
 * @date 2018/3/13
 */
public class PosOrderCodeActivity extends BaseActivity<IPaymentActivity, PaymentPresenter> implements IPaymentActivity {

    private RelativeLayout posBack;
    private TextView posMoneySumTextView, orderCodeTextView, projectMameTextView;
    private Button orderConfirm;
    private ImageView orderQrCodeImageView;
    private Handler handler;
    private String orderCode, posRecharge, dataStatus, orderId;

    @Override
    protected void init() {
        initView();  //初始化控件
        initData(); //初始化数据
        showQRCode();  //生成二维码
        startPollingOrder();  //轮询订单
    }

    /**
     * 初始化组件
     */
    private void initView() {
        orderQrCodeImageView = (ImageView) findViewById(R.id.order_qr_code);
        posBack = (RelativeLayout) findViewById(R.id.pos_back);
        posMoneySumTextView = (TextView) findViewById(R.id.pos_money_sum);
        orderCodeTextView = (TextView) findViewById(R.id.order_code);
        projectMameTextView = (TextView) findViewById(R.id.project_mame);
        orderConfirm = (Button) findViewById(R.id.order_confirm);
    }

    void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        orderCode = intent.getStringExtra("order_code");
        orderId = intent.getStringExtra("order_id");
        posMoneySumTextView.setText("￥" + intent.getStringExtra("amount") + getString(R.string.amount_unit));
        projectMameTextView.setText(intent.getStringExtra("project_name") + getString(R.string.preoject_gathering));
        posRecharge = intent.getStringExtra("pos_recharge");
        this.orderCodeTextView.setText(orderCode);
    }

    //显示二维码
    private void showQRCode() {
        try {
            /**
             * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
             */
            Bitmap bitmap = EncodingUtils.createQRCode(orderCode, BizConstant.QR_CODE_WIDTH, BizConstant.QR_CODE_HEIGHT,
                    null);
            // 设置图片
            orderQrCodeImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected PaymentPresenter getPresenter() {
        return new PaymentPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_order_code;
    }


    @OnClick({R.id.pos_back, R.id.order_confirm})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.pos_back:
                stopTimer();
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.order_confirm:
                if (!StringUtil.isEmpty(posRecharge) && posRecharge.equals(BizConstant.POS_GATHERING)) {
                    startActivity(new Intent(this, WalletActivity.class));
                    overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                } else if (!StringUtil.isEmpty(posRecharge) && posRecharge.equals(BizConstant.POS_INTEGRAL)) {
                    startActivity(new Intent(this, IntegralActivity.class));
                    overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                }
                break;
        }
    }

    //获取订单的状态
    @Override
    public void showGetOrderPayStatus(AmountPointsBean amountPointsBean) {
        dataStatus = amountPointsBean.data;
    }

    @Override
    public void getOrderInfoByOrderId(PrepayPayBean.DataBean paymentData) {

    }

    @Override
    public void showOrderInfo(OrderInfoBean paymentData) {

    }


    @Override
    public void stopPollingOrder() {
        count = 0;
        stopTimer();
    }

    /**
     * 轮询订单状态
     */
    @Override
    public void pollingOrderPayStatus() {
        if (!StringUtil.isEmpty(dataStatus)) {
            if (count > 3) {
                stopTimer();
                finish();
            }
            if (dataStatus.equals(BizConstant.ORDER_PAY_SUCCESS)) {
                stopTimer();
                finish();
            }
        }
    }


    private void startPollingOrder() {
        if (isStop) {
        } else {
        }

        isStop = !isStop;

        if (!isStop) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    private static String TAG = "TimerDemo";
    private TextView mTextView = null;
    private Button mButton_start = null;
    private Button mButton_pause = null;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private static int count = 0;
    private boolean isPause = false;
    private boolean isStop = true;
    private static int delay = 1000;  //1s
    private static int period = 1000;  //1s

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    sendMessage();

                    do {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);

                    count++;
                }
            };
        }

        if (mTimer != null && mTimerTask != null)
            mTimer.schedule(mTimerTask, delay, period);

    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        count = 0;
    }

    public void sendMessage() {
        presenter.getOrderPayStatus(PosOrderCodeActivity.this, orderId);
    }


}