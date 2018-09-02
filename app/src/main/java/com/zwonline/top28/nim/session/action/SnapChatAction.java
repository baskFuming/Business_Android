package com.zwonline.top28.nim.session.action;

import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zwonline.top28.R;
import com.zwonline.top28.nim.session.extension.SnapChatAttachment;

import java.io.File;

/**
 * Created by zhoujianghua on 2015/7/31.
 */
public class SnapChatAction extends PickImageAction {
    protected SnapChatAction(int iconResId, int titleId, boolean multiSelect) {
        super(iconResId, titleId, multiSelect);
    }

    public SnapChatAction() {
        super(R.drawable.action_bar_search_view_icon, R.string.input_panel_snapchat, false);
    }

    @Override
    protected void onPicked(File file) {
        SnapChatAttachment snapChatAttachment = new SnapChatAttachment();
        snapChatAttachment.setPath(file.getPath());
        snapChatAttachment.setSize(file.length());
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;
        config.enableRoaming = false;
        config.enableSelfSync = false;
        IMMessage stickerMessage = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "阅后即焚消息", snapChatAttachment, config);
        sendMessage(stickerMessage);
    }

}
