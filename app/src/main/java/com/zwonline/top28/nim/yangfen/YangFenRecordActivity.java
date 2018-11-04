package com.zwonline.top28.nim.yangfen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.HongBaoRecordAdpter;
import com.zwonline.top28.adapter.YFHBRecordAdpter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.SendYFPresenter;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ISendYFActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 红包记录
 * 1是商机币红包2鞅分红包
 */
public class YangFenRecordActivity extends BaseActivity<ISendYFActivity, SendYFPresenter> implements ISendYFActivity {

    private ListView redRecordList;
    private RelativeLayout back;
    private List<YfRecordBean.DataBean.ListBean> list;
    private YFHBRecordAdpter yfhbRecordAdpter;
    private View headerView;
    private String hongbaoId;
    private int page = 1;
    private SharedPreferencesUtils sp;
    private String nickname;
    private String avatar;
    private TextView userName;
    private TextView yfCount;
    private ImageViewPlus userHead;
    private SpringView hbRecordSpring;
    private String packgeType; //判断是鞅分红包还是商机币红包

    @Override
    protected void init() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red_title), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        list = new ArrayList<>();
        //判断是鞅分红包还是商机币红包
        packgeType = getIntent().getStringExtra("packge_type");
        sp = SharedPreferencesUtils.getUtil();
        nickname = (String) sp.getKey(getApplicationContext(), "nickname", "");
        avatar = (String) sp.getKey(getApplicationContext(), "avatar", "");
        hongbaoId = getIntent().getStringExtra("hongbao_id");
        initView();
        headerView = getLayoutInflater().inflate(R.layout.yf_record_head, null);
        initListView();
        hbRecordSpring.setType(SpringView.Type.FOLLOW);
        hbRecordSpring.setFooter(new DefaultFooter(getApplicationContext()));
        hbRecordSpring.setHeader(new DefaultHeader(getApplicationContext()));
        presenter.mYfRecord(getApplicationContext(), page);
        redRecordList.addHeaderView(headerView, null, false);
        yfhbRecordAdpter = new YFHBRecordAdpter(list, getApplicationContext());
        redRecordList.setAdapter(yfhbRecordAdpter);
    }

    //list头布局控件初始化
    private void initListView() {
        userHead = (ImageViewPlus) headerView.findViewById(R.id.user_head);
        yfCount = (TextView) headerView.findViewById(R.id.yf_count);
        userName = (TextView) headerView.findViewById(R.id.user_name);
        if (StringUtil.isNotEmpty(nickname)) {
            if (StringUtil.isNotEmpty(packgeType) && packgeType.equals(BizConstant.IS_SUC)) {
                userName.setText(nickname + "共收到商机币");
            } else {
                userName.setText(nickname + "共收到鞅分");
            }
        }
        if (StringUtil.isNotEmpty(avatar)) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
            Glide.with(getApplicationContext()).load(avatar).apply(options).into(userHead);
        }
    }

    //初始化控件
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        redRecordList = (ListView) findViewById(R.id.red_record_list);
        hbRecordSpring = (SpringView) findViewById(R.id.hb_record_spring);
    }

    @Override
    protected SendYFPresenter getPresenter() {
        return new SendYFPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_yang_fen_record;
    }


    /**
     * 展示总共抢到的红包记录
     *
     * @param listBeans
     */
    @Override
    public void showYFRecord(List<YfRecordBean.DataBean.ListBean> listBeans, String ReceiveHong) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(listBeans);
        yfCount.setText(ReceiveHong);
        yfhbRecordAdpter.notifyDataSetChanged();
        hbRecordSpring.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        presenter.mYfRecord(getApplicationContext(), page);
                        hbRecordSpring.onFinishFreshAndLoad();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        presenter.mYfRecord(getApplicationContext(), page);
                        hbRecordSpring.onFinishFreshAndLoad();
                    }
                }, 1000);
            }
        });
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    @Override
    public void showYfdata(SendYFBean sendYFBean) {

    }

    @Override
    public void showSnatchYangFe(List<HongBaoLogBean.DataBean> hongBaoLogBeanList) {

    }

    @Override
    public void showHongBaoLeftCount(HongBaoLeftCountBean hongBaoLeftCountBean) {

    }
}
