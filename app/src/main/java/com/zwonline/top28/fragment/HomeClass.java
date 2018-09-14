package com.zwonline.top28.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.AdvertisingActivity;
import com.zwonline.top28.activity.CompanyActivity;
import com.zwonline.top28.activity.HomeDetailsActivity;
import com.zwonline.top28.adapter.HomeAdapter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.bean.HomeClassBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomePresenter;
import com.zwonline.top28.presenter.RecordUserBehavior;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IHomeFragment;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


/**
 * 描述：主页分类
 *
 * @author YSG
 * @date 2018/1/9
 */
public class HomeClass extends BasesFragment<IHomeFragment, HomePresenter> implements IHomeFragment {

    private int page = 1;
    private String cate_id;
    private String cate_name;
    private HomeAdapter homeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<HomeClassBean.DataBean> hlist;
    private String token;
    private boolean isPrepared;//初始化是否完成
    private boolean isHasLoadedOnce;    //是否已经有过一次加载数据
    private LinearLayout alphaTitle;
    private int refreshTime = 0;
    private int times = 0;
    private XRecyclerView homerecyler;
    private RecyclerView searchrecy;
    private SwipeRefreshLayout swipeSearch;
    private LinearLayout linearHomeclass;
    private GifImageView homePgb;

    @Override
    protected void init(View view) {
        initView(view);
//        homePgb.setMovieResource(R.drawable.loading);
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        if (NetUtils.isConnected(getActivity())) {
            if (getArguments() != null) {
                cate_id = getArguments().getString("cate_id");
                cate_name = getArguments().getString("cate_name");
                if (NetUtils.isConnected(getActivity())) {
                    if (!StringUtil.isEmpty(cate_id) && cate_id.equals("300")) {
                        presenter.mHomeRecommend(getActivity(),String.valueOf(page));
                    } else {
                        presenter.mHomePage(getActivity(),String.valueOf(page), cate_id);
                    }
                }

                homerecyler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
                homerecyler.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
                homerecyler.setArrowImageView(R.drawable.iconfont_downgrey);
                homerecyler.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
                homerecyler.getDefaultFootView().setLoadingHint(getString(R.string.loading));
                homerecyler.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
            }
            hlist = new ArrayList<>();
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            homerecyler.setLayoutManager(linearLayoutManager);
            homeAdapter = new HomeAdapter(hlist, getActivity());
//            homerecyler.addItemDecoration(new ItemDecoration(getActivity()));
            homerecyler.setAdapter(homeAdapter);
        } else {
            //无网络 进度条设置
        }

    }

    /**
     * 初始化组件
     */
    private void initView(View view) {
        homerecyler = (XRecyclerView) view.findViewById(R.id.homerecyler);
        searchrecy = (RecyclerView) view.findViewById(R.id.searchrecy);
        swipeSearch = (SwipeRefreshLayout) view.findViewById(R.id.swipe_search);
        linearHomeclass = (LinearLayout) view.findViewById(R.id.linear_homeclass);
        homePgb = (GifImageView) view.findViewById(R.id.home_pgb);
    }


    @Override
    protected HomePresenter setPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.homeclassfrag;
    }

    @Override
    public void showHomeData(List<HomeClassBean.DataBean> homelist) {
        if (homelist != null && homelist.size() > 0) {
            homePgb.setVisibility(View.GONE);
            linearHomeclass.setVisibility(View.VISIBLE);

        }
        if (page == 1) {
            hlist.clear();
        }
        hlist.addAll(homelist);
        homeAdapter.notifyDataSetChanged();
        setOnItem(hlist);
    }

    @Override
    public void showSearch(List<HomeClassBean.DataBean> searchList) {

    }


    @Override
    public void showHomeclass(HomeClassBean HomeClassBean) {
        token = HomeClassBean.dialog;
    }

    @Override
    public void showInitData() {

    }

    @Override
    public void onErro() {
        if (getActivity() == null) {
            return;
        }
        if (page == 1) {
            hlist.clear();
            homeAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(getActivity(), getString(R.string.load_end));
        }
    }

    @Override
    public void showMyCollect(boolean flag) {

    }

    //设置条目点击事件
    public void setOnItem(final List<HomeClassBean.DataBean> hlist) {

        homeAdapter.setOnClickItemListener(new HomeAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                int positions = position - 1;
                String id = hlist.get(positions).id;
                String is_ad = hlist.get(positions).is_ad;
                String isWebview = hlist.get(positions).is_webview;
                RecordUserBehavior.recordUserBehaviors(getActivity(), BizConstant.VIEW_ARTICLE, id);
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                if (StringUtil.isEmpty(is_ad) || is_ad.equals("0")) {
                    Intent intent = new Intent(getActivity(), HomeDetailsActivity.class);
                    intent.putExtra("id", id + "");
                    intent.putExtra("title", hlist.get(positions).title);
                    intent.putExtra("token", token);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else if (is_ad.equals("1")) {

                    if (StringUtil.isNotEmpty(isWebview) && isWebview.equals("0")) {
//                        ToastUtils.showToast(getActivity(), "project_id==" + hlist.get(positions).project_id);
                        Intent intent = new Intent(getActivity(), CompanyActivity.class);
                        intent.putExtra("pid", hlist.get(positions).project_id);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else if (StringUtil.isNotEmpty(isWebview) && isWebview.equals("1")) {
                        Intent webIntent = new Intent(getActivity(), AdvertisingActivity.class);
                        webIntent.putExtra("jump_path", hlist.get(positions).jump_path);
                        webIntent.putExtra("title", hlist.get(positions).title);
                        startActivity(webIntent);
                    } else {
                        Log.v("is_webview=", isWebview);
                    }
                }
            }
        });

        homerecyler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        if (cate_id != null && cate_id.equals(BizConstant.BIZ_RECOMMEND_ID)) {
                            presenter.mHomeRecommend(getActivity(),String.valueOf(page));

                        } else {
                            presenter.mHomePage(getActivity(),String.valueOf(page), cate_id);
                        }
                        if (homerecyler != null)
                            homerecyler.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                Log.e("aaaaa", "call onLoadMore");

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        if (cate_id != null && cate_id.equals(BizConstant.BIZ_RECOMMEND_ID)) {
                            presenter.mHomeRecommend(getActivity(),String.valueOf(page));
                        } else {
                            presenter.mHomePage(getActivity(),String.valueOf(page), cate_id);
                        }
                        if (homerecyler != null) {
                            homerecyler.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

//        homerecyler.refresh();
    }

    //动态获取Fragment
    public static Fragment getInstance(HomeBean.DataBean dataBean) {
        HomeClass home = new HomeClass();
        Bundle bundle = new Bundle();
        bundle.putString("cate_id", dataBean.cate_id);
        bundle.putString("cate_name", dataBean.cate_name);
        home.setArguments(bundle);
        return home;
    }
}
