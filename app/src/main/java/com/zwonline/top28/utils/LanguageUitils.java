package com.zwonline.top28.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.zwonline.top28.APP;
import com.zwonline.top28.api.Api;
import com.zwonline.top28.constants.BizConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author YSG
 * @desc语言的工具类
 * @date ${Date}
 */
public class LanguageUitils {
    public static String getCurCountryLan() {
        String lang = APP.getContext().getResources().getConfiguration().locale
                .getCountry();

        //判断渠道
        String channel = ChannelUtil.getAppMetaData(APP.getContext(), "UMENG_CHANNEL");//获取app当前的渠道号
        if (channel != null && channel.equals(BizConstant.CHANNEL_GOOGLE_ZH_RTW)) {
            return BizConstant.LANGUAGE_ZH_TW;
        }
//        Log.i("TAG","network channel " + channel);
        return APP.getContext().getResources().getConfiguration().locale
                .getLanguage()
                + "-"
                + lang.toLowerCase();
    }

//    public static String getTopActivity(Context context) {
//        try {
//            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            //获取正在运行的task列表，其中1表示最近运行的task，通过该数字限制列表中task数目，最近运行的靠前
//            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
//
//            if (runningTaskInfos != null && runningTaskInfos.size() != 0) {
//                return (runningTaskInfos.get(0).baseActivity).getPackageName();
//            }
//        } catch (Exception e) {
////            logger.error("栈顶应用:" + e);
//        }
//        return "";
//    }

    /**
     * 获取应用 包名
     *
     * @param context
     * @return
     */
    public static String getLollipopRecentTask(Context context) {
        final int PROCESS_STATE_TOP = 2;
        try {
            //通过反射获取私有成员变量processState，稍后需要判断该变量的值
            Field processStateField = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            List<ActivityManager.RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(
                    Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo process : processes) {
                //判断进程是否为前台进程
                if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    int state = processStateField.getInt(process);
                    //processState值为2
                    if (state == PROCESS_STATE_TOP) {
                        String[] packname = process.pkgList;
                        return packname[0];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 跳转应用市场
     *
     * @param context
     * @param packageName
     */
    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
//            goToMarket.setClassName("com.tencent.android.qqdownloader", "com.zwonline.top28.MainActivity");
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 应用市场安装, 跳转到该应用的详情页:
     *
     * @param context
     * @param appStorePackageName
     */
    public static void gotoAppInfoPage(Context context, String appStorePackageName,String appPackageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("market://details?id=" +appPackageName);
        intent.setData(uri);
        intent.setPackage(appStorePackageName);
        context.startActivity(intent);
    }

    /**
     * 应用市场未安装, 跳转到浏览器:
     * @param context
     */
    public static void gotoBrowserDownload(Context context,String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }



    /**
     *判断当前点击屏幕的地方是否是软键盘：
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘的方法：
     * @param context
     * @param v
     * @return
     */
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }


    /*
     * 获取当前程序的版本名
     */
    public static String getVersionName(Context context) {
        PackageInfo packInfo = null;
        try {
            //获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            //getPackageName()是你当前类的包名，0代表是获取版本信息
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packInfo.versionName;

    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
