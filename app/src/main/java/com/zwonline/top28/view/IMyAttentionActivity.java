package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyAttentionBean;

import java.util.List;

/**
 * @author YSG
 * @desc我的关注的v层
 * @date ${Date}
 */
public interface IMyAttentionActivity {
    /**
     * 判断是否有数据
     * @param flag
     */
    void showMyAttention(boolean flag);

    /**
     * 显示数据
     * @param mylist
     */
    void showMyAttentionData(List<MyAttentionBean.DataBean> mylist);

    /**
     * 没有更多数据
     */
    void noData();

}
