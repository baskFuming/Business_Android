package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.AndroidException;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.model.GroupTagsModel;
import com.zwonline.top28.view.IGroupTagsActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 群标签的P层
 */
public class GroupTagsPresenter extends BasePresenter<IGroupTagsActivity> {
    private GroupTagsModel groupTagsModel;
    private IGroupTagsActivity iGroupTagsActivity;

    public GroupTagsPresenter(IGroupTagsActivity iGroupTagsActivity) {
        groupTagsModel = new GroupTagsModel();
        this.iGroupTagsActivity = iGroupTagsActivity;
    }

    /**
     * 群标签推荐
     *
     * @param context
     */
    public void recommondGroupTags(Context context) {
        try {
            Flowable<RecommendTeamsBean> flowable = groupTagsModel.mGroupTags(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<RecommendTeamsBean>(context) {
                        @Override
                        protected void onBaseNext(RecommendTeamsBean recommendTeamsBean) {
                            if (recommendTeamsBean.status == 1) {
                                iGroupTagsActivity.showRecommendTeamTag(recommendTeamsBean.data);
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
    /**
     * 添加标签
     *
     * @param context
     * @param name
     */
    public void AddTeamTag(Context context, String name) {
        try {
            Flowable<AttentionBean> flowable = groupTagsModel.mAddTeamTag(context, name);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AttentionBean>(context) {
                        @Override
                        protected void onBaseNext(AttentionBean recommendTeamsBean) {
                            if (recommendTeamsBean.status == 1) {
                                iGroupTagsActivity.showAddTeamTag(recommendTeamsBean);
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
