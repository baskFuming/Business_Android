package com.zwonline.top28.constants;

import com.zwonline.top28.api.Api;

/**
 * Created by sdh on 2018/3/5.
 */

public class BizConstant {

    /**
     * 获取验证码需要传的类型TYPE
     */
    public static final String TYPE_LOGIN = "login";
    public static final String TYPE_FINDPWD = "findpwd";
    /**
     * 热商机
     */
    public static final String HOT_BUSINESS = "http://www.91jmw.com/";

    /**
     * 极验验证API验证
     */
    public static final String CAPTCHAURL = Api.baseUrl() + "/App/Public/getGtResponseStr";
    public static final String VALIDATEURL = Api.baseUrl() + "/App/Public/getGtResponseStr";
    /**
     * 登录完成返回
     */
    public static final String INFO_LOGIN = "3";
    public static final String MYLOGIN = "4";
    public static final String QITA_LOGIN = "5";
    public static final String BUSINESS_LOGIN = "1";

    /**
     * 推荐名字
     */
    public static final String BIZ_RECOMMEND_ID = "300";
    public static final String User_Agent = "app28/";
    public static final String SEX = "1";
    /**
     * 默认页码和类型
     */
    public static final int PAGE = 1;
    public static final String TYPE_ONE = "1";
    public static final String TYPE_TWO = "2";

    /**
     * 中国台湾渠道
     */
    public static final String CHANNEL_GOOGLE_ZH_RTW = "google_zh_rtw";
    public static final String LANGUAGE_ZH_TW = "zh-tw";

    /**
     * 包名
     */
    public static final String CHANNEL_TW = "com.zwonline.top28.zhtw";
    public static final String CHANNEL_ZH = "com.zwonline.top28";
    public static final String PACKGE = "com.zwonline.top28.activity.";
    public static final String YINGYONGBAO = "com.tencent.android.qqdownloader";
    public static final String XIAOMI = "com.xiaomi.market";
    public static final String OPPO = "com.oppo.market";
//    public static final String VIVO = "com.tencent.android.qqdownloader";
    /**
     * 成功
     */
    public static final String IS_SUC = "1";
    /**
     * 失败
     */
    public static final String IS_FAIL = "0";

    /**
     * 收款点击
     */
    public static final String CLICK_PAY = "click_pay";
    /**
     * 手约保
     */
    public static final String CLICK_SYB = "click_syb";
    /**
     * 二维码宽度
     */
    public static final Integer QR_CODE_WIDTH = 600;
    public static final Integer QR_CODE_HEIGHT = 600;

    public static final Integer QR_CODE_WIDTHS = 1500;
    public static final Integer QR_CODE_HEIGHTS = 1500;
    /**
     * 2支付宝 3银行卡充值
     * 支付方式
     */
    public static final String ALIPAY_METHOD = "2";
    public static final String UNIONPAY_METHOD = "3";
    public static final String POS_METHOD = "4";
    /**
     * 充值支付方式
     * [3 支付宝支付 4 银行卡转账]
     */
    public static final String ALIPAY_RECHARGE_METHOD = "3";
    public static final String UNIONPAY_RECHARGE_METHOD = "4";


    /**
     * 扫一扫订单id
     */
    public static final String ORDERINFO = "http://top28app//orderinfo/";

    /**
     * 是否收藏 1 收藏 2 取消收藏 （默认不传递是收藏）
     */
    public static final String ALREADY_FAVORITE = "1";
    public static final String NO_FAVORITE = "0";

    /**
     * 审核状态 0审核中1审核通过2审核拒绝3注销
     */
    public static final String PROJECT_BEGIN_CHECK_STATUS = "0";
    public static final String PROJECT_SUC_CHECK_STATUS = "1";
    public static final String PROJECT_FAIL_CHECK_STATUS = "2";
    public static final String PROJECT_CANCEL_CHECK_STATUS = "3";

    /**
     * pos机收款    pos机积分充值
     */
    public static final String POS_GATHERING = "1";
    public static final String POS_INTEGRAL = "2";

    /**
     * 1 状态成功
     */
    public static final String ORDER_PAY_SUCCESS = "1";

    /**
     * 判断是否是企业用户
     */
    public static final String ENTERPRISE_tRUE = "0";
    public static final String ENTERPRISE_FALSE = "1";

    /**
     *
     */
    public static final String ATTENTION_CHATTTE = "1";
    public static final String NO_ATTENTION_CHATTTE = "0";

