package com.example.alex.newtestproject.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 格式化为常见文件大小形式(保留两位小数) 如4.32G,2.15M等
 *
 * @author lizy18
 * @Time 2015年10月15日
 */
public class Formatter {
    /**
     * @param length 文件大小(以Byte为单位)
     * @return String 格式化的常见文件大小(保留两位小数)
     */
    public static String formatFileSize2(long length) {
        String result = null;
        int sub_string = 0;
        // 如果文件长度大于1GB
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3) + "GB";
        } else if (length >= 1048576) {
            // 如果文件长度大于1MB且小于1GB,substring(int beginIndex, int endIndex)
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + "MB";
        } else if (length >= 1024) {
            // 如果文件长度大于1KB且小于1MB
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";
        return result;
    }


    /**
     * @param length 文件大小(以Byte为单位)
     * @return String 格式化的常见文件大小(保留两位小数)
     */
    @SuppressWarnings("ConstantConditions")
    public static String formatFileSize(long length, int i) {
        String result = null;
        int sub_string = 0;

        BigDecimal bigDecimal = new BigDecimal(length);

        long GBflagSize = 1073741824;
        long MBflagSize = 1048576;
        long KBflagSize = 1024;


        // 如果文件长度大于1GB
        if (length >= GBflagSize) {
            // sub_string = String.valueOf(length / GBflagSize).indexOf(
            //                    ".");
            bigDecimal.multiply(new BigDecimal(GBflagSize)).toString().indexOf(".");


            result = ((float) length / GBflagSize + "000").substring(0,
                    sub_string + 3) + "GB";
        } else if (length >= MBflagSize) {
            // 如果文件长度大于1MB且小于1GB,substring(int beginIndex, int endIndex)
            sub_string = String.valueOf((float) length / MBflagSize).indexOf(".");
            result = ((float) length / MBflagSize + "000").substring(0,
                    sub_string + 3) + "MB";
        } else if (length >= KBflagSize) {
            // 如果文件长度大于1KB且小于1MB
            sub_string = String.valueOf((float) length / KBflagSize).indexOf(".");
            result = ((float) length / KBflagSize + "000").substring(0,
                    sub_string + 3) + "KB";
        } else if (length < KBflagSize)
            result = Long.toString(length) + "B";
        return result;
    }


    public static String formatFileSize(long bytes) {
        BigDecimal bytesbigDecimal = new BigDecimal(bytes);
        int k = 1024;
        String[] sizes = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int i = (int) Math.floor(Math.log(bytes) / Math.log(k));
        BigDecimal divide = bytesbigDecimal.divide(new BigDecimal(Math.pow(k, i)));
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String format = numberFormat.format(divide.doubleValue());
        String text = format + sizes[i];
        return text;
    }

    public static String ssss2(long bytes) {

        BigDecimal bytesbigDecimal = new BigDecimal(bytes);

        int k = 1024;
        String[] sizes = {"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int i =
                (int) Math.floor(Math.log(bytes) / Math.log(k));

        String text = bytes / Math.pow(k, i) + "" + sizes[i];
        return text;
    }


}
