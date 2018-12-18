package com.example.alex.newtestproject.model;

import com.google.gson.annotations.SerializedName;

/**
 * RESTFul 返回值封装类
 *
 */
public class RESTResult<T> {

    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;

    @SerializedName("res")
    private int res;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    T data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

