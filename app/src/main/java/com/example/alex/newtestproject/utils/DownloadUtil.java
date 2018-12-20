package com.example.alex.newtestproject.utils;

import android.os.Environment;

import java.io.File;

public class DownloadUtil {

    private static final String DOWN_DIRPATH = "/kbmdownload/";

    /**
     * 下载路径
     * @return
     */
    public static String getDownloadUrl() {
        return getSDPath() + DOWN_DIRPATH;
    }

    public static String getSDPath() {
        File sdDir = new File("");
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            // 此目录为公用的Download文件夹
            sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        return sdDir.toString();
    }
}
