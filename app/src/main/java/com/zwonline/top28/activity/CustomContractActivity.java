package com.zwonline.top28.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.AddClauseAdapter;
import com.zwonline.top28.adapter.SignContractAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.bean.AddContractBean;
import com.zwonline.top28.bean.SignContractBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.CustomContractPresenter;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.ScrollLinearLayoutManager;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.utils.popwindow.ContractPopuWindow;
import com.zwonline.top28.view.ICustomContractActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;

/**
 * 描述：添加合同
 *
 * @author YSG
 * @date 2018/4/7
 */
public class CustomContractActivity extends BaseActivity<ICustomContractActivity, CustomContractPresenter> implements ICustomContractActivity {

    private RelativeLayout addContractBack;
    private LinearLayout contractTimeLinear;
    private LinearLayout signContractLinear;
    private LinearLayout contractLinear;
    private LinearLayout linearcount;
    private EditText contractName;
    private TextView contractStartTime;
    private TextView contractStopTime;
    private TextView ensurepoolNum;
    private TextView integralnum;
    private TextView contractBenginTime;
    private TextView contractEndTime;
    private ImageView addClause;
    private TimePickerView pvTime;
    private TextView contractTitleTv;
    private ContractPopuWindow contractPopuWindow;
    private Context context;
    private RecyclerView clauseListView;
    private RecyclerView signRecy;
    private List<AddClauseBean.DataBean.TermsBean> clauseList;
    private List<AddClauseBean.DataBean.TermsBean> customList;
    private EditText clauseName;
    private EditText clauseRatio;
    private EditText clauseContent;
    private Button save;
    private Button signSave;
    private int count = 0;
    private AddClauseAdapter addClauseAdapter;
    private String contractId;
    private String projectId;
    private String contractTitle;
    private String enterprise_name;
    private String cid;
    private SignContractAdapter signContractAdapter;
    private String orderInfoId;


    @Override
    protected void init() {
        context = CustomContractActivity.this;
        initView();
        Intent intent = getIntent();
        projectId = getIntent().getStringExtra("projectId");
        contractId = intent.getStringExtra("contractId");
        orderInfoId = intent.getStringExtra("orderinfo_id");
        cid = intent.getStringExtra("cid");
        enterprise_name = getIntent().getStringExtra("enterprise_name");
        clauseList = new ArrayList<>();
        customList = new ArrayList<>();
        if (StringUtil.isNotEmpty(cid) && cid.equals(BizConstant.MOBAN)) {
            presenter.mCustomContractModel(this, contractId);
        } else if (StringUtil.isNotEmpty(cid) && cid.equals(BizConstant.CUSTOM_CONTRACT)) {
            Log.v("custom", getString(R.string.custom_contract));
            listData(customList);
        } else if (StringUtil.isNotEmpty(cid) && cid.equals(BizConstant.SIGN_CONTRACT)) {
            //扫一扫获取合同详情

            linearcount.setVisibility(View.GONE);
            contractTimeLinear.setVisibility(View.GONE);
            signContractLinear.setVisibility(View.VISIBLE);
            addClause.setVisibility(View.GONE);
            contractLinear.setVisibility(View.VISIBLE);
            signRecy.setVisibility(View.VISIBLE);
            clauseListView.setVisibility(View.GONE);
            save.setText(getString(R.string.consent_sign_contract));
            save.setVisibility(View.GONE);
            signSave.setVisibility(View.VISIBLE);
            presenter.mSignContract(this, orderInfoId);
        }
    }


    @Override
    protected CustomContractPresenter getPresenter() {
        return new CustomContractPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_custom_contract;
    }

    //初始化组件
    private void initView() {
        addContractBack = (RelativeLayout) findViewById(R.id.add_contract_back);
        contractName = (EditText) findViewById(R.id.contract_name);
        contractStartTime = (TextView) findViewById(R.id.contract_start_time);
        contractStopTime = (TextView) findViewById(R.id.contract_stop_time);
        addClause = (ImageView) findViewById(R.id.add_clause);
        clauseListView = (RecyclerView) findViewById(R.id.clause_listview);
        signRecy = (RecyclerView) findViewById(R.id.sign_listview);
        save = (Button) findViewById(R.id.save);
        signSave = (Button) findViewById(R.id.sign_save);
        contractTitleTv = (TextView) findViewById(R.id.contract_title_tv);
        contractTimeLinear = (LinearLayout) findViewById(R.id.contract_time_linear);
        signContractLinear = (LinearLayout) findViewById(R.id.sign_contract_linear);
        contractLinear = (LinearLayout) findViewById(R.id.contract_linear);
        linearcount = (LinearLayout) findViewById(R.id.linearcount);
        ensurepoolNum = (TextView) findViewById(R.id.ensurepool_num);
        integralnum = (TextView) findViewById(R.id.integral_num);
        contractBenginTime = (TextView) findViewById(R.id.contract_bengin_time);
        contractEndTime = (TextView) findViewById(R.id.contract_end_time);

    }


