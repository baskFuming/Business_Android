package com.zwonline.top28.view;


import com.zwonline.top28.bean.WithdrawRecordBean;

import java.util.List;

/**
 * 描述：提现和提现记录
 * @author YSG
 * @date 2018/1/10
 */
public interface IBalanceActivity {
    /**
     * 提现记录
     *
     * @param withdrawList
     */
    void showWithdrawRecord(List<WithdrawRecordBean.DataBean> withdrawList);
    void showWithdrawRecordData(boolean flag);
}
