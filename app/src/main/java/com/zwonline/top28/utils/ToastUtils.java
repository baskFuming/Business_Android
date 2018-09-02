package com.zwonline.top28.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类,使多个吐司都可以显示内容,不至于遮盖住
 */
public class ToastUtils {

    private static Toast sToast;

    /**
     * 弹吐司,如果这个Toast已经在显示了，那么这里会立即修改文本
     * @param context 解决了内存泄漏的问题.所谓的内存优化,就是开放时注意细节
     * @param msg
     */
    public static void showToast(Context context, String msg){
        if (sToast==null){
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.show();
    }
}
