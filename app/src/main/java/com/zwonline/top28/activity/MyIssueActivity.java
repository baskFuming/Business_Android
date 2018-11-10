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
import com.zwonline.top28.adapter.MyIssueAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.presenter.MyIssuePresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyIssueActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：我的发布
 *
 * @author YSG
 * @date 2017/12/25
 */
public class MyIssueActivity extends BaseActivity<IMyIssueActivity, MyIssuePresenter> implements IMyIssueActivity {
    private MyIssueAdapter adapter;
    private String uid;
    private SharedPreferencesUtils sp;
    private String token;
    private RelativeLayout back;
    private TextView issue;
    private TextView no;
    private XRecyclerView myIssueRecy;
    private List<MyIssueBean.DataBean> iList;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;

    @Override
    protected void init() {
        initData();//查找控件
        sp = SharedPreferencesUtils.getUtil();
        token = (String) sp.getKey(this, "dialog", "");
        iList = new ArrayList<>();
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        if (StringUtil.isNotEmpty(intent.getStringExtra("issue"))){
            issue.setText(intent.getStringExtra("issue"));
        }
        if (TextUtils.isEmpty(uid)) {
            presenter.mMyissues(this, page);
        } else {
            presenter.mMyissue(MyIssueActivity.this, uid, page);
        }
        recyclerViewData();
    }

    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        myIssueRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myIssueRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        myIssueRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        myIssueRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        myIssueRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        myIssueRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myIssueRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyIssueAdapter(iList, this);
        myIssueRecy.setAdapter(adapter);

    }


    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        issue = (TextView) findViewById(R.id.issue);
        no = (TextView) findViewById(R.id.no);
        myIssueRecy = (XRecyclerView) findViewById(R.id.my_issue_recy);
    }

    @Override
    protected MyIssuePresenter getPresenter() {
        return new MyIssuePresenter(MyIssueActivity.this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_issue;
    }

    /**
     * 判别是否有数据
     *
     * @param flag
     */
    @Override
    public void showMyIssue(boolean flag) {
        if (flag) {
            no.setVisibility(View.GONE);
            myIssueRecy.setVisibility(View.VISIBLE);
        } else {
            no.setVisibility(View.VISIBLE);
            myIssueRecy.setVisibility(View.GONE);
        }
    }

    /**
     * 显示数据
     *
     * @param issueList
     */
    @Override
    public void showMyIssueDate(final List<MyIssueBean.DataBean> issueList) {
        if (page == 1) {
            iList.clear();
        }
        iList.addAll(issueList);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyIssueAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions=position-1;
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(MyIssueActivity.this, R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyIssueActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {

                    Intent intent = new Intent(MyIssueActivity.this, HomeDetailsActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("id", iList.get(positions).id + "");
                    intent.putExtra("title", iList.get(positions).title);
                    startActivity(intent);
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
        if (MyIssueActivity.this == null) {
            return;
        }
        if (page == 1) {
            iList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }


    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        myIssueRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        if (TextUtils.isEmpty(uid)) {
                            presenter.mMyissuesLoad(MyIssueActivity.this, page);
                        } else {
                            presenter.mMyissueLoad(MyIssueActivity.this, uid, page);
                        }
                        if (myIssueRecy != null)
                            myIssueRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        if (TextUtils.isEmpty(uid)) {
                            presenter.mMyissuesLoad(MyIssueActivity.this, page);
                        } else {
                            presenter.mMyissueLoad(MyIssueActivity.this, uid, page);
                        }
                        if (myIssueRecy != null) {
                            myIssueRecy.loadMoreComplete();
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
