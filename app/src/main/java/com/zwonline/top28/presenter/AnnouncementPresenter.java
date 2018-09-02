package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.NotifyDetailsBean;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.model.AnnouncementMedel;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IAnnouncementActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 公告的列表P层
 */
public class AnnouncementPresenter extends BasePresenter<IAnnouncementActivity> {
    private AnnouncementMedel announcementMedel;
    private IAnnouncementActivity iAnnouncementActivity;

    public AnnouncementPresenter(IAnnouncementActivity iAnnouncementActivity) {
        announcementMedel = new AnnouncementMedel();
        this.iAnnouncementActivity = iAnnouncementActivity;
    }

    /**
     * 公告列表
     *
     * @param context
     * @param page
     */
    public void mAnnouncement(Context context, final String page) {
        try {
            Flowable<AnnouncementBean> flowable = announcementMedel.announcement(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AnnouncementBean>() {
                        @Override
                        public void onNext(AnnouncementBean announcementBean) {
                            Log.d("announcementBean", announcementBean.msg);
                            if (page.equals(BizConstant.ALREADY_FAVORITE)) {
                                if (announcementBean.data.size() > 0) {
                                    iAnnouncementActivity.noAnnouncement(true);
                                    iAnnouncementActivity.showAnnouncement(announcementBean.data);
                                } else {
                                    iAnnouncementActivity.noAnnouncement(false);
                                }
                            } else {
                                if (announcementBean.data.size() > 0) {
                                    iAnnouncementActivity.showAnnouncement(announcementBean.data);
                                } else {
                                    iAnnouncementActivity.noLoadMore();
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

    /**
     * 公告详情
     *
     * @param context
     * @param noticeID
     */
    public void mNotifyDetails(final Context context, final String noticeID) {
        try {
            Flowable<NotifyDetailsBean> flowable = announcementMedel.notifyDetails(context, noticeID);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<NotifyDetailsBean>() {
                        @Override
                        public void onNext(NotifyDetailsBean notifyDetailsBean) {
                            Log.d("notifyDetailsBean==", notifyDetailsBean.msg);
                            if (notifyDetailsBean.data != null) {
                                iAnnouncementActivity.showNotifyDetails(notifyDetailsBean);
                            } else {
                                ToastUtils.showToast(context, notifyDetailsBean.msg);
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

    /**
     * 记录公告是否已读
     *
     * @param context
     * @param noticeID
     */
    public void mreadNotice(final Context context, final String noticeID) {
        try {
            Flowable<NoticeNotReadCountBean> flowable = announcementMedel.readNotices(context, noticeID);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<NoticeNotReadCountBean>() {
                        @Override
                        public void onNext(NoticeNotReadCountBean notifyDetailsBean) {
                            Log.d("mreadNotice==", notifyDetailsBean.msg);
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
