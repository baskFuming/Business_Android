package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.NewRecomdUserBean;
import com.zwonline.top28.bean.RecommendUserBean;
import com.zwonline.top28.model.HomeDetailsModel;
import com.zwonline.top28.view.IRecommnedActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 我的推荐 p层
 */
public class ReconmmnedUserPresenter extends BasePresenter<IRecommnedActivity> {

    public HomeDetailsModel homeDetailsModel;
    public IRecommnedActivity iHomeDetails;

    public ReconmmnedUserPresenter(IRecommnedActivity iHomeDetailsActivity) {
        this.iHomeDetails = iHomeDetailsActivity;
        homeDetailsModel = new HomeDetailsModel();
    }
    /**
     * @param context 我的推荐
     */
    public void myRecommned(Context context) {
        try {
            Flowable<RecommendUserBean> flowable = homeDetailsModel.MyRecommend(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RecommendUserBean>() {
                        @Override
                        public void onNext(RecommendUserBean companyBean) {
                            iHomeDetails.successRecommed(companyBean);
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeDetails.onErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @param context 新的我的推荐
     */
    public void NewMyRecommned(Context context) {
        try {
            Flowable<NewRecomdUserBean> flowable = homeDetailsModel.newRecommend(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<NewRecomdUserBean>() {
                        @Override
                        public void onNext(NewRecomdUserBean newRecomdUserBean) {
                            iHomeDetails.newSuccessRecomed(newRecomdUserBean);
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHomeDetails.onErro();
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
