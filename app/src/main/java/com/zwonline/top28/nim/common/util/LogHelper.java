package com.zwonline.top28.nim.common.util;

import com.netease.nim.uikit.common.util.log.LogUtil;
import com.zwonline.top28.nim.AVChatKit;
import com.zwonline.top28.nim.ILogUtil;

/**
 * Created by winnie on 2017/12/21.
 */

public class LogHelper {

    public static void init() {
        AVChatKit.setiLogUtil(new ILogUtil() {
            @Override
            public void ui(String msg) {
                LogUtil.ui(msg);
            }

            @Override
            public void e(String tag, String msg) {
                LogUtil.e(tag, msg);
            }

            @Override
            public void i(String tag, String msg) {
                LogUtil.i(tag, msg);
            }

            @Override
            public void d(String tag, String msg) {
                LogUtil.d(tag, msg);
            }
        });
    }
}
