package com.zwonline.top28.view;

import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.CompanyBean;
import com.zwonline.top28.bean.ExamineChatBean;
import com.zwonline.top28.bean.MyIssueBean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public interface ICompanyActivity {
    void showCompany(CompanyBean companyBean);
    void showColmpanyArticle(List<MyIssueBean.DataBean>articleList);
    void showErro();

    /**
     * 检查是否聊过天
     * @param examineList
     */
    void showExamineChat(ExamineChatBean.DataBean examineList);

    /**
     * 在线聊天
     * @param amountPointsBean
     */
    void showOnLineChat(AmountPointsBean amountPointsBean);
}
