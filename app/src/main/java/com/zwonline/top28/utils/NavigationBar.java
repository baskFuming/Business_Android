package com.zwonline.top28.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 获取虚拟键高度和判断是否有虚拟键
 */
public class NavigationBar {

    /**
     * 获取虚拟键的高度
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
    int result = 0;
    if (hasNavBar(context)) {
    Resources res = context.getResources();
    int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
    if (resourceId > 0) {
    result = res.getDimensionPixelSize(resourceId);
    }
    }
    return result;
    }

    /**
     * 判断是否有虚拟键
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
    boolean hasNavigationBar = false;
    Resources rs = context.getResources();
    int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
    if (id > 0) {
    hasNavigationBar = rs.getBoolean(id);
    }
    try {
    Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
    Method m = systemPropertiesClass.getMethod("get", String.class);
    String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
    if ("1".equals(navBarOverride)) {
    hasNavigationBar = false;
    } else if ("0".equals(navBarOverride)) {
    hasNavigationBar = true;
    }
    } catch (Exception e) {
    }
    return hasNavigationBar;
    }

    /**
     * 沉浸式图片距顶部
     * @param
     */
    // 重置状态栏。即把状态栏颜色恢复为系统默认的黑色
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBar(Activity activity){
        /**
         * 获取状态栏高度
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public static void Statedata(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            //让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏颜色为透明
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}