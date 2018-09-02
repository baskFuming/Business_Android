package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.DetailsBean;
import com.zwonline.top28.model.DetailsModel;
import com.zwonline.top28.view.IDetailsActivity;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by YU on 2017/12/11.
 */

public class DetailsPresenter extends BasePresenter<IDetailsActivity> {
    public DetailsModel homeDetailsModel;
    public IDetailsActivity iHomeDetails;

    public DetailsPresenter(IDetailsActivity iHomeDetailsActivity) {
        this.iHomeDetails = iHomeDetailsActivity ;
        homeDetailsModel = new DetailsModel();
    }
    //详情页
    public  void mDetails(Context context,int id,String token) throws Exception{
        Flowable<DetailsBean> flowable=homeDetailsModel.Details(context,id,token);
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<DetailsBean>() {
                    @Override
                    public void onNext(DetailsBean homeDetailsBean) {
                        Log.i("xxxx", homeDetailsBean.msg);

                            iHomeDetails.showHomeDetails(homeDetailsBean);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    //添加评论
    public void mAddComment(final Context context, String article_id, String content){
        try {
            Flowable<AddCommentBean>flowable=homeDetailsModel.addComment(context,article_id,content);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddCommentBean>() {
                        @Override
                        public void onNext(AddCommentBean addCommentBean) {
                            if(addCommentBean!=null&&addCommentBean.status==1){
                                iHomeDetails.commentSuccess();
                            }else {
                                Toast.makeText(context, R.string.comment_fail,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(context,R.string.comment_fail,Toast.LENGTH_SHORT).show();
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
    public void mReplyComment(final Context context, String article_id, String content,String pid,String ppid){
        try {
            Flowable<AddCommentBean>flowable=homeDetailsModel.replyComment(context,article_id,content,pid,ppid);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<AddCommentBean>() {
                        @Override
                        public void onNext(AddCommentBean addCommentBean) {
                            if(addCommentBean!=null&&addCommentBean.status==1){
                                iHomeDetails.commentSuccess();
                            }else {
                                Toast.makeText(context, R.string.comment_fail,Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(context,R.string.comment_fail,Toast.LENGTH_SHORT).show();
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
    public void mCollect(Context context,String article_id, String fType){
        try {
            Flowable<AddCommentBean>flowable=homeDetailsModel.collect(context,article_id, fType);
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
    //关注、取消关注
    public void mAttention(Context context,String type,String uid){
        try {
            Flowable<AttentionBean>flowable=homeDetailsModel.attention(context,type,uid);
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


}
