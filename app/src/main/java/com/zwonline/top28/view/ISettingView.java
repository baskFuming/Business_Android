package com.zwonline.top28.view;

import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public interface ISettingView {
    void showIndustry(List<IndustryBean.DataBean> beanList);//感兴趣的行业
    void showSetting(SettingBean headBean);//保存用户信息
    void showSettingHead(HeadBean headBean);//上传头像
    void showUserInfo(UserInfoBean userInfoBean);//

    /**
     * 判断用户保存是否成功
     */
    void onErro();
    void isSucceed();
}
