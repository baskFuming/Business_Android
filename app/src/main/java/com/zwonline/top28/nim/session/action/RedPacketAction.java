package com.zwonline.top28.nim.session.action;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.zwonline.top28.R;
import com.zwonline.top28.nim.redpacket.NIMRedPacketClient;
import com.zwonline.top28.nim.session.extension.RedPacketAttachment;

public class RedPacketAction extends BaseAction {

//    public RedPacketAction() {
//        super(R.drawable.message_plus_rp_selector, R.string.red_packet);
//    }

    private static final int CREATE_GROUP_RED_PACKET = 51;
    private static final int CREATE_SINGLE_RED_PACKET = 10;

    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected RedPacketAction(int iconResId, int titleId) {
        super(iconResId, titleId);
    }
    public RedPacketAction() {
        super(R.drawable.message_plus_guess_normal, Integer.parseInt("红包"));
    }


    @Override
    public void onClick() {
        int requestCode;
        if (getContainer().sessionType == SessionTypeEnum.Team) {
            requestCode = makeRequestCode(CREATE_GROUP_RED_PACKET);
        } else if (getContainer().sessionType == SessionTypeEnum.P2P) {
            requestCode = makeRequestCode(CREATE_SINGLE_RED_PACKET);
        } else {
            return;
        }
        NIMRedPacketClient.startSendRpActivity(getActivity(), getContainer().sessionType, getAccount(), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        sendRpMessage(data);
    }

    private void sendRpMessage(Intent data) {
//        EnvelopeBean groupRpBean = JrmfRpClient.getEnvelopeInfo(data);
//        if (groupRpBean == null) {
//            return;
//        }
        RedPacketAttachment attachment = new RedPacketAttachment();
//        // 红包id，红包信息，红包名称
//        attachment.setRpId(groupRpBean.getEnvelopesID());
//        attachment.setRpContent(groupRpBean.getEnvelopeMessage());
//        attachment.setRpTitle(groupRpBean.getEnvelopeName());

//        String content = getActivity().getString(R.string.rp_push_content);
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;

//        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);

//        sendMessage(message);
    }
}
