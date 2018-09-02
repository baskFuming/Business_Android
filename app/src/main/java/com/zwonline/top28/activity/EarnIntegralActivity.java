package com.zwonline.top28.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.EarnIntegralAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.EarnIntegralBean;
import com.zwonline.top28.presenter.EarnIntegralPresenter;
import com.zwonline.top28.utils.ItemDecoration;
import com.zwonline.top28.view.IEarnIntegralActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：赚取积分
 * @author YSG
 * @date 2018/3/12
 */
public class EarnIntegralActivity extends BaseActivity<IEarnIntegralActivity, EarnIntegralPresenter> implements IEarnIntegralActivity {


    private RelativeLayout earnBack;
    private RecyclerView earnRecy;
    private EarnIntegralAdapter earnIntegralAdapter;

    @Override
    protected void init() {
        earnBack = (RelativeLayout) findViewById(R.id.earn_back);
        earnRecy = (RecyclerView) findViewById(R.id.earn_recy);
        presenter.showEarnItegral(this);
    }

    @Override
    protected EarnIntegralPresenter getPresenter() {
        return new EarnIntegralPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_earn_integral;
    }

    //展示数据
    @Override
    public void showEarnIngral(List<EarnIntegralBean.DataBean> earnList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        earnRecy.setLayoutManager(linearLayoutManager);
        earnIntegralAdapter = new EarnIntegralAdapter(earnList, this);
        earnRecy.setAdapter(earnIntegralAdapter);
        earnRecy.addItemDecoration(new ItemDecoration(this));
        earnIntegralAdapter.notifyDataSetChanged();
    }


    //点击事件
    @OnClick(R.id.earn_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
