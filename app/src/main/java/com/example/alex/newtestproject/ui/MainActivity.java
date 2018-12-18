package com.example.alex.newtestproject.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.base.BaseActivity;
import com.example.alex.newtestproject.ui.activity.CustomAdapterActivity;
import com.example.alex.newtestproject.ui.activity.DownloadAllActivity;
import com.example.alex.newtestproject.ui.activity.DownloadListActivity;
import com.example.alex.newtestproject.ui.activity.ExFilePickerTestActivity;
import com.example.alex.newtestproject.ui.activity.ManyLittleCommonActivity;
import com.example.alex.newtestproject.ui.activity.OkStart2ActivityF;
import com.example.alex.newtestproject.ui.activity.TestLittleCommonActivity;
import com.example.alex.newtestproject.ui.activity.UploadAllActivity;
import com.example.alex.newtestproject.utils.ActivityUtils;
import com.example.alex.newtestproject.utils.DateUtils;
import com.example.alex.newtestproject.utils.DocumentManagement;
import com.example.alex.newtestproject.utils.Formatter;
import com.example.alex.newtestproject.utils.LogUtils;
import com.example.alex.newtestproject.utils.OpenFileUtil;
import com.example.alex.newtestproject.utils.SPUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FilenameFilter;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 每次要测试新功能时,在manylittleCommonActivity中填写onclick事件并绑定
 */
@SuppressWarnings("ALL")
public class MainActivity extends BaseActivity {

    private static final int REQUESTCODE_PERMISSION = 676;

    @BindView(R.id.et_input_number)
    EditText inputNumber;

    @BindView(R.id.act_knowledge_list_rv)
    RecyclerView actKnowledgeListRv;
    private Context mContext;
    private List<Pair<String, Class>> items;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    private ItemEntity[] datas = {
            getItemEntity("CustomAdapterActivity", CustomAdapterActivity.class),
            getItemEntity("TestLittleCommonActivity", TestLittleCommonActivity.class),
            getItemEntity("ManyLittleCommonActivity", ManyLittleCommonActivity.class),
            getItemEntity("OkStart2ActivityF", OkStart2ActivityF.class),
            getItemEntity("OKDownloadActivity", DownloadListActivity.class),
            getItemEntity("OKUploadAllActivity", UploadAllActivity.class),
            getItemEntity("OKDownloadAllActivity", DownloadAllActivity.class),
            getItemEntity("ExFilePicker", ExFilePickerTestActivity.class),
    };

    @SuppressWarnings("unused")
    private void initValues() {
        items = new ArrayList<>();
        items.add(new Pair<String, Class>("OkGo", OkStart2ActivityF.class));
        items.add(new Pair<String, Class>("OKDownloadActivity", DownloadListActivity.class));
        items.add(new Pair<String, Class>("DownloadAllActivity", DownloadAllActivity.class));
    }

    private ItemEntity getItemEntity(String name, Class<? extends BaseActivity> clazz) {
        return new ItemEntity(name, clazz);
    }

    private MyAdapter mAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.activity_test_main;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        this.mContext = this;

        initView();
        initData();


