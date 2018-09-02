package com.zwonline.top28.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.zwonline.top28.R;

@SuppressLint("AppCompatCustomView")
public class AutoTextView extends TextView {
    private int tagTextColor = 0xFFFF0000;
    private String tag = "...全文";

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(this);
        setText(getText().toString());
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTextView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onPreDraw() {
        replaceText();
        return super.onPreDraw();
    }

    public void replaceText() {
        int count = getLineCount();
        if (count > 4) {
            int st = getLayout().getLineEnd(2);
            String content = getText().toString().substring(0, st);
            Paint paint = new Paint();
            paint.setTextSize(getTextSize());
            float pointWidth = paint.measureText(tag);
            char[] textCharArray = content.toCharArray();
            float drawedWidth = 0;
            float charWidth;
            for (int i = textCharArray.length - 1; i > 0; i--) {
                charWidth = paint.measureText(textCharArray, i, 1);
                if (drawedWidth < pointWidth) {
                    drawedWidth += charWidth;
                } else {
                    content = content.substring(0, i) + tag;
                    break;
                }
            }

            setColor(content, content.length() - 2, content.length(), Color.parseColor("#228FFE"));
        }
    }

    private void setColor(String content, int start, int end, int textColor) {
        if (start <= end) {
            SpannableStringBuilder style = new SpannableStringBuilder(content);
            style.setSpan(new ForegroundColorSpan(textColor), start, end,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            setText(style);
        }
    }
}