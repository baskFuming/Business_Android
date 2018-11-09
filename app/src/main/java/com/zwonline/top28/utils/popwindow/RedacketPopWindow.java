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
 *    首页红包
 * 1. 用户初次注册奖励红包
 * 2. 推荐人用户红包
 * <p>
 * 背景图 1.表示红包未打开(未打开) 2.表示红包已经打开
 * <p>
 * 推荐来源：1.用户初次注册 2您的推荐人「用户昵称」赠" introduc_user
 */
public class RedacketPopWindow extends PopupWindow {

    private ImageView readClose;
    private View window;
    private LinearLayout readBg1, readBg2;
    private TextView introduc_user;
    private ImageView imageReceive;//领取红包
    private TextView recrive_code, text_busin, text_cheats;
    public RedacketPopWindow(final Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        window = inflater.inflate(R.layout.readpacket_item, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        readClose = (ImageView) window.findViewById(R.id.read_close);
        readBg1 = (LinearLayout) window.findViewById(R.id.readbackground1);
        readBg2 = (LinearLayout) window.findViewById(R.id.readbackground2);
        introduc_user = (TextView) window.findViewById(R.id.introduc_user);
        imageReceive = (ImageView) window.findViewById(R.id.receive);
        recrive_code = (TextView) window.findViewById(R.id.recrive_code);
        text_busin = (TextView) window.findViewById(R.id.text_busin);
        text_cheats = (TextView)window.findViewById(R.id.text_cheats);
        readBg1.setOnClickListener(itemsOnClick);
        readBg2.setOnClickListener(itemsOnClick);
        introduc_user.setOnClickListener(itemsOnClick);
        imageReceive.setOnClickListener(itemsOnClick);
        recrive_code.setOnClickListener(itemsOnClick);
        text_busin.setOnClickListener(itemsOnClick);
        text_cheats.setOnClickListener(itemsOnClick);
        readClose.setOnClickListener(new View.OnClickListener() {
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
        if (Build.VERSION.SDK_INT >= 24) {
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

    public void showAsDropDown(PopupWindow pw, View anchor, int xoff, int yoff) {
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
