package com.zwonline.top28.view;

import com.zwonline.top28.bean.BannerBean;
import com.zwonline.top28.bean.BusinessClassifyBean;
import com.zwonline.top28.bean.BusinessListBean;
import com.zwonline.top28.bean.JZHOBean;
import com.zwonline.top28.bean.RecommendBean;

import java.util.List;

/**
 * @author YSG
 * @desc 商机的view层
 * @date 2017/12/14
 */
public interface IBusinessClassFra {
    void showBusinessClassFra(List<BusinessClassifyBean.DataBean> classList);

    void showSearch(List<BusinessListBean.DataBean> searchList);

    void showBanner(List<BannerBean.DataBean> bannerList);

    void showRecommend(List<RecommendBean.DataBean> recommendList);

    void showData(List<BusinessListBean.DataBean> list);

    void showJZHO(List<JZHOBean.DataBean> JZHOlist);

    void showErro();
}
