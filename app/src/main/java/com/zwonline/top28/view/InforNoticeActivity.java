package com.zwonline.top28.view;

import com.zwonline.top28.bean.InforNoticeBean;
import com.zwonline.top28.bean.InforNoticeCleanBean;
import com.zwonline.top28.bean.TipBean;

import java.util.List;

public interface InforNoticeActivity {

    /**
     * 获取bean的相关Method
     *
     */
     void inForNoticeMethod(InforNoticeBean inforNoticeBean);

    /**
     * 获取集合列表
     */
    void inForNoticeList(List<InforNoticeBean.DataBean> dataBeanList);

    /**
     * 获取第二个集合属性
     */
    void inForNoticeMethodList(List<InforNoticeBean.DataBean.FromUserBean> dataBeanList);
    /**
     * 是否加载更多
     */
    void inForNoticeLoad();

    /**
     * 是否分页
     */
    void inForNticePage(int page);

    /**
     * 清空集合列表
     */
    void inForNoticeCleanList(InforNoticeCleanBean dataBeancleanList);

    /**
     * 是否已经读
     */
    void inForNoticeTip(TipBean tipBean);

}
