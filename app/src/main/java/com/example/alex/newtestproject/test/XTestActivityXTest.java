package com.example.alex.newtestproject.test;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.adapter.FragmentItemListAdapter;
import com.example.alex.newtestproject.test.base.BaseFragment;
import com.example.alex.newtestproject.test.fragment.Test1Fragment;
import com.example.alex.newtestproject.test.fragment.Test2Fragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 专门用于测试的Activity
 * 上面是按钮专区,下面是展示Fragment
 */
public class XTestActivityXTest extends XTestBaseActivity {

    @BindView(R.id.gv_content)
    GridView gvContent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_xtest;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        ArrayList<FragmengItem> fragmengItems = new ArrayList<>();
        FragmengItem item;
        for (int i = 0; i < 3; i++) {
            item = new FragmengItem();
            item.setName("测试1");
            fragmengItems.add(item);
        }
        gvContent.setAdapter(new FragmentItemListAdapter(context, fragmengItems));
        gvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchIndexButtonEvent(position, id);
            }
        });

        addFragment(new Test2Fragment());
    }

    private void switchIndexButtonEvent(int position, long id) {
        switch (position) {
            case 0: {
                addFragment(new Test1Fragment());
                break;
            }
            case 1: {
                addFragment(new Test2Fragment());
                break;
            }
            case 2: {
                addFragment(new Test1Fragment());
                break;
            }
        }
    }

    private void addFragment(BaseFragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 如果不为null 隐藏上一个Fragment
        BaseFragment oldFragment = (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.ll_container);
        if (oldFragment != null && oldFragment.isAdded()) {
            transaction.hide(oldFragment);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.ll_container, fragment, fragment.getClass().getSimpleName());
        }

        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class FragmengItem {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
