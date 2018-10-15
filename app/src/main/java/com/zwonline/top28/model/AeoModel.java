package com.zwonline.top28.model;


import android.content.Context;

import com.zwonline.top28.api.Api;
import com.zwonline.top28.api.ApiRetrofit;
import com.zwonline.top28.api.service.ApiService;
import com.zwonline.top28.bean.EnterpriseStatusBean;
import com.zwonline.top28.bean.HeadBean;
import com.zwonline.top28.bean.IndustryBean;
import com.zwonline.top28.bean.PicturBean;
import com.zwonline.top28.bean.ProjectBean;
import com.zwonline.top28.utils.SharedPreferencesUtils;
import com.zwonline.top28.utils.SignUtils;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 描述：企业认证的model
 *
 * @author YSG
 * @date 2017/12/30
 */
public class AeoModel {
    private SharedPreferencesUtils sp;

    //企业认证分类
    public Flowable<IndustryBean> AeoClass(Context context) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        SignUtils.removeNullValue(map);
        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        Flowable<IndustryBean> flowable = ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAeoClass(String.valueOf(timestamp), token, sign);
        return flowable;
    }
        //申请企业认证
    public Flowable<HeadBean>Aeo(Context context, String enterprise_name, String company_name,
                                 String slug, String cate_id,
                                 String enterprise_contacts, String enterprise_contact_tel,
                                 String enterprise_contact_address, String[] img) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        map.put("enterprise_name",enterprise_name);
        map.put("company_name",company_name);
        map.put("slug",slug);
        map.put("cate_id",cate_id);
        map.put("enterprise_contacts",enterprise_contacts);
        map.put("enterprise_contact_tel",enterprise_contact_tel);
        map.put("enterprise_contact_address",enterprise_contact_address);
        JSONArray imgArr = new JSONArray();
        for (int i = 0; i < img.length; i++) {
            imgArr.put(img[i]);
            img[i]=img[i];
        }

        String sign = SignUtils.getSignature(map, Api.PRIVATE_KEY);
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(img));
        MultipartBody.Builder builder=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("timestamp",String.valueOf(timestamp))
                .addFormDataPart("token",token)
                .addFormDataPart("sign",sign)
                .addFormDataPart("enterprise_name",enterprise_name)
                .addFormDataPart("company_name",company_name)
                .addFormDataPart("slug",slug)
                .addFormDataPart("cate_id",cate_id)
                .addFormDataPart("enterprise_contacts",enterprise_contacts)
                .addFormDataPart("enterprise_contact_tel",enterprise_contact_tel)
                .addFormDataPart("enterprise_contact_address",enterprise_contact_address)
                .addFormDataPart("img", imgArr.toString());
        List<MultipartBody.Part>parts=builder.build().parts();
        Flowable<HeadBean>flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iAeo(parts);

        return flowable;
    }

    //上传图片
    public Flowable<PicturBean> AeoImage(Context context, File file,String return_origin) throws IOException {
        sp= SharedPreferencesUtils.getUtil();
        String token= (String) sp.getKey(context,"dialog","");
        long timestamp=new Date().getTime()/1000;//时间戳
        Map<String,String>map=new HashMap<>();
        map.put("token",token);
        map.put("return_origin",return_origin);
        map.put("timestamp",String.valueOf(timestamp));
        String sign= SignUtils.getSignature(map, Api.PRIVATE_KEY);
        //多个参数
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("timestamp", String.valueOf(timestamp))
                .addFormDataPart("token", token)
                .addFormDataPart("sign",sign)
                .addFormDataPart("return_origin",return_origin)
                .addFormDataPart("file", file.getName(), imageBody);
        List<MultipartBody.Part> parts = builder.build().parts();

        Flowable<PicturBean> flowable= ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iPicture(parts);
        return flowable;
    }

    /**
     * 获取企业信息详情
     * @param context
     * @param projectId
     * @return
     * @throws IOException
     */
    public Flowable<EnterpriseStatusBean> getEnterpriseDetail(Context context, String projectId)
            throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("project_id", projectId);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        return ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .iGetEnterDetail(String.valueOf(timestamp), token, projectId,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }

    public Flowable<ProjectBean> projectList(Context context, String uid) throws IOException {
        sp = SharedPreferencesUtils.getUtil();
        String token = (String) sp.getKey(context, "dialog", "");
        long timestamp = new Date().getTime() / 1000;//时间戳
        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("token", token);
        map.put("timestamp", String.valueOf(timestamp));
        return ApiRetrofit.getInstance()
                .getClientApi(ApiService.class, Api.url)
                .isProjectList(String.valueOf(timestamp), token,uid,
                        SignUtils.getSignature(map, Api.PRIVATE_KEY));
    }


}
