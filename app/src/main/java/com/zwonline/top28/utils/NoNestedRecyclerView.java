package com.zwonline.top28.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class NoNestedRecyclerView extends RecyclerView {
    boolean isNestedEnable = false;

    public NoNestedRecyclerView(Context context) {
        super(context);
    }

    public NoNestedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoNestedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isNestedEnable() {
        return isNestedEnable;
    }

    public void setNestedEnable(boolean nestedEnable) {
        isNestedEnable = nestedEnable;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (isNestedEnable) {
            return super.startNestedScroll(nestedScrollAxes);
        } else {
            return false;
        }    }

    @Override
    public void onStopNestedScroll(View child) {
        if (isNestedEnable) {
            super.stopNestedScroll();
        }    }

    @Override
    public boolean hasNestedScrollingParent() {
        if (isNestedEnable) {
            return super.hasNestedScrollingParent();
        } else {
            return false;
        }
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        if (isNestedEnable) {
            return super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        } else {
            return false;
        }    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        if (isNestedEnable) {
            return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
        } else {
            return false;
        }
    }
}
