package com.zwonline.top28.nim.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.AnnouncementAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.NotifyDetailsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.AnnouncementPresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IAnnouncementActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公告列表的类
 */
public class AnnouncementActivity extends BaseActivity<IAnnouncementActivity, AnnouncementPresenter> implements IAnnouncementActivity {

    private RelativeLayout searchGroupBack;
    private XRecyclerView notifyRecy;
    private TextView noNotify;
    private List<AnnouncementBean.DataBean> iList;
    private AnnouncementAdapter adapter;
    private int page = 1;
    private int times = 0;
    private int refreshTime = 0;

    @Override
    protected void init() {
        iList = new ArrayList<>();
        presenter.mAnnouncement(this, String.valueOf(page));
        initView();
    }

    @Override
    protected AnnouncementPresenter getPresenter() {
        return new AnnouncementPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_announcement;
    }

    private void initView() {
        searchGroupBack = (RelativeLayout) findViewById(R.id.search_group_back);
        notifyRecy = (XRecyclerView) findViewById(R.id.notify_list);
        noNotify = (TextView) findViewById(R.id.no_notify);
        notifyRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        notifyRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        notifyRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        notifyRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        notifyRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        notifyRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        notifyRecy.setLayoutManager(linearLayoutManager);
        adapter = new AnnouncementAdapter(iList, this);
        notifyRecy.setAdapter(adapter);
    }

    @Override
    public void showAnnouncement(List<AnnouncementBean.DataBean> list) {
        if (page == 1) {
            iList.clear();
        }
        iList.addAll(list);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new AnnouncementAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions = position - 1;
                if (iList.get(positions).is_read.equals(BizConstant.ENTERPRISE_tRUE)) {
                    presenter.mreadNotice(getApplicationContext(), iList.get(positions).notice_id);
                    iList.get(positions).is_read=BizConstant.IS_SUC;
                    adapter.notifyDataSetChanged();
                }
                Intent intent = new Intent(getApplicationContext(), NotifyDetailsActivity.class);
                intent.putExtra("noticeId", iList.get(positions).notice_id);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        loadMore();
    }

    /**
     * 有没有公告
     *
     * @param flag
     */
    @Override
    public void noAnnouncement(boolean flag) {
        if (flag) {
            noNotify.setVisibility(View.GONE);
            notifyRecy.setVisibility(View.VISIBLE);
        } else {
            noNotify.setVisibility(View.VISIBLE);
            notifyRecy.setVisibility(View.GONE);
        }
    }

    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        notifyRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mAnnouncement(getApplicationContext(), String.valueOf(page));
                        if (notifyRecy != null)
                            notifyRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mAnnouncement(getApplicationContext(), String.valueOf(page));
                        if (notifyRecy != null) {
                            notifyRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }

    /**
     * 判断有没有更多数据
     */
    @Override
    public void noLoadMore() {
        if (AnnouncementActivity.this == null) {
            return;
        }
        if (page == 1) {
            iList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }

    @Override
    public void showNotifyDetails(NotifyDetailsBean notifyDetailsBean) {

    }


    @OnClick(R.id.search_group_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
