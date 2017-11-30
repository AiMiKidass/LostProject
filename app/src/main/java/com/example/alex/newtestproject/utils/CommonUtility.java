package com.example.alex.newtestproject.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 *
 * @version V1.0
 * @author:zhangkx
 * @time: 2015-11-6 下午4:54:23
 */
public class CommonUtility {
    private static final String TAG = "Response";
    public static List<File> list = new ArrayList<>();

    /**
     * 判断流量是否可用
     *
     * @author:zhangkx
     * @time: 2015-11-6 下午5:02:32
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 显示提示
     *
     * @param context
     * @param msg     提示内容
     */
    public static void showToast(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    /**
     * 获取出了通知栏外的屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        Activity activity = ((Activity) context);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(localDisplayMetrics);
        int statusBarHeight = getStatusBarHeight(context);
        int smartBarHeight = getSmartBarHeight(context);
        // 去掉通知栏的高度
        return localDisplayMetrics.heightPixels - statusBarHeight
                - smartBarHeight;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Activity activity = ((Activity) context);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(localDisplayMetrics);
        // 去掉通知栏的高度
        return localDisplayMetrics.widthPixels;
    }

    /**
     * 获取通知栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (statusBarHeight <= 0) {
            statusBarHeight = 50;
        }

        return statusBarHeight;
    }

    /**
     * 获取底部菜单栏高度
     *
     * @param context
     * @return
     */
    public static int getSmartBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("smart_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (statusBarHeight <= 0) {
            statusBarHeight = 50;
        }

        return statusBarHeight;
    }

    /**
     * 格式化金额 使输出 123,456.00
     *
     * @author:zhangkx
     * @time: 2015-11-6 下午5:19:35
     */
    public static String format(String number) {
        DecimalFormat format = new DecimalFormat("###,###.00");
        String str = format.format(Double.parseDouble(number));
        if (str.indexOf(".") == 0) {
            str = "0" + str;
        }
        return str;
    }


    /**
     * <h1>取值规则：<h1> <h1>1、当天时间返回 今天 HH:mm<h1> <h1>2、昨天时间返回 昨天 HH:mm<h1> <h1>
     * 3、同年时间返回 昨天 MM-dd HH:mm<h1> <h1>4、隔年时间返回 昨天 yyyy-MM-dd HH:mm<h1>
     *
     * @param time 需要格式化的时间，毫秒数表示
     * @return 格式化后的时间
     * @author zhangkx
     */
    public static String formatConversationDate(long time) {
        // 系统当前时间
        Date currentTime = new Date();
        Date targetTime = new Date(time);
        // 返回的时间
        String formatTime;
        // 按照年月日格式化
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        // 年
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy",
                Locale.getDefault());
        // 计算在一年中的第几天
        SimpleDateFormat formatDayOfYear = new SimpleDateFormat("DD",
                Locale.getDefault());
        String currentDay = formatDay.format(currentTime);
        String targetDay = formatDay.format(targetTime);

        String currentYear = formatYear.format(currentTime);
        String targetYear = formatYear.format(targetTime);

