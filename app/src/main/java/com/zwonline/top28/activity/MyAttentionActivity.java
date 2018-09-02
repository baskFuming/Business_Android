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
import com.zwonline.top28.adapter.MyAttentAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.MyAttentionBean;
import com.zwonline.top28.presenter.MyAttentionPresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyAttentionActivity;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：我的关注
 *
 * @author YSG
 * @date 2017/12/19
 */
public class MyAttentionActivity extends BaseActivity<IMyAttentionActivity, MyAttentionPresenter> implements IMyAttentionActivity {
    private MyAttentAdapter adapter;
    private String uid;
    private List<MyAttentionBean.DataBean> aList;
    private RelativeLayout back;
    private TextView attention;
    private TextView no;
    private XRecyclerView myattentionRecy;
    private int page = 1;
    private int refreshTime = 0;
    private int times = 0;

    @Override
    protected void init() {
        initData();//查找控件
        Intent intent = getIntent();
        aList = new ArrayList<>();
        uid = intent.getStringExtra("uid");
        attention.setText(intent.getStringExtra("attention"));
        if (TextUtils.isEmpty(uid)) {
            presenter.mAttention(this, page);
        } else {
            presenter.mMyAttention(this, uid, page);
        }
        myattentionRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myattentionRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        myattentionRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        myattentionRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        myattentionRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        myattentionRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myattentionRecy.setLayoutManager(linearLayoutManager);
        adapter = new MyAttentAdapter(aList, this);
        myattentionRecy.setAdapter(adapter);
    }

    //查找控件
    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        attention = (TextView) findViewById(R.id.attention);
        no = (TextView) findViewById(R.id.no);
        myattentionRecy = (XRecyclerView) findViewById(R.id.myattention_recy);
    }

    @Override
    protected MyAttentionPresenter getPresenter() {
        return new MyAttentionPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_attention;
    }

    /**
     * 判断有没有数据
     * @param flag
     */
    @Override
    public void showMyAttention(boolean flag) {
            if (flag) {
                no.setVisibility(View.GONE);
                myattentionRecy.setVisibility(View.VISIBLE);
            } else {
                no.setVisibility(View.VISIBLE);
                myattentionRecy.setVisibility(View.GONE);
            }
    }

    @Override
    public void showMyAttentionData(final List<MyAttentionBean.DataBean> mylist) {
        if (page == 1) {
            aList.clear();
        }
        aList.addAll(mylist);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new MyAttentAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions=position-1;
                Intent intent = new Intent(MyAttentionActivity.this, HomePageActivity.class);
                intent.putExtra("nickname", aList.get(positions).nickname);
                intent.putExtra("avatars", aList.get(positions).avatars);
                intent.putExtra("uid", aList.get(positions).followid);
                intent.putExtra("is_attention", aList.get(positions).did_i_follow);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                finish();
            }
        });
        loadMore();
    }

    /**
     * 没有更多数据
     */
    @Override
    public void noData() {
        if (MyAttentionActivity.this == null) {
            return;
        }
        if (page == 1) {
            aList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }

    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        myattentionRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page=1;
                        if (TextUtils.isEmpty(uid)) {
                            presenter.mAttentions(MyAttentionActivity.this,page);
                        } else {
                            presenter.mMyAttentions(MyAttentionActivity.this, uid, page);
                        }
                        if (myattentionRecy != null)
                            myattentionRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        if (TextUtils.isEmpty(uid)) {
                            presenter.mAttentions(MyAttentionActivity.this, page);
                        } else {
                            presenter.mMyAttentions(MyAttentionActivity.this, uid, page);
                        }
                        if (myattentionRecy != null) {
                            myattentionRecy.loadMoreComplete();
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
