package com.zwonline.top28.model;

import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.PayService;
import com.zwonline.top28.bean.AnnouncementBean;
import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.NotifyDetailsBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 公告的M层
 */
public class AnnouncementMedel {
    private SharedPreferencesUtils sp;

    /**
     * 公告列表
     *
     * @param context
     * @param page
     * @return
     * @throws IOException
     */
    public Flowable<AnnouncementBean> announcement(Context context, String page) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("page", page);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<AnnouncementBean> flowable = ApiRetrofit.getInstance().getClientApi(PayService.class, Api.baseUrl())
                .iNoticeList(String.valueOf(timestamp), token, page, sign);
        return flowable;
    }

    /**
     * 公告详情
     * @param context
     * @param noticeId
     * @return
     * @throws IOException
     */
    public Flowable<NotifyDetailsBean> notifyDetails(Context context, String noticeId) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("notice_id", noticeId);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<NotifyDetailsBean> flowable = ApiRetrofit.getInstance().getClientApi(PayService.class, Api.baseUrl())
                .iNotifyDetails(String.valueOf(timestamp), token, noticeId, sign);
        return flowable;
    }


    /**
     * 记录公告是否已读
     * @param context
     * @param noticeId
     * @return
     * @throws IOException
     */
    public Flowable<NoticeNotReadCountBean> readNotices(Context context, String noticeId) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("notice_id", noticeId);
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<NoticeNotReadCountBean> flowable = ApiRetrofit.getInstance().getClientApi(PayService.class, Api.baseUrl())
                .readNotice(String.valueOf(timestamp), token, noticeId, sign);
        return flowable;
    }
}
