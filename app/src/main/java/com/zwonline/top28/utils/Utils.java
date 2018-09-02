package com.zwonline.top28.utils;

import android.app.Activity;
import android.content.Intent;

import com.zwonline.top28.R;
import com.zwonline.top28.activity.EditActivity;
import com.zwonline.top28.activity.TransmitActivity;

public class Utils {
    private static final String TAG = "Util";
    public static void switchToLaunch(Activity activity) {
        Intent intent = activity.getIntent();
        intent.setClass(activity, TransmitActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
    public static void switchBack(Activity activity) {
        Intent intent = activity.getIntent();
        intent.setClass(activity, EditActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }
}
