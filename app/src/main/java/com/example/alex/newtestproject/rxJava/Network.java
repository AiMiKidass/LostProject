package com.example.alex.newtestproject.rxJava;

import com.example.alex.newtestproject.rxJava.fragment.bean.HttpResult;
import com.example.alex.newtestproject.rxJava.net.FakeApi;
import com.example.alex.newtestproject.rxJava.net.GankApi;
import com.example.alex.newtestproject.rxJava.net.LaiDaiApi;
import com.example.alex.newtestproject.rxJava.net.MineCustomConverterFactory;
import com.example.alex.newtestproject.rxJava.net.ZhuangbiApi;
import com.example.alex.newtestproject.rxJava.util.ObservableHelper;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/6/7.
 */
public class Network {
    private static ZhuangbiApi zhuangbiApi;
    private static GankApi gankApi;

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static FakeApi fakeApi;
    private LaiDaiApi laiDaiApi;
    private static Converter.Factory myCustomConverterFactory = MineCustomConverterFactory.create();

    private Network() {
    }

    public static ZhuangbiApi getZhuangbiApi() {
        if (zhuangbiApi == null) {
            MineCustomConverterFactory s;
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new CommonParamsInterceptor())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://www.zhuangbi.info/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            zhuangbiApi = retrofit.create(ZhuangbiApi.class);
        }
        return zhuangbiApi;
    }

    public static GankApi getGankApi() {
        if (gankApi == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new CommonParamsInterceptor())
                    .build();
            Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }

    /**
     * <img width="640" height="310" src="https://ws1.sinaimg.cn/large/d23c7564ly1fg6qckyqxkj20u00zmaf1.jpg" alt="">
     *
     * @return
     */
    public static FakeApi getFakeApi() {
        if (fakeApi == null) {
            fakeApi = new FakeApi();
        }
        return fakeApi;
    }

    public static Network getInstance() {
        return Holder.INSTANCE;
    }

    public LaiDaiApi getLaiDaiApi() {
        if (laiDaiApi == null) {
            okHttpClient = initOKHttpclient();

            // 注意: 如果添加多了ConvertFactory,那么Retrofit会按照add的顺序依次解析,若成功,不再执行下一个,若失败,则执行下一个
            Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                    .baseUrl("http://192.168.5.69:8080")
                    .addConverterFactory(myCustomConverterFactory)
                    // .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            laiDaiApi = retrofit.create(LaiDaiApi.class);
        }
        return laiDaiApi;
    }

    /**
     * 初始化OKHttp
     *
     * @return
     */
    private OkHttpClient initOKHttpclient() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                //.addInterceptor(interceptor)
                .addInterceptor(new CommonParamsInterceptor())  // 拦截器,可用于给请求添加公用参数
                .retryOnConnectionFailure(true)                 // 错误时重试连接
                .connectTimeout(5, TimeUnit.SECONDS)            // 超时时间
                //.addNetworkInterceptor(mTokenInterceptor)       // 让所有网络请求都附上你的拦截器，我这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        return client;
    }





    public int requestTest() {
        try {
            Thread.sleep(500);
            return 2;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public HttpResult<ArrayList<Integer>> requestTest02(int n1) {
        try {
            Thread.sleep(500);
            int[] array = {5, 6, 7, 8};
            int temp = array[n1];
            HttpResult<ArrayList<Integer>> stringResult = new HttpResult<>();
            ArrayList<Integer> objects = new ArrayList<>();
            objects.add(temp * 5 * 55);
            stringResult.obj = objects;
            return stringResult;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Holder {
        static Network INSTANCE;

        static {
            INSTANCE = new Network();
        }
    }


    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.code != 1) {
                ObservableHelper s;
                //  throw new OldApiException(httpResult.code);
            }
            return httpResult.obj;
        }
    }
}
