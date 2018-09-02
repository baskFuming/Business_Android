package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zwonline.top28.R;
import com.zwonline.top28.adapter.BusinessCirclerAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.presenter.BusnicCirclerPresenter;
import com.zwonline.top28.view.BusincCirclerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BusinessCirclActivity extends BaseActivity<BusincCirclerActivity,BusnicCirclerPresenter> implements BusincCirclerActivity {
    @BindView(R.id.search_message)
    RelativeLayout re_search;
    @BindView(R.id.add_fuction)
    RelativeLayout re_add;
    @BindView(R.id.business_cicler)
    XRecyclerView  xy_bus_circl;
    private List<BusinessCircleBean.DataBean> dlist;
    private BusinessCirclerAdapter businessCirclerAdapter;
    @Override
    protected void init() {
          dlist  = new ArrayList<>();
          presenter.BusincComment(this);
          recyclerViewData();
    }
    //加载数据
    private void recyclerViewData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        xy_bus_circl.setLayoutManager(linearLayoutManager);
        businessCirclerAdapter = new BusinessCirclerAdapter(this,dlist);
        xy_bus_circl.setAdapter(businessCirclerAdapter);
        businessCirclerAdapter.setOnClickItemListener(new BusinessCirclerAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(View view, int position) {
                Intent intent = new Intent(BusinessCirclActivity.this,AddGetRelistActivity.class);
                intent.putExtra("title",dlist.get(position).title);
                intent.putExtra("item_id",dlist.get(position).item_id+"");
                startActivity(intent);
                Toast.makeText(BusinessCirclActivity.this,position+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected BusnicCirclerPresenter getPresenter() {
        return new BusnicCirclerPresenter(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_business_circl;
    }

    @Override
    public void BusincNoLoadMore() {

    }

    @Override
    public void showBusincPro(List<BusinessCircleBean.DataBean.ListBean> bList) {
    }

    @Override
    public void showBusincDateList(List<AddFollowBean.DataBean.ListBean> issueList) {

    }

    @Override
    public void showAttention(AttentionBean attentionBean) {

    }

    @Override
    public void showUnAttention(AttentionBean attentionBean) {

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
        businessCirclerAdapter.changeData(issueList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.BusincComment(this);
    }
}
