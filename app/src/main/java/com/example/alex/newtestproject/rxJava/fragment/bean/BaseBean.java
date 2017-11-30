package com.example.alex.newtestproject.rxJava.fragment.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/13.
 */
public class BaseBean implements Serializable {
    public int code;
    public String msg = "";

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
