package com.example.alex.newtestproject.net;

import com.example.alex.newtestproject.model.DoubanResult;
import com.example.alex.newtestproject.model.Movie;
import com.example.alex.newtestproject.model.RESTResult;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Subscriber;

/**
 * 豆瓣 Retrofit API
 *
 * Created by XiaoFeng on 16/12/20.
 */

public interface IDoubanApi {

    @GET("top250")
    Observable<DoubanResult<List<Movie>>> getTopMovies(@Query("start") int start, @Query("count") int count);

    /**
     * Json格式的Post请求（application/json）
     */
    @POST("account/update_user_info")
    Observable<RESTResult<String>> updateUserInfo(@Body RequestBody body);

    /**
     * Form格式的Post请求（application/x-www-form-urlencoded）
     */
    @FormUrlEncoded
    @POST("account/update_user_info")
    Observable<RESTResult<String>> updateUserInfo(@FieldMap Map<String, String> params);


}
