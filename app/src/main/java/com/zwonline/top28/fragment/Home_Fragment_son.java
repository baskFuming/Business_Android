package com.zwonline.top28.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.CompanyActivity;
import com.zwonline.top28.activity.WithoutCodeLoginActivity;
import com.zwonline.top28.adapter.BusinessAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.presenter.BusinessListPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IBusinessListFrag;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * 1.商机的分类
 * 2.@authorDell
 * 3.@date2017/12/7 11:51
 */

public class Home_Fragment_son extends BaseFragment<IBusinessListFrag, BusinessListPresenter> implements IBusinessListFrag {

    private XRecyclerView businessRecycler;
    private GifImageView projectGif;
    private int page = 1;
    private BusinessAdapter adapter;
    private List<BusinessListBean.DataBean> sumList;
    private String cate_id;
    private List<BusinessListBean.DataBean> hlist;
    private SharedPreferencesUtils sp;
    private String token;
    private int refreshTime = 0;
    private int times = 0;

    @Override
    protected void init(View view) {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
        initData(view);//查找控件
        sp = SharedPreferencesUtils.getUtil();
        if (getArguments() != null) {
            cate_id = getArguments().getString("cate_id");
            presenter.mBusinessList(getActivity(),String.valueOf(page), cate_id);
        }
        token = (String) sp.getKey(getActivity(), "dialog", "");
        businessRecycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        businessRecycler.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        businessRecycler.setArrowImageView(R.drawable.iconfont_downgrey);
        businessRecycler.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        businessRecycler.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        businessRecycler.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        hlist = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        businessRecycler.setLayoutManager(linearLayoutManager);
        adapter = new BusinessAdapter(hlist, getActivity());
        businessRecycler.setAdapter(adapter);
    }

    private void initData(View v) {
        businessRecycler = (XRecyclerView) v.findViewById(R.id.business_recyler);
        projectGif = (GifImageView) v.findViewById(R.id.project_gif);
    }

    @Override
    protected BusinessListPresenter setPresenter() {
        return new BusinessListPresenter(this);
    }

    @Override
    protected int setLayouId() {
        return R.layout.home_fragment_son;
    }

    //动态获取Fragment
    public static Fragment getInstance(String cate_id) {
        Home_Fragment_son home = new Home_Fragment_son();
        Bundle bundle = new Bundle();
        bundle.putString("cate_id", cate_id);
        home.setArguments(bundle);
        return home;
    }


    @Override
    public void showData(final List<BusinessListBean.DataBean> list) {
        if (list != null && list.size() > 0) {
            projectGif.setVisibility(View.GONE);
            businessRecycler.setVisibility(View.VISIBLE);
        }
        if (page == 1) {
            hlist.clear();
        }
        hlist.addAll(list);
        adapter.notifyDataSetChanged();

//            ToastUtils.showToast(getActivity(), getString(R.string.data_loading));
        adapter.setOnClickItemListener(new BusinessAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                int positions = position - 1;
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(getActivity(), R.string.user_not_login, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), WithoutCodeLoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    Intent intent = new Intent(getActivity(), CompanyActivity.class);
                    intent.putExtra("uid", hlist.get(positions).uid);
                    intent.putExtra("enterprise_name", hlist.get(positions).cate_name);
                    intent.putExtra("pid", hlist.get(positions).id);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }

            }
        });

//        businessSpring.setListener(new SpringView.OnFreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page = 1;
//                        hlist.clear();
//                        presenter.mBusinessList(String.valueOf(page), cate_id);
//                        adapter.notifyDataSetChanged();
//                        businessSpring.onFinishFreshAndLoad();
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public void onLoadmore() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page++;
//                        presenter.mBusinessList(String.valueOf(page), cate_id);
//                        businessSpring.onFinishFreshAndLoad();
//                    }
//                }, 1000);
//            }
//        });
        businessRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.mBusinessList(getActivity(),String.valueOf(page), cate_id);
//                        adapter.notifyDataSetChanged();
                        if (businessRecycler != null) {
                            businessRecycler.refreshComplete();
                        }
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                Log.e("aaaaa", "call onLoadMore");

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.mBusinessList(getActivity(),String.valueOf(page), cate_id);
                        if (businessRecycler != null) {
                            businessRecycler.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });


    }

    @Override
    public void showErro() {
        if (getActivity() == null) {
            return;
        }
        if (page == 1) {
            hlist.clear();
            adapter.notifyDataSetChanged();
        } else {
//            ToastUtils.showToast(getActivity(), getString(R.string.load_end));
        }
    }
}
