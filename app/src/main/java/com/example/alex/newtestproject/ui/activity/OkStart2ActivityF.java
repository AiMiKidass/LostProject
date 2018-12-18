package com.example.alex.newtestproject.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.application.AppConstants;
import com.example.alex.newtestproject.base.BaseActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 练习用
 * 文档:https://github.com/jeasonlzy/okhttp-OkGo#start-of-content
 */
public class OkStart2ActivityF extends BaseActivity {

    private static final String DOWN_DIRPATH = "_aaaaa_test";
    private List<String> sourcedatas = new ArrayList<>();
    private String mtest = "该帐号存在,帐号类型是:用户昵称帐号(USER_NICKNAME)";

    @Override
    protected int getRootViewResId() {
        return R.layout.activity_ok_start;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {


    }

    void downloadSingleFile() {
        // 进过测试本段代码没有问题
        OkGo.<File>get(AppConstants.downloadUrl) // Urls
                .tag(this)
                .execute(new FileCallback(getDownloadUrl(), "123") {
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        // file即为文件数据 文件保存在指定目录
                        log("response:" + response);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        log("response.getException().getMessage() = " + response.getException().getMessage());
                    }
                });
    }

    @SuppressWarnings("EmptyCatchBlock")
    String getStatent(String content) {
        String userType = "";
        try {
            int index = content.indexOf("(") + 1;
            int lastIndexOf = content.lastIndexOf(")");

            userType = mtest.substring(index, lastIndexOf);
        } catch (Exception e) {

        }
        return userType;
    }

    /**
     * 下载路径
     *
     * @return
     */
    public static String getDownloadUrl() {
        return getSDPath() + DOWN_DIRPATH;
    }

