package com.zwonline.top28.bean;

import com.google.gson.annotations.SerializedName;
import com.zwonline.top28.base.BaseBean;

import java.io.Serializable;

/** 订单信息
 * Created by sdh on 2018/3/12.
 */

public class PrepayPayBean extends BaseBean implements Serializable {

    /**
     * data : {"appid":"wx979d60eb9639eb65","noncestr":"1545381204368","package":"Sign=WXPay","partnerid":"1498354802","prepayid":"wx21163324288168337d0963dc2039178187","sign":"CB8E85348F7BCA910A71184B0CE69904","timestamp":"1545381204"}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * appid : wx979d60eb9639eb65
         * noncestr : 1545381204368
         * package : Sign=WXPay
         * partnerid : 1498354802
         * prepayid : wx21163324288168337d0963dc2039178187
         * sign : CB8E85348F7BCA910A71184B0CE69904
         * timestamp : 1545381204
         */

        public String appid;
        public String noncestr;
        @SerializedName("package")
        public String packageX;
        public String partnerid;
        public String prepayid;
        public String sign;
        public String timestamp;
        public String order_str;
    }
}
