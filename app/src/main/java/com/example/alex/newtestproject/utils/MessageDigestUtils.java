package com.example.alex.newtestproject.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 用于计算各种算法的工具类
 */
public class MessageDigestUtils {
    /**
     * hash码计算
     * @param bytes bytes
     * @return
     */
    public static String bytesToSha256(byte[] bytes){
        String hexCode = "";
        try {
            byte[] sha256 = DigestUtils.sha256(bytes);
            // 这里写法必须这么写 否则会有问题 (android的bug)
            hexCode = new String(Hex.encodeHex(sha256));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexCode;
    }

    /**
     * hash码计算
     * @param file
     * @return
     */
    public static String getFileSha256(File file){
        String hexCode = "";
        try {
            InputStream in = new FileInputStream(file);
            byte[] sha256 = DigestUtils.sha256(in);
            // 这里写法必须这么写 否则会有问题 (android的bug)
            hexCode = new String(Hex.encodeHex(sha256));
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexCode;
    }

    /**
     * Get the md5 value of the filepath specified file
     * @param filePath The filepath of the file
     * @return The md5 value
     */
    public static String fileToMD5(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath); // Create an FileInputStream instance according to the filepath
            byte[] buffer = new byte[1024]; // The buffer to read the file
            MessageDigest digest = MessageDigest.getInstance("MD5"); // Get a MD5 instance
            int numRead = 0; // Record how many bytes have been read
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead); // Update the digest
            }
            byte [] md5Bytes = digest.digest(); // Complete the hash computing
            return convertHashToString(md5Bytes); // Call the function to convert to hex digits
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // Close the InputStream
                } catch (Exception e) { }
            }
        }
    }

    /**
     * Get the sha1 value of the filepath specified file
     * @param filePath The filepath of the file
     * @return The sha1 value
     */
    public static String fileToSHA1(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath); // Create an FileInputStream instance according to the filepath
            byte[] buffer = new byte[1024]; // The buffer to read the file
            MessageDigest digest = MessageDigest.getInstance("SHA-1"); // Get a SHA-1 instance
            int numRead = 0; // Record how many bytes have been read
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead); // Update the digest
            }
            byte [] sha1Bytes = digest.digest(); // Complete the hash computing
            return convertHashToString(sha1Bytes); // Call the function to convert to hex digits
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close(); // Close the InputStream
                } catch (Exception e) { }
            }
        }
    }

    /**
     * Convert the hash bytes to hex digits string
     * @param hashBytes
     * @return The converted hex digits string
     */
    private static String convertHashToString(byte[] hashBytes) {
        String returnVal = "";
        for (int i = 0; i < hashBytes.length; i++) {
            returnVal += Integer.toString(( hashBytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toLowerCase();
    }


}
