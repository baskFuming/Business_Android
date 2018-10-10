package com.zwonline.top28.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.xys.libzxing.zxing.bean.ZxingConfig;
import com.xys.libzxing.zxing.common.Constant;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.PayMentAdapter;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PaymentBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.WallletPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IPayMentView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述：钱包
 *
 * @author YSG
 * @date 2017/12/21
 */
public class WalletActivity extends BaseActivity<IPayMentView, WallletPresenter> implements IPayMentView {


    private XRecyclerView walletrecy;
    private PayMentAdapter adapter;
    private String balanceNumber;
    private int page = 1;
    private LinearLayoutManager linearLayoutManager;
    private List<PaymentBean.DataBean> wlist;
    private RelativeLayout back;
    private TextView bill;
    private TextView richScan;
    private TextView gathering;
    private TextView balance;
    private TextView balanceNum;
    private TextView bank;
    private TextView no;
    private WebView zxingWeb;
    private String isEnter;
    private int refreshTime = 0;
    private int times = 0;
    private int status;
    private String backMy;
    private int REQUEST_CODE_SCAN = 111;
    private String mPermissions[] = {Manifest.permission.CAMERA};
    private static final int Permissions_CAMERA_KEY = 2;
    @Override
    protected void init() {
        Intent intent = getIntent();
        backMy = intent.getStringExtra("back");
        initData();//查找控件
        isEnter = (String) SharedPreferencesUtils.getUtil().getKey(this,
                "is_enterprise", "");
        presenter.mBalance(this);
        presenter.mPayMent(this, String.valueOf(page));
        walletrecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        walletrecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        walletrecy.setArrowImageView(R.drawable.iconfont_downgrey);
        walletrecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        walletrecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        walletrecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
//        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(this);
//        scrollLinearLayoutManager.setScrollEnabled(true);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        walletrecy.setLayoutManager(linearLayoutManager);
        wlist = new ArrayList<>();
        adapter = new PayMentAdapter(wlist, this);
        walletrecy.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //个人 企业区分
        if (!StringUtil.isEmpty(isEnter) && BizConstant.IS_SUC.equals(isEnter)) {
            gathering.setVisibility(View.VISIBLE);
        } else {
            gathering.setVisibility(View.GONE);
        }

    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        bill = (TextView) findViewById(R.id.bill);
        richScan = (TextView) findViewById(R.id.richScan);
        gathering = (TextView) findViewById(R.id.gathering);
        balance = (TextView) findViewById(R.id.balance);
        balanceNum = (TextView) findViewById(R.id.balance_num);
        bank = (TextView) findViewById(R.id.bank);
        no = (TextView) findViewById(R.id.no);
        walletrecy = (XRecyclerView) findViewById(R.id.walletrecy);
        zxingWeb = (WebView) findViewById(R.id.zxing_web);

    }

    @Override
    protected WallletPresenter getPresenter() {
        return new WallletPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wallet;
    }

    //我的余额
    @Override
    public void showBalance(BalanceBean balanceBean) {
        balanceNumber = balanceBean.data;
        balanceNum.setText(balanceBean.data);
    }

