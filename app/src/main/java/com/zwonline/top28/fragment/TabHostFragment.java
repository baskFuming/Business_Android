package com.zwonline.top28.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zwonline.top28.base.BaseFragment;
import com.zwonline.top28.base.BasePresenter;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public abstract class TabHostFragment<V,T extends BasePresenter<V>> extends BaseFragment<V,T>{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView!=null){
            ViewGroup parent = (ViewGroup)mContentView.getParent();
            if(parent!=null){
                parent.removeView(mContentView);
            }
            return mContentView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
