package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.MyProjectAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.MyProjectPresenter;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IMyProjectActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by sdh on 2018/3/8.
 * <p>
 * 我的项目列表
 */

public class MyProjectActivity extends BaseActivity<IMyProjectActivity, MyProjectPresenter> implements IMyProjectActivity {

    private MyProjectAdapter adapter;
    private TextView myProjectNo, myProjectTitle;
    private RecyclerView myProjectrecy;
    private RelativeLayout myproject_back;
    private Button addMyProject;
    private String paySource;

    @Override
    protected void init() {
        myProjectNo = (TextView) findViewById(R.id.my_project_no);
        myProjectrecy = (RecyclerView) findViewById(R.id.myprojectrecy);
        myproject_back = (RelativeLayout) findViewById(R.id.myproject_back);
        addMyProject = (Button) findViewById(R.id.addMyProject);
        myProjectTitle = (TextView) findViewById(R.id.myproject_title);
//        Intent intent = getIntent();
//        String uid = intent.getStringExtra("uid");
        //获取企业项目列表
        String uid = (String) SharedPreferencesUtils.getUtil().getKey(this, "sharedUid", "");
        presenter.showMyProjectList(this, uid);
        //判断是否是通过付款界面过来
        paySource = getIntent().getStringExtra("clickSource");
        if (!StringUtil.isEmpty(paySource) && BizConstant.CLICK_PAY.equals(paySource)) {
            addMyProject.setVisibility(View.GONE);
        } else {
            addMyProject.setVisibility(View.VISIBLE);
        }
        //判断是否企业
//        String isEnter = (String)SharedPreferencesUtils.getUtil().getKey(this,
//                "is_enterprise","");
//        if (!StringUtil.isEmpty(isEnter) && BizConstant.IS_SUC.equals(isEnter)) {
//            addMyProject.setVisibility(View.GONE);
//        } else {
//            addMyProject.setVisibility(View.VISIBLE);
//        }
        //初始化文字
        initTitleAndText();
    }

    void initTitleAndText() {
        String paySource = getIntent().getStringExtra("clickSource");
        if (!StringUtil.isEmpty(paySource) && BizConstant.CLICK_PAY.equals(paySource)) {
            myProjectTitle.setText(R.string.enterprise_collection_list);
        } else if (!StringUtil.isEmpty(paySource) && BizConstant.CLICK_SYB.equals(paySource)) {
            myProjectTitle.setText(R.string.safeguard_pool);
            addMyProject.setText(R.string.syb_apply_ensure);
        } else {
            myProjectTitle.setText(R.string.enterprise_manager);
        }
    }

    @Override
    protected MyProjectPresenter getPresenter() {
        return new MyProjectPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_myproject;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.myproject_back, R.id.addMyProject})
    public void onViewClicked(View view) {
        if (AntiShake.check(view.getId())) {    //判断是否多次点击
            return;
        }
        switch (view.getId()) {
            case R.id.myproject_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.addMyProject:
                if (!StringUtil.isEmpty(paySource) && BizConstant.CLICK_SYB.equals(paySource)) {
                    startActivity(new Intent(MyProjectActivity.this, InsuranceActivity.class));
                } else {
                    startActivity(new Intent(MyProjectActivity.this, AeosActivity.class));
                }
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    @Override
    public void showMyProjectList(final List<ProjectBean.DataBean> projectList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myProjectrecy.setLayoutManager(linearLayoutManager);

        String paySource = getIntent().getStringExtra("clickSource");
        if (!StringUtil.isEmpty(paySource) &&
                (BizConstant.CLICK_PAY.equals(paySource) || BizConstant.CLICK_SYB.equals(paySource))) {
            //重新组装
            List<ProjectBean.DataBean> newProjectList = makeNewProjectList(projectList);
            adapter = new MyProjectAdapter(newProjectList, this);
        } else {
            adapter = new MyProjectAdapter(projectList, this);
        }

        myProjectrecy.setAdapter(adapter);
        adapter.setOnClickItemListener(new MyProjectAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position) {
                //判断是否企业
                String projectId = projectList.get(position).id;
                String gatheringProject = projectList.get(position).enterprise_name;
                String paySource = getIntent().getStringExtra("clickSource");
                if (!StringUtil.isEmpty(paySource) && BizConstant.CLICK_PAY.equals(paySource)) {
                    //跳付款界面
                    Intent gatheringIntent = new Intent(MyProjectActivity.this, OptionContractActivity.class);
                    gatheringIntent.putExtra("projectId", projectId);
                    gatheringIntent.putExtra("gathering_project", gatheringProject);
                    startActivity(gatheringIntent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else if (!StringUtil.isEmpty(paySource) && BizConstant.CLICK_SYB.equals(paySource)) {
                    Intent poolIntent = new Intent(MyProjectActivity.this, EnsurePoolActivity.class);
                    poolIntent.putExtra("projectId", projectId);
                    startActivity(poolIntent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    //跳项目详情 根据ID的状态不一样跳转不一样 审核状态 0审核中1审核通过2审核拒绝3注销
                    String checkStatus = projectList.get(position).check_status;
                    if (!StringUtil.isEmpty(checkStatus)) {
//                        ToastUtils.showToast(MyProjectActivity.this, checkStatus);
                        if (BizConstant.PROJECT_SUC_CHECK_STATUS.equals(checkStatus)) { //审核通过
                            Intent intent = new Intent(MyProjectActivity.this, ManagementActivity.class);
                            intent.putExtra("projectId", projectId);
                            intent.putExtra("project", BizConstant.ALREADY_FAVORITE);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        } else { //审核拒绝
                            Intent intent = new Intent(MyProjectActivity.this, AeosActivity.class);
                            intent.putExtra("checkStatus", checkStatus);
                            intent.putExtra("project", BizConstant.ALREADY_FAVORITE);
                            intent.putExtra("projectId", projectId);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                        }
                    }
//                    startActivity(new Intent(MyProjectActivity.this, EnsurePoolActivity.class));
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    List<ProjectBean.DataBean> makeNewProjectList(List<ProjectBean.DataBean> projectList) {
        List<ProjectBean.DataBean> payProjectList = new ArrayList<ProjectBean.DataBean>();
        for (ProjectBean.DataBean dataBean : projectList) {
            String checkStatus = dataBean.check_status;
            if (checkStatus.equals(BizConstant.PROJECT_SUC_CHECK_STATUS)) {
                payProjectList.add(dataBean);
            }
        }
        return payProjectList;
    }

}