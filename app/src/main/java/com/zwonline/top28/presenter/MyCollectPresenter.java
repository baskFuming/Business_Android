package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyCollectBean;
import com.zwonline.top28.model.MyCollectModel;
import com.zwonline.top28.view.IMyCollectActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 描述：收藏
 *
 * @author YSG
 * @date 2017/12/24
 */
public class MyCollectPresenter extends BasePresenter<IMyCollectActivity> {
    private MyCollectModel myCollectModel;
    private IMyCollectActivity iMyColeectActivity;

    public MyCollectPresenter(IMyCollectActivity iMyColeectActivity) {
        this.iMyColeectActivity = iMyColeectActivity;
        myCollectModel = new MyCollectModel();
    }

    //收藏
    public void mMyCollectLoadMore(Context context, String uid, final int page) {
        try {
            Flowable<MyCollectBean> flowable = myCollectModel.myConnect(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyCollectBean>() {
                        @Override
                        public void onNext(MyCollectBean myCollectBean) {
                            Log.e("myCollectBean===", myCollectBean.msg);
                            if (page == 1) {
                                if (myCollectBean.data.size() > 0) {
                                    iMyColeectActivity.showMyCollect(true);
                                    iMyColeectActivity.showMyCollectDate(myCollectBean.data);
                                } else {
                                    iMyColeectActivity.showMyCollect(false);
                                }
                            } else {
                                if (myCollectBean.data.size() > 0) {
                                    iMyColeectActivity.showMyCollectDate(myCollectBean.data);
                                } else {
                                    iMyColeectActivity.noLoadMore();
                                }
                            }

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

    //收藏
    public void mMyCollect(Context context, String uid, final int page) {
        try {
            Flowable<MyCollectBean> flowable = myCollectModel.myConnect(context, uid, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyCollectBean>(context) {
                        @Override
                        protected void onBaseNext(MyCollectBean myCollectBean) {
                            Log.e("myCollectBean===", myCollectBean.msg);
                            if (page==1){
                                if (myCollectBean.data.size() > 0) {
                                    iMyColeectActivity.showMyCollect(true);
                                    iMyColeectActivity.showMyCollectDate(myCollectBean.data);
                                }else {
                                    iMyColeectActivity.showMyCollect(false);
                                }
                            }else {
                                if (myCollectBean.data.size() > 0) {
                                    iMyColeectActivity.showMyCollectDate(myCollectBean.data);
                                }else {
                                    iMyColeectActivity.noLoadMore();
                                }
                            }
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
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
}
