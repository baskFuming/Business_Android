package com.zwonline.top28.view;

import com.zwonline.top28.bean.NoticeNotReadCountBean;
import com.zwonline.top28.bean.UserInfoBean;

import java.util.List;

/**
 * @author YSG
 * @desc获取个人信息
 * @date ${Date}
 */
public interface IUserInfo {
    /**
     * 个人信息
     * @param userInfoBean
     */
    void showUserInfo(UserInfoBean userInfoBean);

    /**
     * 异常
     */
    void showErro();

    /**
     * 查询公告未读数量
     * @param noticeNotReadCountBean
     */
    void showNoticeNoRead(NoticeNotReadCountBean noticeNotReadCountBean);
}
