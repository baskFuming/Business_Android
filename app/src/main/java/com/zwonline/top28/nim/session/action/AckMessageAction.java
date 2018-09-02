package com.zwonline.top28.nim.session.action;

import android.content.Intent;
import android.widget.Toast;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.impl.cache.TeamDataCache;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.zwonline.top28.nim.session.activity.SendAckMsgActivity;

/**
 * 已读回执action
 * Created by winnie on 2018/3/14.
 */

public class AckMessageAction extends BaseAction {
    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected AckMessageAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }

//    public AckMessageAction() {
//        super(R.drawable.message_plus_ack_selector, R.string.input_panel_ack_msg);
//    }

    @Override
    public void onClick() {
        // 只在小于100人的群里有效
        Team team = TeamDataCache.getInstance().getTeamById(getContainer().account);
        if (team != null && team.getMemberCount() > 100) {
            Toast.makeText(getContainer().activity, "已读回执适用于小于100人的群", Toast.LENGTH_SHORT).show();
            return;
        }
        SendAckMsgActivity.startActivity(getContainer().activity, getContainer().account, makeRequestCode(8));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 8) {
            String content = data.getStringExtra(SendAckMsgActivity.EXTRA_CONTENT);
            IMMessage message = MessageBuilder.createTextMessage(getContainer().account, SessionTypeEnum.Team, content);
//            message.setMsgAck();
            sendMessage(message);
        }
    }
}
