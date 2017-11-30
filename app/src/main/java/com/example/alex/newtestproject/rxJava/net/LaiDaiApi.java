package com.example.alex.newtestproject.rxJava.net;

import com.example.alex.newtestproject.rxJava.fragment.bean.HttpResult;
import com.example.alex.newtestproject.rxJava.fragment.bean.LoginVo;
import com.example.alex.newtestproject.rxJava.fragment.bean.StartPage;
import com.example.alex.newtestproject.rxJava.fragment.view.GankBeautyResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Administrator on 2017/6/13.
 * 来贷API 用来测试功能
 *
 * retrofit2 @Path 多用于 路径式,如第一个接口为: data/福利/3/2  (number = 3 , page = 2)
 * retrofit2 @Query key-value式,如 key = aaa value = "aaa" 那么 GET下,请求为/login/loginSuccess/?aaa="aaa"
 * retrofit2 @Query 动态参数 如果某些参数不是必须的,那么将这些参数类型设置为引用类型,并传入null,在构建时不会发送这些参数
 */
public interface LaiDaiApi {

    @GET("data/福利/{number}/{page}")
    Observable<GankBeautyResult> getBeauties(@Path("number") int number, @Path("page") int page);

    @GET("login/loginSuccess")
    Observable<LoginVo> login(@Query("account") String account
            , @Query("pwd") String pwd, @Query("vcode") String vcode
            , @Query("imageId") String imageId, @Query("deviceToken") String deviceToken);

    /**
     * 通过map方式传递参数
     * @param map mapvalue
     * @return LoginVo
     */
    @GET("login/loginSuccess")
    Observable<LoginVo> login2(@QueryMap Map<String ,String> map);

    @GET("appCms/startPage")
    Observable<HttpResult<StartPage.ImageVo>> getStartPage();

    @GET("appCms/startPage")
    Observable<String> getStartPage2();


    @POST("HeBingService/servlet/getUserName1")
    Observable<String> getTestApi();
}
