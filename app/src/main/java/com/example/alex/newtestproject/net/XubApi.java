//package com.example.alex.newtestproject.net;
//
//import com.example.alex.newtestproject.model.Movie;
//import com.example.alex.newtestproject.model.RxUtil;
//
//import org.json.JSONObject;
//
//import java.util.List;
//import java.util.Map;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import rx.Subscriber;
//
///**
// * 写Form格式的Post请求时，需要添加 @FormUrlEncoded 注解，否则编译器会报错
// * 写Json格式的Post请求时，不使用@FieldMap注解，而是使用 @Body 注解，并声明 RequestBody 类型变量
// */
//public class XubApi {
//
//    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//    private void init() {
//
//    }
//
//    /**
//     * 获取豆瓣电影Top250的列表数据
//     *
//     * @param subscriber 由调用者传过来的观察者对象
//     * @param page       页码
//     * @param count      每页个数
//     */
//    public void requestTop250Movies(Subscriber<List<Movie>> subscriber, int page, int count) {
////        doubanApi.getTopMovies(page * count, count)
////                .map(RxUtil.<List<Movie>>handleDoubanResult())
////                .compose(RxUtil.<List<Movie>>normalSchedulers())
////                .subscribe(subscriber);
//    }
//
//    /**
//     * 修改用户信息
//     * Json格式
//     *
//     * @param subscriber
//     * @param params
//     */
//    public void updateUserInfo(Subscriber<String> subscriber, Map<String, Object> params) {
////        xzApi.updateUserInfo(toRequestBody(params))
////                .map(RxUtil.<String>handleRESTFulResult())
////                .compose(RxUtil.<String>normalSchedulers())
////                .subscribe(subscriber);
//    }
//
//    /**
//     * 修改用户信息
//     * Form格式
//     *
//     * @param subscriber
//     * @param params
//     */
//    public void updateUserInfo(Subscriber<String> subscriber, Map<String, Object> params) {
////        xzApi.updateUserInfo(params)
////                .map(RxUtil.<String>handleRESTFulResult())
////                .compose(RxUtil.<String>normalSchedulers())
////                .subscribe(subscriber);
//    }
//
//    private RequestBody toRequestBody(Map params) {
//        return RequestBody.create(JSON, toJsonStr(params));
//    }
//
//    private String toJsonStr(Map params) {
//        return new JSONObject(params).toString();
//    }
//
//}
