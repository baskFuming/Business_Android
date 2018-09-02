package com.zwonline.top28.view;

import com.zwonline.top28.bean.EnterpriseStatusBean;
import com.zwonline.top28.bean.ProjectBean;

import java.util.List;

/**
 * Created by sdh on 2018/3/8.
 * 我的项目列表
 */

public interface IMyProjectActivity {
    /**
     * 展示我的项目列表
     * @param myProjectList
     */
    void showMyProjectList(List<ProjectBean.DataBean> myProjectList);

}
