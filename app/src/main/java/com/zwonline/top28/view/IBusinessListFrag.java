package com.zwonline.top28.view;

import com.zwonline.top28.bean.BusinessListBean;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date 2017/12/14
 */
public interface IBusinessListFrag {
    void showData(List<BusinessListBean.DataBean> list);
    void showErro();
}
