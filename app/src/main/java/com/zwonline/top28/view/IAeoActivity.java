package com.zwonline.top28.view;


import com.zwonline.top28.bean.EnterpriseStatusBean;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.ProjectBean;

import java.util.List;

/**
 * 描述：申请企业认证
 * @author YSG
 * @date 2017/12/31
 */
public interface IAeoActivity {
    void showAeo(HeadBean headBean);

    void showAeoClass(List<IndustryBean.DataBean> classList);
    void showAeoImage(PicturBean headBean);

    /**
     * 获取项目详情
     * @param enterpriseData
     */
    void getEnterpriseDetail(EnterpriseStatusBean.DataBean enterpriseData);

    /**
     * 初始化详情
     */
    void initEnterpriseDetail();

    /**
     * 展示我的项目列表
     * @param myProjectList
     */
    void showMyProjectList(List<ProjectBean.DataBean> myProjectList);
}