        if (currentDay.equals(targetDay)) {
            // 判断是否当天
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
                    Locale.getDefault());
            formatTime = sdf.format(targetTime);
            return "今天 " + formatTime;
        } else if (currentYear.equals(targetYear)) {
            // 同年
            int currentDayOfYear = Integer.parseInt(formatDayOfYear
                    .format(currentTime));
            int targetDayOfYear = Integer.parseInt(formatDayOfYear
                    .format(targetTime));
            // 判断是否昨天
            if (currentDayOfYear - 1 == targetDayOfYear) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
                        Locale.getDefault());
                formatTime = sdf.format(targetTime);
                return "昨天 " + formatTime;
            } else {
                // 同年同月不同天，返回月日
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd   HH:mm",
                        Locale.getDefault());
                formatTime = sdf.format(targetTime);
            }
        } else {
            // 隔年
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd   HH:mm", Locale.getDefault());
            formatTime = format.format(targetTime);
        }
        return formatTime;
    }

    /**
     * 将时间格式化成年月日
     *
     * @param time 时间
     * @return 年月日格式
     * @author zhangkx
     */
    public static String formatYYMMDDHHMMSS(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        return formatDay.format(date);
    }

    /**
     * 将时间格式化成年月日 时分秒
     *
     * @param time 时间
     * @return 年月日格式
     * @author zhangkx
     */
    public static String formatYYMMDD(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        return formatDay.format(date);
    }

    /**
     * 格式化 EditText
     *
     * @param et
     * @return
     */
    public static String formatText(EditText et) {
//		String result = null;
//		if (et.getText().toString().trim() == null) {
//			result = "未填写";
//		} else {
//			result = et.getText().toString().trim();
//		}
        return et.getText().toString().trim();

    }

    /**
     * 格式化 TextView
     *
     * @param
     * @return
     */
    public static String formatText(TextView str) {
//		String result = null;
//		if (str.getText().toString().trim() == null) {
//			result = "未填写";
//		} else {
//			result = str.getText().toString().trim();
//		}
        return str.getText().toString().trim();

    }

    /*
     * 判断手机格式是否正确
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|17[0,6-8]|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches() && mobiles.length() == 11;
    }

    /**
     * 判断是否全是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches() && str.length() > 0;
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断email格式是否正确
     *
     * @param jinE
     * @return
     */
    public static boolean isJinE(String jinE) {
        String str = "^(([0-9]|([1-9][0-9]{0,9}))((\\.[0-9]{1,2})?))$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(jinE);
        return m.matches();
    }

    // 获取当前目录下所有的office文件
    public static List<File> getVideoFileName(String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        for (File aSubFile : subFile) {
            // 判断是否为文件夹
            if (!aSubFile.isDirectory()) {
                String filename = aSubFile.getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".doc")
                        || filename.trim().toLowerCase().endsWith(".xls")
                        || filename.trim().toLowerCase().endsWith(".ppt")
                        || filename.trim().toLowerCase().endsWith(".docx")
                        || filename.trim().toLowerCase().endsWith(".docm")
                        || filename.trim().toLowerCase().endsWith(".dotx")
                        || filename.trim().toLowerCase().endsWith(".dotm")
                        || filename.trim().toLowerCase().endsWith(".xlsx")
                        || filename.trim().toLowerCase().endsWith(".xlsm")
                        || filename.trim().toLowerCase().endsWith(".xltx")
                        || filename.trim().toLowerCase().endsWith(".xltm")
                        || filename.trim().toLowerCase().endsWith(".xlsb")
                        || filename.trim().toLowerCase().endsWith(".xlam")
                        || filename.trim().toLowerCase().endsWith(".pptx")
                        || filename.trim().toLowerCase().endsWith(".pptm")
                        || filename.trim().toLowerCase().endsWith(".ppsx")
                        || filename.trim().toLowerCase().endsWith(".ppsm")
                        || filename.trim().toLowerCase().endsWith(".potx")
                        || filename.trim().toLowerCase().endsWith(".potm")
                        || filename.trim().toLowerCase().endsWith(".ppam")) {
                    list.add(aSubFile);
                }
            } else {
                getVideoFileName(aSubFile.getPath());
            }
        }
        return list;
    }

    public static String getRealPath(Context context, Uri fileUrl) {
        // 4.3和4.4的路径不同,需要注意;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

        }

        String fileName = null;
        if (fileUrl != null) {
            if (fileUrl.getScheme().compareTo("content") == 0) {

                String realPathFromURI = getRealPathFromURI(context, fileUrl);
                fileName = realPathFromURI;
                /*
                // content://开头的uri
                Cursor cursor = context.getContentResolver().query(fileUrl, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileName = cursor.getString(column_index); // 取出文件路径

                    // Android 4.1 更改了SD的目录，sdcard映射到/storage/sdcard0
                    if (!fileName.startsWith("/storage") && !fileName.startsWith("/mnt")) {
                        // 检查是否有”/mnt“前缀
                        fileName = "/mnt" + fileName;
                    }
                    cursor.close();

                }*/
            } else if (fileUrl.getScheme().compareTo("file") == 0) {// file:///开头的uri
                fileName = fileUrl.toString().replace("file://", "");
                int index = fileName.indexOf("/sdcard");
                fileName = index == -1 ? fileName : fileName.substring(index);
//                if (!fileName.startsWith("/mnt")) {
//                    // 加上"/mnt"头
//                    fileName = "/mnt" + fileName;
//                }
            }
        }
        return fileName;
    }

    private static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

//    /**
//     * 是否拦截
//     */
//    public static boolean isLanJie() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String start = "2016-02-07";
//        String end = "2016-02-14";
//        long current = System.currentTimeMillis();
//        try {
//            if (current > format.parse(start).getTime() && current < format.parse(end).getTime())
//                return true;
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
