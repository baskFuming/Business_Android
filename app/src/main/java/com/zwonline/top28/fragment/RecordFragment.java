package com.zwonline.top28.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.RecordAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.bean.IntegralRecordBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.IntergralPresenter;
import com.zwonline.top28.view.IIntegralActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YSG
 * @desc积分纪录
 * @date ${Date}
 */
public class RecordFragment extends BaseFragment<IIntegralActivity, IntergralPresenter> implements IIntegralActivity {
    private int cate_ids;
    private String cate_name;
    private XRecyclerView recordRecy;
    private RecordAdapter recordAdapter;
    private int page = 1;
    private int refreshTime = 0;
    private int times = 0;
    private List<IntegralBean.DataBean.ListBean> list;

    @Override
    protected void init(View view) {
        recordRecy = (XRecyclerView) view.findViewById(R.id.record_recy);
        list = new ArrayList<>();
        if (getArguments() != null) {
            cate_ids = getArguments().getInt("cate_id");

            cate_name = getArguments().getString("cate_name");
        }
        if (cate_ids == 200) {
            presenter.showAllIntergralList(getActivity(), String.valueOf(BizConstant.PAGE));
        } else if (cate_ids == 300) {
            presenter.showIntergralList(getActivity(), BizConstant.TYPE_ONE, String.valueOf(BizConstant.PAGE));
        } else if (cate_ids == 400) {
            presenter.showIntergralList(getActivity(), BizConstant.TYPE_TWO, String.valueOf(BizConstant.PAGE));
        }
//        presenter.showAllIntergralList(getActivity(), "1");
        recordRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recordRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recordRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        recordRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recordRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        recordRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recordRecy.setLayoutManager(linearLayoutManager);
        recordAdapter = new RecordAdapter(list, getActivity());
        recordRecy.setAdapter(recordAdapter);
    }

    @Override
    protected IntergralPresenter setPresenter() {
        return new IntergralPresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.recordfragment;
    }

    //动态获取Fragment
    public static Fragment getInstance(IntegralRecordBean integralRecordBean) {
        RecordFragment recordFragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cate_id", integralRecordBean.cate_id);
        bundle.putString("cate_name", integralRecordBean.record_name);
        recordFragment.setArguments(bundle);
        return recordFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cate_ids == 200) {
            presenter.showAllIntergralList(getActivity(), String.valueOf(BizConstant.PAGE));
        } else if (cate_ids == 300) {
            presenter.showIntergralList(getActivity(), BizConstant.TYPE_ONE, String.valueOf(BizConstant.PAGE));
        } else if (cate_ids == 400) {
            presenter.showIntergralList(getActivity(), BizConstant.TYPE_TWO, String.valueOf(BizConstant.PAGE));
        }
    }

    //展示数据
    @Override
    public void showIntegralListByTypeId(List<IntegralBean.DataBean.ListBean> integralList) {

        if (page == 1) {
            list.clear();
        }
        list.addAll(integralList);

        recordAdapter.notifyDataSetChanged();
        recordRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (cate_ids == 200) {
                            presenter.showAllIntergralList(getActivity(), String.valueOf(BizConstant.PAGE));
                        } else if (cate_ids == 300) {
                            presenter.showIntergralList(getActivity(), BizConstant.TYPE_ONE, String.valueOf(BizConstant.PAGE));
                        } else if (cate_ids == 400) {
                            presenter.showIntergralList(getActivity(), BizConstant.TYPE_TWO, String.valueOf(BizConstant.PAGE));
                        }
                        if (recordRecy != null)
                            recordRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                Log.e("aaaaa", "call onLoadMore");

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        if (cate_ids == 200) {
                            presenter.showAllIntergralList(getActivity(), String.valueOf(page));
                        } else if (cate_ids == 300) {
                            presenter.showIntergralList(getActivity(), BizConstant.TYPE_ONE, String.valueOf(page));
                        } else if (cate_ids == 400) {
                            presenter.showIntergralList(getActivity(), BizConstant.TYPE_TWO, String.valueOf(page));
                        }
                        if (recordRecy != null) {
                            recordRecy.loadMoreComplete();
                        } else {
                            recordRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
                        }
                    }
                }, 1000);
                times++;
            }
        });


    }
}
