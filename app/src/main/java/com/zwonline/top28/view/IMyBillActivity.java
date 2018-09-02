package com.zwonline.top28.view;


import com.zwonline.top28.bean.MyBillBean;

import java.util.List;

/**
 * 描述：我的账单
 * @author YSG
 * @date 2017/12/26
 */
public interface IMyBillActivity {
    void showMyBill(boolean flag);

    void showMyBillData(List<MyBillBean.DataBean> billList);
}
