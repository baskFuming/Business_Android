package com.zwonline.top28.view;

import com.zwonline.top28.bean.NewRecomdUserBean;
import com.zwonline.top28.bean.RecommendUserBean;

import java.util.List;

/**
 *   推荐用户
 */
public interface IRecommnedActivity {
    void successRecommed(RecommendUserBean recommendUserBean);
    void successRecommedList(List<NewRecomdUserBean.DataBean> recommendlist);
    void  onErro();
    void newSuccessRecomed(NewRecomdUserBean newRecomdUserBean);
}
