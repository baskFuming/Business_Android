package com.zwonline.top28.nim.shangjibi;

import com.alibaba.fastjson.JSONObject;
import com.zwonline.top28.nim.session.extension.CustomAttachment;
import com.zwonline.top28.nim.session.extension.CustomAttachmentType;

/**
 * 商机币红包解析器
 */
public class SJBAttachment extends CustomAttachment {
    private String content;//  消息文本内容
    private String redPacketId;//  红包id
    private String title;// 红包名称
    private String redpackType;//红包类型
    private String redpackUserName;//红包所属用户名字
    private String redpackUserHeader;////红包所属用户头像
    private String redpackUserID;  //红包所属用户id
    private String redpackUserToken;
    private static final String KEY_CONTENT = "content";
    private static final String KEY_ID = "redPacketId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "redpackType";
    private static final String KEY_USER_NAME = "redpackUserName";
    private static final String KEY_USER_HEADER = "redpackUserHeader";
    private static final String KEY_USER_ID = "redpackUserID";
    private static final String KEY_USER_TOKEN = "redpackUserToken";

    public SJBAttachment() {
        super(CustomAttachmentType.SHANGJIBI);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.getString(KEY_CONTENT);
        redPacketId = data.getString(KEY_ID);
        title = data.getString(KEY_TITLE);
        redpackType = data.getString(KEY_TYPE);
        redpackUserName = data.getString(KEY_USER_NAME);
        redpackUserHeader = data.getString(KEY_USER_HEADER);
        redpackUserID = data.getString(KEY_USER_ID);
        redpackUserToken = data.getString(KEY_USER_TOKEN);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_CONTENT, content);
        data.put(KEY_ID, redPacketId);
        data.put(KEY_TITLE, title);
        data.put(KEY_TYPE, redpackType);
        data.put(KEY_USER_NAME, redpackUserName);
        data.put(KEY_USER_HEADER, redpackUserHeader);
        data.put(KEY_USER_ID, redpackUserID);
        data.put(KEY_USER_TOKEN, redpackUserToken);
        return data;
    }

    public String getRedpackUserToken() {
        return redpackUserToken;
    }

    public void setKeyUserToken(String userToken) {
        this.redpackUserToken = userToken;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(String redPacketId) {
        this.redPacketId = redPacketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRedpackType() {
        return redpackType;
    }

    public void setRedpackType(String redpackType) {
        this.redpackType = redpackType;
    }

    public String getRedpackUserName() {
        return redpackUserName;
    }

    public void setRedpackUserName(String redpackUserName) {
        this.redpackUserName = redpackUserName;
    }

    public String getRedpackUserHeader() {
        return redpackUserHeader;
    }

    public void setRedpackUserHeader(String redpackUserHeader) {
        this.redpackUserHeader = redpackUserHeader;
    }

    public String getRedpackUserID() {
        return redpackUserID;
    }

    public void setRedpackUserID(String redpackUserID) {
        this.redpackUserID = redpackUserID;
    }
}
