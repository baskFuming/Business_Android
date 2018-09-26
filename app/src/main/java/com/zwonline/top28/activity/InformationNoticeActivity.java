package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.InforNoticeAdpater;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.InforNoticeBean;
import com.zwonline.top28.bean.InforNoticeCleanBean;
import com.zwonline.top28.bean.TipBean;
import com.zwonline.top28.presenter.InForNoticePresenter;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.InforNoticeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 商机圈我的消息通知列表
 */
public class InformationNoticeActivity extends BaseActivity<InforNoticeActivity, InForNoticePresenter> implements InforNoticeActivity {

    private XRecyclerView noticerecyclerView;
    private InforNoticeAdpater adpater;
    private List<InforNoticeBean.DataBean> dList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;
    private RelativeLayout relativeLayout;

    @Override
    protected void init() {
        dList = new ArrayList<>();
        presenter.InforNoticePageList(this, page);
        presenter.InforNoticePageListTip(InformationNoticeActivity.this, page);
        //查找ID资源
        initData();
        //加载RecycleView 通知列表
        InforeRecyclerViewData();

    }

    private void InforeRecyclerViewData() {
        noticerecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        noticerecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        noticerecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        noticerecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        noticerecyclerView.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        noticerecyclerView.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        noticerecyclerView.setLayoutManager(linearLayoutManager);
        adpater = new InforNoticeAdpater(this, dList);
        noticerecyclerView.setAdapter(adpater);

    }

    private void initData() {
        relativeLayout = (RelativeLayout) findViewById(R.id.backnotice);
        noticerecyclerView = (XRecyclerView) findViewById(R.id.notice_recyclerView);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected InForNoticePresenter getPresenter() {
        return new InForNoticePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_information_notice;
    }

    @Override
    public void inForNoticeMethod(InforNoticeBean inforNoticeBean) {

    }

    @Override
    public void inForNoticeList(List<InforNoticeBean.DataBean> dataBeanList) {
        if (page == 1) {
            dList.clear();
        }
        dList.addAll(dataBeanList);
        adpater.notifyDataSetChanged();
        adpater.setOnClickItemListener(new InforNoticeAdpater.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                String url = dList.get(position).url;
                String[] str = url.split("/");

//                //跳转到动态详情页面
                Intent intent = new Intent(getApplicationContext(), DynamicDetailsActivity.class);
                intent.putExtra("moment_id", str[1]);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        inForNoticeLoadMore();
    }

    private void inForNoticeLoadMore() {
        noticerecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.InforNoticePageList(InformationNoticeActivity.this, page);
                        if (noticerecyclerView != null)
                            noticerecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.InforNoticePageListTip(InformationNoticeActivity.this, page);
                        presenter.InforNoticePageList(InformationNoticeActivity.this, page);
                        if (noticerecyclerView != null) {
                            noticerecyclerView.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });
    }

    @Override
    public void inForNoticeMethodList(List<InforNoticeBean.DataBean.FromUserBean> dataBeanList) {
    }

    @Override
    public void inForNoticeLoad() {

    }

    @Override
    public void inForNticePage(int page) {


    }

    @Override
    public void inForNoticeCleanList(InforNoticeCleanBean dataBeancleanList) {


    }

    @Override
    public void inForNoticeTip(TipBean tipBean) {

    }

    @OnClick({R.id.notice_dele_tip})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.notice_dele_tip://清空数据
                if (dList != null && dList.size() != 0) {
                    for (int i = 0; i < dList.size(); i++) {
                        page++;
                        presenter.InforNoticePageCleanList(this, String.valueOf(page));
                    }
                    dList.clear();
                    adpater.notifyDataSetChanged();
                }
                break;
        }
    }
}
