package com.zwonline.top28.bean;

import com.zwonline.top28.base.BaseBean;

import java.io.Serializable;

/**
 * 余额充值界面
 * Created by sdh on 2018/3/12.
 */

public class BalanceRechargeBean extends BaseBean implements Serializable {

    public DataBean data;

    public static class DataBean {
        public String order_str;
    }


}
