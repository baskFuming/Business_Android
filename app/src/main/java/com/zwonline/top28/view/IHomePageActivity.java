package com.zwonline.top28.view;

import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.UserBean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public interface IHomePageActivity {
    /**
     * 获取内容失败
     */
    void onErro();

    /**
     * 获取用户信息
     *
     * @param companyBean
     */
    void showCompany(PersonageInfoBean companyBean);

    /**
     * 我的发布判断是否
     *
     * @param flag
     */
    void showMyIssue(boolean flag);

    /**
     * 发布文章
     *
     * @param issueList
     */
    void showMyIssueDate(List<MyIssueBean.DataBean> issueList);

    /**
     * 没有更多文章了
     */
    void issueNoLoadMore();

    /**
     * 我的分享判断是否
     *
     * @param flag
     */
    void showMyShare(boolean flag);

    /**
     * 我的分享
     *
     * @param shareList
     */
    void showMyShareDte(List<MyShareBean.DataBean> shareList);

    /**
     * 分享文章没有更多数据了
     */
    void shareNoLoadMore();

    /**
     * 文章详情
     *
     * @param homeDetails
     */
    void showHomeDetails(HomeDetailsBean homeDetails);//文章详情

    /**
     * 检查是否聊过天
     *
     * @param examineList
     */
    void showExamineChat(ExamineChatBean.DataBean examineList);

    /**
     * 在线聊天
     *
     * @param amountPointsBean
     */
    void showOnLineChat(AmountPointsBean amountPointsBean);
}
