package com.zwonline.top28.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomeSearchActivity;
import com.zwonline.top28.activity.YangShiActivity;
import com.zwonline.top28.adapter.HomeAdapter;
import com.zwonline.top28.adapter.YSListAdapter;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.bean.YSBannerBean;
import com.zwonline.top28.bean.YSListBean;
import com.zwonline.top28.presenter.YangShiPresenter;
import com.zwonline.top28.utils.LogUtils;
import com.zwonline.top28.utils.NetUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IYangShiActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 鞅市首页列表
 */
public class YangShiListFragment extends BasesFragment<IYangShiActivity, YangShiPresenter> implements IYangShiActivity {
    private int page = 1;
    private RecyclerView ysXrecy;
    private List<YSListBean.DataBean.ListBean> ysLists;
    private YSListAdapter ysListAdapter;
    private String cate_name;

    @Override
    protected void init(View view) {
        ysLists = new ArrayList<>();
        ysXrecy = view.findViewById(R.id.ys_xrecy);
        if (getArguments() != null) {
            String cate_id = getArguments().getString("cate_id");
            cate_name = getArguments().getString("cate_name");
            if (NetUtils.isConnected(getActivity())) {
                if (!StringUtil.isEmpty(cate_id) && cate_id.equals("1")) {
                    presenter.AuctionList(getActivity(), "1", page);
                } else if (!StringUtil.isEmpty(cate_id) && cate_id.equals("2")) {
                    presenter.AuctionList(getActivity(), "2", page);
                } else if (!StringUtil.isEmpty(cate_id) && cate_id.equals("3")) {
                    presenter.AuctionList(getActivity(), "3", page);
                }
            }

        }

//        ysXrecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        ysXrecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
//        ysXrecy.setArrowImageView(R.drawable.iconfont_downgrey);
//        ysXrecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
//        ysXrecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
//        ysXrecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ysXrecy.setLayoutManager(linearLayoutManager);
        ysListAdapter = new YSListAdapter(ysLists, getActivity());
        ysXrecy.setAdapter(ysListAdapter);
    }

    @Override
    protected YangShiPresenter setPresenter() {
        return new YangShiPresenter(this);
    }


    @Override
    protected int setLayouId() {
        return R.layout.yslist_fargment;
    }

    @Override
    public void showBannerList(List<YSBannerBean.DataBean> ysBannerBeanList) {

    }

    /**
     * 鞅分拍卖列表
     *
     * @param ysList
     */
    @Override
    public void showAuctionList(final List<YSListBean.DataBean.ListBean> ysList) {
//        if (page == 1) {
//            ysList.clear();
//        }
        if (ysList!=null){
            ysLists.addAll(ysList);
        }
        ysListAdapter.setOnClickItemListener(new YSListAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(getActivity(), YangShiActivity.class);
                intent.putExtra("jump_url", ysLists.get(position).url);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        ysListAdapter.notifyDataSetChanged();
    }


    //动态获取Fragment
    public static Fragment getInstance(HomeBean.DataBean dataBean) {
        YangShiListFragment home = new YangShiListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cate_id", dataBean.cate_id);
        bundle.putString("cate_name", dataBean.cate_name);
        home.setArguments(bundle);
        return home;
    }
}
