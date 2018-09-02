package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.model.HomeModel;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.view.IHomeClassFrag;
import com.zwonline.top28.base.BasePresenter;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class HomeClassPresenter extends BasePresenter<IHomeClassFrag> {
    private HomeModel homeModel;
    private IHomeClassFrag iHomeClassFrag;
    private  SharedPreferencesUtils sp;
    public HomeClassPresenter(IHomeClassFrag iHomeClassFrag,Context context) {
        homeModel = new HomeModel();
        this.iHomeClassFrag = iHomeClassFrag;
    }

    //首页分类信息
    public void mHomeClass(final Context context) {
        try {
            Flowable<HomeBean> flowable = homeModel.homeClass(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HomeBean>() {
                        @Override
                        public void onNext(HomeBean homeBean) {
                            if (homeBean != null && homeBean.data != null && homeBean.data.size() > 0) {
                                iHomeClassFrag.showHomeClass(homeBean.data);
                                iHomeClassFrag.showHomesClass(homeBean);
                            } else {
                                iHomeClassFrag.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeClassFrag.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
