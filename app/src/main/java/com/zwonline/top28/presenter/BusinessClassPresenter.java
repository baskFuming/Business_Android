package com.zwonline.top28.presenter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.BannerBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.JZHOBean;
import com.zwonline.top28.bean.RecommendBean;
import com.zwonline.top28.model.BusinessModel;
import com.zwonline.top28.view.IBusinessClassFra;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc 商机分类
 * @date ${Date}
 */
public class BusinessClassPresenter extends BasePresenter<IBusinessClassFra> {
    private BusinessModel businessModel;
    private IBusinessClassFra iBusinessClassFra;
    private  Context context;
    public BusinessClassPresenter(Context context,IBusinessClassFra iBusinessClassFra) {
        this.iBusinessClassFra = iBusinessClassFra;
        businessModel = new BusinessModel();
        this.context=context;

    }

    //商机分类
    public void mBusinessClass() {
        try {
            Flowable<BusinessClassifyBean> flowable = businessModel.businessClass(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BusinessClassifyBean>() {
                        @Override
                        public void onNext(BusinessClassifyBean businessClassifyBean) {
                            iBusinessClassFra.showBusinessClassFra(businessClassifyBean.data);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //商机搜索
    public void mSearch(String page, String title) {
        try {
            Flowable<BusinessListBean> flowable = businessModel.buSearch(context,page, title);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BusinessListBean>() {
                        @Override
                        public void onNext(BusinessListBean businessListBean) {
                            if (businessListBean != null && businessListBean.data != null && businessListBean.data.size() > 0) {
                                iBusinessClassFra.showSearch(businessListBean.data);
                            } else {
                                iBusinessClassFra.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iBusinessClassFra.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //商机轮播图
    public void mBanner(Context context) {
        try {
            Flowable<BannerBean> flowable = businessModel.businessBanner(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<BannerBean>(context) {
                        @Override
                        protected void onBaseNext(BannerBean bannerBean) {
                            if (bannerBean != null && bannerBean.data != null && bannerBean.data.size() > 0) {
                                iBusinessClassFra.showBanner(bannerBean.data);
                            } else {
                                iBusinessClassFra.showErro();
                            }
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            iBusinessClassFra.showErro();
                            return true;
                        }

                        @Override
                        protected void onBaseComplete() {

                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //商机推荐项目
    public void mRecommend( ) {
        try {
            Flowable<RecommendBean> flowable = businessModel.bRecommend(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RecommendBean>() {
                        @Override
                        public void onNext(RecommendBean recommendBean) {
                            if (recommendBean != null && recommendBean.data != null && recommendBean.data.size() > 0) {
                                iBusinessClassFra.showRecommend(recommendBean.data);
                            } else {
                                iBusinessClassFra.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iBusinessClassFra.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //商机列表
    public void mBusinessList() {
        try {
            Flowable<BusinessListBean> flowable = businessModel.businessList(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BusinessListBean>() {
                        @Override
                        public void onNext(BusinessListBean businessListBean) {
                            if (businessListBean != null && businessListBean.data != null && businessListBean.data.size() > 0) {
                                iBusinessClassFra.showData(businessListBean.data);
                            } else {
                                iBusinessClassFra.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iBusinessClassFra.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创业资讯
    public void mJZHO() {
        try {
            Flowable<JZHOBean> flowable = businessModel.bJZHO(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<JZHOBean>() {
                        @Override
                        public void onNext(JZHOBean jzhoBean) {
                            if (jzhoBean != null && jzhoBean.data != null && jzhoBean.data.size() > 0) {
                                iBusinessClassFra.showJZHO(jzhoBean.data);
                            } else {
                                iBusinessClassFra.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iBusinessClassFra.showErro();
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
