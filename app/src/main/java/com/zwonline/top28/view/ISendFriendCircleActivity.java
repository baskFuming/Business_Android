package com.zwonline.top28.view;

import com.zwonline.top28.activity.ShieldUserActivity;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.GiftBean;
import com.zwonline.top28.bean.GiftSumBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;

import java.util.List;

/**
 * 发表商机圈的view
 */
public interface ISendFriendCircleActivity {
    /**
     * 上传图片
     *
     * @param pictursBean
     */
    void showPictures(PictursBean pictursBean);

    /**
     * 发表商机圈
     *
     * @param sendNewMomentBean
     */
    void showSendNewMoment(SendNewMomentBean sendNewMomentBean);

    /**
     * 动态列表展示数据
     *
     * @param newList
     */
    void showConment(List<NewContentBean.DataBean> newList);

    /**
     * 意见反馈
     *
     * @param settingBean
     */
    void showFeedBack(SettingBean settingBean);

    /**
     * 动态评论
     *
     * @param dataBeanList
     */
    void showDynamicComment(List<DynamicDetailsBean.DataBean> dataBeanList);

    /**
     * 动态分享
     *
     * @param dynamicShareBean
     */
    void showDynamicShare(DynamicShareBean dynamicShareBean);

    /**
     * 添加评论
     *
     * @param addBankBean
     */
    void showNewComment(AddBankBean addBankBean);

    /**
     * 删除动态
     *
     * @param settingBean
     */
    void showDeleteMoment(SettingBean settingBean);

    /**
     * 关注
     *
     * @param attentionBean
     */
    void showAttention(AttentionBean attentionBean);

    /**
     * 一键关注
     *
     * @param attentionBean
     */
    void showAttentions(AttentionBean attentionBean);

    /**
     * 取消关注
     *
     * @param attentionBean
     */
    void showUnAttention(AttentionBean attentionBean);

    /**
     * 屏蔽
     *
     * @param refotPasswordBean
     */
    void showBlockUser(RefotPasswordBean refotPasswordBean);

    /**
     * 动态点赞
     *
     * @param attentionBean
     */
    void showLikeMoment(AttentionBean attentionBean);

    /**
     * 屏蔽列表
     *
     * @param shielduserList
     */
    void showBlockUserList(List<ShieldUserBean.DataBean> shielduserList);

    /**
     * 判断是否有数据
     *
     * @param flag
     */
    void showUserList(boolean flag);

    /**
     * 加载没有数据了
     */
    void noLoadMore();

    /**
     * 动态评论点赞
     *
     * @param attentionBean
     */
    void showLikeMomentComment(AttentionBean attentionBean);

    /**
     * 删除动态评论
     *
     * @param attentionBean
     */
    void showDeleteComment(AttentionBean attentionBean);


    /**
     * 推荐人展示集合
     *
     * @param issueList
     */
    void showBusincDate(List<BusinessCircleBean.DataBean> issueList);

    /**
     * 顶部banner接口
     *
     * @param issueList
     */
    void showAttentionDynamic(List<AtentionDynamicHeadBean.DataBean.ListBean> issueList);

    /**
     * 商机圈我的消息提醒
     *
     * @param attentionBean
     */
    void showGetMyNotificationCount(AttentionBean attentionBean);

    /**
     * 点赞列表
     *
     * @param likeList
     */
    void showGetLikeList(List<LikeListBean.DataBean> likeList);

    /**
     * 动态详情接口
     *
     * @param mommentList
     */
    void showMomentDetail(DynamicDetailsesBean mommentList);

    /**
     * 举报接口
     *
     * @param attentionBean
     */
    void showReport(AttentionBean attentionBean);

    /**
     * 礼物数量
     *
     * @param giftSumBean
     */
    void showGiftSummary(GiftSumBean giftSumBean);

    /**
     * 礼物
     *
     * @param giftBean
     */
    void showGift(GiftBean giftBean);
}
