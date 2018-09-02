package com.zwonline.top28.view;

import com.zwonline.top28.bean.AmountPointsBean;
import com.zwonline.top28.bean.OrderInfoBean;
import com.zwonline.top28.bean.PrepayPayBean;

/**
 * Created by sdh on 2018/3/12.
 * 确认付款功能视图层
 */

public interface IPaymentActivity {

    /**
     * 获取支付宝orderStr
     * @param paymentData
     */
    void getOrderInfoByOrderId(PrepayPayBean.DataBean paymentData);

    /**
     *获取支付订单详情
     * @param paymentData
     */
    void showOrderInfo(OrderInfoBean paymentData);

    /**
     *获取订单支付状态接口
     * @param amountPointsBean
     */
    void showGetOrderPayStatus(AmountPointsBean amountPointsBean);

    /**
     * 轮询订单信息
     */
    void pollingOrderPayStatus();

    /**
     * 停止订单轮询
     */
    void stopPollingOrder();
}
