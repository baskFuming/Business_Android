package com.zwonline.top28.bean;

import java.util.List;


/**
 * 描述：我的收藏
 * @author YSG
 * @date 2017/12/24
 */
public class MyCollectBean {

/**
 * status : 1
 * msg :
 * data : [{"id":"1505","title":"这所曾经亚洲第一的大学，被迫拆分得断腿残足，仍能进入双一流！","cdate":"2017-12-12","write_type":"1","view":"7","path":"https://toutiao.28.com/data/upload/article_img/1712/12/07decc8a45e86e5c5013cf476f8de752.jpg_150x150.jpg"},{"id":"1422","title":"人工智能第一妖股96元跌至5元，突破42亿巨资抢筹，已回调完毕，买入坐等赚钱!","cdate":"2017-12-11","write_type":"2","view":"3","path":"https://toutiao.28.com/data/upload/article_img/1712/11/c1a7c8ee3604b6f2baacda65ffc3c3be.jpg_150x150.jpg"},{"id":"1318","title":"为何股神能在股市稳赚不赔，他们都遵循了这三个选股原则!","cdate":"2017-12-08","write_type":"2","view":"10","path":"https://toutiao.28.com/data/upload/article_img/1712/08/7b9eb4de315315e4ef32aa3a9a3362b9.jpg_150x150.jpg"},{"id":"1289","title":"马云说京东员工会达一百万，然后自己崩溃","cdate":"2017-12-08","write_type":"2","view":"6","path":"https://toutiao.28.com/data/upload/article_img/1712/08/12269044b876bc5cb0c65cb66386b8f3.jpg_150x150.jpg"},{"id":"1029","title":"腾讯在美实施特洛伊木马式投资，中国乐信将赴纳斯达克上市","cdate":"2017-12-06","write_type":"2","view":"11","path":"https://toutiao.28.com/data/upload/article_img/1712/06/e5cef737cf1590720bd2eb1d0e4a8f1f.jpg_150x150.jpg"}]
 * dialog :
 */

public int status;
public String msg;
public String dialog;
public List<DataBean> data;

public static class DataBean {
    /**
     * id : 1505
     * title : 这所曾经亚洲第一的大学，被迫拆分得断腿残足，仍能进入双一流！
     * cdate : 2017-12-12
     * write_type : 1
     * view : 7
     * path : https://toutiao.28.com/data/upload/article_img/1712/12/07decc8a45e86e5c5013cf476f8de752.jpg_150x150.jpg
     */

    public String id;
    public String title;
    public String cdate;
    public String write_type;
    public String view;
    public String path;
}
}
