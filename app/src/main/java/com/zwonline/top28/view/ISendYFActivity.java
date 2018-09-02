package com.zwonline.top28.view;

import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.YfRecordBean;

import java.util.List;

/**
 * 鞅分红包
 */
public interface ISendYFActivity {
    /**
     * 发红包展示数据
     *
     * @param sendYFBean
     */
    void showYfdata(SendYFBean sendYFBean);

    /**
     * 红包记录
     *
     * @param hongBaoLogBeanList
     */
    void showSnatchYangFe(List<HongBaoLogBean.DataBean> hongBaoLogBeanList);

    /**
     * 红包剩余量查询
     *
     * @param hongBaoLeftCountBean
     */
    void showHongBaoLeftCount(HongBaoLeftCountBean hongBaoLeftCountBean);

    /**
     * 抢到总共的鞅分红包
     *
     */
    void showYFRecord(List<YfRecordBean.DataBean.ListBean> yfRecordBean,String ReceiveHong);
}
