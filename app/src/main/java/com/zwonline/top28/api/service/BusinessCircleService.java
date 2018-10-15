package com.zwonline.top28.api.service;

import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicDetailsesBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.InforNoticeBean;
import com.zwonline.top28.bean.InforNoticeCleanBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RealBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.bean.TipBean;
import com.zwonline.top28.bean.UserInfoBean;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BusinessCircleService {
    //上传多张图片
    @Multipart
    @POST("/App/Public/uploadImage")
    Flowable<PictursBean> iPictures(@Part List<MultipartBody.Part> partList);


    /**
     * 商机圈发表动态
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param images
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/newMoment")
    Flowable<SendNewMomentBean> iSendNewMoment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("images") String images,
            @Field("content") String content
    );

    /**
     * 动态列表接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param page
     * @param user_id
     * @return
     */

    @FormUrlEncoded
    @POST("/App/BusinessCircle/momentList")
    Flowable<NewContentBean> iMomentList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") int page,
            @Field("app_version") String app_version,
            @Field("follow_flag") String follow_flag,
            @Field("recommend_flag") String recommend_flag,
            @Field("user_id") String user_id
    );

    /**
     * 动态评论列表接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param page
     * @param moment_id
     * @param comment_id
     * @param author_id
     * @param sort_by
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/getComments")
    Flowable<DynamicDetailsBean> iBusinessMomentList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") int page,
            @Field("moment_id") String moment_id,
            @Field("comment_id") String comment_id,
            @Field("author_id") String author_id,
            @Field("app_version") String app_version,
            @Field("sort_by") String sort_by
    );

    /**
     * 动态分享
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param moment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/getShareData")
    Flowable<DynamicShareBean> iGetShareData(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("moment_id") String moment_id
    );

    /**
     * 动态评论
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param content
     * @param pid
     * @param ppid
     * @param moment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/newComment")
    Flowable<AddBankBean> iNewComment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("content") String content,
            @Field("pid") String pid,
            @Field("ppid") String ppid,
            @Field("moment_id") String moment_id
    );

    /**
     * 删除动态
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param moment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/deleteMoment")
    Flowable<SettingBean> iDeleteMoment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("moment_id") String moment_id
    );

    /**
     * 屏蔽用户
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param user_id
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/blockUser")
    Flowable<RefotPasswordBean> iBlockUser(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("user_id") String user_id,
            @Field("type") String type
    );

    /**
     * 动态点赞
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param moment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/likeMoment")
    Flowable<AttentionBean> iLikeMoment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("moment_id") String moment_id
    );

    /**
     * 屏蔽用户列表
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/blockUserList")
    Flowable<ShieldUserBean> iBlockUserList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("page") int page
    );

    /**
     * 动态评论点赞
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param comment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/likeMomentComment")
    Flowable<AttentionBean> iLikeMomentComment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("comment_id") String comment_id
    );

    /**
     * 删除动态评论
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param comment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/deleteComment")
    Flowable<AttentionBean> iDeleteComment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("moment_id") String moment_id,
            @Field("comment_id") String comment_id
    );

    /**
     * 分享动态成功回调
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param comment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/shareMomentSuccessfullyCallback")
    Flowable<AttentionBean> iShareMomentSuccess(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("id") String comment_id
    );

    /**
     * 推荐关注人列表接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/getRecommendUser")
    Flowable<BusinessCircleBean> GetRecommendUser(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version
    );

    /**
     * 推荐关注人列表接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param item_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/getRecommendUserList")
    Flowable<AddFollowBean> GetRecommendUserList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("item_id") int item_id
    );

    /**
     * 关注动态顶部banner接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/bannerRecommendUserList")
    Flowable<AtentionDynamicHeadBean> bannerRecommendUserList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version

    );

    /**
     * 关注动态顶部banner接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/getMyUnreadNotificationCount")
    Flowable<AttentionBean> getMyNotificationCount(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign

    );

    /**
     * 获取消息列表
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/getMyNotifications")
    Flowable<InforNoticeBean> InforNoticeList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("page") int page
    );

    /**
     * 清空消息列表
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/clearNotifications")
    Flowable<InforNoticeCleanBean> InforNoticeCLeanList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("page") String page
    );

    /**
     * 是否已经读取
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/markNotificationRead")
    Flowable<TipBean> markNotificationRead(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("page") int page
    );


    /**
     * 微信分享名片
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param show_realname
     * @param show_telephone
     * @param show_weixin
     * @param show_address
     * @param show_enterprise
     * @param show_position
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/sjPageCallback")
    Flowable<RealBean> sjPageCallback(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("app_version") String app_version,
            @Field("show_realname") String show_realname,
            @Field("show_telephone") String show_telephone,
            @Field("show_weixin") String show_weixin,
            @Field("show_address") String show_address,
            @Field("show_enterprise") String show_enterprise,
            @Field("show_position") String show_position
    );

    //微信分享名片更新用户
    @FormUrlEncoded
    @POST("/App/Member/update_profile")
    Flowable<SettingBean> iSetting(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("nick_name") String nick_name,
            @Field("real_name") String real_name,
            @Field("sex") int sex,
            @Field("age") String age,
            @Field("address") String address,
            @Field("favourite_industry") String favourite_industry,
            @Field("bio") String bio,
            @Field("weixin") String weixin,
            @Field("email") String email,
            @Field("telephone") String telephone,
            @Field("job_cate_pid") String job_cate_pid,
            @Field("enterprise") String enterprise,
            @Field("position") String position
    );

    //获取个人信息
    @FormUrlEncoded
    @POST("/App/Member/user_info")
    Flowable<UserInfoBean> iUserInfo(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    /**
     * 获取动态点赞列表接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param app_version
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/getLikeList")
    Flowable<LikeListBean> getLikeList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("moment_id") String moment_id,
            @Field("app_version") String app_version,
            @Field("page") int page
    );

    /**
     * 动态详情接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param moment_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/momentDetail")
    Flowable<DynamicDetailsesBean> momentDetail(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("moment_id") String moment_id
    );

    /**
     * 举报接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param target_id
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/report")
    Flowable<AttentionBean> report(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("target_type") String target_type,
            @Field("junk_type") String junk_type,
            @Field("target_id") String target_id
    );

    /**
     * 商机圈明星推荐
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/BusinessCircle/starRecommendUserList")
    Flowable<AtentionDynamicHeadBean> starRecommendUserList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign

    );


}
