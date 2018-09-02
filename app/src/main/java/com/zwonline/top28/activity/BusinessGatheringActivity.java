package com.zwonline.top28.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.QrCodeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.QrCodePresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IQrCode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：企业收款  生成二维码
 *
 * @author YSG
 * @date 2018/1/22
 */
public class BusinessGatheringActivity extends BaseActivity<IQrCode, QrCodePresenter> implements IQrCode {


    TextView busGatheringMoney;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.qrcode)
    ImageView qrcodeImage;
    @BindView(R.id.business_gathering_money)
    TextView businessGatheringMoney;
    @BindView(R.id.business_liner)
    LinearLayout businessLiner;
    @BindView(R.id.share)
    Button share;


    @Override
    protected void init() {
        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
        String contractId = intent.getStringExtra("contractId");
        Double amount = intent.getDoubleExtra("amount", 0);
        presenter.mQrCode(this, amount, projectId, contractId);
        busGatheringMoney = (TextView) findViewById(R.id.business_gathering_money);
        busGatheringMoney.setText("￥" + String.valueOf(amount));
    }

    @Override
    protected QrCodePresenter getPresenter() {
        return new QrCodePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_business_gathering;
    }

    @Override
    public void showQrCode(QrCodeBean qrCodeBean) {
        String qrcode = qrCodeBean.data.url;
        if (StringUtil.isEmpty(qrcode)) {
            Toast.makeText(BusinessGatheringActivity.this, R.string.data_not_empty, Toast.LENGTH_SHORT).show();
        } else {
            // 位图
            try {
                /**
                 * 参数：1.文本 2 3.二维码的宽高 4.二维码中间的那个logo
                 */
                Bitmap bitmap = EncodingUtils.createQRCode(qrcode, BizConstant.QR_CODE_WIDTH, BizConstant.QR_CODE_HEIGHT,
                        null);
                // 设置图片
                qrcodeImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.back, R.id.share})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.share:
                Intent intent = new Intent(BusinessGatheringActivity.this, WalletActivity.class);
                intent.putExtra("back", BizConstant.ALIPAY_METHOD);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }
}
