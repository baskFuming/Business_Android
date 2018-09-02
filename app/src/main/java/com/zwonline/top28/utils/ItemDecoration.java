package com.zwonline.top28.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

/**
     * 描述：给recyclerview设置分割线
     * @author YSG
     * @date 2017/12/19
     */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int with;
    public ItemDecoration(Context context){
        this.context=context;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        with=metrics.widthPixels;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Paint paint=new Paint();
        paint.setColor(Color.parseColor("#DDDDDD"));
        paint.setStrokeWidth(1.5f);
        int childCount=parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view=parent.getChildAt(i);
            int bottom=view.getBottom();
            c.drawLine(0,bottom,with,bottom,paint);
        }
    }
}
