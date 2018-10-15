package com.zwonline.top28.view;

import android.service.voice.VoiceInteractionService;

import com.zwonline.top28.bean.YSBannerBean;
import com.zwonline.top28.bean.YSListBean;

import java.util.List;

/**
 * 鞅市View层
 */
public interface IYangShiActivity {
    /**
     * 鞅市轮播图banner
     * @param ysBannerBeanList
     */
    void showBannerList(List<YSBannerBean.DataBean> ysBannerBeanList);

    /**
     * 鞅市拍卖列表
     * @param ysList
     */
    void showAuctionList(List<YSListBean.DataBean.ListBean> ysList);
}
