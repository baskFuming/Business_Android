package com.zwonline.top28.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomeDetailsActivity;
import com.zwonline.top28.adapter.MyIssueAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.message.MessageFollow;
import com.zwonline.top28.presenter.IHomeWordPresenter;
import com.zwonline.top28.utils.NavigationBar;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IHomeWordActivity;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragmnet extends BasesFragment<IHomeWordActivity, IHomeWordPresenter> implements IHomeWordActivity {
    private XRecyclerView xRecyclerView;
    private String uid;
    private SharedPreferencesUtils sp;
    private String nickname;
    private MessageFollow messageFollow;
    private int currentNum;
    private String sex;
    private List<MyIssueBean.DataBean> iList;
    private MyIssueAdapter issueAdapter;
    private boolean islogins;
    private String userUid;
    private int refreshTime = 0;
    private int times = 0;
    private int page = 1;
    private LinearLayout linearLayout;

    @Override
    protected void init(View view) {
//        NavigationBar.Statedata(getActivity());
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);//设置状态栏字体为白色
        linearLayout = view.findViewById(R.id.lay_re);
        xRecyclerView = view.findViewById(R.id.article_recy);
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(getActivity(), "islogin", false);
        userUid = (String) sp.getKey(getActivity(), "uid", "");
        Intent intent = getActivity().getIntent();
        uid = intent.getStringExtra("uid");
        sex = intent.getStringExtra("sex");
        iList = new ArrayList<>();
        presenter.mMyissue(getActivity(), uid, page);
        messageFollow = new MessageFollow();
//        linearLayout.setFocusable(true);
//        linearLayout.setFocusableInTouchMode(true);
//        linearLayout.requestFocus();
        issueRecyclerViewData();
    }

    @Override
    protected IHomeWordPresenter setPresenter() {
        return new IHomeWordPresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.activity_art;
    }

    @Override
    public void onErro() {
        Toast.makeText(getActivity(), "数据加载错误", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMyIssue(boolean flag) {
//        if (flag) {
//            xRecyclerView.setVisibility(View.VISIBLE);
//        } else {
//            xRecyclerView.setVisibility(View.GONE);
//        }
    }

    @Override
    public void showMyIssueDate(List<MyIssueBean.DataBean> issueList) {
        if (page == 1) {
            iList.clear();
        }
        iList.addAll(issueList);
        issueAdapter.notifyDataSetChanged();
        issueAdapter.setOnClickItemListener(new MyIssueAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
                intent.putExtra("id", iList.get(position - 1).id + "");
                intent.putExtra("title", iList.get(position - 1).title);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        IssueLoadMore();
    }

    @Override
    public void issueNoLoadMore() {
//        if (this == null) {
//            return;
//        }
//        if (page == 1) {
//            iList.clear();
//            issueAdapter.notifyDataSetChanged();
//        } else {
//            ToastUtils.showToast(getActivity(), getString(R.string.load_end));
//        }

    }

    private void IssueLoadMore() {
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mMyissueLoad(getActivity(), uid, page);
                        if (xRecyclerView != null)
                            xRecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mMyissueLoad(getActivity(), uid, page);
                        if (xRecyclerView != null) {
                            xRecyclerView.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });
    }

    /**
     * xRecyclerview发布文章配置
     */
    private void issueRecyclerViewData() {
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        xRecyclerView.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager scrollLinearLayoutManager = new LinearLayoutManager(getActivity());
        xRecyclerView.setLayoutManager(scrollLinearLayoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        issueAdapter = new MyIssueAdapter(iList, getActivity());
        xRecyclerView.setAdapter(issueAdapter);

    }

}
