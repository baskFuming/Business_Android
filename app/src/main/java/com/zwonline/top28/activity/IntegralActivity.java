package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BusinessCoinBean;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.bean.IntegralRecordBean;
import com.zwonline.top28.bean.MyCurrencyBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.fragment.RecordFragment;
import com.zwonline.top28.presenter.MyCurrencyPresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyCurrencyActivity;
import com.zwonline.top28.web.BaseWebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：积分
 *
 * @author YSG
 * @date 2018/3/8
 * type=1算力2商机币
 */
public class IntegralActivity extends BaseActivity<IMyCurrencyActivity, MyCurrencyPresenter> implements IMyCurrencyActivity {

    private RelativeLayout integralBack;
    private ImageView moneyTW;
    private TextView moneyZh;
    private TextView integralTvTw;
    private TextView integralTvZh;
    private TextView earnIntegralCh;
    private TextView integtalPayCh;
    private RelativeLayout integralCh;
    private TextView earnIntegralTw;
    private TextView convertTw;
    private TextView integtalPayTw;
    private RelativeLayout integralTw;
    private TabLayout integralTab;
    private ViewPager integralView;
    private List<IntegralRecordBean> recordList = null;
    private TextView earnState;
    private String type;
    private TextView title;
    private TextView freeze;
    private LinearLayout businessCoinLinear;
    private String buyHashrateUrl = Api.baseUrl() + "/Members/boc_list_pay.html";
    private String buyGoldenUrl = Api.baseUrl() + "/Integral/exchangeMoney.html";
    private String yangfenConvert = Api.baseUrl() + "/Integral/exchange.html?version=";//兑换鞅分
    private TextView buyGolden;
    private TextView buyHashrate;

    @Override
    protected void init() {
        type = getIntent().getStringExtra("type");
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//状态栏调成白色字体
        initView();
        isLanguage();
        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
            presenter.mMyCurrencys(this);//算力
        } else {
            presenter.BalanceLog(this, "", 0);//商机币
        }
    }

    /**
     * 判断是大陆版还是台湾版
     */
    public void isLanguage() {
        //获取包名
        String recentTask = LanguageUitils.getLollipopRecentTask(this);
//        ToastUtils.showToast(this,"recentTask="+recentTask);
        if (recentTask.contains(BizConstant.CHANNEL_TW)) {
            integralTw.setVisibility(View.VISIBLE);
            integralCh.setVisibility(View.GONE);
        } else {
            integralTw.setVisibility(View.GONE);
            integralCh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected MyCurrencyPresenter getPresenter() {
        return new MyCurrencyPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_integral;
    }

    //初始化
    private void initView() {
        title = (TextView) findViewById(R.id.title);
        integralBack = (RelativeLayout) findViewById(R.id.integral_back);
        moneyTW = (ImageView) findViewById(R.id.money_tw);
        moneyZh = (TextView) findViewById(R.id.money_zh);
        earnState = (TextView) findViewById(R.id.earn_state);//赚取算力说明
        integralTvTw = (TextView) findViewById(R.id.integral_tv_tw);
        integralTvZh = (TextView) findViewById(R.id.integral_tv_zh);
        earnIntegralCh = (TextView) findViewById(R.id.earn_integral_ch);
        integtalPayCh = (TextView) findViewById(R.id.integtal_pay_ch);
        integralCh = (RelativeLayout) findViewById(R.id.integral_ch);
        earnIntegralTw = (TextView) findViewById(R.id.earn_integral_tw);
        convertTw = (TextView) findViewById(R.id.convert_tw);
        integtalPayTw = (TextView) findViewById(R.id.integtal_pay_tw);
        integralTw = (RelativeLayout) findViewById(R.id.integral_tw);
        integralTab = (TabLayout) findViewById(R.id.integral_tab);
        integralView = (ViewPager) findViewById(R.id.integral_view);
        integralView = (ViewPager) findViewById(R.id.integral_view);
        buyHashrate = (TextView) findViewById(R.id.buy_hashrate);
        buyGolden = (TextView) findViewById(R.id.buy_golden);
        //冻结商机币
        freeze = (TextView) findViewById(R.id.freeze);
        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
            title.setText(getString(R.string.coin_bole_coin_record));
            moneyZh.setText(getString(R.string.my_coin_bole_coin));
            earnState.setText(getString(R.string.earn_integral_explain));
            freeze.setVisibility(View.GONE);
//            businessCoinLinear.setVisibility(View.GONE);
            buyGolden.setVisibility(View.GONE);
            buyHashrate.setVisibility(View.VISIBLE);
            buyHashrate.setText("购买算力");
            integralCh.setBackgroundResource(R.mipmap.suanli_bg);
        } else {
            title.setText(getString(R.string.opportunities_currency));
            moneyZh.setText(getString(R.string.my_opportunities_currency));
            earnState.setText(getString(R.string.buy_opportunities_currency));
            integralCh.setBackgroundResource(R.mipmap.reward_bunner);
            freeze.setVisibility(View.VISIBLE);
//            businessCoinLinear.setVisibility(View.VISIBLE);
            buyGolden.setVisibility(View.VISIBLE);
            buyHashrate.setVisibility(View.VISIBLE);
            buyHashrate.setText("兑换鞅分");
        }
        recordList = new ArrayList<>();
        IntegralRecordBean integralRecordBean = new IntegralRecordBean();
        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
            recordList.add(new IntegralRecordBean(getString(R.string.all_record), 200));
            recordList.add(new IntegralRecordBean(getString(R.string.gain_integtal), 300));
            recordList.add(new IntegralRecordBean(getString(R.string.deduct_integtal), 400));
        } else {
            recordList.add(new IntegralRecordBean(getString(R.string.all_record), 500));
            recordList.add(new IntegralRecordBean(getString(R.string.get_opportunities_currency), 600));
            recordList.add(new IntegralRecordBean(getString(R.string.deduct_opportunities_currency), 700));
        }

        for (int i = 0; i < recordList.size(); i++) {
            integralTab.newTab().setText(recordList.get(i).record_name);
        }
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), recordList);
        integralView.setAdapter(myFragmentAdapter);
        integralTab.setupWithViewPager(integralView);
        integralTab.setTabsFromPagerAdapter(myFragmentAdapter);
    }

    @Override
    public void showMyCurrency(MyCurrencyBean bean) {
        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
            integralTvZh.setText(bean.data.point + "");
        }

