package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.xys.libzxing.zxing.activity.CaptureActivity;
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
                startActivityForResult(new Intent(WalletActivity.this, CaptureActivity.class), 0);
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
            String result = data.getExtras().getString("result");
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

}
