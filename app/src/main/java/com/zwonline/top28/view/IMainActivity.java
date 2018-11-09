package com.zwonline.top28.view;

import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.RegisterRedPacketsBean;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UpdateCodeBean;

/**
 * MainActivity的View
 */
public interface IMainActivity {
    /**
     * 版本自动检查更新
     *
     * @param updateCodeBean
     */
    void showUpdataVersion(UpdateCodeBean updateCodeBean);

    /**
     * 鞅分未领取
     *
     * @param unclaimedMbpCountBean
     */

    void showUnclaimedMbpCount(UnclaimedMbpCountBean unclaimedMbpCountBean);

    /**
     * 红包权限
     *
     * @param hongbaoPermissionBean
     */
    void showHongBaoPermission(HongbaoPermissionBean hongbaoPermissionBean);

    /**
     * 新人注册红包
     *
     * @param registerRedPacketBean
     */
    void showRedPacketDialog(RegisterRedPacketsBean.DataBean.DialogItemBean.RegisterRedPacketBean registerRedPacketBean);

    /**
     * 点击领取新人 红包接口
     *
     * @param showRegisterRedPacketBean
     */
    void showGetRedPacketDialog(RegisterRedPacketsBean.DataBean.DialogItemBean.ShowRegisterRedPacketBean showRegisterRedPacketBean);

}
