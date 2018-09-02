package com.zwonline.top28.view;


import com.zwonline.top28.bean.HomeBean;

import java.util.List;

/**
 * 描述：首页分类
 *
 * @author YSG
 * @date 2018/1/9
 */
public interface IHomeClassFrag {
    /**
     * 首页分类
     *
     * @param classList
     */
    void showHomeClass(List<HomeBean.DataBean> classList);

    /**
     * 首页列表
     *
     * @param homeBean
     */
    void showHomesClass(HomeBean homeBean);

    /**
     * 异常
     */
    void showErro();
}
