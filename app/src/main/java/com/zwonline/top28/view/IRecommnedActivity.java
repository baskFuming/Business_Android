package com.zwonline.top28.view;

import com.zwonline.top28.bean.RecommendUserBean;

import java.util.List;

/**
 * 我的推荐 V 层
 */
public interface IRecommnedActivity {
    void successRecommed(RecommendUserBean recommendUserBean);
    void successRecommedList(List<RecommendUserBean.DataBean> recommendlist);
    void  onErro();
}
