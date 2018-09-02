package com.zwonline.top28.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.utils.StringUtil;

/**
 * 版本更新
 */
public class CustomPopuWindow extends PopupWindow {
    private TextView no, sure;
    private View window;
    private final TextView coerceSure;

    public CustomPopuWindow(final Activity context, String description, String forceUpdate, View.OnClickListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.auto_dialog, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        no = (TextView) window.findViewById(R.id.no);
        sure = (TextView) window.findViewById(R.id.sure);
        coerceSure = (TextView) window.findViewById(R.id.coerce_sure);
        TextView descriptions = (TextView) window.findViewById(R.id.description);
        TextView coerceDescription = (TextView) window.findViewById(R.id.coerce_description);
        coerceDescription.setMovementMethod(new ScrollingMovementMethod());
        descriptions.setMovementMethod(new ScrollingMovementMethod());
        LinearLayout updata = (LinearLayout) window.findViewById(R.id.updata);
        LinearLayout updateLinear = (LinearLayout) window.findViewById(R.id.update_linear);
        LinearLayout coerceUpdate = (LinearLayout) window.findViewById(R.id.coerce_update);
        coerceDescription.setText(description);
        descriptions.setText(description);
        no.setOnClickListener(listener);
        coerceSure.setOnClickListener(listener);
        sure.setOnClickListener(listener);
        if (!StringUtil.isEmpty(forceUpdate) && forceUpdate.equals("0")) {
            updateLinear.setVisibility(View.VISIBLE);
            coerceUpdate.setVisibility(View.GONE);
        } else if (!StringUtil.isEmpty(forceUpdate) && forceUpdate.equals("1")) {
            updateLinear.setVisibility(View.GONE);
            coerceUpdate.setVisibility(View.VISIBLE);
        }
//        no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                //销毁弹出框
//                dismiss();
//                backgroundAlpha(context, 1f);
//            }
//        });
        //设置SelectPicPopupWindow的View
        this.setContentView(window);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.5f);//0.0-1.0
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        window.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = window.findViewById(R.id.pop_layout).getTop();
//                int y=(int) event.getY();
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    if(y<height){
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
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

}