package com.zwonline.top28.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.zwonline.top28.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/6 13:49
 */

public abstract class BaseFragment<V,T extends BasePresenter<V>> extends Fragment  {

    public T presenter;
    protected View mContentView;
    public Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContentView = inflater.inflate(setLayouId(),container,false);
        StatusBarUtil.setColor(getActivity(),getResources().getColor(R.color.reded),0);

        unbinder = ButterKnife.bind(this, mContentView);
        presenter=setPresenter();
        init(mContentView);
        return mContentView;
    }
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//
//    }

    protected abstract void init(View view);

    protected abstract T setPresenter();

    protected abstract int setLayouId();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
    }
}
