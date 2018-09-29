package com.zwonline.top28.api;


import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AddCommentBean;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.AttentionChatBean;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.BankBean;
import com.zwonline.top28.bean.BannerAdBean;
import com.zwonline.top28.bean.BannerBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.CompanyBean;
import com.zwonline.top28.bean.DetailsBean;
import com.zwonline.top28.bean.EnterpriseStatusBean;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.HomeBean;
import com.zwonline.top28.bean.HomeClassBean;
import com.zwonline.top28.bean.HomeDetailsBean;
import com.zwonline.top28.bean.HotExamineBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.JZHOBean;
import com.zwonline.top28.bean.LoginBean;
import com.zwonline.top28.bean.MyAttentionBean;
import com.zwonline.top28.bean.MyBillBean;
import com.zwonline.top28.bean.MyCollectBean;
import com.zwonline.top28.bean.MyCurrencyBean;
import com.zwonline.top28.bean.MyExamine;
import com.zwonline.top28.bean.MyFansBean;
import com.zwonline.top28.bean.MyIssueBean;
import com.zwonline.top28.bean.MyPageBean;
import com.zwonline.top28.bean.MyShareBean;
import com.zwonline.top28.bean.PaymentBean;
import com.zwonline.top28.bean.PersonageInfoBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.bean.QrCodeBean;
import com.zwonline.top28.bean.RecommendBean;
import com.zwonline.top28.bean.RegisterBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShareDataBean;
import com.zwonline.top28.bean.ShortMessage;
import com.zwonline.top28.bean.UnclaimedMbpCountBean;
import com.zwonline.top28.bean.UserInfoBean;
import com.zwonline.top28.bean.VideoBean;
import com.zwonline.top28.bean.WithdrawRecordBean;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 1.类的用途
 * 2.@authorDell
 * 3.@date2017/11/6 15:26
 */

public interface ApiService {

    //获取手机验证码
    @FormUrlEncoded
    @POST("/App/Public/send_sms_code")
    Flowable<ShortMessage> getPhoneCode(
            @Field("mobile") String phone,
            @Field("timestamp") String timestamp,
            @Field("type") String type,
            @Field("country_code") String country_code,
            @Field("sign") String sign);

    //注册手机用户
    @FormUrlEncoded
    @POST("/App/Public/register")
    Flowable<RegisterBean> registerUser(
            @Field("mobile") String mobile,
            @Field("smscode") String smscode,
            @Field("password") String password,
            @Field("passwordVerify") String passwordVerify,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("token") String dialog
    );

    //账号登录
    @FormUrlEncoded
    @POST("/App/Public/login")
    Flowable<LoginBean> loginUserNumber(
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );


    //验证码登录
    @FormUrlEncoded
    @POST("/App/Public/login_sms")
    Flowable<LoginBean> loginUserVerify(
            @Field("mobile") String mobile,
            @Field("smscode") String smscode,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("token") String token
    );

    //验证码登录有邀请码
    @FormUrlEncoded
    @POST("/App/Public/login_sms")
    Flowable<LoginBean> loginUserVerifys(
            @Field("mobile") String mobile,
            @Field("smscode") String smscode,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("incode") String incode,
            @Field("token") String token
    );

    //首页
    @FormUrlEncoded
    @POST("/App/Index/article_list")
    Flowable<HomeClassBean> iHomePage(
            @Field("page") String page,
            @Field("cate_id") String cate_id,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("show_ad") String showAd,
            @Field("app_version") String app_version,
            @Field("sign") String sign
    );

    //首页推荐
    @FormUrlEncoded
    @POST("/App/Index/article_list")
    Flowable<HomeClassBean> iHomeRecommend(
            @Field("page") String page,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("show_ad") String showAd,
            @Field("app_version") String app_version,
            @Field("sign") String sign
    );

    //首页分类
    @FormUrlEncoded
    @POST("/App/Index/article_category")
    Flowable<HomeBean> iHomeClass(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("sign") String sign
    );

