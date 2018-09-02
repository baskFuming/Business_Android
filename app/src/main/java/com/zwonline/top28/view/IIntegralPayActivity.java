package com.zwonline.top28.view;

import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.BalanceBean;
import com.zwonline.top28.bean.BalancePayBean;
import com.zwonline.top28.bean.IntegralPayBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;


/**
 * 积分充值接口层
 * Created by sdh on 2018/3/13.
 */

public interface IIntegralPayActivity {

    /**
     * 获取积分充值返回的金额
     *
     * @param dataBean
     */
    void getPointsRechargeBackAmount(AmountPointsBean dataBean);

    /**
     * 积分充值接口
     *
     * @param dataBean
     */
    void pointsRecharge(IntegralPayBean dataBean);

    /**
     * 获取支付宝orderStr
     *
     * @param paymentData
     */
    void getOrderInfoByOrderId(PrepayPayBean paymentData);

    /**
     * 初始化orderStr
     */
    void initAlipayOrderStr();

    /**
     * 初始化订单信息
     */
    void initOrderInfo();

//    /**
//     * 初始化银行卡信息
//     */
//    void initBankPayInfo();

    /**
     * 单价
     *
     * @param dataBean
     */
    void IUnitPriceId(AmountPointsBean dataBean);

    /**
     * 获取支付订单详情
     *
     * @param paymentData
     */
    void showOrderInfo(OrderInfoBean paymentData);

    /**
     * 余额
     * @param balanceBean
     */
    void showBalance(BalanceBean balanceBean);

    /**
     * 余额
     * @param balancePayBean
     */
    void showBalancePay(BalancePayBean balancePayBean);
}
