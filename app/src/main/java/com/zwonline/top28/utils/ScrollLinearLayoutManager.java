package com.zwonline.top28.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

    /**
     * 描述：解决recyclerview与scrollview滑动冲突问题
     * @author YSG
     * @date 2018/4/8
     */
public class ScrollLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public ScrollLinearLayoutManager(Context context) {
            super(context);
        }

        public ScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public ScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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