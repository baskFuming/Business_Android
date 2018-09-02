package com.zwonline.top28.nim.session.action;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zwonline.top28.R;

/**
 * Tip类型消息测试
 * Created by hzxuwen on 2016/3/9.
 */
public class TipAction extends BaseAction {
    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected TipAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }

    public TipAction() {
        super(R.drawable.message_plus_guess_normal, R.string.input_panel_tip);
    }

    @Override
    public void onClick() {
        IMMessage msg = MessageBuilder.createTipMessage(getAccount(), getSessionType());
        msg.setContent("一条Tip测试消息");

        CustomMessageConfig config = new CustomMessageConfig();
        config.enablePush = false; // 不推送
        msg.setConfig(config);

        sendMessage(msg);
    }
}
