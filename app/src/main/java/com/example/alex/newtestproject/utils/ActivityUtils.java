package com.example.alex.newtestproject.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.alex.newtestproject.R;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Activity工具类
 */
@SuppressWarnings("ALL")
public class ActivityUtils {
    public static long startBorrowTime = 0;
    public static long timeInvael = 1000 * 60;
    public static final String KEY_ACTIVITY_DATA = "data";
    public static final String KEY_ACTIVITY_RESULT_DATA = "data_result";
    public static final String KEY_ACTIVITY_SERIALIZABLE = "Serializable";

    /**声明各种类型文件的dataType**/
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";

    /**
     * 启动一个Activity并关闭当前Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity
     */
    public static void startActivityAndFinish(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        startActivityForIntent(activity, intent);
        activity.finish();
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     */
    public static void startActivity(Context activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        startActivityForIntent(activity, intent);
    }

    /**
     * 启动Activity并传int数据 key:KEY_ACTIVITY_DATA
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param data     int型数据
     */
    public static void startActivityForIntData(Context activity, Class<?> cls, int data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        startActivityForIntent(activity, intent);
    }

    /**
     * 启动Activity并传String数据 key:KEY_ACTIVITY_DATA
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param data     String型数据
     */
    public static void startActivityForData(Context activity, Class<?> cls, String data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        startActivityForIntent(activity, intent);
    }

    public static void startActivityForData(Context activity, Class<?> cls, double data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        startActivityForIntent(activity, intent);
    }

    public static void startActivityForData(Context activity, Class<?> cls, int data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        startActivityForIntent(activity, intent);
    }

    public static void startActivityForData(Context activity, Class<?> cls, long data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        startActivityForIntent(activity, intent);
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:KEY_ACTIVITY_DATA
     *
     * @param activity    当前Activity
     * @param cls         要启动的Activity Class
     * @param data        String型数据
     * @param requestCode int标记
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, String data,
                                              int requestCode) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_RESULT_DATA, data);
        intent.setFlags(requestCode);
        activity.startActivityForResult(intent, requestCode);
        enterBeginAnimation(activity);
    }

    /**
     * 启动Activity传String数据并接收返回结果 key:KEY_ACTIVITY_DATA
     *
     * @param fragment    当前fragment
     * @param cls         要启动的Activity Class
     * @param data        String型数据
     * @param requestCode int标记
     */
    public static void startActivityForResult(Fragment fragment, Class<?> cls, String data,
                                              int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        intent.putExtra(KEY_ACTIVITY_RESULT_DATA, data);
        intent.setFlags(requestCode);
        fragment.startActivityForResult(intent, requestCode);
        enterBeginAnimation(fragment.getActivity());
    }

    /**
     * 启动Activity并传序列化对象数据 key:"Serializable"
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     * @param data     String型数据
     */
    public static void startActivityForSerializable(Context activity, Class<?> cls,
                                                    Serializable data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(KEY_ACTIVITY_SERIALIZABLE, data);
        startActivityForIntent(activity, intent);
    }

    /**
     * 启动Activity并传String对象数据
     *
     * @param context 当前Activity
     * @param name    key
     * @param cls     要启动的Activity Class
     * @param data    String数据
     */
    public static void startActivityForStringData(Context context, String name, Class<?> cls,
                                                  String data) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(name, data);
        startActivityForIntent(context, intent);
    }

    /**
     * 启动Activity并传对象数据
     *
     * @param context 对象
     * @param cls     对象
     * @param bundle  对象数据
     */
    public static void startActivityForBundle(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForIntent(context, intent);
    }

    /**
     * 启动activity基类
     *
     * @param activity
     */
    public static void startActivityForIntent(Context activity, Intent intent) {
        activity.startActivity(intent);
        if (activity instanceof Activity) {
            enterBeginAnimation((Activity) activity);
        }
    }

    /**
     * 启动网络设置
     *
     * @param activity 当前Activity
     */
    public static void startSetNetActivity(Context activity) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 启动系统设置
     *
     * @param activity 当前Activity
     */
    public static void startSetActivity(Context activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 获取当前栈顶的类名
     *
     * @param context
     * @return
     */
    public static String getCurrentClassName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
        String packageName = runningTaskInfos.get(0).topActivity.getClassName();
        return packageName;
    }


    /**
     * 计算时间 60秒只能成功发起一次请求
     *
     * @return
     */
    public static boolean caculTime() {
        if (System.currentTimeMillis() - startBorrowTime >= timeInvael) {
            return true;
        }
        return false;
    }

    /**
     * 动画效果 for activity
     *
     * @param activity
     */
    protected static void enterBeginAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    /**
     * 调用第三方浏览器打开
     *
     * @param context context
     * @param url     要浏览的资源地址
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用第三方浏览器打开
     *
     * @param context context
     * @param url     要浏览的资源地址
     */
    public static void openBrowserByLocalFile(Context context, String filePath) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        Uri directoryUri = FileProvider.getUriForFile(context
                , context.getPackageName() + ".provider"
                , new File(filePath));

        intent.setData(directoryUri);
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            // 打印Log   ComponentName到底是什么
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
//            context.startActivity((intent));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }


}