        String text = "4000000000";
        long number = Long.valueOf(text);

        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.DOWN);


        double number2 = 3.725;

        String format = numberFormat.format(number2);

        // DisposableObserver<Long> disposableObserver = getTimeDemoObserver();
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //  mCompositeDisposable.add(disposableObserver);


    }

    @Override
    protected void initView() {
        actKnowledgeListRv.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyAdapter(new ArrayList<ItemEntity>());
        actKnowledgeListRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtils.startActivity(MainActivity.this, datas[position].getClazz());
            }
        });
        mAdapter.openLoadAnimation();

        requestMustPermission();
    }

    @Override
    protected void initData() {
        init();

        int i = "2018-11-9".compareTo("2100-12-31");
        i++;


        boolean exists = new File("/storage/emulated/0/DCIM/300M.rar").exists();
        exists = false;


        String ssss = "^[A-Z0-9]{18}(_[A-Z0-9]+)*$";
        String value1 = "91371502XA3DDUQR57";
        String value2 = "91371508XA3DDUQR58";
        String value3 = "91371503XA3DDUQR59";
        boolean isF1 = value1.matches(ssss);
        boolean isF2 = value2.matches(ssss);
        boolean isF3 = value3.matches(ssss);
        if (isF3) {

        }
        boolean equals = TextUtils.equals("", null);
        boolean equals1 = TextUtils.equals("123", null);
        boolean equals2 = TextUtils.equals(null, "3123");
        equals2 = equals1;
        String jsonStr = "{\"flagSuccess\":true,\"idStatus\":200,\"statusContent\":\"[认证成功] 恭喜，您的认证申请已成功通过稽核！ 您的实名认证申请成功，即时生效！\",\"data\":{\"idUser\":556,\"userNickname\":\"bhtsaa\",\"userMobile\":\"17636607370\",\"userEmail\":null,\"userIdentity\":\"110101199803076526\",\"userType\":\"PERSONAL\",\"idUserGuid\":null,\"userAvatarIconIdFileLogic\":null,\"userBindMobileDatetime\":null,\"userBindEmailDatetime\":null,\"userNameFull\":\"liuliuliu\",\"userNameBrief\":\"66666\",\"userOftenTelephone\":\"\",\"userOftenAddress\":\"\",\"userOftenZipcode\":\"\",\"userQq\":\"\",\"userWechat\":\"\",\"userBirthday\":null,\"userIntroduce\":\"\",\"userDescription\":\"\",\"userMemo\":\"\",\"userNameImeJianpin\":null,\"userWebsite\":\"\",\"userCurrencyAmount\":0.0,\"userBounsAmount\":0.0,\"userBankName\":null,\"userBankNumber\":null,\"userBindBankDatetime\":null,\"userIdentityTypeName\":\"居民身份证\",\"userIdentityDateBegin\":\"2018-09-08 08:00:00\",\"userIdentityDateEnd\":\"2018-11-12 08:00:00\",\"userIdentityIdFileLogic01\":null,\"userIdentityIdFileLogic02\":null,\"userBindIdentityDatetime\":\"2018-11-12 13:27:47\",\"userLoginPassword\":\"666666\",\"userSafePassword\":\"777777\",\"userLoginPasswordBak\":null,\"userSafePasswordBak\":null,\"userAccountCreateDatetime\":\"2018-11-12 11:15:48\",\"userAccountUpdateDatetime\":\"2018-11-12 11:48:41\",\"recChecksumCode\":\"3fa6204e0f8c40dd02bf0da4ff60a465f110af979af6b62e080e4057d1ddc33d\"},\"total\":null}" +
                "";
        Gson gson = new Gson();
        SPUtils.getInstance().put("user", jsonStr);
        SPUtils.getInstance().put("user1", jsonStr);
        SPUtils.getInstance().put("user2", jsonStr);
        SPUtils.getInstance().put("user3", jsonStr);

        String basename = "逻辑文件存储分类总根/我的云盘/我的文件/自定义文件/20181016李雪雯/777/新建文件夹/Dream_It_Possible/";
        String basename2 = "逻辑文件存储分类总根/我的云盘/我的文件/自定义文件/20181016李雪雯";
        String filename = "Dream_It_Possible";


        String name1 = "逻辑文件存储分类总根/我的云盘/我的文件/自定义文件/20181016李雪雯/777/";
        String name2 = "逻辑文件存储分类总根/我的云盘/我的文件/自定义文件/20181016李雪雯/777/新建文件夹/";
        String sdcardPath = "/sdcard/download/";
        String mfilename1 = "777";
        String mfilename2 = "新建文件夹";
        String virurlFilename = "逻辑文件存储分类总根/我的云盘/我的文件/自定义文件/20181016李雪雯/777/新建文件夹/Dream_It_Possible";
        String virurlFilename2 = "Dream_It_Possible";

        File file = new File("");

        List<String> arrays = new ArrayList<>();
        String name = "新建文件夹";

        String newFileName = "新建文件夹";
        arrays.add(name);

        // 遍历文件夹 检查是否存在
        if (arrays.contains(newFileName)) {

        }

        // 是否已经存在的规则是: 文件名 + 文件名(1)/(2)等等
        String tempFile = "sss.java";
        final String ext = "java";
        final String bfilename = "sss";


        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(ext);
            }
        };
        // 遍历 检查相同文件名是否存在
        File sssss = new File(tempFile);
        if (sssss.exists()) {

        }

        getXFileName(tempFile);

    }

    /**
     * @param tempFile
     * @return
     */
    private String getXFileName(String tempFile) {


    }

    /**
     * 获取所有文件存储的基本路径
     *
     * @param baseDirectoryPath 完整路径
     * @param filename          文件名
     * @return 云盘路径的基础相对路径
     */
    private String subStringDiskFilePath(String baseDirectoryPath, String filename) {
        String result = null;
        int lastIndexOf = baseDirectoryPath.lastIndexOf(filename);
        // 截取索引最后一个字符串到长度的字符串
        String path1 = baseDirectoryPath.substring(0, lastIndexOf);
        // 云盘路径的基础相对路径 + path1
        result = path1;
        // 真实路径为SD卡路径+path1
        return result;
    }

    /**
     * 获取所有文件存储的基本路径
     */
    private String subStringRealDiskFilePath(String levelName, String realDirectoryPath, String baseDirectoryPath) {
        String result = null;
        String realPath = levelName.replace(baseDirectoryPath, realDirectoryPath);
        return realPath;
    }

    /**
     * 获取 文件类型 的
     *
     * @param path
     * @param filename
     * @return
     */
    private String substringDirectoryPath(String path, String filename) {
        int lastIndexOf = path.lastIndexOf(filename);
        String tempPath = path.substring(0, lastIndexOf);
        return tempPath;
    }


    private void init() {
        mAdapter.replaceData(Arrays.asList(datas));
    }

    private void testRxjava() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                e.onNext("");
                e.onComplete();
            }
        })
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        int i = 0;
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        int i = 0;
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        int i = 0;
                    }
                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        int i = 0;
                    }

                    @Override
                    public void onNext(Object o) {
                        int i = 0;
                    }

                    @Override
                    public void onError(Throwable e) {
                        int i = 0;
                    }

                    @Override
                    public void onComplete() {
                        int i = 0;
                    }
                })
        ;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestMustPermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasWriteContactsPermission2 = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED
                || hasWriteContactsPermission2 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    REQUESTCODE_PERMISSION);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTCODE_PERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意写文件
            } else {
                // 用户不同意，自行处理即可
                showToastMessage("请在系统设置-应用-权限中开启相应权限,否则云盘相关功能无法运行");
                finish();
            }
        }
    }

    void onClickTest() {
        String directory = "/storage/emulated/0/Download/kbmdownload/";
        File directoryFile = new File(directory);

        OpenFileUtil instance = OpenFileUtil.getInstance(this);

        int number = Integer.valueOf(inputNumber.getText().toString().trim());

        String filePath = "";
        File file = new File("");
        switch (number) {
            case 0: {
                file = directoryFile.listFiles()[0];
                break;
            }
            case 1: {
                file = directoryFile.listFiles()[1];
                break;
            }
            case 2: {
                file = directoryFile.listFiles()[2];
                break;
            }
            case 3: {
                file = directoryFile.listFiles()[3];
                break;
            }
            case 4: {
                file = directoryFile.listFiles()[4];
                break;
            }
        }

        LogUtils.d("当前选择文件路径=" + file.getPath());

        instance.openFile(file.getPath());

    }

    @OnClick({R.id.btn_click})
    void onClick4() {
        long aLong = Long.valueOf(inputNumber.getText().toString().trim());
        String value = Formatter.formatFileSize(aLong);
        LogUtils.d(value);


        String ssss = Formatter.formatFileSize(aLong);


        aLong = 10000;
        value = Formatter.formatFileSize(aLong);
        LogUtils.d(value);
    }

    private void openAssignFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri directoryUri = FileProvider.getUriForFile(this
                , getApplicationContext().getPackageName() + ".provider"
                , file.getParentFile());

        intent.setDataAndType(directoryUri, "file/*");
        try {
//            startActivity(intent);
            startActivity(Intent.createChooser(intent, "选择浏览工具"));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0)
            return type;
        /* 获取文件的后缀名 */
        String fileType = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (fileType == null || "".equals(fileType))
            return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (fileType.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }


    private void getFile() {
        // 600Mb 10秒分割完成 单个20Mb  小米8 6G 64G cpu845
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/test0007.mp4");

                LogUtils.d("开始时间：" + DateUtils.getCurrentDate1());
                //切割文件
                int count = DocumentManagement.getSplitFile(file, 20 * 1024 * 1024);
                LogUtils.d("count", count + "个文件");

                //////////////////////////////////////////////////////////////////////////////

                //合并文件
                //创建合并文件路径
//                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/video2018";
//                DocumentManagement.merge(filePath, file, 20 * 1024 * 1024);

                LogUtils.d("开始时间：" + DateUtils.getCurrentDate1());
            }
        }).start();
    }

    public class ItemEntity {
        private String name;
        private Class<? extends BaseActivity> clazz;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<? extends BaseActivity> getClazz() {
            return clazz;
        }

        public void setClazz(Class<? extends BaseActivity> clazz) {
            this.clazz = clazz;
        }

        public ItemEntity(String name, Class<? extends BaseActivity> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
    }

    public static class MyAdapter extends BaseQuickAdapter<ItemEntity, BaseViewHolder> {

        public MyAdapter(@Nullable List<ItemEntity> datas) {
            super(R.layout.item_main_recyleview, datas);
        }

        @Override
        protected void convert(BaseViewHolder helper, ItemEntity item) {
            helper.setText(R.id.view_item_title, "CODE:" + item.getName());
        }
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };

}
