package com.example.alex.newtestproject.rxJava.base;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.alex.newtestproject.rxJava.permission.CheckPermissionUtils;

/**
 */
public abstract class PermissionBaseActivity extends AppCompatActivity {


    private static final int REQUESTCODE_PERMISSION_DEFAULT = -100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {

    }

    /**
     * 调用该方法实现requestpermission
     *
     * @return true = 需要处理权限逻辑  false = 不需要处理权限逻辑
     */
    protected boolean callPermission(String... permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return false;
        }

        String[] neededPermission = CheckPermissionUtils.getNeededPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (neededPermission != null && neededPermission.length > 0) {
            super.requestPermissions(neededPermission, REQUESTCODE_PERMISSION_DEFAULT);

        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}
