package com.zwonline.top28.view;

import com.zwonline.top28.bean.OptionContractBean;

import java.util.List;

/**
 * @author YSG
 * @desc选择合同v层
 * @date ${Date}
 */
public interface IOptionContractActivity {
    /**
     * 显示合同
     */
    void showOptionContract(List<OptionContractBean.DataBean> optionContractBean);

    /**
     * 判断是否有数据
     * @param flag
     */
    void noContract(boolean flag);
}
