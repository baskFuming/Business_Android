package com.zwonline.top28.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.utils.ImageViewPlus;

public class MyQrCodePopwindow extends PopupWindow {

    private View window;
    private ImageViewPlus myHead;
    private TextView myName;
    private Button close;
    private final TextView mySign;
    private final ImageView my_qrcode;

    public MyQrCodePopwindow(final Activity context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.me_qrcode_pop, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myHead = (ImageViewPlus) window.findViewById(R.id.my_head);
        myName = (TextView) window.findViewById(R.id.my_name);
        mySign = (TextView)window.findViewById(R.id.my_sign);
        my_qrcode = (ImageView)window.findViewById(R.id.my_qrcode);
        close = (Button) window.findViewById(R.id.close);
//        close.setOnClickListener(listener);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
                backgroundAlpha(context, 1f);
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(window);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.5f);//0.0-1.0
        // 重写onKeyListener
        window.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public interface OnClickItemListener {
        void setOnItemClick(String clauseNames, String clauseRatio, String clauseContent);
    }

    public String clauseContentData() {

        return "";
    }
}