//        integralTvTw.setText(bean.data.point + this.getString(R.string.coin_bole_coin));
    }

    @Override
    public void showMyCurrencyData(List<MyCurrencyBean.DataBean.ListBean> currencyList) {

    }

    @Override
    public void showBalanceLog(BusinessCoinBean integralBean) {
        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.RECOMMEND)) {
            if (StringUtil.isNotEmpty(integralBean.data.balance)) {
                integralTvZh.setText(integralBean.data.balance + "");
            } else {
                integralTvZh.setText(0);
            }
            if (StringUtil.isNotEmpty(integralBean.data.freeze_amount))
                freeze.setText(getString(R.string.freeze_opportunities_currency) + ":" + integralBean.data.freeze_amount);
        }

    }


    @OnClick({R.id.integral_back, R.id.earn_integral_tw, R.id.convert_tw, R.id.integtal_pay_tw, R.id.integral_tab, R.id.earn_integral_ch, R.id.integtal_pay_ch, R.id.earn_state, R.id.buy_hashrate, R.id.buy_golden})
    public void onViewClicked(View v) {
        if (AntiShake.check(v.getId())) {    //判断是否多次点击
            return;
        }
        switch (v.getId()) {
            case R.id.integral_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.earn_integral_tw:
                Intent intent = new Intent(this, RecommendUserActivity.class);
                intent.putExtra("jumPath", BizConstant.EARNINTEGRAL);
                startActivity(intent);
                break;
            case R.id.convert_tw:
                startActivity(new Intent(IntegralActivity.this, ConvertBOCActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.integtal_pay_tw:
                startActivity(new Intent(IntegralActivity.this, IntegralPayActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.integral_tab:
                break;
            case R.id.earn_integral_ch:
                Intent intent1 = new Intent(this, RecommendUserActivity.class);
                intent1.putExtra("jumPath", BizConstant.EARNINTEGRAL);
                startActivity(intent1);
                break;
            case R.id.integtal_pay_ch:
                startActivity(new Intent(IntegralActivity.this, IntegralPayActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.earn_state:
                //判断是算力还是商机币
                if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
                    //算力跳转赚取算力说明
                    Intent earnIntent = new Intent(this, BaseWebViewActivity.class);
                    earnIntent.putExtra("weburl", BizConstant.EARNINTEGRAL);
                    startActivity(earnIntent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    //商机币充值
                    Intent rewardIntent = new Intent(this, IntegralPayActivity.class);
                    startActivity(rewardIntent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
                break;
            case R.id.buy_hashrate:
                Intent hashrateIntent = new Intent(IntegralActivity.this, BaseWebViewActivity.class);
                if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
                    hashrateIntent.putExtra("weburl", buyHashrateUrl);
                    hashrateIntent.putExtra("titleBarColor","#5023DC");
                } else {
                    hashrateIntent.putExtra("titleBarColor","#5023DC");
                    hashrateIntent.putExtra("weburl", yangfenConvert + LanguageUitils.getVerName(IntegralActivity.this));
                }

                startActivity(hashrateIntent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.buy_golden:
                Intent goldenIntent = new Intent(IntegralActivity.this, BaseWebViewActivity.class);
                goldenIntent.putExtra("weburl", buyGoldenUrl);
                startActivity(goldenIntent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            default:
                break;
        }
    }

    //每次进入界面刷新数据
    @Override
    protected void onResume() {
        super.onResume();
        if (StringUtil.isNotEmpty(type) && type.equals(BizConstant.IS_SUC)) {
            presenter.mMyCurrencys(this);//算力
        } else {
            presenter.BalanceLog(this, "", 0);//商机币
        }
    }

    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<IntegralRecordBean> hlist;

        public MyFragmentAdapter(FragmentManager fm, List<IntegralRecordBean> list) {
            super(fm);
            this.hlist = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return hlist.get(position).record_name;
        }

        @Override
        public Fragment getItem(int position) {
            return RecordFragment.getInstance(hlist.get(position));
        }

        @Override
        public int getCount() {
            return hlist.size();
        }

    }

}
