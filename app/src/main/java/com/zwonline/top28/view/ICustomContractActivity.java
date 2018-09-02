package com.zwonline.top28.view;

import com.zwonline.top28.bean.AddClauseBean;
import com.zwonline.top28.bean.AddContractBean;
import com.zwonline.top28.bean.SettingBean;
import com.zwonline.top28.bean.SignContractBean;

import java.util.List;

/**
 * @author YSG
 * @desc生成合同
 * @date ${Date}
 */
public interface ICustomContractActivity {
    /**
     * 显示合同
     */
    void showCustomContract(List<AddClauseBean.DataBean.TermsBean> addList);

    /**
     * 显示合同名字和时间
     *
     * @param addClauseBean
     */
    void showCustomContracts(AddClauseBean addClauseBean);

    /**
     * 添加合同保存
     *
     * @param settingBean
     */
    void showAddContract(AddContractBean settingBean);

    /**
     * 签署合同
     * @param signContractBean
     */
    void showSignContract(List<SignContractBean.DataBean.TermsBean> signContractBean);

    void showSinContractTime(SignContractBean signContractBean);
}
