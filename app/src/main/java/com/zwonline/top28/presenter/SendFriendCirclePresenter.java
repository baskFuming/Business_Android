package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;

import com.lzy.imagepicker.bean.ImageItem;
import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.model.SendFriendCircleModel;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.ISendFriendCircleActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 发朋友圈的p层
 */
public class SendFriendCirclePresenter extends BasePresenter<ISendFriendCircleActivity> {
    private SendFriendCircleModel sendFriendCircleModel;
    private ISendFriendCircleActivity iSendFriendCircleActivity;

    public SendFriendCirclePresenter(ISendFriendCircleActivity iSendFriendCircleActivity) {
        this.iSendFriendCircleActivity = iSendFriendCircleActivity;
        sendFriendCircleModel = new SendFriendCircleModel();
    }

    //上传图片
    public void mPictures(final Context context, List<File> compressFile) {
        try {
            Flowable<PictursBean> flowable = sendFriendCircleModel.FriendCircleImage(context, compressFile);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<PictursBean>(context) {

                        @Override
                        protected void onBaseNext(PictursBean pictursBean) {
                            Log.e("data==", pictursBean.msg);
                            iSendFriendCircleActivity.showPictures(pictursBean);
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
     * 发表动态
     *
     * @param context
     * @param images
     * @param content
     */
    public void mSendNewMoment(Context context, String images, String content) {
        try {
            Flowable<SendNewMomentBean> flowable = sendFriendCircleModel.sendNewMoment(context, images, content);
            flowable.subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<SendNewMomentBean>(context) {

                        @Override
                        protected void onBaseNext(SendNewMomentBean sendNewMomentBean) {
                            Log.e("data==", sendNewMomentBean.msg);
                            iSendFriendCircleActivity.showSendNewMoment(sendNewMomentBean);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

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

    //动态列表
    public void MomentList(final Context context, final int page, String userId, String follow_flag, String recommend_flag) {
        try {
            Flowable<NewContentBean> flowable = sendFriendCircleModel.momentList(context, page, userId, follow_flag, recommend_flag);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<NewContentBean>() {
                        @Override
                        public void onNext(NewContentBean newContentBean) {
                            Log.e("newContentBean", newContentBean.msg);
                            iSendFriendCircleActivity.showConment(newContentBean.data);
//                            if (newContentBean.status==1){
//                                if (page!=1&&newContentBean.data.size()==0){
//                                    ToastUtils.showToast(context,"没有更多数据");
//                                }
//                            }
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
     * 评论列表
     *
     * @param context
     * @param page
     * @param userId
     */
    public void MomentLists(Context context, int page, String userId, String follow_flag, String recommend_flag) {
        try {
            Flowable<NewContentBean> flowable = sendFriendCircleModel.momentList(context, page, userId, follow_flag, recommend_flag);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<NewContentBean>(context) {

                        @Override
                        protected void onBaseNext(NewContentBean newContentBean) {
                            Log.e("newContentBean", newContentBean.msg);
                            iSendFriendCircleActivity.showConment(newContentBean.data);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

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
     * 意见反馈
     *
     * @param context
     * @param content
     * @param images
     */
    public void mFeedBack(Context context, String type, String content, String images) {
        try {
            Flowable<SettingBean> flowable = sendFriendCircleModel.feedBack(context, type, content, images);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<SettingBean>(context) {

                        @Override
                        protected void onBaseNext(SettingBean settingBean) {
                            Log.e("newContentBean", settingBean.msg);
                            iSendFriendCircleActivity.showFeedBack(settingBean);
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
     * 动态详请列表
     *
     * @param context
     * @param page
     * @param moment_id
     * @param comment_id
     * @param author_id
     * @param sort_by
     */
    public void mDynamicComment(final Context context, int page, String moment_id, String comment_id, String author_id, String sort_by) {
        try {
            Flowable<DynamicDetailsBean> flowable = sendFriendCircleModel.dynamicComment(context, page, moment_id, comment_id, author_id, sort_by);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<DynamicDetailsBean>(context) {

                        @Override
                        protected void onBaseNext(DynamicDetailsBean dynamicDetailsBean) {
                            Log.e("newContentBean", dynamicDetailsBean.msg);
                            iSendFriendCircleActivity.showDynamicComment(dynamicDetailsBean.data);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

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
     * 动态分享
     *
     * @param context
     * @param moment_id
     */
    public void mDynamicShare(final Context context, String moment_id) {
        try {
            Flowable<DynamicShareBean> flowable = sendFriendCircleModel.dynamicShare(context, moment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<DynamicShareBean>(context) {

                        @Override
                        protected void onBaseNext(DynamicShareBean dynamicShareBean) {
                            Log.e("newContentBean", dynamicShareBean.msg);
                            iSendFriendCircleActivity.showDynamicShare(dynamicShareBean);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

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
     * 动态新评论
     *
     * @param context
     * @param content
     * @param pid
     * @param ppid
     * @param moment_id
     */
    public void NewComment(Context context, String content, String pid, String ppid, String moment_id) {
        try {
            Flowable<AddBankBean> flowable = sendFriendCircleModel.newComment(context, content, pid, ppid, moment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<AddBankBean>(context) {

                        @Override
                        protected void onBaseNext(AddBankBean settingBean) {
                            Log.e("newContentBean", settingBean.msg);
                            iSendFriendCircleActivity.showNewComment(settingBean);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

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
     * 删除动态
     *
     * @param context
     * @param moment_id
     */
    public void DeleteMoment(Context context, String moment_id) {
        try {
            Flowable<SettingBean> flowable = sendFriendCircleModel.deleteMoment(context, moment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<SettingBean>(context) {

                        @Override
                        protected void onBaseNext(SettingBean settingBean) {
                            Log.e("newContentBean", settingBean.msg);
                            iSendFriendCircleActivity.showDeleteMoment(settingBean);
                        }

                        @Override
                        protected String getTitleMsg() {
                            return null;
                        }

                        @Override
                        protected boolean isNeedProgressDialog() {
                            return false;
                        }

                        @Override
                        protected void onBaseComplete() {

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

    //关注
    public void mAttention(Context context, String type, String uid, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.attention(context, type, uid, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showAttention(attentionBean);
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


    //关注
    public void mAttentions(Context context, String type, String uid, String uid_list, String allow_be_call) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.attentions(context, type, uid, uid_list, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showAttentions(attentionBean);
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

    //取消关注
    public void mUnAttention(Context context, String type, String uid) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.UnAttention(context, type, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showUnAttention(attentionBean);
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
     * 动态点赞
     *
     * @param context
     * @param moment_id
     */
    public void LikeMoment(Context context, String moment_id) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.likeMoment(context, moment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showLikeMoment(attentionBean);
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
     * 屏蔽
     *
     * @param context
     * @param type
     * @param user_id
     */
    public void BlockUser(Context context, String type, String user_id) {
        try {
            Flowable<RefotPasswordBean> flowable = sendFriendCircleModel.blockUser(context, type, user_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RefotPasswordBean>() {
                        @Override
                        public void onNext(RefotPasswordBean attentionBean) {
                            iSendFriendCircleActivity.showBlockUser(attentionBean);
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
     * 屏蔽列表
     *
     * @param context
     * @param page
     */
    public void BlockUserList(Context context, final int page) {
        try {
            Flowable<ShieldUserBean> flowable = sendFriendCircleModel.blockUserList(context, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ShieldUserBean>() {
                        @Override
                        public void onNext(ShieldUserBean shieldUserBean) {
                            if (page == 1) {
                                if (shieldUserBean.data.size() > 0) {
                                    iSendFriendCircleActivity.showUserList(true);
                                    iSendFriendCircleActivity.showBlockUserList(shieldUserBean.data);
                                } else {
                                    iSendFriendCircleActivity.showUserList(false);
                                }
                            } else {
                                if (shieldUserBean.data.size() > 0) {
                                    iSendFriendCircleActivity.showUserList(true);
                                } else {
                                    iSendFriendCircleActivity.noLoadMore();
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
     * 动态评论点赞
     *
     * @param context
     * @param comment_id
     */
    public void LikeMomentComment(Context context, String comment_id) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.likeMomentComment(context, comment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showLikeMomentComment(attentionBean);
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
     * 删除动态评论
     *
     * @param context
     * @param comment_id
     */
    public void DeleteComment(Context context, String comment_id, String moment_id) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.deleteComment(context, comment_id, moment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showDeleteComment(attentionBean);
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
     * 推荐关注
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
     * 推荐关注banner
     *
     * @param context
     */
    public void BannerRecommendUserList(Context context) {
        try {
            Flowable<AtentionDynamicHeadBean> flowable = sendFriendCircleModel.mBannerRecommendUserList(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AtentionDynamicHeadBean>() {
                        @Override
                        public void onNext(AtentionDynamicHeadBean attentionBean) {
                            iSendFriendCircleActivity.showAttentionDynamic(attentionBean.data.list);
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
     * 商机圈我的消息未读数量
     *
     * @param context
     */
    public void GetMyNotificationCount(Context context) {
        try {
            Flowable<AttentionBean> flowable = sendFriendCircleModel.mGetMyNotificationCount(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            iSendFriendCircleActivity.showGetMyNotificationCount(attentionBean);
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
     * 商机圈我的消息未读数量
     *
     * @param context
     */
    public void GetLikeList(Context context, String momment_id, int page) {
        try {
            Flowable<LikeListBean> flowable = sendFriendCircleModel.mGetLikeList(context, momment_id, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<LikeListBean>() {
                        @Override
                        public void onNext(LikeListBean likeListBean) {
                            iSendFriendCircleActivity.showGetLikeList(likeListBean.data);
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
     * 商机圈我的消息未读数量
     *
     * @param context
     */
    public void MomentDetail(Context context, String momment_id) {
        try {
            Flowable<DynamicDetailsesBean> flowable = sendFriendCircleModel.mMomentDetail(context, momment_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<DynamicDetailsesBean>() {
                        @Override
                        public void onNext(DynamicDetailsesBean dynamicDetailsesBean) {
                            iSendFriendCircleActivity.showMomentDetail(dynamicDetailsesBean);
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
