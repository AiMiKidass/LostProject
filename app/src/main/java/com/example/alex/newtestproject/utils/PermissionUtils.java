package com.example.alex.newtestproject.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * 权限类
 * 整合,配合插件一起使用
 */

public class PermissionUtils {

    public static void requestPermission(final Activity context, final int actionRequestCode, String[] permissions, final OnPermissionResultListener listener) {
        if (listener == null) {
            LogUtils.d("请保证permissionlistener不为null");
            return;
        }

        // 申请单个权限。
        AndPermission.with(context)
                .requestCode(actionRequestCode)
                .permission(permissions)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestcode, @NonNull List<String> permissions) {
                        if (requestcode == actionRequestCode) {
                            if (AndPermission.hasPermission(context, permissions)) {
                                // Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
                                // success
                                listener.onSuccess();
                            } else {
                                Toast.makeText(context, "授权失败,请进入设置页设置", Toast.LENGTH_SHORT).show();
                                // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                // 去掉注释进入设置页
                                AndPermission.defaultSettingDialog(context, actionRequestCode).show();
                                listener.onFailure();
                            }
                        }
                    }

                    @Override
                    public void onFailed(int requestcode, @NonNull List<String> deniedPermissions) {
                        if (requestcode == actionRequestCode) {
                            Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
                            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                            if (AndPermission.hasAlwaysDeniedPermission(context, deniedPermissions)) {
                                // 第一种：用默认的提示语。
                                AndPermission.defaultSettingDialog(context, actionRequestCode).show();
                                listener.onFailure();
                            }
                        }
                    }
                })
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(context, rationale).show();
                    }
                })
                .start();
    }

    public interface OnPermissionResultListener {
        void onSuccess();

        void onFailure();
    }
}
