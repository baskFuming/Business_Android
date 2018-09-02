package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.IntegralRecordBean;
import com.zwonline.top28.bean.MyCurrencyBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.fragment.RecordFragment;
import com.zwonline.top28.presenter.MyCurrencyPresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyCurrencyActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：积分
 * @author YSG
 * @date 2018/3/8
 */
public class IntegralActivity extends BaseActivity<IMyCurrencyActivity, MyCurrencyPresenter> implements IMyCurrencyActivity {

    private RelativeLayout integralBack;
    private ImageView moneyTW;
    private ImageView moneyZh;
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

    @Override
    protected void init() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//状态栏调成白色字体
        initView();
        isLanguage();
        presenter.mMyCurrencys(this);
    }

    /**
     * 判断是大陆版还是台湾版
     */
    public void isLanguage() {
        //获取包名
        String recentTask = LanguageUitils.getLollipopRecentTask(this);
//        ToastUtils.showToast(this,"recentTask="+recentTask);
        if (recentTask.contains(BizConstant.CHANNEL_TW)){
            integralTw.setVisibility(View.VISIBLE);
            integralCh.setVisibility(View.GONE);
        }else {
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
        integralBack = (RelativeLayout) findViewById(R.id.integral_back);
        moneyTW = (ImageView) findViewById(R.id.money_tw);
        moneyZh = (ImageView) findViewById(R.id.money_zh);
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
        recordList = new ArrayList<>();
        IntegralRecordBean integralRecordBean = new IntegralRecordBean();
        recordList.add(new IntegralRecordBean(getString(R.string.all_record), 200));
        recordList.add(new IntegralRecordBean(getString(R.string.gain_integtal), 300));
        recordList.add(new IntegralRecordBean(getString(R.string.deduct_integtal), 400));
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
        integralTvZh.setText(bean.data.point + this.getString(R.string.coin_bole_coin));
        integralTvTw.setText(bean.data.point + this.getString(R.string.coin_bole_coin));
    }

    @Override
    public void showMyCurrencyData(List<MyCurrencyBean.DataBean.ListBean> currencyList) {

    }


    @OnClick({R.id.integral_back, R.id.earn_integral_tw, R.id.convert_tw, R.id.integtal_pay_tw, R.id.integral_tab, R.id.earn_integral_ch, R.id.integtal_pay_ch})
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
                startActivity(new Intent(this, EarnIntegralActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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
                startActivity(new Intent(this, EarnIntegralActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
            case R.id.integtal_pay_ch:
                startActivity(new Intent(IntegralActivity.this, IntegralPayActivity.class));
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }
    //每次进入界面刷新数据
    @Override
    protected void onResume() {
        super.onResume();
        presenter.mMyCurrency(this);
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
