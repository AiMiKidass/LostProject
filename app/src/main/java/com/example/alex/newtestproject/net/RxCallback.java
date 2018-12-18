package com.example.alex.newtestproject.net;

import android.text.TextUtils;

import com.example.alex.newtestproject.XApplication;
import com.example.alex.newtestproject.utils.ToastUtils;

import java.io.IOException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * 暴露给最上层的网络请求回调处理类
 *
 * Created by XiaoFeng on 16/12/28.
 */
public abstract class RxCallback<T> extends Subscriber<T> {

    /**
     * 成功返回结果时被调用
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 成功或失败到最后都会调用
     */
    public abstract void onFinished();

    @Override
    public void onCompleted() {
        onFinished();
    }

    @Override
    public void onError(Throwable e) {
        String errorMsg;
        if (e instanceof IOException) {
            /** 没有网络 */
            errorMsg = "Please check your network status";
        } else if (e instanceof HttpException) {
            /** 网络异常，http 请求失败，即 http 状态码不在 [200, 300) 之间, such as: "server internal error". */
            errorMsg = ((HttpException) e).response().message();
        } else if (e instanceof ApiException) {
            /** 网络正常，http 请求成功，服务器返回逻辑错误 */
            errorMsg = e.getMessage();
        } else {
            /** 其他未知错误 */
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "unknown error";
        }

        ToastUtils.showToast(XApplication.getInstance(),errorMsg);
        onFinished();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }
}