    @OnClick({R.id.add_contract_back, R.id.start_time_relat, R.id.stop_time_relat, R.id.add_clause, R.id.save, R.id.sign_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_contract_back:
                finish();
                overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
                break;
            case R.id.start_time_relat:
                timeStartPicker();
                break;
            case R.id.stop_time_relat:
                timeStopPicker();
                break;
            case R.id.add_clause://添加条款
                contractPopuWindow = new ContractPopuWindow(CustomContractActivity.this, listener);
                contractPopuWindow.showAtLocation(CustomContractActivity.this.findViewById(R.id.scrollview_contract), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.save://保存
                String contractBengin = contractStartTime.getText().toString();
                String contractEnd = contractStopTime.getText().toString();
                String contract = contractName.getText().toString().trim();

                if (StringUtil.isNotEmpty(contract)) {
                    if (StringUtil.isNotEmpty(contractStartTime.getText().toString())) {
                        if (StringUtil.isNotEmpty(contractStopTime.getText().toString())) {
                            String begin[] = contractBengin.split("-");
                            Long beginTime = Long.valueOf(begin[0] + begin[1] + begin[2]);
                            String end[] = contractEnd.split("-");
                            Long endTime = Long.valueOf(end[0] + end[1] + end[2]);
                            if (beginTime <= endTime) {
                                saveData(contract, contractBengin, contractEnd);
                            } else {
                                ToastUtils.showToast(this, getString(R.string.start_not_less_than_end));
                            }
                        } else {
                            ToastUtils.showToast(this, getString(R.string.end_not_empty));
                        }
                    } else {
                        ToastUtils.showToast(this,  getString(R.string.start_not_empty));
                    }
                } else {
                    ToastUtils.showToast(this, getString(R.string.contract_title_not_empty));
                }
                break;
            case R.id.sign_save://签署合同
                //扫码签署合同跳转付款
                Intent intent = new Intent(CustomContractActivity.this, PaymentActivity.class);
                intent.putExtra("orderinfo_id", orderInfoId);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                break;
        }
    }

    /**
     * 保存添加合同
     */
    public void saveData(String contractName, String contractBengin, String contractEnd) {

//                flyRouteBean=initdata(flyRouteBean);//根据Bean类初始化一个需要提交的数据类
        AddClauseBean.DataBean.TermsBean termsBean = new AddClauseBean.DataBean.TermsBean();
        Gson gson = new Gson();
        //判断是自定义合同还是模板和已有合同
        if (StringUtil.isNotEmpty(cid) && cid.equals(BizConstant.CUSTOM_CONTRACT)) {
            //自定义合同
            //String route = gson.toJson(customList);//通过Gson将Bean转化为Json字符串形式
            int count = 0;
            for (int i = 0; i < customList.size(); i++) {
                int percent = Integer.parseInt(customList.get(i).percent);
                count += percent;
            }
            if (count == 100) {
                presenter.mAddContract(this, projectId, contractId, contractName, contractBengin, contractEnd, gson.toJson(customList));
            } else {
                showNormalDialogFollow();
            }
        } else {
            int count = 0;
            for (int i = 0; i < clauseList.size(); i++) {
                int percent = Integer.parseInt(clauseList.get(i).percent);
                count += percent;
            }
            if (count == 100) {
                presenter.mAddContract(this, projectId, contractId, contractName, contractBengin, contractEnd, gson.toJson(clauseList));
            } else {
                showNormalDialogFollow();
            }
        }
    }

    /**
     * popwindow弹窗
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {//关闭
                case R.id.clause_close:
                    contractPopuWindow.dismiss();
                    contractPopuWindow.backgroundAlpha(CustomContractActivity.this, 1f);
                    break;
                case R.id.sure://保存
                    View contentView = contractPopuWindow.getContentView();
                    clauseName = (EditText) contentView.findViewById(R.id.clause_name);
                    clauseRatio = (EditText) contentView.findViewById(R.id.clause_ratio);
                    clauseContent = (EditText) contentView.findViewById(R.id.clause_content);
                    clauseName.setSelection(clauseName.getText().length());
                    if (StringUtil.isNotEmpty(clauseName.getText().toString())) {
                        if (StringUtil.isNotEmpty(clauseRatio.getText().toString())) {
                            if (StringUtil.isNotEmpty(clauseContent.getText().toString())) {
                                contractPopuWindow.dismiss();
                                contractPopuWindow.backgroundAlpha(CustomContractActivity.this, 1f);
                                if (StringUtil.isNotEmpty(cid) && cid.equals(BizConstant.CUSTOM_CONTRACT)) {
                                    //添加自带默认动画
                                    addClauseAdapter.addData(customList.size(), clauseName.getText().toString(), clauseRatio.getText().toString(), clauseContent.getText().toString());
                                    addClauseAdapter.notifyDataSetChanged();
                                } else {
                                    //添加自带默认动画
                                    addClauseAdapter.addData(clauseList.size(), clauseName.getText().toString(), clauseRatio.getText().toString(), clauseContent.getText().toString());
                                    addClauseAdapter.notifyDataSetChanged();
                                }
                            } else {
                                ToastUtils.showToast(CustomContractActivity.this, getString(R.string.clause_content_not_empty));
                            }
                        } else {
                            ToastUtils.showToast(CustomContractActivity.this, getString(R.string.clause_proportion_not_empty));
                        }
                    } else {
                        ToastUtils.showToast(CustomContractActivity.this, getString(R.string.clause_title_not_empty));
                    }


                    break;
            }
        }
    };

    @Override
    public void showCustomContract(List<AddClauseBean.DataBean.TermsBean> addList) {
        clauseList.addAll(addList);
        listData(clauseList);
    }

    /**
     * 合同详情请求数据并赋值
     *
     * @param addClauseBean
     */
    @Override
    public void showCustomContracts(AddClauseBean addClauseBean) {
        contractTitle = addClauseBean.data.title;
        String beginDate = addClauseBean.data.begin_date;
        String endDate = addClauseBean.data.end_date;
        contractStartTime.setText(beginDate);
        contractStopTime.setText(endDate);
        contractName.setText(contractTitle);
        contractName.setSelection(contractName.getText().length());
    }

    /**
     * 添加合同请求数据
     * 请求成功之后跳转收款界面
     *
     * @param addContractBean
     */
    @Override
    public void showAddContract(AddContractBean addContractBean) {
        if (addContractBean.status == 1) {
            Intent intent = new Intent(CustomContractActivity.this, GatheringActivity.class);
            intent.putExtra("projectId", projectId);
            intent.putExtra("enterprise_name", enterprise_name);
            intent.putExtra("contract_id", addContractBean.data.contract_id);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        } else {
            ToastUtils.showToast(this, addContractBean.msg);
        }
    }

    /**
     * 扫码签署合同详情
     *
     * @param signContractBean
     */
    @Override
    public void showSignContract(List<SignContractBean.DataBean.TermsBean> signContractBean) {
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        signRecy.setLayoutManager(scrollLinearLayoutManager);
        signContractAdapter = new SignContractAdapter(signContractBean, this);
        signRecy.setAdapter(signContractAdapter);
        signContractAdapter.notifyDataSetChanged();
    }

    /**
     * 扫码签署合同显示时间
     *
     * @param signContractBean
     */
    @Override
    public void showSinContractTime(SignContractBean signContractBean) {
        contractTitleTv.setText(signContractBean.data.title);
        ensurepoolNum.setText(signContractBean.data.insurance_pool_points);
        integralnum.setText(signContractBean.data.enterprise_points);
        contractBenginTime.setText(signContractBean.data.begin_date);
        contractEndTime.setText(signContractBean.data.end_date);
    }

    //条款列表数据
    private void listData(List<AddClauseBean.DataBean.TermsBean> clauseList) {
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(this);
        scrollLinearLayoutManager.setScrollEnabled(false);
        clauseListView.setLayoutManager(scrollLinearLayoutManager);
        addClauseAdapter = new AddClauseAdapter(clauseList, context);
        clauseListView.setAdapter(addClauseAdapter);
        //      添加动画
        clauseListView.setItemAnimator(new DefaultItemAnimator());
    }


    //开始时间日期
    public void timeStartPicker() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(CustomContractActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
//                ToastUtils.showToast(CustomContractActivity.this, format);
                contractStartTime.setText(format);
            }
        }).build();
        pvTime.show();
    }

    //截止时间日期
    public void timeStopPicker() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(CustomContractActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
//                ToastUtils.showToast(CustomContractActivity.this, format);
                contractStopTime.setText(format);
            }
        }).build();
        pvTime.show();
    }

    /**
     * 点击输入框其他地方软件盘隐藏
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (LanguageUitils.isShouldHideInput(v, ev)) {
                if (LanguageUitils.hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showNormalDialogFollow() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("普通的对话框的标题");
        builder.setMessage(getString(R.string.contract_should_proportion));
        builder.setPositiveButton(getString(R.string.toast_know), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            System.out.println("按下了back键   onKeyDown()");
//            return false;
//        }else {
//            return super.onKeyDown(keyCode, event);
//        }
//
//    }
}