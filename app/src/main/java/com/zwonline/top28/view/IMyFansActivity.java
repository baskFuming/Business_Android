package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyFansBean;

import java.util.List;


/**
 * 描述：粉丝
 *
 * @author YSG
 * @date 2017/12/22
 */
public interface IMyFansActivity {
    /**
     * 显示数据
     *
     * @param fansList
     */
    void showMyFansDate(List<MyFansBean.DataBean> fansList);

    /**
     * 判断是否有数据
     *
     * @param flag
     */
    void showMyFans(boolean flag);

    /**
     * 没有更多
     */
    void noLoadMore();
}
