package com.zwonline.top28.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.utils.click.AntiShake;

/**
 * 描述：兑换BOC
 * @author YSG
 * @date 2018/3/9
 */
public class ConvertBOCActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout convertBack;
    private TextView allConvert;
    private TextView convertible;
    private TextView integralBill;
    private EditText iphoneNum;
    private EditText verificationCode;
    private TextView walletAddress;
    private Button affirmConvert;

    @Override
    protected void init() {
        initView();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_convert_boc;
    }

    private void initView() {
        convertBack = (RelativeLayout) findViewById(R.id.convert_back);
        allConvert = (TextView) findViewById(R.id.all_convert);
        convertible = (TextView) findViewById(R.id.convertible);
        integralBill = (TextView) findViewById(R.id.integral_bill);
        iphoneNum = (EditText) findViewById(R.id.iphone_num);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        walletAddress = (TextView) findViewById(R.id.wallet_address);
        affirmConvert = (Button) findViewById(R.id.affirm_convert);

        affirmConvert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.convert_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.affirm_convert:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;

        }
    }

    private void submit() {
        // validate
        String num = iphoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            Toast.makeText(this, "num不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = verificationCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "code不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
