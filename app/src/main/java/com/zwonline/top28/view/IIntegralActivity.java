package com.zwonline.top28.view;

import com.zwonline.top28.bean.IntegralBean;

import java.util.List;

/**
 * Created by sdh on 2018/3/10.
 * 积分操作接口
 */

public interface IIntegralActivity {

    /**
     * 积分列表
     * @param integralList
     */
    void showIntegralListByTypeId (List<IntegralBean.DataBean.ListBean> integralList);
}
