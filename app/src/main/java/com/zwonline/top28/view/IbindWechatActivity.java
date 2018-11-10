package com.zwonline.top28.view;

import com.zwonline.top28.bean.BindWechatBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;

/**
 * 绑定微信号
 */
public interface IbindWechatActivity {
    /**
     * 绑定微信
     *
     * @param bindWechatBean
     */
    void bindWechat(BindWechatBean bindWechatBean);

    /**
     * 绑定微信弹窗
     *
     * @param bindWechatBean
     */
    void showBindWechatPop(RegisterRedPacketsBean.DataBean.DialogItemBean.WXBind bindWechatBean);

    /**
     * 绑定微信成功
     *
     * @param bindSuccess
     */
    void showBindWechatSuccess(RegisterRedPacketsBean.DataBean.DialogItemBean.WXBindSuccess bindSuccess);

    /**
     * 绑定手机号弹窗
     *
     * @param mobileBind
     */
    void showBindMobile(RegisterRedPacketsBean.DataBean.DialogItemBean.MobileBind mobileBind);

    /**
     * 绑定手机号成功
     *
     * @param mobileBindSuccess
     */
    void showBindMobileSuccess(RegisterRedPacketsBean.DataBean.DialogItemBean.MobileBindSuccess mobileBindSuccess);

    void onErro();
}
