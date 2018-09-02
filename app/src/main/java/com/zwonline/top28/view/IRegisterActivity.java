package com.zwonline.top28.view;

import com.zwonline.top28.bean.ShortMessage;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/12/7 13:51
 */

public interface IRegisterActivity {
    void getToken(String token);
    void isSuccess(int status);
    void onErro();
    void showStatus(ShortMessage shortMessage);
}
