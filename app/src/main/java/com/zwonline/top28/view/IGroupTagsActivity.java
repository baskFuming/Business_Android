package com.zwonline.top28.view;

import com.zwonline.top28.bean.AttentionBean;
import com.zwonline.top28.bean.RecommendTeamsBean;

import java.util.List;

/**
 * 群标签的View
 */
public interface IGroupTagsActivity {
    /**
     * 群标签推荐
     *
     * @param tagsList
     */
    void showRecommendTeamTag(List<RecommendTeamsBean.DataBean> tagsList);

    /**
     * 添加标签
     *
     * @param attentionBean
     */
    void showAddTeamTag(AttentionBean attentionBean);
}