package com.example.alex.newtestproject.rxJava.permission;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/6/22.
 *
 */
public class RxPermissions {

    static final Object TRIGGER = new Object();
     RxPermissionsFragment mRxPermissionsFragment;

    public RxPermissions(@NonNull Activity activity) {
        mRxPermissionsFragment = getRxPermissionsFragment(activity);
    }

    private RxPermissionsFragment getRxPermissionsFragment(Activity activity) {
        return null;
    }











}
