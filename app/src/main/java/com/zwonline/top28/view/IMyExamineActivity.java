package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyExamine;

import java.util.List;

/**
 * @author YSG
 * @desc我的考察
 * @date ${Date}
 */
public interface IMyExamineActivity {
    /**
     * 判别是否有数据
     *
     * @param flag
     */
    void showMyExamine(boolean flag);

    /**
     * 显示数据
     *
     * @param myExamineList
     */
    void showMyExamineDate(List<MyExamine.DataBean> myExamineList);

    /**
     * 没有更多数据了
     */
    void noLoadMore();
}
