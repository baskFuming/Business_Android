package com.zwonline.top28.view;

import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.NotifyDetailsBean;

import java.util.List;

/**
 * 公告的view层
 */
public interface IAnnouncementActivity {
    /**
     * 展示列表数据
     *
     * @param list
     */
    void showAnnouncement(List<AnnouncementBean.DataBean> list);

    /**
     * 没有公告展示
     *
     * @param flag
     */
    void noAnnouncement(boolean flag);

    /**
     * 没有更多数据了
     */
    void noLoadMore();

    /**
     * 公告详情
     *
     * @param notifyDetailsBean
     */
    void showNotifyDetails(NotifyDetailsBean notifyDetailsBean);
}
