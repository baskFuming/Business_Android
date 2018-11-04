package com.zwonline.top28.nim.yangfen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.DemoCache;
import com.zwonline.top28.presenter.SendYFPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.NumberOperateUtil;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.view.ISendYFActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发送鞅分界面
 */
public class SendYangFenActivity extends BaseActivity<ISendYFActivity, SendYFPresenter> implements ISendYFActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView yangfenNum;
    private TextView geTv;
    private EditText pointsEdittext;
    private TextView groupCount;
    private TextView yfAll;
    private TextView yangfenTv;
    private ImageView pin;
    private EditText yangfenCount;
    private TextView ordinaryPin;
    private EditText redContent;
    private TextView showYfNum;
    private Button sendYF;
    private boolean randomFlag = true;
    private TextView pinTv;
    private static NimUserInfo selfInfo;
    private long group_num;
    private SharedPreferencesUtils sp;
    private String token;
    private RelativeLayout red_num;
    private LinearLayout pin_linear;
    private String packageType;//红包类型
    private TextView pointType;

    @Override
    protected void init() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.reded), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(getApplicationContext(), "dialog", "");
        group_num = Long.parseLong(getIntent().getStringExtra("group_num"));
        packageType = getIntent().getStringExtra("package_type");
        initView();
        yangfenCount.addTextChangedListener(textWatcher);
        pointsEdittext.addTextChangedListener(textWatcher);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(observer, true);
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver, true);
    }

    @Override
    protected SendYFPresenter getPresenter() {
        return new SendYFPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_send_yang_fen;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        yangfenNum = (TextView) findViewById(R.id.yangfen_num);
        geTv = (TextView) findViewById(R.id.ge_tv);
        pointsEdittext = (EditText) findViewById(R.id.points_edittext);
        groupCount = (TextView) findViewById(R.id.group_count);
        //String.format("共%d人", team.getMemberCount())
        groupCount.setText(String.format("本群共%d人", group_num));
        pinTv = (TextView) findViewById(R.id.pin_tv);
        yfAll = (TextView) findViewById(R.id.yf_all);
        yangfenTv = (TextView) findViewById(R.id.yangfen_tv);
        pin = (ImageView) findViewById(R.id.pin);
        yangfenCount = (EditText) findViewById(R.id.yangfen_count);
        ordinaryPin = (TextView) findViewById(R.id.ordinary_pin);
        redContent = (EditText) findViewById(R.id.red_content);
        showYfNum = (TextView) findViewById(R.id.show_yf_num);
        sendYF = (Button) findViewById(R.id.send_yf);
        red_num = (RelativeLayout) findViewById(R.id.red_num);
        pin_linear = (LinearLayout) findViewById(R.id.pin_linear);
        pointType = (TextView) findViewById(R.id.point_type);
        //判断是鞅分红包还是商机币红包
        if (StringUtil.isNotEmpty(packageType) && packageType.equals(BizConstant.IS_SUC)) {
            //商机币红包
            title.setText("商机币红包");
            yfAll.setText("总商机币");
            yangfenCount.setHint("商机币数量");
            yangfenTv.setText("币");
            pointType.setText("商机币:");
        } else if (StringUtil.isNotEmpty(packageType) && packageType.equals(BizConstant.RECOMMEND)) {
            //鞅分红包
            title.setText("鞅分红包");
            yfAll.setText("总鞅分：");
            yangfenCount.setHint("鞅分数量");
            yangfenTv.setText("分");
            pointType.setText("鞅分:");
        }
        //判断是单人还是群组
        if (StringUtil.isNotEmpty(String.valueOf(group_num)) && group_num == 1) {

            red_num.setVisibility(View.GONE);
            yfAll.setText("单个红包");
            pin.setVisibility(View.GONE);
            pin_linear.setVisibility(View.GONE);
            groupCount.setVisibility(View.GONE);
        } else {
            red_num.setVisibility(View.VISIBLE);
            pin_linear.setVisibility(View.VISIBLE);
            groupCount.setVisibility(View.VISIBLE);
            pin.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.back, R.id.ordinary_pin, R.id.send_yf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.ordinary_pin:
                randomFlag = !randomFlag;
                if (randomFlag) {
                    pinTv.setText("当前为拼手气红包 ");
                    ordinaryPin.setText("改为普通红包");
                    if (StringUtil.isNotEmpty(packageType) && packageType.equals(BizConstant.IS_SUC)) {
                        //商机币红包
                        yfAll.setText("总商机币");
                    } else if (StringUtil.isNotEmpty(packageType) && packageType.equals(BizConstant.RECOMMEND)) {
                        //鞅分红包
                        yfAll.setText("总鞅分");
                    }
                    pin.setVisibility(View.VISIBLE);
                    yangfenCount.setText("");
                    pointsEdittext.setText("");
                    showYfNum.setText("0");
                } else {
                    pinTv.setText("当前为普通红包 ");
                    ordinaryPin.setText("改为拼手气红包");
                    yfAll.setText("单个红包");
                    pin.setVisibility(View.GONE);
                    yangfenCount.setText("");
                    pointsEdittext.setText("");
                    showYfNum.setText("0");
                }
                break;
            case R.id.send_yf:
                String hongBaoCount = pointsEdittext.getText().toString().trim();
                String yangFenNum = yangfenCount.getText().toString().trim();
                String content = redContent.getText().toString().trim();
                if (StringUtil.isNotEmpty(hongBaoCount)) {
                    if (StringUtil.isNotEmpty(yangFenNum)) {
                        if (randomFlag) {
                            //随机红包
                            presenter.mSendYFs(SendYangFenActivity.this, content, yangFenNum, hongBaoCount, BizConstant.PAGE);
                        } else {
                            //普通红包
                            presenter.mSendYFs(SendYangFenActivity.this, content, hongBaoCount, showYfNum.getText().toString(), Integer.parseInt(BizConstant.IS_FAIL));
//                            ToastUtil.showToast(getApplicationContext(),showYfNum.getText().toString());
                        }
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "数量不能为空");
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), "红包个数不能为空");
                }
                break;
        }
    }

    /**
     * 发送红包数据展示
     *
     * @param sendYFBean
     */
    @Override
    public void showYfdata(SendYFBean sendYFBean) {
        if (sendYFBean.status == 1) {
            Intent intent = new Intent();
            intent.putExtra("hongbao_id", sendYFBean.data.hongbao_id + "");
            intent.putExtra("content", sendYFBean.data.postscript);
            intent.putExtra("redType", sendYFBean.data.random_flag + "");
            intent.putExtra("redType", sendYFBean.data.random_flag + "");
            intent.putExtra("user_id", sendYFBean.data.user_id + "");
            intent.putExtra("user_token", token);
            setResult(Activity.RESULT_OK, intent);//返回页面1
            finish();
//            ToastUtil.showToast(getApplicationContext(), "hongbao_id"+sendYFBean.data.hongbao_id);
        } else {
            ToastUtil.showToast(getApplicationContext(), sendYFBean.msg);
        }
    }

    @Override
    public void showSnatchYangFe(List<HongBaoLogBean.DataBean> hongBaoLogBeanList) {

    }

    @Override
    public void showHongBaoLeftCount(HongBaoLeftCountBean hongBaoLeftCountBean) {

    }

    @Override
    public void showYFRecord(List<YfRecordBean.DataBean.ListBean> yfRecordBean, String ReceiveHong) {

    }


    private static Observer<StatusCode> observer = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            if (statusCode == StatusCode.LOGINED) {
            }
        }
    };

    private static Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> nimUserInfo) {
            for (NimUserInfo userInfo : nimUserInfo) {
                if (userInfo.getAccount().equals(DemoCache.getAccount())) {
                    // 更新 jrmf 用户昵称、头像信息
                    selfInfo = userInfo;
                    return;
                }
            }
        }
    };


    /**
     * 监听输入框的输入状态
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            String yangfenCounts = yangfenCount.getText().toString().trim();
            String pointsEdittexts = pointsEdittext.getText().toString().trim();
            if (StringUtil.isNotEmpty(yangfenCounts)) {
                if (randomFlag) {
                    //随机红包
                    showYfNum.setText(yangfenCount.getText().toString().toString());
                } else {
                    if (StringUtil.isNotEmpty(pointsEdittexts)) {
                        Double parseDouble = Double.parseDouble(yangfenCounts);
                        Double parseDoubles = Double.parseDouble(pointsEdittexts);
                        Double cp = NumberOperateUtil.mul(parseDouble, parseDoubles);
                        //普通红包
                        showYfNum.setText(cp + "");
                    } else {
                        showYfNum.setText("0");
                    }
                }
            } else {
                showYfNum.setText("0");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String yangfenCounts = yangfenCount.getText().toString().trim();
            String pointsEdittexts = pointsEdittext.getText().toString().trim();
            if (StringUtil.isNotEmpty(yangfenCounts)) {
                if (randomFlag) {
                    //随机红包
                    showYfNum.setText(yangfenCount.getText().toString().toString());
                } else {
                    if (StringUtil.isNotEmpty(pointsEdittexts)) {
                        Double parseDouble = Double.parseDouble(yangfenCounts);
                        Double parseDoubles = Double.parseDouble(pointsEdittexts);
                        Double cp = NumberOperateUtil.mul(parseDouble, parseDoubles);
                        //普通红包
                        showYfNum.setText(cp + "");
                    } else {
                        showYfNum.setText("0");
                    }
                }
            } else {
                showYfNum.setText("0");
            }
//            if (randomFlag) {
//                //随机红包
//                showYfNum.setText(yangfenCount.getText().toString().toString());
//            } else {
//                if (StringUtil.isNotEmpty(yangfenCounts)){
//                    if (StringUtil.isNotEmpty(pointsEdittexts)){
//                        Double parseDouble = Double.parseDouble(yangfenCounts);
//                        Double parseDoubles = Double.parseDouble(pointsEdittexts);
//                        Double cp = NumberOperateUtil.mul(parseDouble, parseDoubles);
//                        //普通红包
//                        showYfNum.setText(cp + "");
//                    }else {
//                        showYfNum.setText(0);
//                    }
//                }else {
//                    showYfNum.setText(0);
//                }
//
//            }
        }

    };


    private TextWatcher textWatchers = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            String yangfenCounts = yangfenCount.getText().toString().trim();
            String pointsEdittexts = pointsEdittext.getText().toString().trim();

            if (randomFlag) {
                //随机红包
//                showYfNum.setText(yangfenCount.getText().toString().toString());
            } else {
                if (StringUtil.isNotEmpty(yangfenCounts)) {
                    if (StringUtil.isNotEmpty(pointsEdittexts)) {
                        Double parseDouble = Double.parseDouble(yangfenCounts);
                        Double parseDoubles = Double.parseDouble(pointsEdittexts);
                        Double cp = NumberOperateUtil.mul(parseDouble, parseDoubles);
                        //普通红包
                        showYfNum.setText(cp + "");
                    } else {
                        showYfNum.setText(0);
                    }
                } else {
                    showYfNum.setText(0);
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            String yangfenCounts = yangfenCount.getText().toString().trim();
            String pointsEdittexts = pointsEdittext.getText().toString().trim();

            if (randomFlag) {
                //随机红包
//                showYfNum.setText(yangfenCount.getText().toString().toString());
            } else {
                if (StringUtil.isNotEmpty(yangfenCounts)) {
                    if (StringUtil.isNotEmpty(pointsEdittexts)) {
                        Double parseDouble = Double.parseDouble(yangfenCounts);
                        Double parseDoubles = Double.parseDouble(pointsEdittexts);
                        Double cp = NumberOperateUtil.mul(parseDouble, parseDoubles);
                        //普通红包
                        showYfNum.setText(cp + "");
                    } else {
                        showYfNum.setText(0);
                    }
                } else {
                    showYfNum.setText(0);
                }

            }
        }

    };
}
