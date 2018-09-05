package com.zwonline.top28.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.HomePageActivity;
import com.zwonline.top28.adapter.MyFansListAdapter;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.IntegralRecordBean;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.MyFansPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyFansActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YSG
 * @desc粉丝的列表
 * @date ${Date}
 */
public class FansListFragment extends BaseFragment<IMyFansActivity, MyFansPresenter> implements IMyFansActivity {
    private int cate_ids;
    private String cate_name;
    private RelativeLayout title_relay;
    private XRecyclerView myFansRecy;
    private MyFansListAdapter myFansListAdapter;
    private int page = 1;
    private int refreshTime = 0;
    private int times = 0;
    private List<MyFansBean.DataBean> fanList;
    private int attentionPosition;
    private SharedPreferencesUtils sp;
    private String myUid;

    @Override
    protected void init(View view) {
        sp = SharedPreferencesUtils.getUtil();
        myUid = (String) sp.getKey(getActivity(), "uid", "");
        fanList = new ArrayList<>();
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.white), 0);
        myFansRecy = (XRecyclerView) view.findViewById(R.id.myfans_recy);
        title_relay = (RelativeLayout) view.findViewById(R.id.title_relay);
        title_relay.setVisibility(View.GONE);
        if (getArguments() != null) {
            cate_ids = getArguments().getInt("cate_id");
            cate_name = getArguments().getString("cate_name");
        }
//        presenter.mMyFans(getActivity());
        initData(cate_ids);
        myFansRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        myFansRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        myFansRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        myFansRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        myFansRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        myFansRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        myFansRecy.setLayoutManager(linearLayoutManager);
        myFansListAdapter = new MyFansListAdapter(fanList, getActivity());
        myFansRecy.setAdapter(myFansListAdapter);

    }

    @Override
    protected MyFansPresenter setPresenter() {
        return new MyFansPresenter(this);
    }

    //网络请求
    public void initData(int cate_ids) {
        if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 1) {
            presenter.mMyFanses(getActivity(), "", page);
        } else if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 2) {
            presenter.mMyFanses(getActivity(), BizConstant.FOLLOW, page);
        } else if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 3) {
            presenter.mMyFanses(getActivity(), BizConstant.UN_FOLLOW, page);
        }
//        else if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 4) {
//            presenter.mMyFanses(getActivity(), BizConstant.CONTACTED);
//        } else if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 5) {
//            presenter.mMyFanses(getActivity(), BizConstant.UN_CONTACTED);
//        }

    }

    @Override
    protected int setLayouId() {
        return R.layout.activity_my_fans;
    }

    //动态获取Fragment
    public static Fragment getInstance(IntegralRecordBean integralRecordBean) {
        FansListFragment recordFragment = new FansListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cate_id", integralRecordBean.cate_id);
        bundle.putString("cate_name", integralRecordBean.record_name);
        recordFragment.setArguments(bundle);
        return recordFragment;
    }


    //展示数据
    @Override
    public void showMyFansDate(final List<MyFansBean.DataBean> MyFansList) {
        if (page == 1) {
            fanList.clear();
        }
        fanList.addAll(MyFansList);
        myFansListAdapter.notifyDataSetChanged();
        myFansListAdapter.setOnClickItemListener(new MyFansListAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position, View view) {
                int positions = position - 1;
                String uid = fanList.get(positions).uid;
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.putExtra("nickname", fanList.get(positions).nickname);
                intent.putExtra("avatars", fanList.get(positions).avatars);
                intent.putExtra("uid", uid);
                intent.putExtra("is_attention", fanList.get(positions).did_i_follow);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
//                getActivity().finish();
            }
        });
        myFansRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        initData(cate_ids);
                        if (myFansRecy != null)
                            myFansRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }


            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        initData(cate_ids);
                        if (myFansRecy != null) {
                            myFansRecy.loadMoreComplete();
                        } else {
                            myFansRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
                        }
                    }
                }, 1000);
                times++;
            }
        });
        myFansListAdapter.attentionMomentSetOnclick(new MyFansListAdapter.AttentionMomentInterface() {


            @Override
            public void onclick(View view, int position) {
                attentionPosition = position;
                if (fanList.get(attentionPosition).did_i_follow.equals(BizConstant.IS_FAIL)) {
                    presenter.mAttentions(getActivity(), "follow", fanList.get(position).uid, "");
                } else {
                    presenter.mUnAttention(getActivity(), "un_follow", fanList.get(position).uid);
                }
            }
        });

    }

    @Override
    public void showMyFans(boolean flag) {

    }

    @Override
    public void noLoadMore() {

    }

    /**
     * 关注关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttention(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 1) {
                fanList.get(attentionPosition).did_i_follow = BizConstant.IS_SUC;
            } else if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 3) {
                fanList.remove(attentionPosition);
            }
            myFansListAdapter.notifyDataSetChanged();
        }
        ToastUtils.showToast(getActivity(), attentionBean.msg);
    }

    /**
     * 取消关注
     *
     * @param attentionBean
     */
    @Override
    public void showUnAttention(AttentionBean attentionBean) {
        if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 1) {
            fanList.get(attentionPosition).did_i_follow = BizConstant.IS_FAIL;
        } else if (StringUtil.isNotEmpty(String.valueOf(cate_ids)) && cate_ids == 2) {
            fanList.remove(attentionPosition);
        }
        myFansListAdapter.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), attentionBean.msg);
    }
}
