package com.zwonline.top28.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.zwonline.top28.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qinglanhui on 2017/3/17.
 */
public class NetUtils {
    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                } else {
                    ToastUtils.showToast(context, context.getString(R.string.network_no_service));
                }
            } else {
                ToastUtils.showToast(context,context.getString(R.string.network_no_service));
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        ToastUtils.showToast(context,context.getString(R.string.network_connect_wifi));
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivity(intent);
    }

    /**
     * 判断手机是否连接在Wifi上
     */
    public static boolean isConnectWifi(Context mContext) {
        // 获取ConnectivityManager对象
        ConnectivityManager conMgr = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        // 获取连接的方式为wifi
        NetworkInfo.State wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (info != null && info.isAvailable() && wifi == NetworkInfo.State.CONNECTED)

        {
//            ToastUtils.showShort(mContext,"连接wifi");
            return true;
        } else {
//            ToastUtils.showShort(mContext,"wei连接wifi");
            return false;
        }
    }

    /**
     * 获取当前手机所连接的wifi信息
     */
    public static WifiInfo getCurrentWifiInfo(Context mContext) {
        WifiManager mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
//        Log.e(TAG, "getCurrentWifiInfo: "+ mWifiManager.getConnectionInfo().getSSID()+
//                "地址"+mWifiManager.getConnectionInfo().getMacAddress());
        return mWifiManager.getConnectionInfo();
    }

    /**
     * 搜索附近的热点信息，并返回所有热点为信息的SSID集合数据
     */
    public static List<String> getScanSSIDsResult(Context mContext) {
        WifiManager mWifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        // 扫描的热点数据
        List<ScanResult> resultList;
        // 开始扫描热点
        mWifiManager.startScan();//需要添加状态改变权限
        resultList = mWifiManager.getScanResults();
        ArrayList<String> ssids = new ArrayList<String>();
        if (resultList != null) {
            for (ScanResult scan : resultList) {
                ssids.add(scan.SSID);// 遍历数据，取得ssid数据集
            }
        }
//        Log.e(TAG, "getScanSSIDsResult: "+ssids.get(0) );
        return ssids;
    }

//    /**
//     * 得到手机搜索到的ssid集合，从中判断出设备的ssid（dssid）
//     */
//    public List<String> accordSsid(Context mContext) {
//        List<String> s = getScanSSIDsResult(mContext);
//        List<String> result = new ArrayList<String>();
//        for (String str : s) {
//            if (checkDssid(str)) {
//                result.add(str);
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 检测指定ssid是不是匹配的ssid，目前支持GBELL，TOP,后续可添加。
//     *
//     * @param ssid
//     * @return
//     */
//    private boolean checkDssid(String ssid,String condition) {
//        if (!TextUtils.isEmpty(ssid)&&!TextUtils.isEmpty(condition)) {
//            //这里条件根据自己的需求来判断，我这里就是随便写的一个条件
//            if (ssid.length() > 8 && (ssid.substring(0, 8).equals(condition))) {
//                return true;
//            }
//            else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }
}
