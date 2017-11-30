package com.example.alex.newtestproject.test;

import android.os.Bundle;

import com.example.alex.newtestproject.R;

public class TesterListItemActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester_list_item);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {

    }
}