    /**
     * 提现拦截添加银行卡
     */
    public static final String INTERCEPT_ADD_BANK = "http://top28app//pushToAddBankCard";


    /**
     * 记录用户行为open_app  click_search_bar
     */
    public static final String OPEN_APP = "open_app";
    public static final String CLOSE_APP = "close_app";
    public static final String SIGN_IN = "sign_in";
    public static final String SIGN_OUT = "sign_out";
    public static final String CLICK_SEARCH_BAR = "click_search_bar";
    public static final String DO_SEARCH = "do_search";
    public static final String VIEW_ARTICLE = "view_article";
    public static final String CLICK_FOLLOW = "click_follow_btn_in_article";
    public static final String CLICK_USER_AVATAR = "click_user_avatar_in_article";
    public static final String FAVORITE_ARTICLE = "favorite_article";
    public static final String SUBMIT_COMMENT = "submit_comment";
    public static final String CLICK_SHARE_ICON = "click_share_article_icon";
    public static final String SHARED_ARTICLE = "shared_article";
    public static final String CLICK_SETTING_ICON = "click_setting_icon";
    public static final String EDITED_PROFILE = "edited_profile";
    public static final String CLICK_PERSONAL_AVATAR = "click_personal_avatar";
    public static final String EDITED_PASSWORD = "edited_password";
    public static final String CLICK_EDIT_PASSWORD = "click_edit_password_label";
    public static final String CLICK_PERSONAL_FOLLOW = "click_personal_follow";
    public static final String CLICK_PERSONAL_FANS = "click_personal_fans";
    public static final String CLICK_PERSONAL_FAVORITE = "click_personal_favorite";
    public static final String CLICK_PUBLISH_ARTICLE = "click_publish_article";
    public static final String CLICK_TRANSFER_ARTICLE = "click_transfer_article";
    public static final String CLICK_WALLET = "click_wallet";
    public static final String CLICK_POINT = "click_point";
    public static final String CLICK_SHOUYUEBAO = "click_shouyuebao";
    public static final String CLICK_RECOMMEND_USER = "click_recommend_user";
    public static final String CLICK_PERSONAL_PUBLISH = "click_personal_publish";
    public static final String CLICK_PERSONAL_SHARE = "click_personal_share";
    public static final String CLICK_PERSONAL_INVESTIGATION = "click_personal_investigation";
    public static final String CLICK_ENTERPRISE_MANAGE = "click_enterprise_manage";
    public static final String CLICK_ENTERPRISE_AUTH = "click_enterprise_auth";

    /**
     * 粉丝
     */
    public static final String FOLLOW = "follow";
    public static final String UN_FOLLOW = "un_follow";
    public static final String CONTACTED = "contacted";
    public static final String UN_CONTACTED = "un_contact";


    /**
     * 合同
     */
    public static final String CUSTOM_CONTRACT = "1";
    public static final String MOBAN = "2";
    public static final String SIGN_CONTRACT = "3";
    /**
     * 联系客服
     */
    public static final String SEVERCEUid = "272";


    /**
     * 云信
     */
    public static final boolean DEBUG = Boolean.parseBoolean("true");
    public static final String APPLICATION_ID = "com.netease.nim.demo";
    public static final String BUILD_TYPE = "debug";
    public static final String FLAVOR = "";
    public static final int VERSION_CODE = 47;
    public static final String VERSION_NAME = "5.0.0";
    // Fields from default config.
    public static final String BUILD_DATE = "2018-5-17 9:28:39";
    public static final String GIT_REVISION = "";

    /**
     * 屏蔽
     */
    public static final String PINGBI = "block";
    public static final String UNPINGBI = "unblock";

    /**
     * 商机圈评论返回EventBus通知
     */
    public static final String NEW = "1";
    public static final String RECOMMEND = "2";
    public static final String ATTENTION = "3";
    public static final String MY = "4";


    public static final int MAGIC = 0;

    /**
     * 举报
     */

    public static final String USER = "1";
    public static final String articleARTICLE = "2";
    public static final String DYNAMIC = "3";
    public static final String COMMENT = "4";

    /**
     * 赚取算力
     */
    public static final String EARNINTEGRAL = "http://toutiao.28.com/Members/boc_list.html";
    /**
     * 推荐用户
     */
    public static final String RECOMMENTUSER = "http://toutiao.28.com/Members/recommend_list.html";
    public static final String YSMY="http://store-toutiao.28.com/wap/index.php?ctl=settings";
    public static final String YSSEARCH="http://store-toutiao.28.com/wap/index.php?ctl=deals";

}
