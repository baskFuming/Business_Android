package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircle;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.model.SendFriendCircleModel;
import com.zwonline.top28.view.BusincCirclerActivity;

import java.io.IOException;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class BusnicCirclerPresenter extends BasePresenter<BusincCirclerActivity> {
    private SendFriendCircleModel sendFriendCircleModel;
    private BusincCirclerActivity iSendFriendCircleActivity;

    public BusnicCirclerPresenter(BusincCirclerActivity iSendFriendCircleActivity) {
        this.iSendFriendCircleActivity = iSendFriendCircleActivity;
        sendFriendCircleModel = new SendFriendCircleModel();
    }

    /**
     * 推荐
     *
     * @param context
     */
    public void BusincComment(Context context) {
        try {
            Flowable<BusinessCircleBean> flowable = sendFriendCircleModel.getBusinessCircle(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BusinessCircleBean>() {
                        @Override
                        public void onNext(BusinessCircleBean attentionBean) {
                            iSendFriendCircleActivity.showBusincDate(attentionBean.data);
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
     * 推荐列表
     *
     * @param context
     */
    public void BusincCommentList(Context context, int item_id) {
        try {
            Flowable<AddFollowBean> flowable = sendFriendCircleModel.getBusinessCircleList(context, item_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddFollowBean>() {
                        @Override
                        public void onNext(AddFollowBean as) {
                            iSendFriendCircleActivity.showBusincDateList(as.data.list);
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
     * 关注
     *
     * @param context
     * @param type
     * @param uid
     * @param uid_list
     * @param allow_be_call
     */
    public void attention(Context context, String type, String uid, String uid_list, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.attentions(context, type, uid, uid_list, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean as) {
                            iSendFriendCircleActivity.showAttention(as);
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
     * 取消关注
     *
     * @param context
     * @param type
     * @param uid
     * @param uid_list
     * @param allow_be_call
     */
    public void unAttention(Context context, String type, String uid, String uid_list, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.attentions(context, type, uid, uid_list, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean as) {
                            iSendFriendCircleActivity.showUnAttention(as);
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
