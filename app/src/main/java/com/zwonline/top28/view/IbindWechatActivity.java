package com.zwonline.top28.view;

import com.zwonline.top28.bean.BindWechatBean;

/**
 * 绑定微信号
 */
public interface IbindWechatActivity {
    void bindWechat(BindWechatBean bindWechatBean);
    void onErro();
}
