package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.HomeAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.HomeClassBean;
import com.zwonline.top28.presenter.HomePresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IHomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：首页搜索
 *
 * @author YSG
 * @date 2018/1/17
 */
public class HomeSearchActivity extends BaseActivity<IHomeFragment, HomePresenter> implements IHomeFragment {

    private String title;
    private int page = 1;
    private String token;
    private TextView no;
    private RelativeLayout back;
    private XRecyclerView homesearchRecy;
    private List<HomeClassBean.DataBean> list;
    private HomeAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private int refreshTime = 0;
    private int times = 0;

    @Override
    protected void init() {
        list = new ArrayList<>();
        no = (TextView) findViewById(R.id.no);
        back = (RelativeLayout) findViewById(R.id.back);
        homesearchRecy = (XRecyclerView) findViewById(R.id.homesearch_recy);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        presenter.mSearch(getApplicationContext(),title, String.valueOf(page));

        homesearchRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        homesearchRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        homesearchRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        homesearchRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        homesearchRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        homesearchRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        linearLayoutManager = new LinearLayoutManager(HomeSearchActivity.this, LinearLayoutManager.VERTICAL, false);
        homesearchRecy.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeAdapter(list, HomeSearchActivity.this);
        homesearchRecy.setAdapter(homeAdapter);
    }

    @Override
    protected HomePresenter getPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home_search;
    }

    @Override
    public void showHomeData(List<HomeClassBean.DataBean> homelist) {

    }

    @Override
    public void showHomeclass(HomeClassBean HomeClassBean) {
        token = HomeClassBean.dialog;
    }

    @Override
    public void showInitData() {

    }

    @Override
    public void showSearch(final List<HomeClassBean.DataBean> searchList) {
        if (page == 1) {
            list.clear();
        }
        list.addAll(searchList);
        homeAdapter.notifyDataSetChanged();
        homeAdapter.setOnClickItemListener(new HomeAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(HomeSearchActivity.this, HomeDetailsActivity.class);
                intent.putExtra("id", list.get(position - 1).id + "");
                intent.putExtra("title", list.get(position - 1).title);
                intent.putExtra("token", token);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        loadMore();
    }

    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        homesearchRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mSearch(getApplicationContext(), title, String.valueOf(page));
                        if (homesearchRecy != null)
                            homesearchRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                Log.e("aaaaa", "call onLoadMore");

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mSearch(getApplicationContext(),title, String.valueOf(page));
                        ;
                        if (homesearchRecy != null) {
                            homesearchRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }

    @Override
    public void onErro() {
        if (this == null) {
            return;
        }
        if (page == 1) {
            list.clear();
            homeAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }

    @Override
    public void showMyCollect(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            homesearchRecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            homesearchRecy.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
