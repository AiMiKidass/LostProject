package com.example.alex.newtestproject.model;


import com.example.alex.newtestproject.net.ApiException;
import com.example.alex.newtestproject.rxJava.exception.OldApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Func1;


/**
 * RESTFul 返回值封装类
 */
public class RxUtil<T> {

    /// RxUtil.java
    /**
     * 对RESTful返回结果做预处理，对逻辑错误抛出异常
     *
     * @param <T>
     * @return
     */
    public static <T> Func1<RESTResult<T>, T> handleRESTFulResult() {
        return new Func1<RESTResult<T>, T>() {
            @Override
            public T call(RESTResult<T> restResult) {
                if (restResult.getRes() != RESTResult.SUCCESS) {
                    try {
                        throw new ApiException(restResult.getRes(), restResult.getMsg());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
                return restResult.getData();
            }
        };
    }

//    /**
//     * 普通线程切换: IO -> Main
//     *
//     * @param <T>
//     * @return
//     */
//    public static <T> Observable.Transformer<T, T> normalSchedulers() {
//        return new Observable.Transformer<T, T>() {
//            @Override
//            public Observable<T> call(Observable<T> source) {
//                return source.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }


    public static final ObservableTransformer IO_TRANSFORMER = new ObservableTransformer() {
        @Override public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());
        }
    };


}

