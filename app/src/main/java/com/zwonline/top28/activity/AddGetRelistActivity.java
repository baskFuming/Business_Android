package com.zwonline.top28.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.AddReListAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.BusnicCirclerPresenter;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.BusincCirclerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 关注大V列表
 */
public class AddGetRelistActivity extends BaseActivity<BusincCirclerActivity, BusnicCirclerPresenter> implements BusincCirclerActivity {
    private AddReListAdapter addReListAdapter;
    private List<AddFollowBean.DataBean.ListBean> dlist;

    private SharedPreferencesUtils sp;
    private String userUid;
    private String title;
    private int item_id;
    private String getUserUid;
    private TextView te_attention;
    private XRecyclerView xRecyclerView;
    private int isAttentionPosition;
    private boolean islogins;

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(this, "islogin", false);
        userUid = (String) sp.getKey(this, "uid", "");
        initView();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        getUserUid = intent.getStringExtra("item_id");
        item_id = Integer.parseInt(getUserUid);
        dlist = new ArrayList<>();
        te_attention.setText(title);
        presenter.BusincCommentList(this, item_id);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.getDefaultFootView().setLoadingHint(getString(R.string.loading));
        xRecyclerView.getDefaultFootView().setNoMoreHint(getString(R.string.load_end));
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(AddGetRelistActivity.this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        xRecyclerView.setLayoutManager(scrollLinearLayoutManager);
        addReListAdapter = new AddReListAdapter(dlist, this);
        xRecyclerView.setAdapter(addReListAdapter);
    }

    /**
     * 查找控件
     */
    private void initView() {
        te_attention = (TextView) findViewById(R.id.attention);
        xRecyclerView = (XRecyclerView) findViewById(R.id.add_xey);
    }

    @Override
    protected BusnicCirclerPresenter getPresenter() {
        return new BusnicCirclerPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_add_get_relist;
    }

    @Override
    public void OnSuccess() {

    }

    @Override
    public void OnErron() {

    }

    @Override
    public void showBusinc(boolean flag) {

    }

    @Override
    public void showBusincDate(List<BusinessCircleBean.DataBean> issueList) {


    }


    @Override
    public void BusincNoLoadMore() {

    }

    @Override
    public void showBusincPro(List<BusinessCircleBean.DataBean.ListBean> bList) {

    }

    @Override
    public void showBusincDateList(List<AddFollowBean.DataBean.ListBean> issueList) {
        dlist.clear();
        dlist.addAll(issueList);
        /**
         * 点击关注，取消关注
         */
        addReListAdapter.attentionSetOnclick(new AddReListAdapter.AttentionSetOnclick() {


            @Override
            public void onclick(View view, int position) {
                isAttentionPosition = position;
                if (islogins) {
                    if (dlist.get(position).did_i_follow.equals(BizConstant.IS_FAIL)) {
                        presenter.attention(getApplicationContext(), BizConstant.FOLLOW, dlist.get(position).uid, "", "");
                    } else {
                        presenter.unAttention(getApplicationContext(), BizConstant.UN_FOLLOW, dlist.get(position).uid, "", "");
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "请先登录");
                }

            }
        });
        addReListAdapter.notifyDataSetChanged();
    }

    /**
     * 关注
     *
     * @param attentionBean
     */
    @Override
    public void showAttention(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            dlist.get(isAttentionPosition).did_i_follow = BizConstant.IS_SUC;
            addReListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 取消关注
     *
     * @param attentionBean
     */
    @Override
    public void showUnAttention(AttentionBean attentionBean) {
        if (attentionBean.status == 1) {
            dlist.get(isAttentionPosition).did_i_follow = BizConstant.IS_FAIL;
            addReListAdapter.notifyDataSetChanged();
        }
    }


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }
}
