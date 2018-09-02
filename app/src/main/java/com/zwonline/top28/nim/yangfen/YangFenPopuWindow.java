package com.zwonline.top28.nim.yangfen;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwonline.top28.R;
import com.zwonline.top28.utils.StringUtil;

public class YangFenPopuWindow extends PopupWindow {
    private TextView look_details;
    private RelativeLayout close;
    private View window;
    private final ImageView openRedPack;
    private FrameAnimation mFrameAnimation;

    public YangFenPopuWindow(final Activity context, View.OnClickListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.yangfen_dialog, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        close = (RelativeLayout) window.findViewById(R.id.close);
        look_details = (TextView) window.findViewById(R.id.look_details);
        openRedPack = (ImageView) window.findViewById(R.id.open_redpack);
        openRedPack.setOnClickListener(listener);
        look_details.setOnClickListener(listener);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //销毁弹出框
                dismiss();
                backgroundAlpha(context, 1f);
            }
        });

//        openRedPack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mFrameAnimation != null) {
//                    //如果正在转动，则直接返回
//                    return;
//                }
//
//
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
        this.setAnimationStyle(R.style.yangfen_anim_style);
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