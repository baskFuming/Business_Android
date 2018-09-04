package com.zwonline.top28.model;

import android.content.Context;

import com.lzy.imagepicker.bean.ImageItem;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.ApiService;
import com.zwonline.top28.api.BusinessCircleService;
import com.zwonline.top28.api.PayService;
import com.zwonline.top28.bean.AddBankBean;
import com.zwonline.top28.bean.AddFollowBean;
import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.AtentionDynamicHeadBean;
import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.BusinessCircle;
import com.zwonline.top28.bean.BusinessCircleBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.DynamicDetailsBean;
import com.zwonline.top28.bean.DynamicShareBean;
import com.zwonline.top28.bean.LikeListBean;
import com.zwonline.top28.bean.NewContentBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.PictursBean;
import com.zwonline.top28.bean.RefotPasswordBean;
import com.zwonline.top28.bean.SendNewMomentBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.ShieldUserBean;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SendFriendCircleModel {
    private SharedPreferencesUtils sp;

    //上传多张图片
    public Flowable<PictursBean> FriendCircleImage(Context context, List<File> compressFil) throws IOException {

        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("timestamp", String.valueOf(timestamp))
                .addFormDataPart("token", token)
                .addFormDataPart("sign", sign);
        //多个参数
        if (compressFil != null && !compressFil.isEmpty()) {
            for (int i = 0; i < compressFil.size(); i++) {
                File file = compressFil.get(i);
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("file[]", file.getName(), imageBody);
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        Flowable<PictursBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iPictures(parts);
        return flowable;
    }

    /**
     * 发表动态
     *
     * @param context
     * @param content
     * @param images
     * @return
     * @throws IOException
     */
    public Flowable<SendNewMomentBean> sendNewMoment(Context context, String images, String content) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("content", content);
        map.put("images", images);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<SendNewMomentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iSendNewMoment(String.valueOf(timestamp), token, sign, images, content);
        return flowable;
    }


    /**
     * 动态列表
     *
     * @param context
     * @param page
     * @param userId
     * @return
     * @throws IOException
     */
    public Flowable<NewContentBean> momentList(Context context, int page, String userId, String follow_flag, String recommend_flag) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", String.valueOf(page));
        map.put("user_id", userId);
        map.put("follow_flag", follow_flag);
        map.put("app_version", versionName);
        map.put("recommend_flag", recommend_flag);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<NewContentBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iMomentList(String.valueOf(timestamp), token, sign, page, versionName, follow_flag, recommend_flag, userId);
        return flowable;
    }

    /**
     * 意见反馈
     *
     * @param context
     * @param content
     * @param images
     * @return
     * @throws IOException
     */
    public Flowable<SettingBean> feedBack(Context context, String type, String content, String images) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("content", String.valueOf(content));
        map.put("images", images);
        map.put("type", type);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<SettingBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iFeedBack(String.valueOf(timestamp), token, sign, type, content, images);
        return flowable;
    }

    /**
     * 商机圈评论列表
     *
     * @param context
     * @param page
     * @param moment_id
     * @param comment_id
     * @param author_id
     * @param sort_by
     * @return
     * @throws IOException
     */
    public Flowable<DynamicDetailsBean> dynamicComment(Context context, int page, String moment_id, String comment_id, String author_id, String sort_by) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String versionName = LanguageUitils.getVersionName(context);
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", String.valueOf(page));
        map.put("moment_id", moment_id);
        map.put("comment_id", comment_id);
        map.put("author_id", author_id);
        map.put("sort_by", sort_by);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<DynamicDetailsBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iBusinessMomentList(String.valueOf(timestamp), token, sign, page, moment_id, comment_id, author_id, versionName, sort_by);
        return flowable;
    }

    /**
     * 动态分享
     *
     * @param context
     * @param moment_id
     * @return
     * @throws IOException
     */
    public Flowable<DynamicShareBean> dynamicShare(Context context, String moment_id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("moment_id", moment_id);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<DynamicShareBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iGetShareData(String.valueOf(timestamp), token, sign, moment_id);
        return flowable;
    }

    /**
     * 动态评论
     *
     * @param context
     * @param content
     * @param pid
     * @param ppid
     * @param moment_id
     * @return
     * @throws IOException
     */
    public Flowable<AddBankBean> newComment(Context context, String content, String pid, String ppid, String moment_id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("moment_id", moment_id);
        map.put("content", content);
        map.put("pid", pid);
        map.put("ppid", ppid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AddBankBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iNewComment(String.valueOf(timestamp), token, sign, content, pid, ppid, moment_id);
        return flowable;
    }

    /**
     * 删除动态
     *
     * @param context
     * @param moment_id
     * @return
     * @throws IOException
     */
    public Flowable<SettingBean> deleteMoment(Context context, String moment_id) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("moment_id", moment_id);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<SettingBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iDeleteMoment(String.valueOf(timestamp), token, sign, moment_id);
        return flowable;
    }


    /**
     * 关注
     *
     * @param context
     * @param type
     * @param uid
     * @param allow_be_call
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> attention(Context context, String type, String uid, String allow_be_call) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        map.put("allow_be_call", allow_be_call);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid, allow_be_call);
        return flowable;
    }

    /**
     * 一键关注
     *
     * @param context
     * @param type
     * @param uid
     * @param uid_list
     * @param allow_be_call
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> attentions(Context context, String type, String uid, String uid_list, String allow_be_call) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        map.put("uid_list", uid_list);
        map.put("allow_be_call", allow_be_call);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAttentions(token, String.valueOf(timestamp), sign, type, uid, uid_list, allow_be_call);
        return flowable;
    }


    //取消关注
    public Flowable<AttentionBean> UnAttention(Context context, String type, String uid) throws IOException {
        long timestamp = new Date().getTime() / 1000;//获取时间戳
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(timestamp));
        map.put("type", type);
        map.put("token", token);
        map.put("uid", uid);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(PayService.class, Api.url)
                .iAttention(token, String.valueOf(timestamp), sign, type, uid);
        return flowable;
    }


    /**
     * 屏蔽
     *
     * @param context
     * @param type
     * @param userId
     * @return
     * @throws IOException
     */
    public Flowable<RefotPasswordBean> blockUser(Context context, String type, String userId) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("user_id", userId);
        map.put("type", type);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<RefotPasswordBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iBlockUser(String.valueOf(timestamp), token, sign, versionName, userId, type);
        return flowable;
    }

    /**
     * 动态点赞
     *
     * @param context
     * @param moment_id
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> likeMoment(Context context, String moment_id) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("moment_id", moment_id);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iLikeMoment(String.valueOf(timestamp), token, sign, versionName, moment_id);
        return flowable;
    }

    /**
     * 屏蔽用户列表
     *
     * @param context
     * @param page
     * @return
     * @throws IOException
     */
    public Flowable<ShieldUserBean> blockUserList(Context context, int page) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", String.valueOf(page));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<ShieldUserBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iBlockUserList(String.valueOf(timestamp), token, sign, versionName, page);
        return flowable;
    }


    /**
     * 动态评论点赞
     *
     * @param context
     * @param comment_id
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> likeMomentComment(Context context, String comment_id) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("comment_id", comment_id);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iLikeMomentComment(String.valueOf(timestamp), token, sign, versionName, comment_id);
        return flowable;
    }

    /**
     * 删除动态评论
     *
     * @param context
     * @param comment_id
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> deleteComment(Context context, String comment_id, String moment_id) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("comment_id", comment_id);
        map.put("moment_id", moment_id);
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .iDeleteComment(String.valueOf(timestamp), token, sign, versionName, moment_id, comment_id);
        return flowable;
    }

    /**
     * 推荐用户列表
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<BusinessCircleBean> getBusinessCircle(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<BusinessCircleBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .GetRecommendUser(String.valueOf(timestamp), token, sign, versionName);
        return flowable;
    }

    /**
     * 推荐用户加载更多
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<AddFollowBean> getBusinessCircleList(Context context, int item_id) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        map.put("item_id", String.valueOf(item_id));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AddFollowBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .GetRecommendUserList(String.valueOf(timestamp), token, sign, versionName, item_id);
        return flowable;
    }

    /**
     * 关注动态顶部banner接口
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<AtentionDynamicHeadBean> mBannerRecommendUserList(Context context) throws IOException {
        String versionName = LanguageUitils.getVersionName(context);
        sp = SharedPreferencesUtils.getUtil();
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("app_version", versionName);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AtentionDynamicHeadBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .bannerRecommendUserList(String.valueOf(timestamp), token, sign, versionName);
        return flowable;
    }

    /**
     * 获取未读通知数接口
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<AttentionBean> mGetMyNotificationCount(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String versionName = LanguageUitils.getVersionName(context);
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<AttentionBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .getMyNotificationCount(String.valueOf(timestamp), token, sign);
        return flowable;
    }


    /**
     * 获取动态点赞列表接口
     *
     * @param context
     * @return
     * @throws IOException
     */
    public Flowable<LikeListBean> mGetLikeList(Context context, String moment_id, int page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String versionName = LanguageUitils.getVersionName(context);
        long timestamp = new Date().getTime() / 1000;//时间戳
        String token = (String) sp.getKey(context, "dialog", "");
        Map<String, String> map = new HashMap<>();
        map.put("moment_id", moment_id);
        map.put("token", token);
        map.put("app_version", versionName);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("page", String.valueOf(page));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        SignUtils.removeNullValue(map);
        Flowable<LikeListBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(BusinessCircleService.class, Api.url)
                .getLikeList(String.valueOf(timestamp), token, sign, moment_id, versionName, page);
        return flowable;
    }
}
