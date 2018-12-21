package com.zwonline.top28.api.service;

import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.bean.AddContractBean;
import com.zwonline.top28.bean.AddFriendBean;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.ArticleCommentBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.BalancePayBean;
import com.zwonline.top28.bean.BalanceRechargeBean;
import com.zwonline.top28.bean.BusinessCoinBean;
import com.zwonline.top28.bean.EarnIntegralBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.GetHongBaoBean;
import com.zwonline.top28.bean.HongBaoLeftCountBean;
import com.zwonline.top28.bean.HongBaoLogBean;
import com.zwonline.top28.bean.HongbaoPermissionBean;
import com.zwonline.top28.bean.IntegralBean;
import com.zwonline.top28.bean.IntegralPayBean;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.NotifyDetailsBean;
import com.zwonline.top28.bean.OptionContractBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;
import com.zwonline.top28.bean.RecommendTeamsBean;
import com.zwonline.top28.bean.SendYFBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.SignContractBean;
import com.zwonline.top28.bean.UpdateCodeBean;
import com.zwonline.top28.bean.YfRecordBean;
import com.zwonline.top28.bean.ZanBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by sdh on 2018/3/10.
 * 支付相关接口
 */

public interface PayService {

    //获取积分记录
    @FormUrlEncoded
    @POST("/App/Member/my_integral")
    Flowable<IntegralBean> iIntegralList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") String page,
            @Field("type") String type,
            @Field("sign") String sign);

    /**
     * 商机币详情
     *
     * @param timestamp
     * @param token
     * @param page
     * @param type
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("App/BusinessOpportunityCoin/balanceLog")
    Flowable<IntegralBean> iBalanceLog(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") int page,
            @Field("type") String type,
            @Field("sign") String sign);


    @FormUrlEncoded
    @POST("App/BusinessOpportunityCoin/balanceLog")
    Flowable<BusinessCoinBean> iBalanceLogs(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") int page,
            @Field("type") String type,
            @Field("sign") String sign);

    //获取积分记录
    @FormUrlEncoded
    @POST("/App/Member/my_integral")
    Flowable<IntegralBean> iAllIntegralList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") String page,
            @Field("sign") String sign);

    //赚取积分说明
    @FormUrlEncoded
    @POST("/App/Member/pointRewardIntro")
    Flowable<EarnIntegralBean> iEarnIntegral(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );


    //获取订单详情信息
    @FormUrlEncoded
    @POST("/App/Member/getOrderInfo")
    Flowable<OrderInfoBean> iGetOrderInfo(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("order_id") String orderId,
            @Field("sign") String sign);

    //获取订单支付状态接口
    @FormUrlEncoded
    @POST("/App/Member/getOrderPayStatus")
    Flowable<AmountPointsBean> iGetOrderPayStatus(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("order_id") String orderId,
            @Field("sign") String sign);


//    @FormUrlEncoded
//    @POST("/App/Member/getOrderPayStatus")
//    Observable<AmountPointsBean> iGetOrderPayStatus(
//            @Field("timestamp") String timestamp,
//            @Field("token") String token,
//            @Field("order_id") String orderId,
//            @Field("sign") String sign);

    //获取支付宝orderStr
    @FormUrlEncoded
    @POST("/App/Member/getOrderStrOfAlipay")
    Flowable<PrepayPayBean> iGetOrderAipayInfo(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("order_id") String orderId,
            @Field("sign") String sign);

    //获取微信orderStr
    @FormUrlEncoded
    @POST("/App/Member/getResponseOfLebao")
    Flowable<PrepayPayBean> iGetResponseOfLebao(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("order_id") String orderId,
            @Field("sign") String sign);

    //余额充值  3 支付宝支付 4 银行卡转账
    @FormUrlEncoded
    @POST("/App/Member/recharge")
    Flowable<BalanceRechargeBean> iBalanceRecharge(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("amount") Double amount,
            @Field("type") String rechargeType,
            @Field("sign") String sign);

    //余额充值 银行卡转账
    @FormUrlEncoded
    @POST("/App/Member/recharge")
    Flowable<AmountPointsBean> iBalanceRecharges(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("amount") Double amount,
            @Field("type") String rechargeType,
            @Field("sign") String sign);


    //付款方式 2支付宝 3银行卡充值
    @FormUrlEncoded
    @POST("App/BusinessOpportunityCoin/recharge")
    Flowable<IntegralPayBean> iPointRecharge(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("type") String rechargeType,
            @Field("amount") String amount,
            @Field("sign") String sign);


    //根据积分获取金额接口
    @FormUrlEncoded
    @POST("/App/Member/getAmountByPoints")
    Flowable<AmountPointsBean> iGetAmountByPoints(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("points") String points,
            @Field("sign") String sign);

    //返回积分单价接口
    @FormUrlEncoded
    @POST("App/BusinessOpportunityCoin/getPrice")
    Flowable<AmountPointsBean> iUnitPrice(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign);

    //检查是否和某人聊过天接口
    @FormUrlEncoded
    @POST("/App/Member/getChatLog")
    Flowable<ExamineChatBean> iExamineChat(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("uid") String uid,
            @Field("sign") String sign);

    //企业主页在线聊天接口
    @FormUrlEncoded
    @POST("/App/Member/toChat")
    Flowable<AmountPointsBean> iOnLineChat(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("uid") String uid,
            @Field("project_id") String projectId,
            @Field("kefu_uid") String kefuUid,
            @Field("sign") String sign);

    //企业主页在线聊天接口
    @FormUrlEncoded
    @POST("/App/Member/toChat")
    Flowable<AmountPointsBean> iOnLineChats(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("uid") String uid,
            @Field("project_id") String projectId,
            @Field("sign") String sign);

    //个人主页在线聊天接口
    @FormUrlEncoded
    @POST("/App/Member/toChat")
    Flowable<AmountPointsBean> iOnLineChated(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("uid") String uid,
            @Field("sign") String sign);

    //取消用户关注
    @FormUrlEncoded
    @POST("/App/Member/attention")
    Flowable<AttentionBean> iAttention(
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("type") String type,
            @Field("uid") String uid
    );

    //自动检查更新版本
    @FormUrlEncoded
    @POST("/App/Index/checkForLatestVersion")
    Flowable<UpdateCodeBean> iupdataVersion(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("platform") String platform,
            @Field("app_version") String app_version,
            @Field("version_code") String vCode
    );

    //记录用户行为的接口
    @FormUrlEncoded
    @POST("/App/Index/recordUserBehavior")
    Flowable<AmountPointsBean> iRecordUserBehavior(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("event") String event
    );

    //记录用户行为的接口
    @FormUrlEncoded
    @POST("/App/Index/recordUserBehavior")
    Flowable<AmountPointsBean> iRecordUserBehaviors(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("target_id") String targetId,
            @Field("event") String event
    );

    //选择合同
    @FormUrlEncoded
    @POST("/App/Member/getMyContract")
    Flowable<OptionContractBean> iOptionContract(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //选择合同模板
    @FormUrlEncoded
    @POST("/App/Member/getMyContract")
    Flowable<OptionContractBean> iOptionContracts(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("is_official_template") String is_official_template
    );

    //获取合同详情接口
    @FormUrlEncoded
    @POST("/App/Member/getContractDetail")
    Flowable<AddClauseBean> iContractDetail(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("contract_id") String contract_id
    );

    //获取合同详情接口
    @FormUrlEncoded
    @POST("/App/Member/addContact")
    Flowable<AddContractBean> iAddContact(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("project_id") String project_id,
            @Field("template_id") String template_id,
            @Field("title") String title,
            @Field("begin_date") String begin_date,
            @Field("end_date") String end_date,
            @Field("terms") String terms
    );

    //扫码获取合同详情接口
    @FormUrlEncoded
    @POST("/App/Member/getContractInfo")
    Flowable<SignContractBean> iSignContact(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("order_id") String orderId
    );

    //验证登录/App/Member/balanceRechargePoint
    @FormUrlEncoded
    @POST("/App/Public/gtValidate")
    Flowable<SettingBean> iGtValidate(
            @Field("timestamp") String timestamp,
            @Field("geetest_challenges") String geetest_challenge,
            @Field("geetest_validates") String geetest_validate,
            @Field("geetest_seccodes") String geetest_seccode,
            @Field("sign") String sign
    );

    //验证登录
    @FormUrlEncoded
    @POST("/App/Member/balanceRechargePoint")
    Flowable<BalancePayBean> iBalancePay(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("amount") String amount,
            @Field("sign") String sign
    );

    //    //文章评论
    @FormUrlEncoded
    @POST("App/Article/get_comments")
    Flowable<ArticleCommentBean> iArticleComment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("article_id") String article_id,
            @Field("comment_id") String comment_id,
            @Field("author_id") String author_id,
            @Field("sort_by") String sort_by,
            @Field("page") int page,
            @Field("sign") String sign
    );

    //点赞
    @FormUrlEncoded
    @POST("/App/Article/zan_comment")
    Flowable<ZanBean> iZan(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("comment_id") String comment_id,
            @Field("sign") String sign
    );

    /**
     * 添加好友的接口
     *
     * @param timestamp
     * @param token
     * @param keyword
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getUserAccountList")
    Flowable<AddFriendBean> iAddFriend(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("keyword") String keyword,
            @Field("sign") String sign
    );

    /**
     * 公告列表的接口
     *
     * @param timestamp
     * @param token
     * @param page
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/getNoticeList")
    Flowable<AnnouncementBean> iNoticeList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") String page,
            @Field("sign") String sign
    );


    /**
     * 公告详情
     *
     * @param timestamp
     * @param token
     * @param notice_id
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/getNoticeDetails")
    Flowable<NotifyDetailsBean> iNotifyDetails(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("notice_id") String notice_id,
            @Field("sign") String sign
    );

    /**
     * 公告未读数量/App/Member/readNotice
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/getNoticeNotReadCount")
    Flowable<NoticeNotReadCountBean> iNotReadCount(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("sign") String sign
    );

    /**
     * 记录公告是否已读
     *
     * @param timestamp
     * @param token
     * @param notice_id
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/readNotice")
    Flowable<NoticeNotReadCountBean> readNotice(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("notice_id") String notice_id,
            @Field("sign") String sign
    );

    /**
     * 发红包
     *
     * @param timestamp
     * @param token
     * @param postscript
     * @param total_amount
     * @param total_package
     * @param random_flag
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/sendHongbao")
    Flowable<SendYFBean> sendHongbao(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("postscript") String postscript,
            @Field("total_amount") String total_amount,
            @Field("total_package") String total_package,
            @Field("random_flag") int random_flag,
            @Field("sign") String sign
    );


    /**
     * 查看红包剩余量接口
     *
     * @param timestamp
     * @param token
     * @param hongbao_id
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getHongbaoLeftCount")
    Flowable<HongBaoLeftCountBean> hongbaoLeftCount(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("hongbao_id") String hongbao_id,
            @Field("sign") String sign
    );

    /**
     * 抢红包接口/App/Im/hongbaoLog
     *
     * @param timestamp
     * @param token
     * @param hongbao_id
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getHongbao")
    Flowable<GetHongBaoBean> getHongbao(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("hongbao_id") String hongbao_id,
            @Field("sign") String sign
    );

    /**
     * 抢红包记录
     *
     * @param timestamp
     * @param token
     * @param hongbao_id
     * @param page
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/hongbaoLog")
    Flowable<HongBaoLogBean> hongbaoLog(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("hongbao_id") String hongbao_id,
            @Field("page") int page,
            @Field("sign") String sign
    );

    /**
     * 总共鞅分记录/App/Im/getSendHongbaoPermission
     *
     * @param timestamp
     * @param token
     * @param page
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/hongbaoLog")
    Flowable<YfRecordBean> yFRecord(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") int page,
            @Field("sign") String sign
    );


    /**
     * 获取是否有发红包权限接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getSendHongbaoPermission")
    Flowable<HongbaoPermissionBean> hongBaoPermission(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("sign") String sign
    );


    /**
     * 发商机币红包
     *
     * @param timestamp
     * @param token
     * @param postscript
     * @param total_amount
     * @param total_package
     * @param random_flag
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/sendBocHongbao")
    Flowable<SendYFBean> sendBocHongbao(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("postscript") String postscript,
            @Field("total_amount") String total_amount,
            @Field("total_package") String total_package,
            @Field("random_flag") int random_flag,
            @Field("sign") String sign
    );


    /**
     * 商机币查看红包剩余量接口
     *
     * @param timestamp
     * @param token
     * @param hongbao_id
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getBocHongbaoLeftCount")
    Flowable<HongBaoLeftCountBean> getBocHongbaoLeftCount(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("hongbao_id") String hongbao_id,
            @Field("sign") String sign
    );


    /**
     * 抢商机币红包接口
     *
     * @param timestamp
     * @param token
     * @param hongbao_id
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getBocHongbao")
    Flowable<GetHongBaoBean> getBocHongbao(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("hongbao_id") String hongbao_id,
            @Field("sign") String sign
    );


    /**
     * 商机币抢红包记录
     *
     * @param timestamp
     * @param token
     * @param hongbao_id
     * @param page
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/bocHongbaoLog")
    Flowable<HongBaoLogBean> bocHongbaoLog(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("hongbao_id") String hongbao_id,
            @Field("page") int page,
            @Field("sign") String sign
    );

    /**
     * 总共商机币记录
     *
     * @param timestamp
     * @param token
     * @param page
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/bocHongbaoLog")
    Flowable<YfRecordBean> bocRecord(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("page") int page,
            @Field("sign") String sign
    );


    /**
     * 群推荐接口
     * App/Im/getRecommendTeamTag
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/recommendTeams")
    Flowable<RecommendTeamsBean> recommendTeams(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    /**
     * 群推荐接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/getRecommendTeamTag")
    Flowable<RecommendTeamsBean> groupTags(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );


    /**
     * 添加群标签
     *
     * @param timestamp
     * @param token
     * @param name
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/addTeamTag")
    Flowable<AttentionBean> addTeamTag(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("name") String name,
            @Field("sign") String sign
    );


}
