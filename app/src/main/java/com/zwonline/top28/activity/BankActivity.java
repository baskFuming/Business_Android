package com.zwonline.top28.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.BankAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BankBean;
import com.zwonline.top28.presenter.BankPresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.utils.popwindow.UnBindBankPopwindow;
import com.zwonline.top28.view.IBankActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：银行卡
 *
 * @author YSG
 * @date 2017/12/27
 */
public class BankActivity extends BaseActivity<IBankActivity, BankPresenter> implements IBankActivity {
    private BankAdapter adapter;
    private TextView addbank;
    private TextView no;
    private RecyclerView bankrecy;
    private RelativeLayout back;
    private UnBindBankPopwindow mPopwindow;
    private String bankId;
    private List<BankBean.DataBean> bList=null;
    private int bankPosition;
    @Override
    protected void init() {
        bList=new ArrayList<>();
        addbank = (TextView) findViewById(R.id.addbank);
        no = (TextView) findViewById(R.id.no);
        bankrecy = (RecyclerView) findViewById(R.id.bankrecy);
        back = (RelativeLayout) findViewById(R.id.back);
        presenter.mBank(this);
    }

    @Override
    protected BankPresenter getPresenter() {
        return new BankPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bank;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.mBank(this);
    }

    @OnClick({R.id.back, R.id.addbank})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.addbank:
                startActivity(new Intent(BankActivity.this, AddBankActivity.class));
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    @Override
    public void showBank(final List<BankBean.DataBean> bankList) {
//        bList.addAll(bankList);
        bList = bankList;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bankrecy.setLayoutManager(linearLayoutManager);
        adapter = new BankAdapter(bList, this);
        bankrecy.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new BankAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                bankId = bankList.get(position).id;
                bankPosition=position;
                mPopwindow = new UnBindBankPopwindow(BankActivity.this, itemsOnClick, position);
                mPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    @Override
    public void showUnBindBank() {
        bList.remove(bankPosition);
        adapter.notifyDataSetChanged();
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {



        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(BankActivity.this, mPermissionList, 123);
            }
            mPopwindow.setOutsideTouchable(true);
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(BankActivity.this, 1f);
            switch (v.getId()) {
                case R.id.chat:
                    break;
                case R.id.unbind:
                    presenter.munBindBank(BankActivity.this,bankId);
                    showUnBindBank();
                    ToastUtils.showToast(BankActivity.this, getString(R.string.bank_unbind_suc));
                default:
                    break;
            }
        }

    };

}
