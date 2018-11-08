package com.zwonline.top28.nim.shangjibi;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.zwonline.top28.R;
import com.zwonline.top28.constants.BizConstant;
import com.zwonline.top28.nim.yangfen.SendYangFenActivity;
import com.zwonline.top28.nim.shangjibi.SJBAttachment;
import com.zwonline.top28.utils.SharedPreferencesUtils;

/**
 * 商机币红包点击到发送页面
 */
public class SJBAction extends BaseAction {

    private static final int CREATE_GROUP_RED_PACKET = 51;
    private static final int CREATE_SINGLE_RED_PACKET = 10;
    private SharedPreferencesUtils sp;
    private static final String EXTRA_ID = "EXTRA_ID";

    public SJBAction() {
        super(R.mipmap.message_gift2, R.string.opportunities_currency_red_packet);

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
//        NIMRedPacketClient.startSendRpActivity(getActivity(), getContainer().sessionType, getAccount(), requestCode);
        Team t = NimUIKit.getTeamProvider().getTeamById(getAccount());
        Intent intent = new Intent(getActivity(), SendYangFenActivity.class);
        intent.putExtra("package_type", BizConstant.NEW);
        //判断是群组还是个人is_group字段
        if (getContainer().sessionType == SessionTypeEnum.Team) {//群组
            intent.putExtra("group_num", t.getMemberCount() + "");
            intent.putExtra("is_group", BizConstant.NEW);
        } else if (getContainer().sessionType == SessionTypeEnum.P2P) {//个人
            intent.putExtra("group_num", "1");
            intent.putExtra("is_group", BizConstant.RECOMMEND);
        }
        getActivity().startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        sendRpMessage(data);

    }

    private void sendRpMessage(Intent data) {
        sp = SharedPreferencesUtils.getUtil();
        String avatar = (String) sp.getKey(getActivity(), "avatar", "");
        String nickname = (String) sp.getKey(getActivity(), "nickname", "");
        SJBAttachment attachment = new SJBAttachment();
//        // 红包id，红包信息，红包名称
        attachment.setTitle(data.getExtras().getString("content"));
        attachment.setRedPacketId(data.getExtras().getString("hongbao_id") + "");
        attachment.setRedpackType(data.getExtras().getString("redType") + "");
        attachment.setContent("商机币红包");
        attachment.setRedpackUserID(data.getExtras().getString("user_id") + "");
        attachment.setRedpackUserHeader(avatar);
        attachment.setRedpackUserName(nickname);
        attachment.setKeyUserToken(data.getExtras().getString("user_token"));
//        String content = getActivity().getString(R.string.rp_push_content);
        // 不存云消息历史记录
//        CustomMessageConfig config = new CustomMessageConfig();
//        config.enableHistory = false;
        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), "商机币红包", attachment);
        sendMessage(message);
    }
}
