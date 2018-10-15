package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.bean.YSBannerBean;
import com.zwonline.top28.bean.YSListBean;
import com.zwonline.top28.model.YangShiModel;
import com.zwonline.top28.view.IYangShiActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 鞅市P层
 */
public class YangShiPresenter extends BasePresenter<IYangShiActivity> {
    private YangShiModel yangShiModel;
    private IYangShiActivity yangShiActivity;

    public YangShiPresenter(IYangShiActivity iYangShiActivity) {
        this.yangShiActivity = iYangShiActivity;
        yangShiModel = new YangShiModel();
    }


    /**
     * 鞅市轮播
     *
     * @param context
     */
    public void BannerList(final Context context) {
        try {
            Flowable<YSBannerBean> flowable = yangShiModel.mBannerList(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<YSBannerBean>() {
                        @Override
                        public void onNext(YSBannerBean ysBannerBean) {
                            yangShiActivity.showBannerList(ysBannerBean.data);
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


    /**
     * 鞅市轮播
     *
     * @param context
     */
    public void AuctionList(Context context, String filter, int page) {
        try {
            Flowable<YSListBean> flowable = yangShiModel.mAuctionList(context, filter, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<YSListBean>() {
                        @Override
                        public void onNext(YSListBean ysBannerBean) {
                            yangShiActivity.showAuctionList(ysBannerBean.data.list);
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
}
