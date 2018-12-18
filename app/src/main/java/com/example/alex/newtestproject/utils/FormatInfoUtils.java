package com.example.alex.newtestproject.utils;

import android.content.Context;
import android.text.format.Formatter;

/**
 * 格式化工具类
 */
public class FormatInfoUtils {

    /**
     * 格式化文件大小
     *
     * @param context
     * @param currentSize
     */
    public static String formatFileSize(Context context, long currentSize) {
        String sizeStr = Formatter.formatFileSize(context, currentSize);
        return sizeStr;
    }


}
