package com.zwonline.top28.presenter;

import android.content.Context;

import com.zwonline.top28.api.subscriber.BaseDisposableSubscriber;
import com.zwonline.top28.base.BasePresenter;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.model.ProjectModel;
import com.zwonline.top28.view.IMyProjectActivity;

import java.io.IOException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**我的项目列表
 * Created by sdh on 2018/3/8.
 */

public class MyProjectPresenter extends BasePresenter<IMyProjectActivity> {

    private ProjectModel projectModel;
    private IMyProjectActivity iMyProjectActivity;

    public MyProjectPresenter(IMyProjectActivity iMyProjectActivity){
        this.iMyProjectActivity=iMyProjectActivity;
        projectModel=new ProjectModel();
    }

    /**
     * 我的项目列表
     * @param context
     */
    public void showMyProjectList(Context context, String uid){
        try {
            projectModel.projectList(context, uid).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseDisposableSubscriber<ProjectBean>(context) {

                        @Override
                        protected void onBaseNext(ProjectBean projectBean) {
                            iMyProjectActivity.showMyProjectList(projectBean.data);
//                            Log.i("gest", projectBean.toString());
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
