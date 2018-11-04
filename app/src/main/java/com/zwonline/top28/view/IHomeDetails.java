package com.zwonline.top28.view;

import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCoinBean;
import com.zwonline.top28.bean.GiftBean;
import com.zwonline.top28.bean.GiftSumBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.RewardListBean;
import com.zwonline.top28.bean.ShareDataBean;

import java.util.List;

/**
 * Created by YU on 2017/12/11.
 */

public interface IHomeDetails {
    void showHomeDetails(HomeDetailsBean homeDetails);//文章详情

    void showShareData(ShareDataBean.DataBean shareList);//文章分享

    void commentSuccess();//获取文章成功

    void onErro();//获取内容失败


    void showCompany(PersonageInfoBean companyBean);//获取用户信息

    /**
     * 初始化收藏
     */
    void initFavorite();

    /**
     * 文章评论
     *
     * @param articleCommentList
     */
    void showArticleComment(List<ArticleCommentBean.DataBean> articleCommentList);

    /**
     * 评论失败
     *
     * @param articleCommentBean
     */
    void onError(AddCommentBean articleCommentBean);

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

    /**
     * 打赏接口
     *
     * @param attentionBean
     */
    void showSendGifts(AttentionBean attentionBean);

    /**
     * 打赏列表
     *
     * @param rewardLists
     */
    void showGiftList(List<RewardListBean.DataBean.ListBean> rewardLists);

    /**
     * 商机币余额
     *
     * @param rewardListBean
     */
    void showBocBanlance(BusinessCoinBean rewardListBean);
}
