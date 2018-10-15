package com.zwonline.top28.api.service;

import com.zwonline.top28.bean.YSBannerBean;
import com.zwonline.top28.bean.YSListBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YangShiService {
    /**
     * 个人中心菜单接口
     *
     * @param timestamp
     * @param token
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Shop/bannerList")
    Flowable<YSBannerBean> bannerList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("sign") String sign

    );

    /**
     * 鞅市拍卖列表
     *
     * @param timestamp
     * @param token
     * @param app_version
     * @param filter
     * @param page
     * @param sign
     * @return
     */
    @FormUrlEncoded
    @POST("/App/Shop/auctionList")
    Flowable<YSListBean> auctionList(
            @Field("timestamp") String timestamp,
            @Field("token") String token,
            @Field("app_version") String app_version,
            @Field("filter") String filter,
            @Field("page") int page,
            @Field("sign") String sign

    );
}
