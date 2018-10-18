package com.zwonline.top28.view;

import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.LoginWechatBean;
import com.zwonline.top28.bean.ShortMessage;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/7 13:51
 */

public interface IRegisterActivity {
    void isSuccess(int status, String dialog, String token, String account);

    void getToken(String token);

    void isSuccess(int status);

    void onErro();

    void showStatus(ShortMessage shortMessage);

    void loginShowWechat(LoginWechatBean bean);

    /**
     * 验证短信验证码是否正确
     *
     * @param attentionBean
     */
    void showVerifySmsCode(AttentionBean attentionBean);

    /**
     * 绑定手机号
     *
     * @param attentionBean
     */
    void showBindMobile(AttentionBean attentionBean);

}
