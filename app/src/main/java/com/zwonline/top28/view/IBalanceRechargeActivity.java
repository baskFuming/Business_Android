package com.zwonline.top28.view;

import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BalanceRechargeBean;
import com.zwonline.top28.bean.OrderInfoBean;

/**
 * 余额充值
 * Created by sdh on 2018/3/14.
 */

public interface IBalanceRechargeActivity {

    /**
     * 余额充值接口
     *
     * @param dataBeanList
     */
    void walletBalanceRecharge(BalanceRechargeBean dataBeanList);
    void walletBalanceRecharges(AmountPointsBean amountPointsBean);

    /**
     * 获取订单orderStr
     * @param prepayPayBean
     */
//    void getOrderInfoByOrderId(PrepayPayBean.DataBean prepayPayBean);

    /**
     * 获取支付订单详情
     *
     * @param paymentData
     */
    void showOrderInfo(OrderInfoBean.DataBean paymentData);

    /**
     * 单价
     *
     * @param dataBean
     */
    void IUnitPriceId(AmountPointsBean dataBean);
}
