package com.zwonline.top28.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.zwonline.top28.R;
import com.zwonline.top28.activity.MainActivity;
import com.zwonline.top28.activity.YangShiActivity;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.base.BasesFragment;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.presenter.HomeClassPresenter;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.view.IHomeClassFrag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 1.视频的页面
 * 2.@authorDell
 * 3.@date2017/12/6 14:27
 */

public class ExamineFragment extends BasesFragment<IHomeClassFrag, HomeClassPresenter> implements IHomeClassFrag {


    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.video_recy)
    RecyclerView videoRecy;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    protected void init(View view) {
        if (isAdded()) {
            getResources().getString(R.string.app_name);
        }
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.black), 0);
//        startActivity(new Intent(getActivity(), YangShiActivity.class));
//        getActivity().finish();
//        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        presenter.mHomeClass(getActivity());
    }

    @Override
    public void showHomeClass(List<HomeBean.DataBean> classList) {
        for (int i = 0; i < classList.size(); i++) {
            tablayout.newTab().setText(classList.get(i).cate_name);
        }
        tablayout.newTab().setText(R.string.center_recommend);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if (!isAdded()) {
            return;
        }

        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager(), classList);
        viewpager.setAdapter(myFragmentAdapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabsFromPagerAdapter(myFragmentAdapter);
        StringUtil.reflex(tablayout);
    }

    @Override
    public void showHomesClass(HomeBean homeBean) {

    }

    @Override
    public void onStart() {
        super.onStart();
//        startActivity(new Intent(getActivity(), YangShiActivity.class));
//        getActivity().finish();
//        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        if (isAdded()) {
            getResources().getString(R.string.app_name);
        }
    }

    @Override
    public void showErro() {

    }


    @Override
    protected HomeClassPresenter setPresenter() {
        return new HomeClassPresenter(this,getActivity());
    }

    @Override
    protected int setLayouId() {
        return R.layout.examinefragment;
    }


    //适配Fragment
    class MyFragmentAdapter extends FragmentPagerAdapter {
        private List<HomeBean.DataBean> hlist;

        public MyFragmentAdapter(FragmentManager fm, List<HomeBean.DataBean> list) {
            super(fm);
            if (hlist == null) {
                hlist = new ArrayList<>();
            }
            hlist.addAll(list);
            HomeBean.DataBean bean = new HomeBean.DataBean("300", getString(R.string.center_recommend));

            hlist.add(0, bean);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return hlist.get(position).cate_name;
        }


        @Override
        public int getCount() {
            return hlist.size();
        }

        @Override
        public Fragment getItem(int position) {
            return VideoFragmentSon.getInstance(hlist.get(position).cate_id);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onStop() {
        super.onStop();
        JCVideoPlayer.releaseAllVideos();
    }



}
