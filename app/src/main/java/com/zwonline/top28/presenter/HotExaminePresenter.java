package com.zwonline.top28.presenter;

import android.content.Context;
import android.util.Log;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.HotExamineBean;
import com.zwonline.top28.bean.VideoBean;
import com.zwonline.top28.model.HotExamineModel;
import com.zwonline.top28.view.IHotExamine;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * @author YSG
 * @desc热门考察presenter层
 * @date 2017/12/14
 */
public class HotExaminePresenter extends BasePresenter<IHotExamine> {
    private HotExamineModel hotExamineModel;
    private IHotExamine iHotExamine;

    public HotExaminePresenter(IHotExamine iHotExamine) {
        this.iHotExamine = iHotExamine;
        hotExamineModel = new HotExamineModel();
    }

    //热门考察
    public void mHotExamine(Context context,int page) {
        try {
            Flowable<HotExamineBean> flowable = hotExamineModel.hotExamine(context,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HotExamineBean>() {
                        @Override
                        public void onNext(HotExamineBean hotExamineBean) {
                            Log.e("hotExamine===", hotExamineBean.msg);
                            iHotExamine.showHotExamineData(hotExamineBean.data);
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

    //视频
    public void mVideo(Context context,String page,String cate_id) {
        try {
            Flowable<VideoBean> flowable = hotExamineModel.videoList(context,page,cate_id);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<VideoBean>() {
                        @Override
                        public void onNext(VideoBean videoBean) {
                            if (videoBean != null && videoBean.data != null && videoBean.data.size() > 0) {
                                if (videoBean.status==1){
                                    iHotExamine.showVideo(videoBean.data);
                                }
                            } else {
                                iHotExamine.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHotExamine.showErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //视频推荐
    public void mVideoRecomment(Context context,String page) {
        try {
            Flowable<VideoBean> flowable = hotExamineModel.videoList(context,page);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<VideoBean>() {
                        @Override
                        public void onNext(VideoBean videoBean) {
                            if (videoBean != null && videoBean.data != null && videoBean.data.size() > 0) {
                                iHotExamine.showVideo(videoBean.data);
                            } else {
                                iHotExamine.showErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iHotExamine.showErro();
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
