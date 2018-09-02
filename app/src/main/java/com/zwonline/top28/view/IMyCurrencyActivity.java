package com.zwonline.top28.view;


import com.zwonline.top28.bean.MyCurrencyBean;

import java.util.List;

/**
 * 描述：我的创业币
 * @author YSG
 * @date 2017/12/25
 */
public interface IMyCurrencyActivity {
    void showMyCurrency(MyCurrencyBean bean);
    void showMyCurrencyData(List<MyCurrencyBean.DataBean.ListBean> currencyList);
}
