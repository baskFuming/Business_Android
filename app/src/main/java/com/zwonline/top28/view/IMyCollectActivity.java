package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyCollectBean;

import java.util.List;


/**
 * 描述：收藏
 *
 * @author YSG
 * @date 2017/12/24
 */
public interface IMyCollectActivity {
    /**
     * 判断是否有数据
     * @param flag
     */
    void showMyCollect(boolean flag);

    /**
     * 显示数据
     * @param collectList
     */
    void showMyCollectDate(List<MyCollectBean.DataBean> collectList);

    /**
     * 没有更多了
     */
    void noLoadMore();
}
