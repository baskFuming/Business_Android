package com.zwonline.top28.view;

import com.zwonline.top28.bean.HotExamineBean;
import com.zwonline.top28.bean.VideoBean;

import java.util.List;

/**
 * @author YSG
 * @desc 热门考察
 * @date 2017/12/14
 */
public interface IHotExamine {
    void showHotExamineData(List<HotExamineBean.DataBean> hotExamineList);

    void showVideo(List<VideoBean.DataBean> videolist);
    void showErro();
}
