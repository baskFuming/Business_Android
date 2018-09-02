package com.zwonline.top28.view;

import com.zwonline.top28.bean.HongbaoPermissionBean;
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


}
