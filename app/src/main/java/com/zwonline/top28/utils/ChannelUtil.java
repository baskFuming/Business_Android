package com.zwonline.top28.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.netease.nim.uikit.common.util.string.StringUtil;

/**
 * Created by sdh on 2018/3/7.
 */

public class ChannelUtil {
    /**
     * 获取app当前的渠道号或application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || StringUtil.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }

    public static void  main(String args[]) {
        /**
         * 在需要的地方调用上述方法
         */
//        String channelNumber = getAppMetaData(getBaseContext(), "UMENG_CHANNEL");//获取app当前的渠道号
    }

}
