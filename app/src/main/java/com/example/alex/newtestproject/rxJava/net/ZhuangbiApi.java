package com.example.alex.newtestproject.rxJava.net;

import com.example.alex.newtestproject.rxJava.fragment.view.ZhuangbiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/6/7.
 * 注意是接口
 */
public interface ZhuangbiApi {
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
    @POST("search")
    Observable<List<ZhuangbiImage>> searchByPost(@Query("q") String query);
}
