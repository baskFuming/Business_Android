package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.MyShareAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.presenter.MySharePresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyShareActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：我的分享
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyShareActivity extends BaseActivity<IMyShareActivity, MySharePresenter> implements IMyShareActivity {
    private String uid;
    private MyShareAdapter adapter;
    private SharedPreferencesUtils sp;
    private String token;
    private RelativeLayout back;
    private TextView no;
    private XRecyclerView myShareRecy;
    private List<MyShareBean.DataBean> sList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;

    @Override
    protected void init() {
        initData();//查找控件
        sp = SharedPreferencesUtils.getUtil();
        sList = new ArrayList<>();
        token = (String) sp.getKey(this, "dialog", "");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        presenter.mMyShare(MyShareActivity.this, uid, page);
        recyclerViewData();
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        myShareRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myShareRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        myShareRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        myShareRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        myShareRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        myShareRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myShareRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyShareAdapter(sList, this);
        myShareRecy.setAdapter(adapter);

    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        no = (TextView) findViewById(R.id.no);
        myShareRecy = (XRecyclerView) findViewById(R.id.my_share_recy);
    }

    @Override
    protected MySharePresenter getPresenter() {
        return new MySharePresenter(MyShareActivity.this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_share;
    }

    /**
     * 判别是否有数据
     *
     * @param flag
     */
    @Override
    public void showMyShare(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            myShareRecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            myShareRecy.setVisibility(View.GONE);
        }
    }

    /**
     * 显示数据
     *
     * @param shareList
     */
    @Override
    public void showMyShareDte(final List<MyShareBean.DataBean> shareList) {
        if (page == 1) {
            sList.clear();
        }
        sList.addAll(shareList);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyShareAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions = position - 1;
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(MyShareActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyShareActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {

                    Intent intent = new Intent(MyShareActivity.this, HomeDetailsActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("id", sList.get(positions).id + "");
                    intent.putExtra("title", sList.get(positions).title);
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
        if (this == null) {
            return;
        }
        if (page == 1) {
            sList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }


    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        myShareRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mMyShareLoad(MyShareActivity.this, uid, page);
                        if (myShareRecy != null)
                            myShareRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mMyShareLoad(MyShareActivity.this, uid, page);
                        if (myShareRecy != null) {
                            myShareRecy.loadMoreComplete();
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