    //文章详情
    @FormUrlEncoded
    @POST("/App/Index/article_view")
    Flowable<HomeDetailsBean> iHomeDetails(
            @Field("id") int id,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //文章详情
    @FormUrlEncoded
    @POST("/App/Index/article_view")
    Flowable<HomeDetailsBean> iHomeDetail(
            @Field("id") int id,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    //文章详情
    @FormUrlEncoded
    @POST("/App/Index/article_view")
    Flowable<DetailsBean> iDetails(
            @Field("id") int id,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //文章分享
    @FormUrlEncoded
    @POST("App/Article/get_share_data")
    Flowable<ShareDataBean> iSharData(
            @Field("article_id") String id,
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    //搜索
    @FormUrlEncoded
    @POST("/App/Index/article_search")
    Flowable<HomeClassBean> iSearch(
            @Field("title") String title,
            @Field("page") String page,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //添加评论
    @FormUrlEncoded
    @POST("/App/Article/add_comment")
    Flowable<AddCommentBean> AddComment(
            @Field("article_id") String article_id,
            @Field("content") String content,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //回复评论
    @FormUrlEncoded
    @POST("/App/Article/add_comment")
    Flowable<AddCommentBean> ReplyComment(
            @Field("article_id") String article_id,
            @Field("content") String content,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("ppid") String ppid,
            @Field("pid") String pid
    );

    //收藏
    @FormUrlEncoded
    @POST("/App/Article/favorite")
    Flowable<AddCommentBean> Collect(
            @Field("article_id") String article_id,
            @Field("type") String fType,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //商机分类
    @FormUrlEncoded
    @POST("/App/Business/category_list")
    Flowable<BusinessClassifyBean> iBusinessClass(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //商机轮播图
    @FormUrlEncoded
    @POST("/App/BusinessOpportunity/banner_list")
    Flowable<BannerBean> iBusinessBanner(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //商机推荐项目
    @FormUrlEncoded
    @POST("/App/Business/recommended_list")
    Flowable<RecommendBean> iRecommend(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //商机列表
    @FormUrlEncoded
    @POST("/App/Business/business_list")
    Flowable<BusinessListBean> iBusinessList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //创业资讯
    @FormUrlEncoded
    @POST("/App/BusinessOpportunity/news_list")
    Flowable<JZHOBean> iJZHO(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //商机列表
    @FormUrlEncoded
    @POST("/App/Business/business_list")
    Flowable<BusinessListBean> iBusiness(
            @Field("page") String page,
            @Field("cate_id") String cate_id,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //商机搜索
    @FormUrlEncoded
    @POST("/App/Business/business_search")
    Flowable<BusinessListBean> bSearch(
            @Field("title") String title,
            @Field("page") String page,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //热门考察项目
    @FormUrlEncoded
    @POST("/App/Review/research_project")
    Flowable<HotExamineBean> iHotExamine(
            @Field("page") String page,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign
    );

    //我的考察项目
    @FormUrlEncoded
    @POST("/App/Review/research_list")
    Flowable<MyExamine> iMyExamine(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") int page
    );

    //视频列表
    @FormUrlEncoded
    @POST("/App/Video/video_list")
    Flowable<VideoBean> iVideo(
            @Field("cate_id") String cate_id,
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") String page
    );

    //视频列表推荐
    @FormUrlEncoded
    @POST("/App/Video/video_list")
    Flowable<VideoBean> iVideoRecommennt(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") String page
    );

    //视频列表推荐
    @FormUrlEncoded
    @POST("/App/Video/update_video_view")
    Flowable<VideoBean> iVideoCount(
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("id") String id
    );

    //我的关注
    @FormUrlEncoded
    @POST("/App/Member/my_follow")
    Flowable<MyAttentionBean> iMyAttention(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid,
            @Field("page") int page
    );

    //我的关注
    @FormUrlEncoded
    @POST("/App/Member/my_follow")
    Flowable<MyAttentionBean> iAttention(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") int page
    );

    //关注/取消关注   用户
    @FormUrlEncoded
    @POST("/App/Member/attention")
    Flowable<AttentionBean> iAttention(
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("type") String type,
            @Field("uid") String uid,
            @Field("allow_be_call") String allow_be_call

    );


    //关注多个用户一键关注
    @FormUrlEncoded
    @POST("/App/Member/attention")
    Flowable<AttentionBean> iAttentions(
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("type") String type,
            @Field("uid") String uid,
            @Field("uid_list") String uid_list,
            @Field("allow_be_call") String allow_be_call

    );

    //关注/取消关注    项目
    @FormUrlEncoded
    @POST("/App/Member/attentionEnterprise")
    Flowable<AttentionBean> iAttentionProject(
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("type") String type,
            @Field("enterpriseId") String pid,
            @Field("allow_be_call") String allow_be_call

    );

    //个人主页的粉丝
    @FormUrlEncoded
    @POST("/App/Member/my_fans")
    Flowable<MyFansBean> iFans(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid,
            @Field("page") int page
    );

    //我的粉丝
    @FormUrlEncoded
    @POST("/App/Member/my_fans")
    Flowable<MyFansBean> iMyFans(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //我的粉丝
    @FormUrlEncoded
    @POST("/App/Member/my_fans")
    Flowable<MyFansBean> iMyFanses(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") int page,
            @Field("filter") String filter
    );

    //收藏
    @FormUrlEncoded
    @POST("/App/Member/my_favorite")
    Flowable<MyCollectBean> iMyCollect(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid,
            @Field("page") int page
    );

    //我的发布
    @FormUrlEncoded
    @POST("/App/Member/my_issue")
    Flowable<MyIssueBean> iMyIssue(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid,
            @Field("page") int page
    );

    //我的发布
    @FormUrlEncoded
    @POST("/App/Member/my_issue")
    Flowable<MyIssueBean> iMyIssues(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") int page
    );

    //我的分享
    @FormUrlEncoded
    @POST("App/Member/my_share")
    Flowable<MyShareBean> iMyShare(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid,
            @Field("page") int page
    );

    //我的创业币
    @FormUrlEncoded
    @POST("/App/Member/my_integral")
    Flowable<MyCurrencyBean> iMyCurrency(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //收付款记录
    @FormUrlEncoded
    @POST("/App/Member/my_order")
    Flowable<PaymentBean> iPayment(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") String page
    );

    //我的账单
    @FormUrlEncoded
    @POST("/App/Member/my_bill")
    Flowable<MyBillBean> iMyBill(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //添加银行卡号
    @FormUrlEncoded
    @POST("/App/Member/add_bankcard")
    Flowable<AddBankBean> iAddBank(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("card_holder") String card_holder,
            @Field("card_number") String card_number,
            @Field("card_bank") String card_bank
    );

    //银行卡列表
    @FormUrlEncoded
    @POST("/App/Member/my_bankcard")
    Flowable<BankBean> iBank(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //解绑银行卡列表
    @FormUrlEncoded
    @POST("/App/Member/unbindBank")
    Flowable<AmountPointsBean> iUnbindBank(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("id") String id,
            @Field("sign") String sign
    );


    //项目列表
    @FormUrlEncoded
    @POST("/App/Member/getProjects")
    Flowable<ProjectBean> isProjectList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("uid") String uid,
            @Field("sign") String sign
    );

    //获取企业认证的审核状态接口
    @FormUrlEncoded
    @POST("/App/Member/get_enterprise_auth_status")
    Flowable<EnterpriseStatusBean> iGetEnterDetail(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("project_id") String projectId,
            @Field("sign") String sign
    );

    //余额
    @FormUrlEncoded
    @POST("App/Member/my_balance")
    Flowable<BalanceBean> iBalance(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //生成二维码
    @FormUrlEncoded
    @POST("/App/Member/make_collection_qrcode")
    Flowable<QrCodeBean> iQrcode(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("project_id") String project_id,
            @Field("amount") Double amount,
            @Field("contract_id") String contract_id
    );

    //提现
    @FormUrlEncoded
    @POST("/App/Member/widthdraw_deposit")
    Flowable<HeadBean> iWithdraw(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("amount") int amount,
            @Field("bank_card_id") String bank_card_id
    );

    //提现记录
    @FormUrlEncoded
    @POST("App/Member/widthdraw_deposit_log")
    Flowable<WithdrawRecordBean> iWithdrawRecord(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("page") String page
    );

    //修改头像
    @Multipart
    @POST("App/Member/update_avatar")
    Flowable<HeadBean> iHead(@Part List<MultipartBody.Part> partList);

    //上传图片
    @Multipart
    @POST("App/Member/upload_image")
    Flowable<PicturBean> iPicture(@Part List<MultipartBody.Part> partList);


    //修改资料
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

    //行业分类
    @FormUrlEncoded
    @POST("App/Member/favourite_industry")
    Flowable<IndustryBean> Industry(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //企业认证
    @Multipart
    @POST("App/Member/enterprise_auth")
    Flowable<HeadBean> iAeo(@Part List<MultipartBody.Part> partList);

//    //企业认证
//    @Multipart
//    @POST("App/Member/enterprise_auth")
//    Flowable<HeadBean> iAeo(
//            @Field("timestamp") String timestamp,
//            @Field("token") String token,
//            @Field("sign") String sign,
//            @Field("enterprise_name")String enterprise_name,
//            @Field("company_name")String company_name,
//            @Field("slug")String slug,
//            @Field("cate_id")String cate_id,
//            @Field("enterprise_contacts")String enterprise_contacts,
//            @Field("enterprise_contact_tel")String enterprise_contact_tel,
//            @Field("enterprise_contact_address")String enterprise_contact_address,
//            @Field("img[]")ArrayList<Integer> img
//    );

    //企业认证分类
    @FormUrlEncoded
    @POST("/App/Member/enterprise_category")
    Flowable<IndustryBean> iAeoClass(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );

    //企业主页
    @FormUrlEncoded
    @POST("/App/Business/business_detail")
    Flowable<CompanyBean> iCompany(
            @Field("token") String token,
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("/App/Member/user_homepage_new")
    Flowable<PersonageInfoBean> iUser(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid
    );

    //获取个人信息
    @FormUrlEncoded
    @POST("/App/Member/user_info")
    Flowable<UserInfoBean> iUserInfo(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign
    );


    //重置密码
    @FormUrlEncoded
    @POST("/App/Member/edit_password")
    Flowable<SettingBean> iRetPossword(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("password") String password
    );

    //验证忘记密码验证码
    @FormUrlEncoded
    @POST("/App/Public/login_sms")
    Flowable<LoginBean> iforgetPossword(
            @Field("timestamp") String timestamp,
            @Field("sign") String sign,
            @Field("mobile") String mobile,
            @Field("smscode") String code,
            @Field("token") String token
    );

    //在线沟通
    @FormUrlEncoded
    @POST("/App/Member/chatWithFans")
    Flowable<AttentionChatBean> iAttentionChat(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("uid") String uid,
            @Field("user_type") String user_type
    );

    //分享文章成功回調
    @FormUrlEncoded
    @POST("/App/Article/shareArticleSuccessfullyCallback")
    Flowable<AmountPointsBean> iShareSuccess(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("id") String id

    );


    /**
     * 意见反馈
     *
     * @param timestamp
     * @param token
     * @param sign
     * @param content
     * @param images
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/feedBack")
    Flowable<SettingBean> iFeedBack(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign,
            @Field("type") String type,
            @Field("content") String content,
            @Field("images") String images
    );

    /**
     * 未领鞅分数量
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Member/unclaimedMbpCount")
    Flowable<UnclaimedMbpCountBean> unclaimedMbpCount(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("sign") String sign

    );

    /**
     * 消息顶部banner广告接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Im/bannerAd")
    Flowable<BannerAdBean> bannerAd(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("sign") String sign

    );

    /**
     * 个人中心菜单接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Public/person_center_menu")
    Flowable<MyPageBean> personCenterMenu(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("sign") String sign

    );


}
