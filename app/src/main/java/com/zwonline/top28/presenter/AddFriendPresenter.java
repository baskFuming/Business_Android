package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AddFriendBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.model.AddFriendModel;
import com.zwonline.top28.view.IAddFriendActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 搜索添加好友的P层
 */
public class AddFriendPresenter extends BasePresenter<IAddFriendActivity> {
    private AddFriendModel addFriendModel;
    private IAddFriendActivity iAddFriendActivity;

    public AddFriendPresenter(IAddFriendActivity iAddFriendActivity) {
        addFriendModel = new AddFriendModel();
        this.iAddFriendActivity = iAddFriendActivity;
    }

    //添加好友
    public void mAddFriend(Context context, String keyword) {
        try {

            Flowable<AddFriendBean> flowable = addFriendModel.addFriend(context, keyword);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddFriendBean>() {
                        @Override
                        public void onNext(AddFriendBean addFriendBean) {
                            Log.i("xxx", addFriendBean.msg);
                            iAddFriendActivity.showAddFriend(addFriendBean.data);
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
     * 群推荐
     * @param context
     */
    public void mRecommendTeams(Context context) {
        try {

            Flowable<RecommendTeamsBean> flowable = addFriendModel.recommendTeamsBean(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RecommendTeamsBean>() {
                        @Override
                        public void onNext(RecommendTeamsBean recommendTeamsBean) {
                            Log.i("xxx", recommendTeamsBean.msg);
                            iAddFriendActivity.showRecommendTeams(recommendTeamsBean.data);
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
     * 群推荐
     * @param context
     */
    public void mRecommendTeam(Context context) {
        try {
            Flowable<RecommendTeamsBean> flowable = addFriendModel.recommendTeamsBean(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<RecommendTeamsBean>(context) {
                        @Override
                        protected void onBaseNext(RecommendTeamsBean recommendTeamsBean) {
                            Log.e("recommendTeamsBean==", recommendTeamsBean.msg);
                            iAddFriendActivity.showRecommendTeams(recommendTeamsBean.data);
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
