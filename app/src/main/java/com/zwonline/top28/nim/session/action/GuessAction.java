package com.zwonline.top28.nim.session.action;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.zwonline.top28.R;
import com.zwonline.top28.nim.session.extension.GuessAttachment;

/**
 * Created by hzxuwen on 2015/6/11.
 */
public class GuessAction extends BaseAction {
    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected GuessAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }

    public GuessAction() {
        super(R.drawable.message_plus_guess_normal, R.string.input_panel_guess);
    }

    @Override
    public void onClick() {
        GuessAttachment attachment = new GuessAttachment();
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomCustomMessage(getAccount(), attachment);
        } else {
            message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), attachment.getValue().getDesc(), attachment);
        }

        sendMessage(message);
    }
}
