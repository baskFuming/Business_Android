package com.zwonline.top28.view;

import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SettingBean;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/7 13:48
 */

public interface ILoginActivity {

    void isSuccess(int status, String dialog, String token, String account);
    void getToken(String dialog);
    void Success(LoginBean loginBean);
    void onErro();
    void showErro();

    /**
     * 判断忘记密码验证成功
     * @param settingBean
     */
    void showForgetPossword(LoginBean settingBean);
}
