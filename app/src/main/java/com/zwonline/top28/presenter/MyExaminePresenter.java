package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.MyExamine;
import com.zwonline.top28.model.MyExamineModel;
import com.zwonline.top28.view.IMyExamineActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;


/**
 * 描述：我的考察P层
 *
 * @author YSG
 * @date 2017/12/21
 */
public class MyExaminePresenter extends BasePresenter<IMyExamineActivity> {
    private MyExamineModel myExamineModel;
    public IMyExamineActivity iMyExamineActivity;

    public MyExaminePresenter(IMyExamineActivity iMyExamineActivity) {
        this.iMyExamineActivity = iMyExamineActivity;
        myExamineModel = new MyExamineModel();
    }

    //我的考察
    public void mMyExamine(Context context, final int page) {
        try {
            Flowable<MyExamine> flowable = myExamineModel.myExamine(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<MyExamine>(context) {
                        @Override
                        protected void onBaseNext(MyExamine myExamine) {
                            Log.e("myExamine===", myExamine.msg);
                            if (page == 1) {
                                if (myExamine.data.size() > 0) {
                                    iMyExamineActivity.showMyExamine(true);
                                    iMyExamineActivity.showMyExamineDate(myExamine.data);
                                } else {
                                    iMyExamineActivity.showMyExamine(false);
                                }
                            } else {
                                if (myExamine.data.size() > 0) {
                                    iMyExamineActivity.showMyExamineDate(myExamine.data);
                                } else {
                                    iMyExamineActivity.noLoadMore();
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

    //我的考察
    public void mMyExamineLoad(Context context, final int page) {
        try {
            Flowable<MyExamine> flowable = myExamineModel.myExamine(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<MyExamine>() {
                        @Override
                        public void onNext(MyExamine myExamine) {
                            Log.e("myExamine===", myExamine.msg);
                            if (page == 1) {
                                if (myExamine.data.size() > 0) {
                                    iMyExamineActivity.showMyExamine(true);
                                    iMyExamineActivity.showMyExamineDate(myExamine.data);
                                } else {
                                    iMyExamineActivity.showMyExamine(false);
                                }
                            } else {
                                if (myExamine.data.size() > 0) {
                                    iMyExamineActivity.showMyExamineDate(myExamine.data);
                                } else {
                                    iMyExamineActivity.noLoadMore();
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
}
