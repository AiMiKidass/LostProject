package com.example.alex.newtestproject.rxJava;

import com.example.alex.newtestproject.rxJava.fragment.bean.HttpResult;
import com.example.alex.newtestproject.rxJava.fragment.bean.LoginVo;
import com.example.alex.newtestproject.rxJava.fragment.bean.StartPage;
import com.example.alex.newtestproject.rxJava.net.FakeApi;
import com.example.alex.newtestproject.rxJava.net.GankApi;
import com.example.alex.newtestproject.rxJava.net.LaiDaiApi;
import com.example.alex.newtestproject.rxJava.net.MineCustomConverterFactory;
import com.example.alex.newtestproject.rxJava.net.ZhuangbiApi;
import com.example.alex.newtestproject.rxJava.util.ObservableHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/7.
 */
public class Network {
    private static ZhuangbiApi zhuangbiApi;
    private static GankApi gankApi;

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
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

    public Observable<LoginVo> loginWithLaiDaiApi(String account, String pwd) {
        Map<String, String> map = new HashMap<>();
        map.put("account", account);
        map.put("pwd", pwd);
        map.put("vcode", null);
        map.put("imageId", null);
        map.put("deviceToken", "AqLYEhdDw0r3iBIXRxFETntDJpZcyXgLhYKh87PfZf35");
        return getLaiDaiApi().login(account, pwd, null, null, "AqLYEhdDw0r3iBIXRxFETntDJpZcyXgLhYKh87PfZf35");
    }

    public void loginWithLaiDaiApiByMapParams(Action0 action0, Subscriber<LoginVo> subscribe) {
        // 注意,如果是@query传入,value不能传"",但是支持动态参数null; 如果是基本数据类型,建议传引用类,如int => Integer
        Map<String, String> map = new HashMap<>();
        map.put("account", "18515531883");
        map.put("pwd", "123456");
        map.put("vcode", "");
        map.put("imageId", "");
        map.put("deviceToken", "AqLYEhdDw0r3iBIXRxFETntDJpZcyXgLhYKh87PfZf35");

        Network.getInstance().getLaiDaiApi().login2(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(action0)
                .subscribe(subscribe);
    }

    /**
     * 测试3号: 一种Rx完善的写法
     *
     * @param observer
     */
    public void getStartPage3(Subscriber<? super HttpResult<StartPage.ImageVo>> observer) {
        Network.getInstance().getLaiDaiApi().getStartPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .first(new Func1<HttpResult<StartPage.ImageVo>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<StartPage.ImageVo> imageVoHttpResult) {
                        return null;
                    }
                })
                .doOnError(new Action1<Throwable>() {   // 当发生错误时
                    @Override
                    public void call(Throwable throwable) {
                        /*
                        mErroImageView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                         */
                    }
                })
                .doOnNext(new Action1<HttpResult<StartPage.ImageVo>>() {    // 下一步调用
                    @Override
                    public void call(HttpResult<StartPage.ImageVo> imageVoHttpResult) {
                        /*
                        mErroImageView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                         */
                    }
                })
                .doOnTerminate(new Action0() {  // 意外终止时调用
                    @Override
                    public void call() {
                        /*
                        mRefreshLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                         */
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(observer);

    }

    public Observable<HttpResult<StartPage.ImageVo>> getStartPage() {
        return getLaiDaiApi().getStartPage();
    }

    public Observable<String> getStartPage2() {
        return getLaiDaiApi().getStartPage2();
    }

    public Observable<String> getTestApi() {
        return getLaiDaiApi().getTestApi();
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
                //  throw new ApiException(httpResult.code);
            }
            return httpResult.obj;
        }
    }
}
