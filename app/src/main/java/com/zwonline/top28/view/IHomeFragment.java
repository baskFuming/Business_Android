package com.zwonline.top28.view;

import com.zwonline.top28.bean.HomeClassBean;

import java.util.List;

/**
 * Created by YU on 2017/12/9.
 */

public interface IHomeFragment {
    void showHomeData(List<HomeClassBean.DataBean> homelist);

    void showSearch(List<HomeClassBean.DataBean> searchList);

    void showHomeclass(HomeClassBean HomeClassBean);

    void showInitData();

    void onErro();

    /**
     * 判断是否有数据
     * @param flag
     */
    void showMyCollect(boolean flag);
}
