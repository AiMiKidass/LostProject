package com.example.alex.newtestproject.rxJava;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.base.PermissionBaseActivity;
import com.example.alex.newtestproject.rxJava.fragment.BaseZhuangBiFragment;
import com.example.alex.newtestproject.rxJava.fragment.CacheFragment;
import com.example.alex.newtestproject.rxJava.fragment.ElementaryFragment;
import com.example.alex.newtestproject.rxJava.fragment.FinalTestFragment;
import com.example.alex.newtestproject.rxJava.fragment.MapFragment;
import com.example.alex.newtestproject.rxJava.fragment.TestFragment1;
import com.example.alex.newtestproject.rxJava.fragment.TokenAdvancedFragment;
import com.example.alex.newtestproject.rxJava.fragment.TokenFragment;
import com.example.alex.newtestproject.rxJava.fragment.ZipFragment;
import com.example.alex.newtestproject.utils.NewPermissionUtils;
import com.yanzhenjie.permission.AndPermission;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RxJavaDemoTestActivity extends PermissionBaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(android.R.id.tabs)
    TabLayout tablayout;

    private static final int INDEX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_demo_test);
        ButterKnife.bind(this);

        initView();
        request1();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 400: { // This is the 400 you are above the number of incoming.
                // Check the permissions, and make the appropriate operation.
                if (AndPermission.hasPermission(this, permissions)) {
                    // 在这里写获得权限后的操作


                }
                break;
            }
        }
    }

    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


    private void request1() {
        NewPermissionUtils.checkPermission(this, permissions);

    }

    private void initView() {

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public BaseZhuangBiFragment getItem(int position) {
                switch (position) {
                    case INDEX - 1:
                        return new TestFragment1();
                    case INDEX:
                        return new FinalTestFragment();
                    case INDEX + 1:
                        return new ElementaryFragment();
                    case INDEX + 2:
                        return new MapFragment();
                    case INDEX + 3:
                        return new ZipFragment();
                    case INDEX + 4:
                        return new TokenFragment();
                    case INDEX + 5:
                        return new TokenAdvancedFragment();
                    case INDEX + 6:
                        return new CacheFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_elementary);
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });
        tablayout.setupWithViewPager(viewPager);
    }


}
