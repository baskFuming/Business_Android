package com.zwonline.top28.view;

import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.UserInfoBean;

/**
 * @author YSG
 * @desc密码重置
 * @date ${Date}
 */
public interface IRetPossword {
    void showRetPossWord(SettingBean settingBean);//重置密码
    void onErro();
    void Success(LoginBean loginBean);//验证密码
    void showUserInfo(UserInfoBean userInfoBean);//获取个人信息
}
