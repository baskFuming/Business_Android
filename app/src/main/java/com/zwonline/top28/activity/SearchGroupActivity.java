package com.zwonline.top28.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.RecommentTeamsAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddFriendBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.nim.session.SessionHelper;
import com.zwonline.top28.presenter.AddFriendPresenter;
import com.zwonline.top28.tip.toast.ToastUtil;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IAddFriendActivity;

import java.util.List;

import butterknife.OnClick;

/**
 * 搜索群组
 */
public class SearchGroupActivity extends BaseActivity<IAddFriendActivity, AddFriendPresenter> implements IAddFriendActivity {

    private RelativeLayout searchGroupBack;
    private RelativeLayout addFriendRelat;
    private EditText addFriendEt;
    private RecyclerView recommentGroup;
    private RecommentTeamsAdapter recommentTeamsAdapter;


    @Override
    protected void init() {
        initView();
        presenter.mRecommendTeam(this);
    }

    @Override
    protected AddFriendPresenter getPresenter() {
        return new AddFriendPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_group;
    }

    private void initView() {
        searchGroupBack = (RelativeLayout) findViewById(R.id.search_group_back);
        addFriendRelat = (RelativeLayout) findViewById(R.id.add_friend_relat);
        recommentGroup = (RecyclerView) findViewById(R.id.recomment_group);
        addFriendEt = (EditText) findViewById(R.id.add_friend_et);
        //搜索群
        addFriendEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtil.isEmpty(addFriendEt.getText().toString().trim())) {
                        SessionHelper.queryTeamById(getApplicationContext(), addFriendEt.getText().toString().trim());
                        //addFriendEt.setCursorVisible(false);//光标隐藏
                        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                    } else {
                        ToastUtil.showToast(SearchGroupActivity.this, "请输入内容！");
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @OnClick({R.id.search_group_back, R.id.add_friend_et})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.search_group_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
        }
    }

    @Override
    public void showAddFriend(List<AddFriendBean.DataBean> addFriendList) {

    }

    @Override
    public void noFriend(boolean flag) {

    }

    /**
     * 群推荐
     * @param recommendList
     */
    @Override
    public void showRecommendTeams(final List<RecommendTeamsBean.DataBean> recommendList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recommentGroup.setLayoutManager(gridLayoutManager);
        recommentTeamsAdapter = new RecommentTeamsAdapter(recommendList, getApplicationContext());
        recommentGroup.setAdapter(recommentTeamsAdapter);
        recommentTeamsAdapter.notifyDataSetChanged();
        //点击事件
        recommentTeamsAdapter.setOnClickItemListener(new RecommentTeamsAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                SessionHelper.queryTeamById(getApplicationContext(), recommendList.get(position).team_id);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }
}
