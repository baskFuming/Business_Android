package com.zwonline.top28.view;

import com.zwonline.top28.bean.AddFriendBean;
import com.zwonline.top28.bean.RecommendTeamsBean;

import java.util.List;

public interface IAddFriendActivity {
    /**
     * 显示出搜索出好友列表
     *
     * @param addFriendList
     */
    void showAddFriend(List<AddFriendBean.DataBean> addFriendList);

    /**
     * 搜索的结果没有
     *
     * @param flag
     */
    void noFriend(boolean flag);

    /**
     * 显示群推荐
     *
     * @param recommendList
     */
    void showRecommendTeams(List<RecommendTeamsBean.DataBean> recommendList);
}
