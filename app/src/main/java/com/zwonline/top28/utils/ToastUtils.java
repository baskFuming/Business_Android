package com.zwonline.top28.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zwonline.top28.R;

/**
 * Toast工具类,使多个吐司都可以显示内容,不至于遮盖住
 */
public class ToastUtils {

    private static Toast sToast;

    /**
     * 弹吐司,如果这个Toast已经在显示了，那么这里会立即修改文本
     *
     * @param context 解决了内存泄漏的问题.所谓的内存优化,就是开放时注意细节
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        }
        sToast.setText(msg);
        sToast.show();
    }

    /**
     * 带图片的吐司，设置吐司弹出的位置为屏幕中心
     * 通过参数传递，可是设置吐司的图片和文字内容
     *
     * @param text
     */
    public static void showCustomToastCenter(Context mContext, String text, int imgResId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mtoast_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setBackgroundResource(imgResId);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 屏幕中心只显示文字
     *
     * @param mContext
     * @param text
     */
    public static void showCustomToastCenters(Context mContext, String text) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mtoast_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.toast_image);
        imageView.setVisibility(View.GONE);
        TextView t = (TextView) view.findViewById(R.id.toast_text);
        t.setText(text);
        Toast toast = null;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
