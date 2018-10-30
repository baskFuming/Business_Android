package com.zwonline.top28.utils.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zwonline.top28.R;

/**
 * dec:打赏弹框
 */
public class RewardPopWindow extends PopupWindow {
    private ImageView imageView;
    private View window;
    private LinearLayout flower;
    private LinearLayout flowers;
    private LinearLayout applause;
    private LinearLayout kiss;

    public RewardPopWindow(final Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.reward_item, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        imageView = (ImageView) window.findViewById(R.id.close_pop);
        TextView sure = (TextView) window.findViewById(R.id.sure);
        flower = window.findViewById(R.id.flower);
        flowers = window.findViewById(R.id.flowers);
        applause = window.findViewById(R.id.applause);
        kiss = window.findViewById(R.id.kiss);
        flower.setOnClickListener(itemsOnClick);
        sure.setOnClickListener(itemsOnClick);
        flowers.setOnClickListener(itemsOnClick);
        applause.setOnClickListener(itemsOnClick);
        kiss.setOnClickListener(itemsOnClick);
//        close.setOnClickListener(listener);
        imageView.setOnClickListener(new View.OnClickListener() {
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
//        设置SelectPicPopupWindow弹出窗体的背景
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
        if (Build.VERSION.SDK_INT >= 24){
            WindowManager.LayoutParams lp = context.getWindow().getAttributes();
            lp.alpha = bgAlpha;
            if (bgAlpha == 1) {
                context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
            } else {
                context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
            }
            context.getWindow().setAttributes(lp);
        }
    }

    public interface OnClickItemListener {
        void setOnItemClick(String clauseNames, String clauseRatio, String clauseContent);
    }

    public String clauseContentData() {
        return "";
    }
    public  void showAsDropDown(PopupWindow pw, View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pw.setHeight(height);
            pw.showAsDropDown(anchor, xoff, yoff);
        } else {
            pw.showAsDropDown(anchor, xoff, yoff);
        }
    }
}
