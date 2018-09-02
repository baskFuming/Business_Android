package com.zwonline.top28.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.MyFansAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.presenter.MyFansPresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyFansActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：粉丝
 *
 * @author YSG
 * @date 2017/12/20
 */
public class MyFansActivity extends BaseActivity<IMyFansActivity, MyFansPresenter> implements IMyFansActivity {
    private MyFansAdapter adapter;
    private String uid;
    private TextView no;
    private TextView fans;
    private RelativeLayout back;
    private XRecyclerView myfansRecy;
    private List<MyFansBean.DataBean> fList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;

    @Override
    protected void init() {
        initData();
        fList = new ArrayList<>();
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        fans.setText(intent.getStringExtra("fans"));
        System.out.print("uid===" + uid);
        presenter.mFans(this, uid, page);
        recyclerViewData();
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        myfansRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myfansRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        myfansRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        myfansRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        myfansRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        myfansRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myfansRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyFansAdapter(fList, MyFansActivity.this);
        myfansRecy.setAdapter(adapter);

    }

    private void initData() {
        no = (TextView) findViewById(R.id.no);
        fans = (TextView) findViewById(R.id.fans);
        back = (RelativeLayout) findViewById(R.id.back);
        myfansRecy = (XRecyclerView) findViewById(R.id.myfans_recy);
    }

    @Override
    protected MyFansPresenter getPresenter() {
        return new MyFansPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_fans;
    }


    @Override
    public void showMyFansDate(final List<MyFansBean.DataBean> fansList) {
        if (page == 1) {
            fList.clear();
        }
        fList.addAll(fansList);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyFansAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions = position - 1;
                Intent intent = new Intent(MyFansActivity.this, HomePageActivity.class);
                intent.putExtra("nickname", fList.get(positions).nickname);
                intent.putExtra("avatars", fList.get(positions).avatars);
                intent.putExtra("uid", fList.get(positions).uid);
                intent.putExtra("is_attention", fList.get(positions).did_i_follow);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                finish();
            }
        });
        loadMore();
    }

    /**
     * 判定是否有没有数据
     *
     * @param flag
     */
    @Override
    public void showMyFans(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            myfansRecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            myfansRecy.setVisibility(View.GONE);
        }

    }

    /**
     * 没有更多了
     */
    @Override
    public void noLoadMore() {
        if (MyFansActivity.this == null) {
            return;
        }
        if (page == 1) {
            fList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }

    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        myfansRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mFansMore(MyFansActivity.this, uid, page);
                        if (myfansRecy != null) {
                            myfansRecy.refreshComplete();
                        }
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mFansMore(MyFansActivity.this, uid, page);
                        if (myfansRecy != null) {
                            myfansRecy.loadMoreComplete();
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
