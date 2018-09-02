package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.adapter.BusinessAdapter;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BannerBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.JZHOBean;
import com.zwonline.top28.bean.RecommendBean;
import com.zwonline.top28.presenter.BusinessClassPresenter;
import com.zwonline.top28.utils.ItemDecoration;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.click.AntiShake;
import com.zwonline.top28.view.IBusinessClassFra;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：商机搜索
 * @author YSG
 * @date 2018/1/17
 */
public class BusinessSearchActivity extends BaseActivity<IBusinessClassFra, BusinessClassPresenter> implements IBusinessClassFra {
    private RecyclerView busearchRecy;
    private TextView no;
    private RelativeLayout back;
    private int page = 1;
    private SharedPreferencesUtils sp;
    private boolean islogins;

    @Override
    protected void init() {
        no = (TextView) findViewById(R.id.no);
        back = (RelativeLayout) findViewById(R.id.back);
        busearchRecy = (RecyclerView) findViewById(R.id.busearch_recy);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        presenter.mSearch(String.valueOf(page), title);
        sp = SharedPreferencesUtils.getUtil();
        islogins = (boolean) sp.getKey(this, "islogin", false);
    }

    @Override
    protected BusinessClassPresenter getPresenter() {
        return new BusinessClassPresenter(getApplicationContext(),this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_business_search;
    }

    @Override
    public void showBusinessClassFra(List<BusinessClassifyBean.DataBean> classList) {

    }

    @Override
    public void showSearch(final List<BusinessListBean.DataBean> searchList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BusinessSearchActivity.this, LinearLayoutManager.VERTICAL, false);
        busearchRecy.setLayoutManager(linearLayoutManager);
        busearchRecy.addItemDecoration(new ItemDecoration(BusinessSearchActivity.this));
        BusinessAdapter adapter = new BusinessAdapter(searchList, BusinessSearchActivity.this);
        busearchRecy.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickItemListener(new BusinessAdapter.OnClickItemListener() {
            @Override
            public void setOnItemClick(int position,View view) {
                if (AntiShake.check(view.getId())) {    //判断是否多次点击
                    return;
                }
                if (islogins) {
                    Intent intent = new Intent(BusinessSearchActivity.this, CompanyActivity.class);
                    intent.putExtra("uid", searchList.get(position).uid);
                    intent.putExtra("pid",searchList.get(position).id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                } else {
                    Toast.makeText(BusinessSearchActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BusinessSearchActivity.this, WithoutCodeLoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }
            }
        });
    }

    @Override
    public void showBanner(List<BannerBean.DataBean> bannerList) {

    }

    @Override
    public void showRecommend(List<RecommendBean.DataBean> recommendList) {

    }

    @Override
    public void showData(List<BusinessListBean.DataBean> list) {

    }

    @Override
    public void showJZHO(List<JZHOBean.DataBean> JZHOlist) {

    }

    @Override
    public void showErro() {
        if (this == null) {
            return;
        }
        no.setVisibility(View.VISIBLE);
        busearchRecy.setVisibility(View.GONE);
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
