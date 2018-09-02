package com.zwonline.top28.presenter;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zwonline.top28.R;
import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.EnterpriseStatusBean;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.model.AeoModel;
import com.zwonline.top28.view.IAeoActivity;

import java.io.File;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 描述：申请企业认证
 *
 * @author YSG
 * @date 2017/12/31
 */
public class AeoPresenter extends BasePresenter<IAeoActivity> {
    private AeoModel aeoModel;
    private IAeoActivity iAeoActivity;

    public AeoPresenter(IAeoActivity iAeoActivity) {
        this.iAeoActivity = iAeoActivity;
        aeoModel = new AeoModel();
    }

    //企业认证分类
    public void mAeoClass(Context context) {
        try {
            Flowable<IndustryBean> flowable = aeoModel.AeoClass(context);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<IndustryBean>() {
                        @Override
                        public void onNext(IndustryBean industryBean) {
                            Log.e("industryBean==", industryBean.msg);
                            iAeoActivity.showAeoClass(industryBean.data);
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

    //    申请企业认证
    public void mAeo(Context context, String enterprise_name, String company_name,
                     String slug, String cate_id,
                     String enterprise_contacts, String enterprise_contact_tel,
                     String enterprise_contact_address, String[] img) {
        try {
                      if (TextUtils.isEmpty(enterprise_name)) {
                Toast.makeText(context, R.string.aeo_company_sname_isempty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(company_name)) {
                Toast.makeText(context, R.string.aeo_company_name_isempty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(enterprise_contacts)) {
                Toast.makeText(context, R.string.aeo_contacts_isempty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(slug)) {
                Toast.makeText(context, R.string.aeo_sign_isempty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(enterprise_contact_tel)) {
                Toast.makeText(context, R.string.aeo_contacts_phone_isempty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(enterprise_contact_address)) {
                Toast.makeText(context, R.string.aeo_contacts_address_isempty, Toast.LENGTH_SHORT).show();
                return;
            }
            Flowable<HeadBean> flowable = aeoModel.Aeo(context, enterprise_name, company_name,
                    slug, cate_id, enterprise_contacts, enterprise_contact_tel, enterprise_contact_address, img);
            flowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<HeadBean>() {
                        @Override
                        public void onNext(HeadBean headBean) {
                            Log.e("mAeo==", headBean.msg);
                            iAeoActivity.showAeo(headBean);
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
        {

        }
    }

    //上传图片
    //上传头像
    public void mSettingHead(final Context context, File file,String return_origin) {
        try {
            Flowable<PicturBean> flowable = aeoModel.AeoImage(context, file,return_origin);
            flowable.subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<PicturBean>() {
                        @Override
                        public void onNext(PicturBean headBean) {
                            Log.e("data==", headBean.url);
                            iAeoActivity.showAeoImage(headBean);
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
     * 获取企业认证的审核状态接口
     *
     * @param context
     * @param projectId
     */
    public void getEnterpriseDetail(Context context, String projectId) {
        try {
            aeoModel.getEnterpriseDetail(context, projectId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<EnterpriseStatusBean>(context) {

                        @Override
                        protected void onBaseNext(EnterpriseStatusBean enterpriseStatusBean) {
                            iAeoActivity.getEnterpriseDetail(enterpriseStatusBean.data);
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
                            //初始化旁控件
                            iAeoActivity.initEnterpriseDetail();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的项目列表
     *
     * @param context
     */
    public void showMyProjectList(Context context, String uid) {
        try {
            aeoModel.projectList(context, uid).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<ProjectBean>() {

                        @Override
                        public void onNext(ProjectBean projectBean) {
                            if (projectBean.data!=null&&projectBean.data.size()>0){
                                iAeoActivity.showMyProjectList(projectBean.data);
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
