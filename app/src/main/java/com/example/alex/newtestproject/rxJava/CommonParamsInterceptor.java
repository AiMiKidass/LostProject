package com.example.alex.newtestproject.rxJava;


import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by Administrator on 2017/6/9.
 * 全局统一参数配置Interceptor
 * 用于OKhttpClient
 */
public class CommonParamsInterceptor implements Interceptor {

    public static final MediaType JSON = MediaType.parse("application/json:charset=utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        // request
        Request request = chain.request();
        // 请求方式
        String method = request.method();

        try {
            HashMap<String, String> requestDataMap = getGlobalParams();


            // 这里目前get和post是相同的逻辑
            if (method.equals("GET")) {
                // 获取请求地址api
                HttpUrl httpUrl = request.url();
                String url = httpUrl.toString();

                /*
                    循环遍历,将请求添加公用参数
                 */
                String encodeParameters = encodeParameters(requestDataMap);
                if (encodeParameters.length() > 0) {
                    if (!url.contains("?")) // =-1表示没有?连接符号
                        url += "?" + encodeParameters;// 如何没有?连接符号则加上?后,再接参数
                    else {// 如果有?连接符则直接连参数
                        url += "&" + encodeParameters;
                    }
                }
                // 重新构建request
                request = request.newBuilder().url(url).build();  //重新构建请求

            } else if (method.equals("POST")) {
                Request.Builder requestBuilder = request.newBuilder();

                // process post body inject
                if (requestDataMap.size() > 0) {
                    FormBody.Builder formBodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : requestDataMap.entrySet()) {
                        formBodyBuilder.add(entry.getKey(), entry.getValue());
                    }

                    RequestBody formBody = formBodyBuilder.build();
                    String postBodyString = bodyToString(request.body());
                    postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                    requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
                }
                request = requestBuilder.build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return chain.proceed(request);
    }

    @NonNull
    private HashMap<String, String> getGlobalParams() {
        // 公共参数
        HashMap<String, String> requestDataMap = new HashMap<>();

        requestDataMap.put("accessPort", "6");
        requestDataMap.put("version", "1.16");
        requestDataMap.put("userId", "483403");
        requestDataMap.put("token", "f03o83DqwnjonKBK3KhgEQOr5lBKh9NPI8ly1U8USgw%3D");

         /*
            if (MiaojieApplication.isLogin) {

                requestDataMap.put("userId", MiaojieApplication.userinfo.userId);
                requestDataMap.put("token", MiaojieApplication.userinfo.token);
            }*/

        return requestDataMap;
    }

    /**
     * 方法描述：对请求参数进行编码(get方式)
     */
    private static String encodeParameters(Map<String, String> map) {
        StringBuilder buf = new StringBuilder();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            if ((key == null) || ("".equals(key)) || (value == null) || ("".equals(value))) {
                continue;
            }
            if (i != 0)
                buf.append("&");
            try {
                buf.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            i++;
        }
        return buf.toString();
    }

    private String bodyToString(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
