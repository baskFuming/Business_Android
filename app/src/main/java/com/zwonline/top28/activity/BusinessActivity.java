package com.zwonline.top28.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.bean.BannerBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.JZHOBean;
import com.zwonline.top28.bean.RecommendBean;
import com.zwonline.top28.fragment.Home_Fragment_son;
import com.zwonline.top28.presenter.BusinessClassPresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.view.IBusinessClassFra;

import java.util.List;

import butterknife.OnClick;

/**
 * 描述：商机项目页面
 *
 * @author YSG
 * @date 2018/1/16
 */
public class BusinessActivity extends BaseActivity<IBusinessClassFra, BusinessClassPresenter> implements IBusinessClassFra {
    private int pid;
    private RelativeLayout back;
    private TabLayout tablayout;
    private ViewPager viewpager;

    @Override
    protected void init() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.black), 0);
        initData();//查找控件
        Intent intent = getIntent();
        pid = intent.getIntExtra("id", 0);
        presenter.mBusinessClass();
    }

    private void initData() {
        back = (RelativeLayout) findViewById(R.id.back);
        tablayout = (TabLayout) findViewById(R.id.tablayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_business;
    }

    @Override
    protected BusinessClassPresenter getPresenter() {
        return new BusinessClassPresenter(getApplicationContext(),this);
    }

    @Override
    public void showBusinessClassFra(List<BusinessClassifyBean.DataBean> classList) {

        for (int i = 0; i < classList.size(); i++) {
            tablayout.newTab().setText(classList.get(i).c_name);
        }
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), classList);
        viewpager.setAdapter(myFragmentAdapter);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(pid);
        tablayout.setTabsFromPagerAdapter(myFragmentAdapter);
        StringUtil.reflex(tablayout);
    }

    @Override
    public void showSearch(List<BusinessListBean.DataBean> searchList) {

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

    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }


    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<BusinessClassifyBean.DataBean> list;

        public MyFragmentAdapter(FragmentManager fm, List<BusinessClassifyBean.DataBean> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).c_name;
        }

        @Override
        public Fragment getItem(int position) {
            return Home_Fragment_son.getInstance(list.get(position).c_id);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
