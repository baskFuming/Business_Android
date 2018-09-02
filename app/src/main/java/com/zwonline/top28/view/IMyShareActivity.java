package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyShareBean;

import java.util.List;

/**
 * 描述：我的分享
 *
 * @author YSG
 * @date 2017/12/25
 */
public interface IMyShareActivity {
    /**
     * 判断有没有数据
     *
     * @param flag
     */
    void showMyShare(boolean flag);

    /**
     * 显示数据
     *
     * @param shareList
     */
    void showMyShareDte(List<MyShareBean.DataBean> shareList);

    /**
     * 没有更多数据了
     */
    void noLoadMore();
}
