package com.zwonline.top28.view;

import com.zwonline.top28.bean.EarnIntegralBean;

import java.util.List;

/**
 * @author YSG
 * @desc赚取积分说明的model
 * @date ${Date}
 */
public interface IEarnIntegralActivity {
    /**
     * 赚取积分
     * @param earnList
     */
    void showEarnIngral (List<EarnIntegralBean.DataBean> earnList);
}
