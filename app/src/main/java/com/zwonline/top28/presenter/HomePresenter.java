package com.zwonline.top28.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.HomeClassBean;
import com.zwonline.top28.model.HomeModel;
import com.zwonline.top28.view.IHomeFragment;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by YU on 2017/12/9.
 */

public class HomePresenter extends BasePresenter<IHomeFragment> {
    public HomeModel homeModel;
    public IHomeFragment iHomeFragment;

    public HomePresenter(IHomeFragment iHomeFragment) {
        this.iHomeFragment = iHomeFragment;
        homeModel = new HomeModel();
    }

    //首页
    public void mHomePage(Context context, String page, String cate_id) {
        try {
            Flowable<HomeClassBean> flowable = homeModel.homePage(context, page, cate_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HomeClassBean>() {
                        @Override
                        public void onNext(HomeClassBean homeBean) {

                            if (homeBean != null && homeBean.data != null && homeBean.data.size() > 0) {
                                Log.i("xxxx", homeBean.msg);
                                iHomeFragment.showHomeData(homeBean.data);
                                iHomeFragment.showHomeclass(homeBean);
                            } else {
                                iHomeFragment.onErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeFragment.onErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //首页
    public void mHomeRecommend(Context context, String page) {
        try {
            Flowable<HomeClassBean> flowable = homeModel.homePage(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HomeClassBean>() {
                        @Override
                        public void onNext(HomeClassBean homeBean) {

                            if (homeBean != null && homeBean.data != null && homeBean.data.size() > 0) {
                                Log.i("xxxx", homeBean.msg);
                                iHomeFragment.showHomeData(homeBean.data);
                                iHomeFragment.showHomeclass(homeBean);
                            } else {
                                iHomeFragment.onErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeFragment.onErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //搜索
    public void mSearch(Context context, String title, final String page) {
        try {
            Flowable<HomeClassBean> flowable = homeModel.Search(context,title, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HomeClassBean>() {
                        @Override
                        public void onNext(HomeClassBean homeBean) {
                            if (Integer.parseInt(page) == 1) {
                                if (homeBean.data.size() > 0) {
                                    iHomeFragment.showSearch(homeBean.data);
                                    iHomeFragment.showHomeclass(homeBean);
                                    iHomeFragment.showMyCollect(true);
                                } else {
                                    iHomeFragment.showMyCollect(false);
                                }
                            } else {
                                if (homeBean.data.size() > 0) {
                                    iHomeFragment.showSearch(homeBean.data);
                                } else {
                                    iHomeFragment.onErro();
                                }
                            }


                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeFragment.onErro();
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
