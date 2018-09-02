package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc积分纪录
 * @date ${Date}
 */
public class IntegralRecordBean {
    public String record_name;
    public int cate_id;

    public IntegralRecordBean(String record_name, int cate_id) {
        this.record_name = record_name;
        this.cate_id = cate_id;
    }

    public IntegralRecordBean() {
    }
}
