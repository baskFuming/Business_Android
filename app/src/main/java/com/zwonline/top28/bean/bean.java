package com.zwonline.top28.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class bean {

    /**
     * status : 1
     * msg : 成功
     * data : []
     * dialog :
     */

    @SerializedName("status")
    public int status;
    @SerializedName("msg")
    public String msg;
    @SerializedName("dialog")
    public String dialog;
    @SerializedName("data")
    public List<?> data;
}