    //判断是否有收付款记录
    @Override
    public void showPayMent(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            walletrecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            walletrecy.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOnErro() {
        if (this == null) {
            return;
        }
        if (page == 1) {
            wlist.clear();
            adapter.notifyDataSetChanged();
        } else {
//            ToastUtils.showToast(this, getString(R.string.data_no_more));
//            ToastUtils.showToast(this, "");
        }
    }

    //收付款记录
    @Override
    public void showPayMentData(final List<PaymentBean.DataBean> payList) {
        if (page == 1) {
            wlist.clear();
        }
        wlist.addAll(payList);
        adapter.notifyDataSetChanged();
//        adapter.setOnClickItemListener(new PayMentAdapter.OnClickItemListener() {
//            @Override
//            public void setOnItemClick(int position) {
//                Intent intent=new Intent(WalletActivity.this,PaymentDetailsActivity.class);
//                intent.putExtra("order_id",payList.get(position).id);
//                startActivity(intent);
//                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//            }
//        });；
        walletrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mPayMent(WalletActivity.this, String.valueOf(page));
                        if (walletrecy != null)
                            walletrecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mPayMent(WalletActivity.this, String.valueOf(page));
                        if (walletrecy != null) {
                            walletrecy.loadMoreComplete();
                        } else {
                            walletrecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
                        }
                    }
                }, 1000);
                times++;
            }
        });


    }

    @OnClick({R.id.bill, R.id.richScan, R.id.gathering, R.id.balance, R.id.balance_num, R.id.bank, R.id.back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.bill://账单
                startActivity(new Intent(WalletActivity.this, MyBillActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.richScan://扫一扫
                if (Build.VERSION.SDK_INT >= 23) {
                    setPermissions(Permissions_CAMERA_KEY);
                } else {
                    saoData();
                }

                break;
            case R.id.gathering://收款
//                startActivity(new Intent(WalletActivity.this, GatheringActivity.class));

//               Intent payIntent =  new Intent(WalletActivity.this, MyProjectActivity.class);
////               payIntent.putExtra("orderinfo_id", 12);
//                startActivity(payIntent);
                //个人 企业区分
                if (!StringUtil.isEmpty(isEnter) && BizConstant.IS_SUC.equals(isEnter)) {
                    Intent enterIntent = new Intent(WalletActivity.this, MyProjectActivity.class);
                    enterIntent.putExtra("clickSource", BizConstant.CLICK_PAY);
                    startActivity(enterIntent);
                } else {
//                    gathering.setVisibility(View.GONE);
                }
//                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.balance://余额
                Intent balance_intent = new Intent(WalletActivity.this, BalanceActivity.class);
                balance_intent.putExtra("balanceNum", balanceNumber);
                startActivity(balance_intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.balance_num:
                break;
            case R.id.bank://银行卡
                startActivity(new Intent(WalletActivity.this, BankActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.back:
                if (StringUtil.isNotEmpty(backMy) && back.equals(BizConstant.ALREADY_FAVORITE)) {
                    finish();
                } else {
                    Intent backIntent = new Intent(WalletActivity.this, MainActivity.class);
                    backIntent.putExtra("loginType", BizConstant.MYLOGIN);
                    startActivity(backIntent);
                    finish();
                    overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                }
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }


    //每次进入界面刷新数据
    @Override
    protected void onResume() {

        super.onResume();
        presenter.mBalances(this);
        presenter.mPayMent(this, String.valueOf(page));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra(Constant.CODED_CONTENT);
//            System.out.print("result==" + result);
//            Toast.makeText(WalletActivity.this, result + "", Toast.LENGTH_SHORT).show();
            String id = result.substring(BizConstant.ORDERINFO.length(), result.length());
            getOrderInfo(id);
        }
    }

    public void getOrderInfo(final String orderId) {
        try {
            SharedPreferencesUtils sp = SharedPreferencesUtils.getUtil();
            sp = SharedPreferencesUtils.getUtil();
            String token = (String) sp.getKey(WalletActivity.this, "dialog", "");
            long timestamp = new Date().getTime() / 1000;//时间戳
            Map<String, String> map = new HashMap<>();
            map.put("order_id", orderId);
            map.put("token", token);
            map.put("timestamp", String.valueOf(timestamp));
            String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
            Flowable<OrderInfoBean> flowable = ApiRetrofit.getInstance()
                    .getClientApi(PayService.class, Api.url)
                    .iGetOrderInfo(String.valueOf(timestamp), token, orderId, sign);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<OrderInfoBean>(WalletActivity.this) {
                        private String msg;

                        @Override
                        protected void onBaseNext(OrderInfoBean data) {
                            status = data.status;
                            msg = data.msg;
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return true;
                        }

                        @Override
                        protected void onBaseComplete() {
                            if (!StringUtil.isEmpty(String.valueOf(status)) && status == 1) {
                                Intent intent = new Intent(WalletActivity.this, CustomContractActivity.class);
                                intent.putExtra("orderinfo_id", orderId);
                                intent.putExtra("cid", BizConstant.SIGN_CONTRACT);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                            } else {
                                ToastUtils.showToast(WalletActivity.this, msg);
                            }
                        }

                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setPermissions(int mPermissions_KEY) {
        /*
        要添加List原因是想判断数组里如果有个别已经授权的权限，就不需要再添加到List中。添加到List中的权限后续将转成数组去申请权限
         */
        List<String> permissionsList = new ArrayList<>();
        //判断系统版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < mPermissions.length; i++) {
                //判断一个权限是否已经允许授权，如果没有授权就会将单个未授权的权限添加到List里面
                if (ContextCompat.checkSelfPermission(this, mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionsList.add(mPermissions[i]);
                }
            }
            //判断List不是空的，如果有内容就运行获取权限
            if (!permissionsList.isEmpty()) {
                String[] permissions = permissionsList.toArray(new String[permissionsList.size()]);
                for (int j = 0; j < permissions.length; j++) {
                }
                //执行授权的代码。此处执行后会弹窗授权
                ActivityCompat.requestPermissions(this, permissions, mPermissions_KEY);
            } else { //如果是空的说明全部权限都已经授权了，就不授权了,直接执行进入相机或者图库
                saoData();
            }
        } else {
            saoData();
        }
    }
    public void saoData(){
        Intent saoIntent = new Intent(this, CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.white);//设置扫描框四个角的颜色 默认为淡蓝色
        config.setFrameLineColor(R.color.transparent);//设置扫描框边框颜色 默认无色
        config.setFullScreenScan(true);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        saoIntent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(saoIntent, REQUEST_CODE_SCAN);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permissions_CAMERA_KEY) {
            if (grantResults.length > 0) { //安全写法，如果小于0，肯定会出错了
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        saoData();
                    } else {
                        saoData();
                    }
                }
            }
        }

    }
}
