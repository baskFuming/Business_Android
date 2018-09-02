package com.zwonline.top28.view;

import com.zwonline.top28.bean.MyIssueBean;

import java.util.List;

public interface IHomeWordActivity {
    /**
     * 获取内容失败
     */
    void onErro();
    /**
     * 我的发布判断是否
     *
     * @param flag
     */
    void showMyIssue(boolean flag);

    /**
     * 发布文章
     *
     * @param issueList
     */
    void showMyIssueDate(List<MyIssueBean.DataBean> issueList);

    /**
     * 没有更多文章了
     */
    void issueNoLoadMore();


}
