package com.example.alex.newtestproject.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.base.BaseActivity;
import com.example.alex.newtestproject.view.NoPermissionAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 功能代码 小程序专用测试界面
 */
public class ManyLittleCommonActivity extends BaseActivity {

    private List<Pair<String, String>> datas = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.activity_many_little_common;
    }

    Pair<String, String> makePair(String key, String value) {
        Pair<String, String> pair = new Pair<>(key, value);
        return pair;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        datas.add(makePair("0", "PDF打开"));

        findViewById(R.id.timeline).setEnabled(false);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.act_example01)
    void test001(){
        int i = 0;

        NoPermissionAlertDialog noPermissionAlertDialog = new NoPermissionAlertDialog(this);
        noPermissionAlertDialog.show();
    }


    public static class MyAdapter extends BaseQuickAdapter<Pair<String, String>, BaseViewHolder> {

        public MyAdapter(@Nullable List<Pair<String, String>> datas) {
            super(R.layout.item_common_single, datas);
        }

        @Override
        protected void convert(BaseViewHolder helper, Pair<String, String> item) {
            helper.setText(R.id.view_item_title, item.second);
        }
    }
}
