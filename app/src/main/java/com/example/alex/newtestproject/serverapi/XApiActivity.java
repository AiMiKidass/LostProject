package com.example.alex.newtestproject.serverapi;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.base.PermissionBaseActivity;
import com.example.alex.newtestproject.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XApiActivity extends PermissionBaseActivity {

    private static final String CONFIG_FILE_NAME = "project_config222.txt";
    private static final String BASEURL = "www.baidu.com";
    private static final int REQUESTCODE_INIT = 301;

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.srl_content)
    SwipeRefreshLayout srlContent;

    @BindView(R.id.wv_content)
    WebView wvContent;

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private String dirPath = "/sdcard/";// 暂定路径(华为6.0是这个)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xapi);
        ButterKnife.bind(this);

        dirPath += CONFIG_FILE_NAME;
        dirPath = Environment.getExternalStorageDirectory() + "/2233.txt";


        // loadWebview();
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {



    }

    private void loadWebview() {
        initWebView(wvContent);
        // tv_name.setText(item.getName());
        // tv_type.setText(item.getType());//
        wvContent.loadUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508131210021&di=bf47cd55c2a0b5cd9698dc0b3029c066&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fd%2F5301802409b26.jpg");

    }

    private void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        webSettings.setUseWideViewPort(true);
        // 缩放比例
        webView.setInitialScale(75);   //100代表不缩放

        CookieManager.getInstance().setAcceptCookie(true);
    }

    @Override
    public void finish() {
        //  wvContent.destroy();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();

        wvContent = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        PermissionUtils.requestPermission(this, REQUESTCODE_INIT, permissions, new PermissionUtils.OnPermissionResultListener() {
//            @Override
//            public void onSuccess() {
//                String builder = ("API:" + BASEURL) + "333 \n" +
//                        "aaa" +
//                        "bbb" +
//                        "ccc" +
//                        "ddd" +
//                        "eee";
//
//                File file = new File(dirPath);
//
//                if (file.exists()) {
//                    readSaveFile(file);
//                } else {
//                    savePackageFile(builder, file);
//                }
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
    }

    private void savePackageFile(String message, File file) {
        FileOutputStream outputStream;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(message.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readSaveFile(File file) {
        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(dirPath);
            byte temp[] = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = inputStream.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            LogUtils.d("readSaveFile: \n" + sb.toString());
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
