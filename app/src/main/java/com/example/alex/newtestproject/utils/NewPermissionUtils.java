package com.example.alex.newtestproject.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.alex.newtestproject.rxJava.permission.CheckPermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 * 一种请求权限的方法
 * 不需要混淆
 */
public class NewPermissionUtils {

    /**
     危险权限表:
     CALENDAR
     READ_CALENDAR
     WRITE_CALENDAR

     CAMERA
     CAMERA

     CONTACTS
     READ_CONTACTS
     WRITE_CONTACTS
     GET_ACCOUNTS

     LOCATION
     ACCESS_FINE_LOCATION
     ACCESS_COARSE_LOCATION

     MICROPHONE
     RECORD_AUDIO

     PHONE
     READ_PHONE_STATE
     CALL_PHONE
     READ_CALL_LOG
     WRITE_CALL_LOG
     ADD_VOICEMAIL
     USE_SIP
     PROCESS_OUTGOING_CALLS

     SENSORS
     BODY_SENSORS

     SMS
     SEND_SMS
     RECEIVE_SMS
     READ_SMS
     RECEIVE_WAP_PUSH
     RECEIVE_MMS

     STORAGE
     READ_EXTERNAL_STORAGE
     WRITE_EXTERNAL_STORAGE
     */

    /**
     // 在Activity：
     AndPermission.with(activity)
     .requestCode(100)
     .permission(Manifest.permission.WRITE_CONTACTS)
     .rationale(...)
     .callback(...)
     .start();

     // 在Fragment：
     AndPermission.with(fragment)
     .requestCode(100)
     .permission(
     // 多个权限，以数组的形式传入。
     Manifest.permission.WRITE_CONTACTS,
     Manifest.permission.READ_SMS
     )
     .rationale(...)
     .callback(...)
     .start();

     // 在其它任何地方：
     AndPermission.with(context)
     .requestCode(100)
     .permission(
     Manifest.permission.WRITE_CONTACTS,
     Manifest.permission.READ_SMS
     )
     .rationale(...)
     .callback(...)
     .start();
     */

    /**
     * https://github.com/yanzhenjie/AndPermission/blob/master/README-CN.md
     *
     * @param permissions
     */
    public static void checkPermission(final Activity activity, String[] permissions) {
        for (String s : permissions) {
            boolean bs = CheckPermissionUtils.isNeedAddPermission(activity, s);
            int i = 0;
            LogUtils.d("" + bs);
        }


        AndPermission.with(activity)
                .requestCode(100)
                .permission(permissions)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantedPermissions) {
                        // success
                        int aaa = 0;

                        if (AndPermission.hasPermission(activity, grantedPermissions)) {
                            int aaa1 = 0;
                            // 成功!!!

                        }

                        /*

                        else {
                            // 是否有不再提示并拒绝的权限。
                            if (AndPermission.hasAlwaysDeniedPermission(activity, grantedPermissions)) {
                                // 第一种：用AndPermission默认的提示语。
                                AndPermission.defaultSettingDialog(activity, 400).show();
                            }
                        }  */
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        // failure
                        int aaa = 0;
                        LogUtils.d("failed");
                        Toast.makeText(activity, "申请权限失败了", Toast.LENGTH_SHORT).show();

                        // 是否有不再提示并拒绝的权限。
                        if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
                            // 第一种：用AndPermission默认的提示语。
                            AndPermission.defaultSettingDialog(activity, 300).show();
                        }
                    }
                })
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int i, Rationale rationale) {
                        int aaa = 0;

                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(activity, rationale).show();
                    }
                })

                .start();

    }
}
