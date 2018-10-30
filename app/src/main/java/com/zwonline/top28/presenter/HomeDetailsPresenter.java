package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.GiftBean;
import com.zwonline.top28.bean.GiftSumBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.RewardListBean;
import com.zwonline.top28.bean.ShareDataBean;
import com.zwonline.top28.model.HomeDetailsModel;
import com.zwonline.top28.utils.ToastUtils;
import com.zwonline.top28.view.IHomeDetails;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by YU on 2017/12/11.
 */

public class HomeDetailsPresenter extends BasePresenter<IHomeDetails> {
    public HomeDetailsModel homeDetailsModel;
    public IHomeDetails iHomeDetails;

    public HomeDetailsPresenter(IHomeDetails iHomeDetailsActivity) {
        this.iHomeDetails = iHomeDetailsActivity;
        homeDetailsModel = new HomeDetailsModel();
    }

    //用户主页信息
    public void mCompany(Context context, String uid) {
        try {
            Flowable<PersonageInfoBean> flowable = homeDetailsModel.Company(context, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<PersonageInfoBean>() {
                        @Override
                        public void onNext(PersonageInfoBean companyBean) {
                            iHomeDetails.showCompany(companyBean);
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

    //详情页
    public void mHomeDetail(Context context, int id) throws Exception {
        Flowable<HomeDetailsBean> flowable = homeDetailsModel.homeDetail(context, id);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<HomeDetailsBean>() {

                    @Override
                    public void onNext(HomeDetailsBean homeDetailsBean) {
                        Log.i("xxxx", homeDetailsBean.msg);
                        if (homeDetailsBean != null && homeDetailsBean.data != null && homeDetailsBean.data.members_info != null) {
                            iHomeDetails.showHomeDetails(homeDetailsBean);
                        } else {
                            iHomeDetails.onErro();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        iHomeDetails.initFavorite();
                    }

                });
    }

    //详情页
    public void mHomeDetails(Context context, final int id) throws Exception {
        Flowable<HomeDetailsBean> flowable = homeDetailsModel.homeDetails(context, id);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<HomeDetailsBean>() {
                    @Override
                    public void onNext(HomeDetailsBean homeDetailsBean) {
                        Log.i("xxxx", homeDetailsBean.msg);
                        if (homeDetailsBean != null && homeDetailsBean.data != null && homeDetailsBean.data.members_info != null) {
                            iHomeDetails.showHomeDetails(homeDetailsBean);
                        } else {
                            iHomeDetails.onErro();
                        }

                    }

                    @Override
                    public void onError(Throwable t) {
                        iHomeDetails.onErro();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("test", "onCompleted");
                        iHomeDetails.initFavorite();
                    }
                });
    }


    //添加评论
    public void mAddComment(final Context context, String article_id, String content) {
        try {
            Flowable<AddCommentBean> flowable = homeDetailsModel.addComment(context, article_id, content);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddCommentBean>() {
                        @Override
                        public void onNext(AddCommentBean addCommentBean) {
                            if (addCommentBean != null && addCommentBean.status == 1) {
                                iHomeDetails.commentSuccess();
                            } else {
                                Toast.makeText(context, addCommentBean.msg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
//                            Toast.makeText(context,R.string.comment_fail,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //回复评论
    public void mReplyComment(final Context context, String article_id, String content, String pid, String ppid) {
        try {
            Flowable<AddCommentBean> flowable = homeDetailsModel.replyComment(context, article_id, content, pid, ppid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddCommentBean>() {
                        @Override
                        public void onNext(AddCommentBean addCommentBean) {
                            iHomeDetails.onError(addCommentBean);
                            if (addCommentBean != null && addCommentBean.status == 1) {
                                iHomeDetails.commentSuccess();
                            } else {
                                Toast.makeText(context, addCommentBean.msg, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
//                            Toast.makeText(context,R.string.comment_fail,Toast.LENGTH_SHORT).show();
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
    public void mCollect(Context context, String article_id, String fType) {
        try {
            Flowable<AddCommentBean> flowable = homeDetailsModel.collect(context, article_id, fType);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddCommentBean>() {
                        @Override
                        public void onNext(AddCommentBean addCommentBean) {

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
            Flowable<AttentionBean> flowable = homeDetailsModel.attention(context, type, uid, allow_be_call);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {

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
            Flowable<AttentionBean> flowable = homeDetailsModel.UnAttention(context, type, uid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {

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

    //文章分享链接
    public void mShareData(Context context, String article_id) {
        try {
            Flowable<ShareDataBean> flowable = homeDetailsModel.shareData(context, article_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ShareDataBean>() {
                        @Override
                        public void onNext(ShareDataBean shareDataBean) {
                            if (shareDataBean != null && shareDataBean.data != null) {
                                Log.i("shareDataBean==", shareDataBean.msg);
                                iHomeDetails.showShareData(shareDataBean.data);
                            } else {
                                iHomeDetails.onErro();
                            }
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

    //文章评论
    public void mArticleComment(final Context context, String article_id, String comment_id, String author_id, String sort_by, int page) {
        try {
            Flowable<ArticleCommentBean> flowable = homeDetailsModel.articleComment(context, article_id, comment_id, author_id, sort_by, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ArticleCommentBean>() {
                        @Override
                        public void onNext(ArticleCommentBean shareDataBean) {
                            if (shareDataBean != null && shareDataBean.data != null) {

                                Log.i("shareDataBean==", shareDataBean.msg);
                                iHomeDetails.showArticleComment(shareDataBean.data);
                            } else {

                                iHomeDetails.onErro();
                            }


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
     * 礼物数量
     *
     * @param context
     * @param target_type
     * @param target_id
     */
    public void GiftSummary(final Context context, String target_type, String target_id) {
        try {
            Flowable<GiftSumBean> flowable = homeDetailsModel.mGiftSummary(context, target_type, target_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<GiftSumBean>() {
                        @Override
                        public void onNext(GiftSumBean attentionBean) {
                            if (attentionBean.status == 1) {
                                iHomeDetails.showGiftSummary(attentionBean);
                            }else {
                                ToastUtils.showToast(context,attentionBean.msg);
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
     * 礼物
     *
     * @param context
     */
    public void Gift(final Context context) {
        try {
            Flowable<GiftBean> flowable = homeDetailsModel.mGift(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<GiftBean>() {
                        @Override
                        public void onNext(GiftBean attentionBean) {
                            if (attentionBean.status == 1) {
                                iHomeDetails.showGift(attentionBean);
                            }else {
                                ToastUtils.showToast(context,attentionBean.msg);
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
    /***
     * 打赏接口
     * @param context
     * @param target_type
     * @param target_id
     * @param gift_id
     * @param gift_count
     */
    public void SendGifts(final Context context, String target_type, String target_id, String gift_id, String gift_count) {
        try {
            Flowable<AttentionBean> flowable = homeDetailsModel.mSendGifts(context, target_type, target_id, gift_id, gift_count);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AttentionBean>() {
                        @Override
                        public void onNext(AttentionBean attentionBean) {
                            if (attentionBean.status == 1) {
                                iHomeDetails.showSendGifts(attentionBean);
                            }else {
                                ToastUtils.showToast(context,attentionBean.msg);
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
     * 打赏列表
     *
     * @param context
     * @param target_type
     * @param target_id
     * @param page
     */
    public void GiftList(final Context context, String target_type, String target_id, int page) {
        try {
            Flowable<RewardListBean> flowable = homeDetailsModel.mGiftList(context, target_type, target_id, page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<RewardListBean>() {
                        @Override
                        public void onNext(RewardListBean rewardListBean) {
                            if (rewardListBean.status == 1) {
                                iHomeDetails.showGiftList(rewardListBean.data.list);
                            } else {
                                ToastUtils.showToast(context, rewardListBean.msg);
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
