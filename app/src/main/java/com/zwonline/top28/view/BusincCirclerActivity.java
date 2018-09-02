package com.zwonline.top28.view;

import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircle;
import com.zwonline.top28.bean.BusinessCircleBean;

import java.util.List;

public interface BusincCirclerActivity {
    /**
     * 获取内容成功
     */
    void OnSuccess();

    /**
     * 获取内容失败
     */
    void OnErron();

    /**
     * 推荐人列表判断是否
     *
     * @param flag
     */
    void showBusinc(boolean flag);

    /**
     * 推荐人展示集合
     *
     * @param issueList
     */
    void showBusincDate(List<BusinessCircleBean.DataBean> issueList);

    /**
     * 没有更多了
     */
    void BusincNoLoadMore();

    void showBusincPro(List<BusinessCircleBean.DataBean.ListBean> bList);

    /**
     * 推荐列表
     *
     * @param issueList
     */
    void showBusincDateList(List<AddFollowBean.DataBean.ListBean> issueList);

    /**
     * 关注
     *
     * @param attentionBean
     */
    void showAttention(AttentionBean attentionBean);

    /**
     * 取消关注
     *
     * @param attentionBean
     */
    void showUnAttention(AttentionBean attentionBean);


}
