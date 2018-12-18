//package com.example.alex.newtestproject.net;
//
//import com.example.alex.newtestproject.XApplication;
//
//import java.io.File;
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Cache;
//import okhttp3.ConnectionSpec;
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * 测试单例写法
// */
//public class RxRetrofitClient {
//
//    private static final long DEFAULT_CONNECT_TIMEOUT = 2500;
//    private static final long DEFAULT_READ_TIMEOUT = 2500;
//    private static final long DEFAULT_WRITE_TIMEOUT = 2500;
//    private static final String BASE_URL_DOUBAN = "https://www.baidu.com";
//
//    private static final long DEFAULT_CACHE_SIZE = 100 * 1024 * 1024;
//    private IDoubanApi doubanApi;
//
//    private void initClient() {
//        // 创建OkHttpClient
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                // 超时设置
//                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
//                // 错误重连
//                .retryOnConnectionFailure(true)
//                // 支持HTTPS
//                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS)) //明文Http与比较新的Https
//                // cookie管理
//                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(XApplication.getInstance())));
//
//        // 添加各种插入器
//        addInterceptor(builder);
//
//        // 创建Retrofit实例
//        Retrofit doubanRetrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                // .addConverterFactory(FastJsonConvertFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(BASE_URL_DOUBAN)
//                .build();
//
//        // 创建API接口类
//        doubanApi = doubanRetrofit.create(IDoubanApi.class);
//    }
//
//    private void addInterceptor(OkHttpClient.Builder builder) {
//        // 添加Header
//        builder.addInterceptor(new HttpHeaderInterceptor());
//
//        // 添加缓存控制策略
//        File cacheDir = XApplication.getInstance().getExternalCacheDir();
//        Cache cache = new Cache(cacheDir, DEFAULT_CACHE_SIZE);
//        builder.cache(cache).addInterceptor(new HttpCacheInterceptor());
//
//        // 添加http log
//        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
//        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(logger);
//
//        // 添加调试工具
//        builder.networkInterceptors().add(new StethoInterceptor());
//    }
//
//    RxCallback s;
//
//
//}
