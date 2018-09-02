package com.zwonline.top28.nim.yangfen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.umeng.socialize.editorpage.ShareActivity;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.HongBaoRecordAdpter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.SendYFPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.ImageViewPlus;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.ISendYFActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 抢红包页面
 */
public class SnatchYangFenActivity extends BaseActivity<ISendYFActivity, SendYFPresenter> implements ISendYFActivity {

    private RelativeLayout back;
    private TextView redPacketRecord;
    private ListView collectList;
    private View headerView;
    private List<HongBaoLogBean.DataBean> list;
    private HongBaoRecordAdpter hongBaoRecordAdpter;
    private ImageViewPlus sendYfHead;
    private TextView sendUserName;
    private ImageView pin;
    private TextView describe;
    private TextView yangfenCount;
    private TextView getYf;
    private String hongbaoId;
    private String redpackType;
    private int page = 1;
    private SpringView snatchSpring;

    @Override
    protected void init() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.red_title), 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        list = new ArrayList<>();
        initView();
        hongbaoId = getIntent().getStringExtra("hongbao_id");
        //红包类型
        redpackType = getIntent().getStringExtra("redpackType");
        headerView = getLayoutInflater().inflate(R.layout.snatch_head, null);
        snatchSpring.setType(SpringView.Type.FOLLOW);
        snatchSpring.setFooter(new DefaultFooter(getApplicationContext()));
        snatchSpring.setHeader(new DefaultHeader(getApplicationContext()));
        initListView();
        if (StringUtil.isNotEmpty(hongbaoId)) {
            presenter.mHongBaoLeftCount(getApplicationContext(), hongbaoId);//查询红包的请求
            presenter.mSnatchYangFen(getApplicationContext(), hongbaoId, page);
        }
        collectList.addHeaderView(headerView, null, false);
        hongBaoRecordAdpter = new HongBaoRecordAdpter(list, getApplicationContext());
        collectList.setAdapter(hongBaoRecordAdpter);
    }

    //头布局控件初始化
    private void initListView() {
        sendYfHead = (ImageViewPlus) headerView.findViewById(R.id.send_yf_head);
        sendUserName = (TextView) headerView.findViewById(R.id.send_user_name);
        pin = (ImageView) headerView.findViewById(R.id.pin);
        describe = (TextView) headerView.findViewById(R.id.describe);
        yangfenCount = (TextView) headerView.findViewById(R.id.yangfen_count);
        getYf = (TextView) headerView.findViewById(R.id.get_yf);
        sendUserName.setText(getIntent().getStringExtra("send_name"));
        describe.setText(getIntent().getStringExtra("title"));
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.no_photo_male).error(R.mipmap.no_photo_male);
        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("send_avatars")).apply(options).into(sendYfHead);
        if (StringUtil.isNotEmpty(redpackType) && redpackType.equals(BizConstant.ALREADY_FAVORITE)) {
            pin.setVisibility(View.VISIBLE);
        } else if (StringUtil.isNotEmpty(redpackType) && redpackType.equals(BizConstant.ENTERPRISE_tRUE)) {
            pin.setVisibility(View.GONE);
        }

    }

    @Override
    protected SendYFPresenter getPresenter() {
        return new SendYFPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_snatch_yang_fen;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        redPacketRecord = (TextView) findViewById(R.id.red_packet_record);
        collectList = (ListView) findViewById(R.id.collect_list);
        snatchSpring = (SpringView) findViewById(R.id.snatch_spring);
    }

    @OnClick({R.id.back, R.id.red_packet_record})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.red_packet_record://红包记录
                Intent intent = new Intent(SnatchYangFenActivity.this, YangFenRecordActivity.class);
                intent.putExtra("hongbao_id", hongbaoId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showYfdata(SendYFBean sendYFBean) {

    }

    /**
     * 抢的鞅分记录
     *
     * @param hongBaoLogBeanList
     */
    @Override
    public void showSnatchYangFe(List<HongBaoLogBean.DataBean> hongBaoLogBeanList) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(hongBaoLogBeanList);
        hongBaoRecordAdpter.notifyDataSetChanged();
        snatchSpring.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        presenter.mSnatchYangFen(getApplicationContext(), hongbaoId, page);
                        snatchSpring.onFinishFreshAndLoad();
                    }
                }, 1000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        presenter.mSnatchYangFen(getApplicationContext(), hongbaoId, page);
                        snatchSpring.onFinishFreshAndLoad();
                    }
                }, 1000);
            }
        });
    }

    /**
     * 鞅分剩余数量查询
     *
     * @param hongBaoLeftCountBean
     */
    @Override
    public void showHongBaoLeftCount(HongBaoLeftCountBean hongBaoLeftCountBean) {
        getYf.setText("已领取" + hongBaoLeftCountBean.data.hasGetPackageCount + "/" + hongBaoLeftCountBean.data.totalPackageCount + ", 共" + hongBaoLeftCountBean.data.hasGetAmount + "/" + hongBaoLeftCountBean.data.totalAmount + "鞅分");
        if (StringUtil.isEmpty(hongBaoLeftCountBean.data.getAmount) || hongBaoLeftCountBean.data.getAmount.equals(BizConstant.ENTERPRISE_tRUE)) {
            yangfenCount.setText("未抢到");
            yangfenCount.setTextSize(20);
        } else {
            yangfenCount.setText(hongBaoLeftCountBean.data.getAmount);
        }
    }

    @Override
    public void showYFRecord(List<YfRecordBean.DataBean.ListBean> yfRecordBean,String ReceiveHong) {

    }


}
