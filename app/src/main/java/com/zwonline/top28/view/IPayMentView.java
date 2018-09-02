package com.zwonline.top28.view;

import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.PaymentBean;

import java.util.List;


/**
 * 描述：收付款记录
 * @author YSG
 * @date 2017/12/27
 */
public interface IPayMentView {
void showBalance(BalanceBean balanceBean);
void showPayMent(boolean flag);
void showOnErro();
void showPayMentData(List<PaymentBean.DataBean> payList);
}
