package com.zwonline.top28.view;

import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.LoginWechatBean;

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

    //微信授权登录
    void isWechatSuccess(int status, String dialog, String token, String account);
    void wecahtSuccess(LoginWechatBean loginWechatBean);


}
