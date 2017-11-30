package com.example.alex.newtestproject.rxJava.permission;

import android.app.Fragment;

import rx.subjects.PublishSubject;

/**
 * Created by Administrator on 2017/6/22.
 */
public class RxPermissionsFragment extends Fragment {
    public boolean isGranted(String permission) {
        return false;
    }

    public boolean isRevoked(String permission) {
        return false;
    }

    public PublishSubject<Permission> getSubjectByPermission(String permission) {
        return null;
    }

    public void setSubjectForPermission(String permission, PublishSubject<Permission> subject) {

    }

    public void log(String s) {

    }

    public void requestPermissions(String[] permissions) {

    }
}
