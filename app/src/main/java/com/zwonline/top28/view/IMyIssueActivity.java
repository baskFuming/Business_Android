package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyIssueBean;

import java.util.List;


/**
 * 描述：我的发布
 * @author YSG
 * @date 2017/12/25
 */
public interface IMyIssueActivity {
    /**
     * 判断是否有数据
     * @param flag
     */
    void showMyIssue(boolean flag);

    /**
     * 显示数据
     * @param issueList
     */
    void showMyIssueDate(List<MyIssueBean.DataBean> issueList);

    /**
     * 加载没有数据了
     */
    void noLoadMore();
}
