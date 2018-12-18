package com.example.alex.newtestproject.listener;

import com.example.alex.newtestproject.utils.LogUtils;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.upload.UploadListener;

/**
 * 上传监听
 * @param <T>
 */
public class LogUploadListener<T> extends UploadListener<T> {

    public LogUploadListener() {
        super("LogUploadListener");
    }

    @Override
    public void onStart(Progress progress) {
        LogUtils.d("onStart: " + progress);
    }

    @Override
    public void onProgress(Progress progress) {
        LogUtils.d("onProgress: " + progress);
    }

    @Override
    public void onError(Progress progress) {
        LogUtils.d("onError: " + progress);
        progress.exception.printStackTrace();
    }

    @Override
    public void onFinish(T t, Progress progress) {
        LogUtils.d("onFinish: " + progress);
    }

    @Override
    public void onRemove(Progress progress) {
        LogUtils.d("onRemove: " + progress);
    }
}