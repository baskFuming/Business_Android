package com.zwonline.top28.utils.click;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.zwonline.top28.activity.HomePageActivity;

public class TextClick extends ClickableSpan {
    public Context context;
    public String uid;
    public TextClick(Context context,String uid) {
        super();
        this.context=context;
        this.uid=uid;
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(context, HomePageActivity.class);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.parseColor("#228FFE"));
    }
}
