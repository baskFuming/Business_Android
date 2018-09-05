package com.zwonline.top28.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.base.BaseActivity;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.IntegralRecordBean;
import com.zwonline.top28.fragment.FansListFragment;
import com.zwonline.top28.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class MyFansesActivity extends BaseActivity {

    private RelativeLayout fansBack;
    private TabLayout fansTab;
    private ViewPager fansView;
    private List<IntegralRecordBean> fansList = null;
    @Override
    protected void init() {
        initView();

        fansList = new ArrayList<>();
        IntegralRecordBean integralRecordBean = new IntegralRecordBean();
        fansList.add(new IntegralRecordBean("全部粉丝", 1));
        fansList.add(new IntegralRecordBean("已关注", 2));
        fansList.add(new IntegralRecordBean("未关注", 3));
//        fansList.add(new IntegralRecordBean("已沟通", 4));
//        fansList.add(new IntegralRecordBean("未沟通", 5));
        for (int i = 0; i < fansList.size(); i++) {
            fansTab.newTab().setText(fansList.get(i).record_name);
        }
        fansTab.setTabMode(TabLayout.MODE_FIXED);
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fansList);
        fansView.setAdapter(myFragmentAdapter);
        fansTab.setupWithViewPager(fansView);
        fansTab.setTabsFromPagerAdapter(myFragmentAdapter);
        StringUtil.dynamicReflexs(fansTab);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_fanses;
    }

    private void initView() {
        fansBack = (RelativeLayout) findViewById(R.id.fans_back);
        fansTab = (TabLayout) findViewById(R.id.fans_tab);
        fansView = (ViewPager) findViewById(R.id.fans_view);
    }
    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<IntegralRecordBean> hlist;

        public MyFragmentAdapter(FragmentManager fm, List<IntegralRecordBean> list) {
            super(fm);
            this.hlist = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return hlist.get(position).record_name;
        }

        @Override
        public Fragment getItem(int position) {
            return FansListFragment.getInstance(hlist.get(position));
        }

        @Override
        public int getCount() {
            return hlist.size();
        }

    }

    @OnClick(R.id.fans_back)
    public void onViewClicked() {
        finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
