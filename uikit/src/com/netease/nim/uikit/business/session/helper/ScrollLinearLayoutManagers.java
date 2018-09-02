package com.netease.nim.uikit.business.session.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * 描述：解决recyclerview与scrollview滑动冲突问题
 * @author YSG
 * @date 2018/4/8
 */
public class ScrollLinearLayoutManagers extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public ScrollLinearLayoutManagers(Context context) {
        super(context);
    }

    public ScrollLinearLayoutManagers(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollLinearLayoutManagers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled && super.canScrollVertically();
    }
}