    public static String getSDPath() {
        File sdDir = new File("");
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            // 此目录为公用的Download文件夹
            sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }
        return sdDir.toString();
    }

    private boolean checkNickname(String nickname) {
        String regex = "^[A-Za-z\\u4e00-\\u9fa5][\\w\\u4e00-\\u9fa5]{0,17}$";

        boolean matches = nickname.matches(regex);

        // 昵称必须为18位以内
        return matches;
    }

    private void init() {
        init();
        initView();
        initData();
    }

    protected void initView() {
        sourcedatas.add("upload");
        sourcedatas.add("download");


    }

    protected void initData() {

    }


    /**
     * 注意以下几点：
     * <p>
     * 这里演示的是一次普通请求所有能配置的参数，真实使用时不需要配置这么多，按自己的需要选择性的使用即可
     * 第一行的泛型一定要特别注意，这个表示你请求网络的数据类型是什么，必须指定，否则无法解析网络数据。
     * .post(url)：这个表示当前请求是post请求，当然一共支持GET，HEAD，OPTIONS，POST，PUT，DELETE, PATCH, TRACE这8种请求方式，你只需要改改这个方法名就行了，很方便。
     * .params()：添加参数的时候,最后一个isReplace为可选参数,默认为true，即代表相同key的时候，后添加的会覆盖先前添加的。
     * .tag(this)：请求的tag，用于标识当前的请求，方便后续取消对应的请求，如果你不需要取消请求，也可以不用设置。
     * .isMultipart()：该方法表示是否强制使用multipart/form-data表单上传，因为该框架在有文件的时候，无论你是否设置这个参数，默认都是multipart/form-data格式上传，但是如果参数中不包含文件，默认使用application/x-www-form-urlencoded格式上传，如果你的服务器要求无论是否有文件，都要使用表单上传，那么可以用这个参数设置为true。
     * .isSpliceUrl()：该方法表示是否强制将params的参数拼接到url后面，默认false不拼接。一般来说，post、put等有请求体的方法应该把参数都放在请求体中，不应该放在url上，但是有的服务端可能不太规范，url和请求体都需要传递参数，那么这时候就使用该参数，他会将你所有使用.params()方法传递的参数，自动拼接在url后面。
     * .retryCount()：该方法是配置超时重连次数，也可以在全局初始化的时候设置，默认使用全局的配置，即为3次，你也可以在这里为你的这个请求特殊配置一个，并不会影响全局其他请求的超时重连次数。
     * .cacheKey() .cacheTime() .cacheMode()：这三个是缓存相关的配置，详细请看缓存介绍
     * .headers()：该方法是传递服务端需要的请求头，如果你不知道什么是请求头，看wiki首页关于网络抓包中的http协议链接。
     * .params()：该方法传递键值对参数，格式也是http协议中的格式，详细参考上面的http协议连接。
     * .addUrlParams() .addFileParams() .addFileWrapperParams()：这里是支持一个key传递多个文本参数，也支持一个key传递多个文件参数，详细也看上面的http协议连接。
     * 另外，每个请求都有一个.client()方法可以传递一个OkHttpClient对象，表示当前这个请求将使用外界传入的这个OkHttpClient对象，其他的请求还是使用全局的保持不变。那么至于这个OkHttpClient你想怎么配置，或者配置什么东西，那就随意了是不，改个超时时间，加个拦截器什么的统统都是可以的，看你心情喽。
     * 文档地址:https://github.com/jeasonlzy/okhttp-OkGo/wiki/OkGo
     */
    protected void testPostRequest() {
        List<String> values = new ArrayList<>();
        List<File> files = new ArrayList<>();
        List<HttpParams.FileWrapper> filewrappers = new ArrayList<>();

        OkGo.<ServerModel>post(Urls.URL_METHOD) // Urls
                .tag(this)  // 请求的tag,主要用于取消对应的请求
                .isMultipart(true)  // 强制使用multipart/form-data上传
                .isSpliceUrl(true)  // 强制将params的参数拼接到Url后面
                .retryCount(3)      // 超时重连次数
                .cacheKey("cacheKey")   // 超时重连次数
                .cacheTime(5000)        // 缓存的过期时间
                .cacheMode(CacheMode.DEFAULT)// 缓存模式
                .headers("header1", "v1")   // 添加请求头参数
                .headers("header2", "v2")   // 支持多请求头同时添加
                .params("params1", "params2")   // 添加请求参数
                .params("params1", "params2")   // 添加请求参数
                .params("file1", new File(""))  // 支持添加文件上传
                .params("file2", new File(""))  // 支持多个文件上传
                .addUrlParams("", values)   // 支持一个key传多个参数
                .addFileParams("", files)   // 支持一个key传多个文件
                .addFileWrapperParams("key", filewrappers)
                .execute(new Callback<ServerModel>() {
                    @Override
                    public void onStart(Request<ServerModel, ? extends Request> request) {
                        // UI线程 请求网络之前调用 可以显示对话框 添加/修改/移除 请求参数

                    }

                    @Override
                    public void onSuccess(Response<ServerModel> response) {

                    }

                    @Override
                    public void onCacheSuccess(Response<ServerModel> response) {

                    }

                    @Override
                    public void onError(Response<ServerModel> response) {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void uploadProgress(Progress progress) {

                    }

                    @Override
                    public void downloadProgress(Progress progress) {

                    }

                    @Override
                    public ServerModel convertResponse(okhttp3.Response response) throws Throwable {
                        // 子线程 可以耗时操作
                        // 根据传进来的response对象 做解析转换

                        return null;
                    }
                });

        // 简单写法
        OkGo.<String>post(Urls.URL_METHOD) // Urls
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

        // 下载文件
        /**
         如果你要下载文件，可以这么写。

         FileCallback具有三个重载的构造方法,分别是

         FileCallback()：空参构造
         FileCallback(String destFileName)：可以额外指定文件下载完成后的文件名
         FileCallback(String destFileDir, String destFileName)：可以额外指定文件的下载目录和下载完成后的文件名

         文件目录如果不指定,默认下载的目录为 sdcard/download/,文件名如果不指定,则按照以下规则命名:

         1.首先检查用户是否传入了文件名,如果传入,将以用户传入的文件名命名
         2.如果没有传入,那么将会检查服务端返回的响应头是否含有Content-Disposition=attachment;filename=FileName.txt该种形式的响应头,如果有,则按照该响应头中指定的文件名命名文件,如FileName.txt
         3.如果上述响应头不存在,则检查下载的文件url,例如:http://image.baidu.com/abc.jpg,那么将会自动以abc.jpg命名文件
         4.如果url也把文件名解析不出来,那么最终将以"unknownfile_" + System.currentTimeMillis()命名文件

         这里面有个新对象叫Progress，关于这个对象的解释，看这里我进行了专门的讲解。
         */
        OkGo.<File>get(Urls.URL_METHOD) // Urls
                .tag(this)
                .execute(new FileCallback() {
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        // file即为文件数据 文件保存在指定目录
                    }

                    @Override
                    public void onError(Response<File> response) {


                    }
                });

        // 上传文件
        // 不需要设置[Content-Type修改掉，变成multipart/form-data]
        // OKGo自动设置
        PostRequest<String> postRequest = OkGo.<String>post(Urls.UPLOAD_URL)
                .params("key", new File(""));
        postRequest.execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

            }

            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
            }
        });


        // 取消请求
        // ondestory()中写
        // 取消全局默认的OKhttpClient中标识为tag的请求
        OkGo.getInstance().cancelTag(this);
        // 取消全局默认的OKHttpClient中的所有请求
        OkGo.getInstance().cancelAll();
        // 取消给定OKHttpClient的所有请求
        //  OkGo.cancelAll(okHttpClient);
        // 取消给定OKhttpClient中标识为tag的请求
        // OkGo.cancelTag(okHttpClient,tag);

        // 同步请求
        Call<String> call = OkGo.<String>get(Urls.UPLOAD_URL)
                .tag(this)
                .headers("header", "v1")
                .params("param1", "paramValue1")
                .converter(new StringConvert()) // 类似于Retrofit的StringConvert
                .adapt();
        try {
            Response<String> response = call.execute();
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class ServerModel {

    }

    public final static class Urls {
        public static final String URL_METHOD = "";
        public static final String UPLOAD_URL = "";
    }


}
