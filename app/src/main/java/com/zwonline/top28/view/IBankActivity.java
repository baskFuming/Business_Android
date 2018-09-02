package com.zwonline.top28.view;

import com.zwonline.top28.bean.BankBean;

import java.util.List;


/**
 * 描述：银行卡列表
 * @author YSG
 * @date 2017/12/27
 */
public interface IBankActivity {
    void showBank(List<BankBean.DataBean> bankList);

    void showUnBindBank();
}
