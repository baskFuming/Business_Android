package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.MyCollectAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.MyCollectBean;
import com.zwonline.top28.presenter.MyCollectPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyCollectActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：收藏
 *
 * @author YSG
 * @date 2018/1/6
 */
public class MyCollectActivity extends BaseActivity<IMyCollectActivity, MyCollectPresenter> implements IMyCollectActivity {
    private String uid;
    private SharedPreferencesUtils sp;
    private String token;
    private RelativeLayout back;
    private TextView collect;
    private TextView no;
    private XRecyclerView mycollectRecy;
    private MyCollectAdapter adapter;
    private List<MyCollectBean.DataBean> cList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;

    @Override
    protected void init() {
        initData();//查找控件
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        cList = new ArrayList<>();
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        presenter.mMyCollect(this, uid, page);
        recyclerViewData();
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        mycollectRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mycollectRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mycollectRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        mycollectRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        mycollectRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        mycollectRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mycollectRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyCollectAdapter(cList, MyCollectActivity.this);
        mycollectRecy.setAdapter(adapter);

    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        collect = (TextView) findViewById(R.id.collect);
        no = (TextView) findViewById(R.id.no);
        mycollectRecy = (XRecyclerView) findViewById(R.id.mycollect_recy);
    }

    @Override
    protected MyCollectPresenter getPresenter() {
        return new MyCollectPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    public void showMyCollect(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            mycollectRecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            mycollectRecy.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMyCollectDate(final List<MyCollectBean.DataBean> collectList) {
        if (page == 1) {
            cList.clear();
        }
        cList.addAll(collectList);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyCollectAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(MyCollectActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyCollectActivity.this, WithoutCodeLoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    int positions = position - 1;
                    Intent intent = new Intent(MyCollectActivity.this, HomeDetailsActivity.class);
                    intent.putExtra("id", cList.get(positions).id + "");
                    intent.putExtra("token", token);
                    intent.putExtra("title", cList.get(positions).title);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            }
        });
        loadMore();
    }

    /**
     * 没有更多数据
     */
    @Override
    public void noLoadMore() {
        if (MyCollectActivity.this == null) {
            return;
        }
        if (page == 1) {
            cList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }


    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        mycollectRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mMyCollectLoadMore(MyCollectActivity.this, uid, page);
                        if (mycollectRecy != null)
                            mycollectRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                Log.e("aaaaa", "call onLoadMore");

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mMyCollectLoadMore(MyCollectActivity.this, uid, page);
                        if (mycollectRecy != null) {
                            mycollectRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
