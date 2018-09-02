package com.zwonline.top28.presenter;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.R;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.model.SettingModel;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.StringUtil;
import com.zwonline.top28.view.ISettingView;

import java.io.File;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：个人设置
 *
 * @author YSG
 * @date 2017/12/28
 */
public class Settingpresenter extends BasePresenter<ISettingView> {
    private SettingModel settingModel;
    private ISettingView iSettingView;
    private SharedPreferencesUtils sp;
    public Settingpresenter(ISettingView iSettingView) {
        this.iSettingView = iSettingView;
        settingModel = new SettingModel();
        sp= SharedPreferencesUtils.getUtil();
    }

    //上传头像
        public void mSettingHead(final Context context, File file) {
        try {
            Flowable<HeadBean> flowable = settingModel.SettingHead(context,file );
            flowable.subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HeadBean>() {
                        @Override
                        public void onNext(HeadBean headBean) {
                            Log.e("data==",headBean.data);
                            sp.insertKey(context,"avatar",headBean.data);
                            iSettingView.showSettingHead(headBean);
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

    //感兴趣的行业
    public void mIndustryBean(Context context) {
        try {
            Flowable<IndustryBean> flowable = settingModel.mIndustry(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<IndustryBean>() {
                        @Override
                        public void onNext(IndustryBean industryBean) {
                            if (industryBean!=null&&industryBean.data!=null&&industryBean.data.size()>0){
                                iSettingView.showIndustry(industryBean.data);
                            }else {
                                iSettingView.onErro();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            iSettingView.onErro();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //设置个人资料
    public void mSetting(final Context context, String nick_name,
                         String real_name, int sex, String age,
                         String address, String favourite_industry,
                         String bio) {
        try {
            Flowable<SettingBean> flowable = settingModel.mSetingModel(context, nick_name, real_name, sex, age, address,
                    favourite_industry, bio);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<SettingBean>() {
                        @Override
                        public void onNext(SettingBean headBean) {
                            if (headBean.status==1){
                                iSettingView.isSucceed();
                            }else {
                                iSettingView.onErro();
                            }
                            iSettingView.showSetting(headBean);
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
    //获取用户信息
    public void mUserInfo(Context context){
        try {
            Flowable<UserInfoBean>flowable=settingModel.UserInfo(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<UserInfoBean>() {
                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            if (userInfoBean!=null&&userInfoBean.data!=null){
                                iSettingView.showUserInfo(userInfoBean);
                            }else {
                                iSettingView.onErro();
                            }

                        }

                        @Override
                        public void onError(Throwable t) {
                            iSettingView.onErro();
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
