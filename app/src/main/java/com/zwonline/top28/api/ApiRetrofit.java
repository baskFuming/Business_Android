package com.zwonline.top28.api;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zwonline.top28.utils.LanguageUitils;
import com.zwonline.top28.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 1. 类的用途
 * 2. @author forever
 * 3. @date 2017/5/13 10:32
 */

public class ApiRetrofit implements ApiRetrofitInterface {

    private Retrofit retrofit;
    //单例设计模式
    private static ApiRetrofit apiRetrofit = null;

    private ApiRetrofit() {}

    //提供一个公共的返回实例的静态方法
    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (ApiRetrofit.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }
    public Retrofit getRetrofit(String url) {
        //打印retrofit日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(final String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
        //设置日志拦截器的优先级
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //创建OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .header("Accept-Language",LanguageUitils.getCurCountryLan())
                        .header("User-Agent","app28"+ "("+Build.MODEL+" Android "+ Build.VERSION.RELEASE+")")
                        .build();
                return chain.proceed(request);
            }
        });
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient okHttpClient = builder.build();
        //创建Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }
    public Retrofit getRetrofits(String url, final String seesionid) {
        //打印retrofit日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(final String message) {
                //打印retrofit日志
                Log.i("RetrofitLog", "retrofitBack = " + message);
            }
        });
        //设置日志拦截器的优先级
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //创建OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .header("Cookie","PHPSESSID="+seesionid)
                        .header("Accept-Language",LanguageUitils.getCurCountryLan())
                        .header("User-Agent","app28"+ "("+Build.MODEL+" Android "+ Build.VERSION.RELEASE+")")
                        .build();
                return chain.proceed(request);
            }
        });
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(loggingInterceptor);
        OkHttpClient okHttpClient = builder.build();
        //创建Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

    //封装参数的接口对象
    @Override
    public <T> T getClientApi(Class<T> cla,String url) {
        Retrofit retrofit = getRetrofit(url);
        return retrofit.create(cla);
    }

    @Override
    public <T> T getClientApis(Class<T> cla, String url, String seesionid) {
        Retrofit retrofit = getRetrofits(url,seesionid);
        return retrofit.create(cla);
    }

}
