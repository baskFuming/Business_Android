package com.zwonline.top28.bean;

import java.util.List;


/**
 * 描述：添加品论
 * @author YSG
 * @date 2018/1/4
 */
public class AddCommentBean {

/**
 * status : 1
 * msg : 发布成功
 * data : [{"id":"119","article_id":"3010","content":"test","cdate":"2018-01-03","ctime":"2018-01-03 17:34:52","isdel":"0","pid":"0","uid":"0","digg":"0","floor":"","member":[]}]
 * dialog :
 */

public int status;
public String msg;
public String dialog;
public List<DataBean> data;

public static class DataBean {
    /**
     * id : 119
     * article_id : 3010
     * content : test
     * cdate : 2018-01-03
     * ctime : 2018-01-03 17:34:52
     * isdel : 0
     * pid : 0
     * uid : 0
     * digg : 0
     * floor :
     * member : []
     */

    public String id;
    public String article_id;
    public String content;
    public String cdate;
    public String ctime;
    public String isdel;
    public String pid;
    public String uid;
    public String digg;
    public String floor;

}
}
