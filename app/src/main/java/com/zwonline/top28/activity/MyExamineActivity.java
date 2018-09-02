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
import com.zwonline.top28.adapter.MyExamineAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.MyExamine;
import com.zwonline.top28.presenter.MyExaminePresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyExamineActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：我的考察
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyExamineActivity extends BaseActivity<IMyExamineActivity, MyExaminePresenter> implements IMyExamineActivity {
    private MyExamineAdapter adapter;
    private RelativeLayout back;
    private TextView hotInspect;
    private TextView no;
    private XRecyclerView myexamineRecy;
    private List<MyExamine.DataBean> eList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;

    @Override
    protected void init() {
        initData();//查找控件
        presenter.mMyExamine(this, page);
        recyclerViewData();
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        myexamineRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myexamineRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        myexamineRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        myexamineRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        myexamineRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        myexamineRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myexamineRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyExamineAdapter(eList, this);
        myexamineRecy.setAdapter(adapter);

    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        hotInspect = (TextView) findViewById(R.id.hot_inspect);
        no = (TextView) findViewById(R.id.no);
        myexamineRecy = (XRecyclerView) findViewById(R.id.myexamine_recy);
    }

    @Override
    protected MyExaminePresenter getPresenter() {
        return new MyExaminePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_examine;
    }

    /**
     * 判别是否有数据
     *
     * @param flag
     */
    @Override
    public void showMyExamine(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            myexamineRecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            myexamineRecy.setVisibility(View.GONE);
        }
    }

    /**
     * 显示数据
     *
     * @param myExamineList
     */
    @Override
    public void showMyExamineDate(final List<MyExamine.DataBean> myExamineList) {
        if (page == 1) {
            eList.clear();
        }
        eList.addAll(myExamineList);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyExamineAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions = position - 1;
                Intent intent = new Intent(MyExamineActivity.this, ProjectActivity.class);
                intent.putExtra("uid", eList.get(positions).id);
                Log.e("id=", eList.get(positions).id);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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
            eList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }


    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        myexamineRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mMyExamineLoad(MyExamineActivity.this, page);
                        if (myexamineRecy != null)
                            myexamineRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mMyExamineLoad(MyExamineActivity.this, page);
                        if (myexamineRecy != null) {
                            myexamineRecy.loadMoreComplete();
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
