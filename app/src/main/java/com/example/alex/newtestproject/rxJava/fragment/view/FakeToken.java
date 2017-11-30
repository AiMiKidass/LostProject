package com.example.alex.newtestproject.rxJava.fragment.view;

/**
 * Created by Administrator on 2017/6/8.
 */
public class FakeToken {
    public String token;
    public boolean expired;

    public FakeToken() {
    }



    public FakeToken(String token, boolean expired) {
        this.token = token;
        this.expired = expired;
    }

    public FakeToken(boolean expired) {
        this.expired = expired;
    }
}
