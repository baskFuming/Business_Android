package com.zwonline.top28.nim.session.action;

import android.widget.Toast;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.zwonline.top28.R;

/**
 * Created by huangjun on 2015/7/7.
 */
public class RTSAction extends BaseAction {
    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected RTSAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }

    public RTSAction() {
        super(R.drawable.message_plus_rts_selector, R.string.input_panel_RTS);
    }

    @Override
    public void onClick() {
        if (NetworkUtil.isNetAvailable(getActivity())) {
//            RTSKit.startRTSSession(getActivity(), getAccount());
        } else {
            Toast.makeText(getActivity(), R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
        }

    }
}
