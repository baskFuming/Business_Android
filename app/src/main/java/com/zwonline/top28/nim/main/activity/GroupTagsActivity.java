package com.zwonline.top28.nim.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;
import com.netease.nimlib.sdk.team.model.Team;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.RecommendTagsAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.RecommendTagsBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.team.TeamCreateHelper;
import com.zwonline.top28.presenter.GroupTagsPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IGroupTagsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 群标签
 */
public class GroupTagsActivity extends BaseActivity<IGroupTagsActivity, GroupTagsPresenter> implements IGroupTagsActivity {

    private RelativeLayout back;
    private TextView title;
    private TextView tvFunction;
    private RecyclerView groupTagsRecy;
    private TextView create;
    private List<RecommendTeamsBean.DataBean> recommendTagsList;
    private RecommendTagsAdapter adapter;
    private List<RecommendTagsBean.TagListBean> createList;
    private EditText createTagEt;
    private String tagContent;
    private String accid;
    private SharedPreferencesUtils sp;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private boolean flag = false;
    private String tag;
    private String alter;
    private String teamId;
    private Team team;

    @Override
    protected void init() {
        sp = SharedPreferencesUtils.getUtil();
        recommendTagsList = new ArrayList<>();
        createList = new ArrayList<>();

        accid = getIntent().getStringExtra("account");
        tag = getIntent().getStringExtra("tags");
        alter = getIntent().getStringExtra("alter");//是否是修改
        teamId = getIntent().getStringExtra("team_id");
        if (StringUtil.isNotEmpty(teamId)) {
            team = NimUIKit.getTeamProvider().getTeamById(teamId);
        }
        initView();
        presenter.recommondGroupTags(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        groupTagsRecy.setLayoutManager(gridLayoutManager);
        adapter = new RecommendTagsAdapter(recommendTagsList, this);
        groupTagsRecy.setAdapter(adapter);
    }

    @Override
    protected GroupTagsPresenter getPresenter() {
        return new GroupTagsPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_group_tags;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        tvFunction = (TextView) findViewById(R.id.tv_function);
        groupTagsRecy = (RecyclerView) findViewById(R.id.group_tags_recy);
        create = (TextView) findViewById(R.id.create);
        createTagEt = (EditText) findViewById(R.id.create_tag_et);
        title.setText("设置群组标签");
        if (StringUtil.isNotEmpty(alter) && alter.equals(BizConstant.IS_SUC)) {
            tvFunction.setText("保存");
        } else {
            tvFunction.setText("下一步");
        }

    }

    /**
     * 群推荐标签
     *
     * @param tagsList
     */
    @Override
    public void showRecommendTeamTag(List<RecommendTeamsBean.DataBean> tagsList) {
        recommendTagsList.clear();
        recommendTagsList.addAll(tagsList);

        try {
            if (StringUtil.isNotEmpty(alter) && alter.equals(BizConstant.IS_SUC)) {

                JSONObject obj = new JSONObject(tag);
                org.json.JSONArray tngou = obj.getJSONArray("tag_list");//得到数组，再一个个遍历
                for (int i = 0; i < tngou.length(); i++) {
                    RecommendTagsBean.TagListBean createBean = new RecommendTagsBean.TagListBean();
                    JSONObject object = tngou.getJSONObject(i);
                    createBean.name = object.getString("name");
                    createBean.tag_id = object.getString("tag_id");
                    createList.add(createBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<RecommendTagsBean.TagListBean> customList = new ArrayList<>();
        customList.clear();
        customList.addAll(createList);
        /**
         * 判断是否已经设置过标签
         */
        if (createList.size() > 0) {
            //循环遍历createlist查找是否有相同tags_id
            for (int i = 0; i < createList.size(); i++) {
                for (int j = 0; j < recommendTagsList.size(); j++) {
                    //判断是有相同的tag_id
                    if (createList.get(i).tag_id.equals(recommendTagsList.get(j).tag_id)) {

                        recommendTagsList.get(j).did_isChecked = BizConstant.IS_SUC;
                        //新建一个listview有相同的给他remove掉
                        for (int k = 0; k < customList.size(); k++) {
                            if (createList.get(i).tag_id.equals(customList.get(k).tag_id)) {
                                customList.remove(k);
                            }
                        }
                    }
                }

            }
            //把没有的标签循环添加
            if (customList.size() > 0) {
                for (int i = 0; i < customList.size(); i++) {
                    RecommendTeamsBean.DataBean bean = new RecommendTeamsBean.DataBean();
                    bean.tag_id = customList.get(i).tag_id;
                    bean.name = customList.get(i).name;
                    bean.did_isChecked = BizConstant.IS_SUC;
                    recommendTagsList.add(bean);
                }
                ToastUtils.showToast(getApplicationContext(), customList.size() + "");
            }

            adapter.notifyDataSetChanged();
        }

        adapter.tagsSetOnclick(new RecommendTagsAdapter.TagsInterface() {
            @Override
            public void onclick(View view, int position, CheckBox choose_like) {
                RecommendTagsBean.TagListBean createBean = new RecommendTagsBean.TagListBean();
                if (choose_like.isChecked()) {
                    if (createList.size() < 3) {
                        createBean.name = recommendTagsList.get(position).name;
                        createBean.tag_id = recommendTagsList.get(position).tag_id;
                        createList.add(createBean);
                        recommendTagsList.get(position).did_isChecked = BizConstant.IS_SUC;
//                        Gson gson = new Gson();
//                        String s = gson.toJson(createList);
//                        ToastUtils.showToast(getApplicationContext(), s);
                    } else {
                        choose_like.setChecked(false);
                        recommendTagsList.get(position).did_isChecked = BizConstant.IS_FAIL;
                        ToastUtils.showToast(getApplicationContext(), "最多选三个标签");
                    }

                } else {
                    if (createList.size() > 0) {
                        for (int i = 0; i < createList.size(); i++) {
                            if (recommendTagsList.get(position).tag_id.equals(createList.get(i).tag_id)) {
                                createList.remove(i);
                                choose_like.setTextColor(Color.parseColor("#ff2b2b"));
                                recommendTagsList.get(position).did_isChecked = BizConstant.IS_FAIL;
//                                Gson gson = new Gson();
//                                String s = gson.toJson(createList);
//                                ToastUtils.showToast(getApplicationContext(), s);
                            }
                        }
                    } else {
                        recommendTagsList.get(position).did_isChecked = BizConstant.IS_FAIL;
                    }

                }

                adapter.notifyDataSetChanged();
            }
        });
        /**
         * 删除自定义标签
         */
        adapter.deleteSetOnclick(new RecommendTagsAdapter.DeleteInterface() {
            @Override
            public void onclick(View view, int position, CheckBox choose_like) {
//                ToastUtils.showToast(getApplicationContext(), recommendTagsList.size() + "");
                for (int i = 0; i < createList.size(); i++) {
                    if (recommendTagsList.get(position).tag_id.equals(createList.get(i).tag_id)) {
                        createList.remove(i);
                    }
                }
                recommendTagsList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.notifyDataSetChanged();
    }

    /**
     * 添加标签
     *
     * @param attentionBean
     */
    @Override
    public void showAddTeamTag(AttentionBean attentionBean) {
        RecommendTeamsBean.DataBean bean = new RecommendTeamsBean.DataBean();

        RecommendTagsBean.TagListBean createBeans = new RecommendTagsBean.TagListBean();

        if (attentionBean.status == 1 && StringUtil.isNotEmpty(attentionBean.data.tag_id)) {
            bean.tag_id = attentionBean.data.tag_id;
            bean.name = tagContent;
            bean.did_isChecked = BizConstant.IS_SUC;

            recommendTagsList.add(bean);
            createBeans.name = tagContent;
            createBeans.tag_id = attentionBean.data.tag_id;
            createList.add(createBeans);
            createTagEt.setText("");
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast(getApplicationContext(), attentionBean.msg);
        }


    }


    @OnClick({R.id.back, R.id.create, R.id.tv_function})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:

                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.create:
                tagContent = createTagEt.getText().toString().trim();
                if (createList.size() < 3) {
                    if (StringUtil.isNotEmpty(tagContent)) {
                        //for循环查找有没有相同的标签
                        for (int i = 0; i < recommendTagsList.size(); i++) {
                            if (tagContent.equals(recommendTagsList.get(i).name)) {
                                flag = true;//如果有相同的flag为true
                            }
                        }
                        //flag来判断是否有相同的标签来判断要不要调接口
                        if (flag) {
                            flag = false;
                            ToastUtils.showToast(getApplicationContext(), "标签已存在");
                        } else {
                            presenter.AddTeamTag(GroupTagsActivity.this, tagContent);
                        }

                    } else {
                        ToastUtils.showToast(getApplicationContext(), "请输入标签名字");
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "最多能选中三个标签");
                }

                break;
            case R.id.tv_function:
                if (createList.size() > 0) {
                    Gson gson = new Gson();
                    String s = gson.toJson(createList);
                    String ss = "{" + "\"tag_list\"" + ":" + s + "}";
                    if (StringUtil.isNotEmpty(alter) && alter.equals(BizConstant.IS_SUC)) {
                        team.setExtension(ss);
                        finish();
                        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                    } else {
                        sp.insertKey(getApplicationContext(), "tags", ss);
                        ArrayList<String> memberAccounts = new ArrayList<>();
                        memberAccounts.add(accid);
                        ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
                        NimUIKit.startContactSelector(GroupTagsActivity.this, option, REQUEST_CODE_ADVANCED);// 创建群
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), "至少选择一个标签");
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //创建群回传
            if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                TeamCreateHelper.createAdvancedTeam(GroupTagsActivity.this, selected, new RequestCallback<CreateTeamResult>() {
                    @Override
                    public void onSuccess(CreateTeamResult createTeamResult) {
                        final Team team = createTeamResult.getTeam();
                        NimUIKit.startTeamSession(GroupTagsActivity.this, team.getId());
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {
                    }

                    @Override
                    public void onException(Throwable throwable) {
                    }
                });
            }
        }
    }

}
