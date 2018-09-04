package com.zwonline.top28.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.ShieldUserAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.SendFriendCirclePresenter;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.ShieldUserPopwindow;
import com.zwonline.top28.view.ISendFriendCircleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 屏蔽用户列表
 */
public class ShieldUserActivity extends BaseActivity<ISendFriendCircleActivity, SendFriendCirclePresenter> implements ISendFriendCircleActivity, View.OnClickListener {

    private RelativeLayout back;
    private TextView title;
    private XRecyclerView shieldRecy;
    private List<ShieldUserBean.DataBean> shieldList;
    private int page = 1;
    private int refreshTime = 0;
    private int times = 0;
    private ShieldUserAdapter adapter;
    private TextView noList;
    private ShieldUserPopwindow shieldUserPopwindow;
    private int positions;

    @Override
    protected void init() {
        shieldList = new ArrayList<>();
        initView();
        presenter.BlockUserList(this, page);
        recyclerViewData();
    }


    /**
     * xRecyclerview配置
     */
    private void recyclerViewData() {
        shieldRecy.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        shieldRecy.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        shieldRecy.setArrowImageView(R.drawable.iconfont_downgrey);
        shieldRecy.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        shieldRecy.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        shieldRecy.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        shieldRecy.setLayoutManager(linearLayoutManager);
        adapter = new ShieldUserAdapter(shieldList, getApplicationContext());
        shieldRecy.setAdapter(adapter);

    }

    @Override
    protected SendFriendCirclePresenter getPresenter() {
        return new SendFriendCirclePresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shield_user;
    }

    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        noList = (TextView) findViewById(R.id.no_list);
        title = (TextView) findViewById(R.id.title);
        title.setText("屏蔽设置");
        shieldRecy = (XRecyclerView) findViewById(R.id.shield_recy);
    }

    /**
     * 屏蔽用户列表
     *
     * @param shielduserList
     */
    @Override
    public void showBlockUserList(List<ShieldUserBean.DataBean> shielduserList) {
        if (page == 1) {
            shieldList.clear();
        }
        shieldList.addAll(shielduserList);

        adapter.notifyDataSetChanged();
        loadMore();//上拉刷新下拉加载
        adapter.setOnClickItemListener(new ShieldUserAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                positions = position - 1;
                shieldUserPopwindow = new ShieldUserPopwindow(ShieldUserActivity.this, itemsOnClicks, position);
                shieldUserPopwindow.showAtLocation(view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    /**
     * 屏蔽列表判断有没有数据
     *
     * @param flag
     */
    @Override
    public void showUserList(boolean flag) {
        if (flag) {
            noList.setVisibility(View.GONE);
            shieldRecy.setVisibility(View.VISIBLE);
        } else {
            noList.setVisibility(View.VISIBLE);
            shieldRecy.setVisibility(View.GONE);
        }
    }

    /**
     * 没有数据
     */
    @Override
    public void noLoadMore() {
        if (ShieldUserActivity.this == null) {
            return;
        }
        if (page == 1) {
            shieldList.clear();
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(this, getString(R.string.load_end));
        }
    }

    /**
     * 动态评论点赞
     *
     * @param attentionBean
     */
    @Override
    public void showLikeMomentComment(AttentionBean attentionBean) {

    }

    /**
     * 删除动态评论
     *
     * @param attentionBean
     */
    @Override
    public void showDeleteComment(AttentionBean attentionBean) {

    }

    /**
     * 推荐关注
     *
     * @param issueList
     */
    @Override
    public void showBusincDate(List<BusinessCircleBean.DataBean> issueList) {

    }

    /**
     * 顶部推荐关注列表
     *
     * @param issueList
     */
    @Override
    public void showAttentionDynamic(List<AtentionDynamicHeadBean.DataBean.ListBean> issueList) {

    }

    /**
     * 商机圈我的消息提醒
     *
     * @param attentionBean
     */
    @Override
    public void showGetMyNotificationCount(AttentionBean attentionBean) {

    }

    /**
     * 点赞列表
     *
     * @param likeList
     */
    @Override
    public void showGetLikeList(List<LikeListBean.DataBean> likeList) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    /**
     * 取消屏蔽
     *
     * @param refotPasswordBean
     */
    @Override
    public void showBlockUser(RefotPasswordBean refotPasswordBean) {
        if (refotPasswordBean.status == 1) {
            shieldList.remove(positions);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(getApplicationContext(), refotPasswordBean.msg);
        }
    }

    @Override
    public void showLikeMoment(AttentionBean attentionBean) {

    }


    @Override
    public void showPictures(PictursBean pictursBean) {

    }

    @Override
    public void showSendNewMoment(SendNewMomentBean sendNewMomentBean) {

    }

    @Override
    public void showConment(List<NewContentBean.DataBean> newList) {

    }

    @Override
    public void showFeedBack(SettingBean settingBean) {

    }

    @Override
    public void showDynamicComment(List<DynamicDetailsBean.DataBean> dataBeanList) {

    }

    @Override
    public void showDynamicShare(DynamicShareBean dynamicShareBean) {

    }

    @Override
    public void showNewComment(AddBankBean addBankBean) {

    }

    @Override
    public void showDeleteMoment(SettingBean settingBean) {

    }

    @Override
    public void showAttention(AttentionBean attentionBean) {

    }

    /**
     * 一键关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttentions(AttentionBean attentionBean) {

    }

    @Override
    public void showUnAttention(AttentionBean attentionBean) {

    }

    /**
     * 上拉刷新下拉加载
     */
    public void loadMore() {
        shieldRecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = 1;
                        presenter.BlockUserList(getApplicationContext(), page);
                        if (shieldRecy != null)
                            shieldRecy.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        presenter.BlockUserList(getApplicationContext(), page);
                        if (shieldRecy != null) {
                            shieldRecy.loadMoreComplete();
                        }
                    }
                }, 1000);
                times++;
            }
        });

    }

    /**
     * pop弹窗
     */
    private View.OnClickListener itemsOnClicks = new View.OnClickListener() {
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(ShieldUserActivity.this, mPermissionList, 123);
            }
            shieldUserPopwindow.setOutsideTouchable(true);
            shieldUserPopwindow.dismiss();
            shieldUserPopwindow.backgroundAlpha(ShieldUserActivity.this, 1f);
            switch (v.getId()) {
                case R.id.look_homepage://关注，取消关注
                    //销毁弹出框
                    shieldUserPopwindow.dismiss();
                    shieldUserPopwindow.backgroundAlpha(ShieldUserActivity.this, 1f);
                    Intent intent = new Intent(ShieldUserActivity.this, HomePageActivity.class);
                    intent.putExtra("uid", shieldList.get(positions).uid);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    break;
                case R.id.cancel_shield://屏蔽
                    //销毁弹出框
                    shieldUserPopwindow.dismiss();
                    shieldUserPopwindow.backgroundAlpha(ShieldUserActivity.this, 1f);
                    presenter.BlockUser(ShieldUserActivity.this, BizConstant.UNPINGBI, shieldList.get(positions).uid);
                    break;

                default:
                    break;
            }
        }

    };
}
