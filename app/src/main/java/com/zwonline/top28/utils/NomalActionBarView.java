package com.zwonline.top28.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zwonline.top28.R;

public class NomalActionBarView extends Toolbar {
    public NomalActionBarView(Context context) {
        super(context);
    }

    public NomalActionBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public NomalActionBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_actionbar, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        addView(view, layoutParams);
    }
}
