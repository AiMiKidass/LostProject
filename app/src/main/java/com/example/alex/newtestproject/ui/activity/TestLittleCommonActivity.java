package com.example.alex.newtestproject.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.application.AppConstants;
import com.example.alex.newtestproject.base.BaseActivity;
import com.example.alex.newtestproject.model.ImageItem;
import com.example.alex.newtestproject.ui.adapter.TestUploadAdapter;
import com.example.alex.newtestproject.utils.DateUtils;
import com.example.alex.newtestproject.utils.FileUtil;
import com.example.alex.newtestproject.utils.FormatInfoUtils;
import com.example.alex.newtestproject.utils.LogUtils;
import com.example.alex.newtestproject.utils.MessageDigestUtils;
import com.example.alex.newtestproject.view.Book;
import com.example.alex.newtestproject.view.BookDeserializer;
import com.example.alex.newtestproject.view.UserSessionInfoEntity;
import com.example.alex.newtestproject.view.UserSessionInfoEntityDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试小功能
 */
public class TestLittleCommonActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String mFileName;
    private int CONSTANT_BLOCK_SIZE = 1024 * 1024;
    private String mTempFileDirectoryPath;
    private String mTempFilePath;
    private long lastTimeFileSize = 0; // 上次文件大小
    private String mMergeFileDirectoryPath;
    private String mMergeSourceFileDirectoryPath;
    private String mMergeTargetFileDirectoryPath;
    private String JPG_IMG_V001_FILENAME;
    private String VIDEO_V001_FILENAME;
    // 三个临时目录
    private File mTempFileDirectory;
    private File mMergeSourceFileDirectory;
    private File mMergeTargetFileDirectory;


    private TestUploadAdapter adapter;
    private ArrayList<ImageItem> images;


    @Override
    protected int getRootViewResId() {
        return R.layout.activity_testlittle_common;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/test0007.rar";
        mTempFileDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kbm/";
        // 写入/合并缓存目录
        mMergeSourceFileDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kbm/cache/source/";
        mMergeTargetFileDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kbm/cache/target/";

        mTempFileDirectory = new File(mTempFileDirectoryPath);
        mMergeSourceFileDirectory = new File(mMergeSourceFileDirectoryPath);
        mMergeTargetFileDirectory = new File(mMergeTargetFileDirectoryPath);

        if (!mTempFileDirectory.exists()) {
            mTempFileDirectory.mkdirs();
        }
        if (!mMergeSourceFileDirectory.exists()) {
            mMergeSourceFileDirectory.mkdirs();
        }
        if (!mMergeTargetFileDirectory.exists()) {
            mMergeTargetFileDirectory.mkdirs();
        }
        // mTempFilePath = mTempFileDirectoryPath + "/kbm/test0007.rar";

        JPG_IMG_V001_FILENAME = Environment.getExternalStorageDirectory() + "/DCIM/test_img_v001.jpg";
        VIDEO_V001_FILENAME = Environment.getExternalStorageDirectory() + "/DCIM/bg.mp4";

        mFileName = VIDEO_V001_FILENAME;

        initView();
        initData();
    }

    @Override
    protected void initView() {
        adapter = new TestUploadAdapter(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.upload: {
                        startOneTask();
                        break;
                    }
                }
            }
        });

    }

    /**
     * 启动任务
     */
    private void startOneTask() {
        // 初始化参数和确认接口
        initTask();
        // 找到上传文件并切割成若干份
        List<String> fileAndSplitList = findFileAndSplit();
        // 再将每个区块新建上传任务
        startUploadTaskByBlock(fileAndSplitList);
        // 通知完成任务并合并
        noticeServerAndMearge();
    }

    private void noticeServerAndMearge() {




    }

    private void startUploadTaskByBlock(List<String> fileAndSplitList) {
        // 创建一个队列 然后开始上传每个子任务
        // LinkedBlockingQueue
        for(String filePath : fileAndSplitList){
            UploadTask<String> uploadTask = createUploadTask(filePath);


        }
    }

    /**
     * 创建上传任务
     * @param filePath
     */
    private UploadTask<String> createUploadTask(String filePath) {
        checkNotNull(filePath);

        // 注意上传地址和参数需要修改
        PostRequest<String> postRequest = OkGo.<String>post(AppConstants.URL_FORM_UPLOAD)//
                .headers("aaa", "111")
                .params("idDirectoryLogic", "109895")
                .params("filePhysicsChecksumCode", "0eedd9a17e79a4b70bffd400531221427740ef7dd2fa5fef0883b40f2cb7bf9b")
                .params("idUserFileLogicOwner", "11")
                .params("fileLogicContent", new File(filePath))
                .converter(new StringConvert());

        UploadTask<String> task = OkUpload.request(filePath, postRequest)//
                .priority(77)//
                .save();
        return task;
    }

    private void checkNotNull(String filePath) {
        if(TextUtils.isEmpty(filePath)){
            throw new NullPointerException("filepath is null");
        }
    }

    private List<String> findFileAndSplit() {
        // 找到指定的文件 TODO
        File sourceFile = new File(mFileName);
        clearTargetDirectory(mMergeSourceFileDirectory);

        // 分割
       return splitFileBySizeAndCount(sourceFile);
    }

    private void initTask() {
        // 1.请求区块大小 目前暂定为1M

        // 判断是否支持秒传(服务器已有)


    }

    @Override
    protected void initData() {
        images = new ArrayList<>();

        ImageItem imageItem = new ImageItem();
        imageItem.name = "300M.rar";
        imageItem.path = "/storage/emulated/0/DCIM/300M.rar";
        images.add(imageItem);

        ImageItem imageItem2 = new ImageItem();
        imageItem2.name = "magazine-unlock-hi1051601.jpg";
        imageItem2.path = "/storage/emulated/0/Huawei/MagazineUnlock/magazine-unlock-hi1051601.jpg";
        images.add(imageItem2);

        adapter.updateData(images);

    }

    @OnClick(R.id.btn_click1)
    void parseClick() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Book.class, new BookDeserializer());
        Gson gson = gsonBuilder.create();

        try { // Parse JSON to Java
            Book book = gson.fromJson(JSONSTR_CREATE_SESSION_BOOK, Book.class);
            LogUtils.d(book.toString());

            gsonBuilder.registerTypeAdapter(UserSessionInfoEntity.class, new UserSessionInfoEntityDeserializer());
            gson = gsonBuilder.create();

            UserSessionInfoEntity entity = gson.fromJson(JSONSTR_CREATE_SESSION, UserSessionInfoEntity.class);
            LogUtils.d(entity.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * hash码计算
     */
    @OnClick(R.id.btn_click2)
    void parseClick2() {
        final String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/test0007.rar";
        showToastMessage("正在计算...");

        final String sourceFile = Environment.getExternalStorageDirectory() + "/DCIM/test_img_v001.jpg";
        final String targetFile2 = mMergeTargetFileDirectoryPath + "";
        final File targetFile = new File(targetFile2);
        final String filenameS = targetFile.listFiles()[0].getPath();

        new Thread() {
            @Override
            public void run() {
                try {
                    LogUtils.d("开始时间：" + DateUtils.getCurrentDate1());
                    File temp = new File(sourceFile);
                    log("文件大小:" + FormatInfoUtils.formatFileSize(mContext, temp.length()));
                    String tempsha256value = MessageDigestUtils.getFileSha256(temp);
                    LogUtils.d("文件名 = " + temp.getPath() + "---sha256结束时间：" + DateUtils.getCurrentDate1() + "---sha256value=" + tempsha256value);

                    File temp2 = new File(filenameS);
                    log("文件大小:" + FormatInfoUtils.formatFileSize(mContext, temp2.length()));
                    String temp2sha256value2 = MessageDigestUtils.getFileSha256(temp2);
                    LogUtils.d("文件名 = " + temp2.getPath() + "---sha256结束时间：" + DateUtils.getCurrentDate1() + "---sha256value=" + temp2sha256value2);

                    LogUtils.d("开始时间：" + DateUtils.getCurrentDate1());
                    File file = new File(filename);
                    log("文件大小:" + FormatInfoUtils.formatFileSize(mContext, file.length()));
                    String sha256value = MessageDigestUtils.getFileSha256(file);
                    LogUtils.d("sha256结束时间：" + DateUtils.getCurrentDate1() + "---sha256value=" + sha256value);

                    log("下一个文件大小:" + FormatInfoUtils.formatFileSize(mContext, file.length()));
                    LogUtils.d("开始时间：" + DateUtils.getCurrentDate1());
                    String md5value = MessageDigestUtils.fileToMD5(file.getPath());
                    LogUtils.d("md5结束时间：" + DateUtils.getCurrentDate1() + "---md5value=" + md5value);
                } catch (Exception e) {
                    log("发生错误:" + e.getMessage());
                }

                showToastMessage("计算完成");

            }
        }.start();

    }

    /**
     * hash码计算
     */
    @OnClick(R.id.btn_click3)
    void parseClick3() {
//        readBigFile();
        loadFile();
    }

    /**
     * 上传文件,并提示完成
     */
    @OnClick(R.id.btn_click4)
    void parseClick4() {
        File file = new File(mTempFileDirectoryPath);
        File[] files = file.listFiles();

        File tempFile = files[0];

        PostRequest<String> postRequest = OkGo.<String>post(AppConstants.URL_FORM_UPLOAD)//
                .headers("aaa", "111")
                .params("idDirectoryLogic", "109895")
                .params("filePhysicsChecksumCode", "0eedd9a17e79a4b70bffd400531221427740ef7dd2fa5fef0883b40f2cb7bf9b")
                .params("idUserFileLogicOwner", "11")
                .params("fileLogicContent", tempFile)
                .converter(new StringConvert());

        UploadTask<String> task = OkUpload.request(tempFile.getPath(), postRequest)//
                .priority(77)//
                .save();

        task.register(new UploadListener<String>(tempFile.getPath()) {
            @Override
            public void onStart(Progress progress) {
                log("上传任务启动 tag=" + progress.tag);
            }

            @Override
            public void onProgress(Progress progress) {

            }

            @Override
            public void onError(Progress progress) {
                log("上传任务错误 tag=" + progress.tag + "---错误=" + progress.exception.getMessage());
            }

            @Override
            public void onFinish(String s, Progress progress) {
                log("上传任务结束 tag=" + progress.tag);
            }

            @Override
            public void onRemove(Progress progress) {

            }
        });
        task.start();
    }

    /**
     * 切割文件
     */
    @OnClick(R.id.btn_click5)
    void onSpit() {
        File sourceFile = new File(mFileName);
        clearTargetDirectory(mMergeSourceFileDirectory);
        splitFileBySizeAndCount(sourceFile);
    }

    /**
     * 返回文件集合size
     * @param sourceFile
     * @return
     */
    private List<String> splitFileBySizeAndCount(File sourceFile) {
        int count = (int) Math.ceil(sourceFile.length() / (double) CONSTANT_BLOCK_SIZE);
        List<String> splitFileSize = null;
        try {
            FileUtil fileUtil = new FileUtil();
            splitFileSize = fileUtil.splitBySize(mFileName, mMergeSourceFileDirectoryPath, CONSTANT_BLOCK_SIZE, count);
            log("切割结束了");
        } catch (Exception e) {
            e.printStackTrace();
            log("错误了" + e.getMessage());
        }

        long maxsize = 0;
        for (File f : mMergeSourceFileDirectory.listFiles()) {
            log("生成缓存 file name= " + f.getPath() + "--length=" + f.length());
            maxsize = maxsize + f.length();
        }

        log("当前文件共计大小=" + maxsize);
        log("指定源文件大小=" + sourceFile.length());

        return splitFileSize;
    }

    /**
     * 合并文件
     * 用于验证切割的文件是否保持正确
     */
    @OnClick(R.id.btn_click6)
    void meargeFiles() {
        // 合并文件
        try {

            // 合并成新文件
            FileUtil fileUtil = new FileUtil();
            int blockFileSize = CONSTANT_BLOCK_SIZE;
            String filename = mMergeTargetFileDirectoryPath
                    + DateUtils.getCurrentDate1()
                    + "_file_mearge.mp4";
            File file = new File(filename);

            fileUtil.mergePartFiles(mMergeSourceFileDirectoryPath, ".part",
                    blockFileSize, filename);

            SystemClock.sleep(1000);
            log("合并成功");
            log("合并成功,文件大小=" + file.length());

            String bytesToSha256Value1 = MessageDigestUtils.fileToSHA1(VIDEO_V001_FILENAME);
            log("源文件的256值为:" + bytesToSha256Value1);

            String bytesToSha256Value2 = MessageDigestUtils.fileToSHA1(file.getPath());
            log("复制后的文件256hash值为:" + bytesToSha256Value2);


        } catch (Exception e) {
            e.printStackTrace();
            log("合并文件发生错误,原因=" + e.getMessage());
        }


    }


    void clearTargetDirectory(File file) {
        // 清空缓存
        File splitFileDirectory = file;
        if (splitFileDirectory.exists()) {
            File[] files = splitFileDirectory.listFiles();
            for (File f : files) {
                if (f.exists()) {
                    f.delete();
                }
            }
        }
        LogUtils.d("清空了缓存");
    }


    /**
     * 生成一个不同的临时缓存目录
     *
     * @return
     */
    private String createTempFileName(String directoryName) {
        String tempFilePath = null;
        if (TextUtils.isEmpty(directoryName)) {
            tempFilePath = mTempFileDirectoryPath + "_" + System.currentTimeMillis();
        } else {
            tempFilePath = directoryName + "_" + System.currentTimeMillis();
        }
        return tempFilePath;
    }

    /**
     * 读取文件并切割 切割大小为1M
     */
    public void loadFile() {
        int baseBytelength = CONSTANT_BLOCK_SIZE;
        lastTimeFileSize = 0;
        mFileName = Environment.getExternalStorageDirectory() + "/DCIM/test_img_v001.jpg";

        clearTargetDirectory(new File(mMergeSourceFileDirectoryPath));


        while (true) {
            try {
                RandomAccessFile randomFile = new RandomAccessFile(mFileName, "r");
                log("这次读取的字节seek起点=" + lastTimeFileSize);
                randomFile.seek(lastTimeFileSize);
                // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
                // 将一次读取的字节数赋给byteread
                /*
                while ((byteread = randomFile.read(bytes)) != -1) {
                    log("读取了1M的文件大小---" + dateFormat.format(new Date()));
                }
                 */


                byte[] bytes = new byte[baseBytelength];
                int byteread = 0;

                byteread = randomFile.read(bytes);
                if (byteread == -1) { // 读完了
                    log("读取完毕");
                    break;
                }

                String bytesToSha256Value = MessageDigestUtils.bytesToSha256(bytes);
                log("本次读取的单个区块的hash码为" + bytesToSha256Value);

                String tempFileName = createTempFileName(null);
                File tempFile = new File(tempFileName);

                // 写入指定目录
                bytesToFile(bytes, createTempFileName(mMergeSourceFileDirectoryPath));

                String sha256value = MessageDigestUtils.getFileSha256(tempFile);
                log("本次生成的单个区块的文件时机读取的hash码为" + sha256value);

                // 递增
                lastTimeFileSize += byteread;
                log("下一次读取的字节seek点=" + lastTimeFileSize);

                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 内存字节转file对象
     */
    public static void bytesToFile(byte[] buffer, final String filePath) {
        File file = new File(filePath);

        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;

        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedOutput) {
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static final String JSONSTR_CREATE_SESSION_BOOK = "{\n" +
            "  \"title\": \"Java Puzzlers: Traps, Pitfalls, and Corner Cases\",\n" +
            "  \"isbn-10\": \"032133678X\",\n" +
            "  \"isbn-13\": \"978-0321336781\",\n" +
            "  \"authors\": [\n" +
            "    \"Joshua Bloch\",\n" +
            "    \"Neal Gafter\"\n" +
            "  ]\n" +
            "}\n";


    private static final String JSONSTR_CREATE_SESSION = "{\n" +
            "  \"flagSuccess\": true,\n" +
            "  \"idStatus\": 200,\n" +
            "  \"statusContent\": \"查询idSession成功\",\n" +
            "  \"data\": {\n" +
            "    \"idSession\": \"7a5e8aea-9a47-40ed-a990-0e0cf4c8aebf\",\n" +
            "    \"idUser\": 11,\n" +
            "    \"userNickName\": \"江苏南通二建集团有限公司\",\n" +
            "    \"userNameFull\": \"江苏南通二建集团有限公司\",\n" +
            "    \"userNameBrief\": \"南：通二建集团\",\n" +
            "    \"userIdentity\": \"91320681138854172P\",\n" +
            "    \"userType\": \"UNIT\",\n" +
            "    \"userDescription\": \"公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////公司描述//////南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集团南：通二建集\",\n" +
            "    \"userAvatarIconIdFileLogic\": null,\n" +
            "    \"userGrpInfoList\": [\n" +
            "      {\n" +
            "        \"idUserGrp\": 236,\n" +
            "        \"idUserGrpParent\": 233,\n" +
            "        \"idUserUnit\": 11,\n" +
            "        \"userGrpNameCn\": \"超级管理组\",\n" +
            "        \"userGrpNameEn\": null,\n" +
            "        \"userGrpTypeEn\": \"USER_PURVIEW_TAG_NAME\",\n" +
            "        \"userGrpTypeCn\": \"用户权限标签名\",\n" +
            "        \"userGrpLevelCode\": \"3-36-233-236\",\n" +
            "        \"userGrpOrderCode\": 23600,\n" +
            "        \"userGrpIsDeletable\": false,\n" +
            "        \"userGrpIsUpdatable\": false,\n" +
            "        \"userGrpIntroduce\": \"\",\n" +
            "        \"userGrpDescription\": \"\",\n" +
            "        \"userGrpMemo\": \"\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"userModulePurviewInfoList\": [\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 98,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-mall-ripe_self_catalog_edit\",\n" +
            "        \"moduleNameCn\": \"编辑商品\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 99,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-mall-ripe_self_catalog_list\",\n" +
            "        \"moduleNameCn\": \"商品列表\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 100,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-mall-ripe_self_catalog_detail\",\n" +
            "        \"moduleNameCn\": \"商品详情\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 101,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-mall-ripe_exchange_pay\",\n" +
            "        \"moduleNameCn\": \"支付管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 102,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-mall-shop_literature\",\n" +
            "        \"moduleNameCn\": \"文库商铺\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 103,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-widget-homepage-theme\",\n" +
            "        \"moduleNameCn\": \"皮肤中心\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 104,\n" +
            "        \"moduleUri\": \"/boot_page\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-login-main-index\",\n" +
            "        \"moduleNameCn\": \"启始页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 105,\n" +
            "        \"moduleUri\": \"/function/home_page\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-homepage-main-home\",\n" +
            "        \"moduleNameCn\": \"首页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 106,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 108,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"crafts_image_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"工艺图片库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 109,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"crafts_courseware_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"工艺课件库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 110,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"project_video_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"项目视频库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 111,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"brand_show_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"品牌展示库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 112,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"technical_bid_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"技术标书库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 113,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"technical_solution_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"技术方案库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 114,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"management_performance_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"管理成果库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 115,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"security_environment_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"安全环境库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 116,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"ci_image_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"CI形象库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 117,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"bim_technology_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"BIM技术库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 118,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"standard_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"规范标准库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 119,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"enterprsie_standard\\\"\",\n" +
            "        \"moduleNameCn\": \"企业标准库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 120,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"science_achievements_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"科技成果库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 122,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"mechanical_equipment_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"机械设备库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 123,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"building_materials_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"建筑材料库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 124,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"labor_contractor_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"分包劳务库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 125,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"exam_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"培训考试库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 126,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"enterprise_expert_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"企业专家库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 127,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"other_information_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"其他档案库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 128,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-rank_dossier\",\n" +
            "        \"moduleNameCn\": \"知识库排行榜\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 129,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-literature-literature_statistic_rank\",\n" +
            "        \"moduleNameCn\": \"文库排行榜\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 130,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-literature-literature_company_coterie\",\n" +
            "        \"moduleNameCn\": \"公司圈子\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 139,\n" +
            "        \"moduleUri\": \"/module/netdisk/manage/netdisk_mydisk\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-netdisk_center-netdisk_mydisk\",\n" +
            "        \"moduleNameCn\": \"我的云盘\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 140,\n" +
            "        \"moduleUri\": \"/module/mall/buyer/buyer_ripe_exchange_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-mall_exchange_center-buyer_ripe_exchange_manage\",\n" +
            "        \"moduleNameCn\": \"买家交易管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 141,\n" +
            "        \"moduleUri\": \"/module/mall/seller/seller_ripe_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-mall_exchange_center-seller_ripe_self_catalog_manage\",\n" +
            "        \"moduleNameCn\": \"卖家商品管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 142,\n" +
            "        \"moduleUri\": \"/module/mall/seller/seller_ripe_exchange_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-mall_exchange_center-seller_ripe_exchange_manage\",\n" +
            "        \"moduleNameCn\": \"卖家交易管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 143,\n" +
            "        \"moduleUri\": \"/module/mall/manage/coin_data_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-mall_exchange_center-coin_exchange_manage\",\n" +
            "        \"moduleNameCn\": \"知识币交易管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 144,\n" +
            "        \"moduleUri\": \"/module/netdisk/manage/archive_data_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-mall_business_center-archive_plan_manage\",\n" +
            "        \"moduleNameCn\": \"文案计划管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 145,\n" +
            "        \"moduleUri\": \"/module/mall/manage/ripe_qc_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-nall_business_center-ripe_qc_manage\",\n" +
            "        \"moduleNameCn\": \"商品质检管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 146,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-system-setup-user\",\n" +
            "        \"moduleNameCn\": \"用户设置管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 147,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-user_center-setup_center-user_logout_kbm_fg\",\n" +
            "        \"moduleNameCn\": \"用户帐号退出\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 95,\n" +
            "        \"moduleUri\": \"/register\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-account-user_register\",\n" +
            "        \"moduleNameCn\": \"帐号注册\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 97,\n" +
            "        \"moduleUri\": \"/init\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-account-user_initialize\",\n" +
            "        \"moduleNameCn\": \"帐号初始管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 96,\n" +
            "        \"moduleUri\": \"/forget_password\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-joint-account-user_forget_password\",\n" +
            "        \"moduleNameCn\": \"忘记密码\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 131,\n" +
            "        \"moduleUri\": \"/function/ori_user_info\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-catalog-user_info_manage\",\n" +
            "        \"moduleNameCn\": \"用户信息管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 132,\n" +
            "        \"moduleUri\": \"/function/account_password_login\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-security-password_login_manage\",\n" +
            "        \"moduleNameCn\": \"用户登录密码管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 133,\n" +
            "        \"moduleUri\": \"/function/account_password_safe\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-security-password_safe_manage\",\n" +
            "        \"moduleNameCn\": \"用户安全密码管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 134,\n" +
            "        \"moduleUri\": \"/function/account_security_qa\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-protect-bind_qa_manage\",\n" +
            "        \"moduleNameCn\": \"用户密保问题管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 135,\n" +
            "        \"moduleUri\": \"/function/account_bind_mobile\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-protect-bind_telephone_manage\",\n" +
            "        \"moduleNameCn\": \"用户密保手机管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 136,\n" +
            "        \"moduleUri\": \"/function/account_bind_email\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-protect-bind_email_manage\",\n" +
            "        \"moduleNameCn\": \"用户密保邮箱管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 137,\n" +
            "        \"moduleUri\": \"/function/auth_identity_remote\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-certification-user_certification_permit_manage\",\n" +
            "        \"moduleNameCn\": \"用户实名认证管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 138,\n" +
            "        \"moduleUri\": \"/function/user_license_manage\",\n" +
            "        \"moduleNameEn\": \"xpp_app_fg-user-license-user_license_manage\",\n" +
            "        \"moduleNameCn\": \"用户系统执照管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 107,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-homepage-main-dossier_search_fulltext\",\n" +
            "        \"moduleNameCn\": \"商品全文检索\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 121,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_app_fg-mall-dossier-dossier_classify_query#ripe_grp_name_en=\\\"project_data_lib\\\"\",\n" +
            "        \"moduleNameCn\": \"项目资料库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 11,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 29,\n" +
            "        \"moduleUri\": \"/module/joint/mall/ripe_self_catalog_edit\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-joint-mall-ripe_self_catalog_edit\",\n" +
            "        \"moduleNameCn\": \"编辑商品\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 30,\n" +
            "        \"moduleUri\": \"/module/joint/mall/ripe_self_catalog_detail\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-joint-mall-ripe_self_catalog_detail\",\n" +
            "        \"moduleNameCn\": \"商品详情\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 31,\n" +
            "        \"moduleUri\": \"/module/joint/mall/ripe_exchange_pay\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-joint-mall-ripe_exchange_pay\",\n" +
            "        \"moduleNameCn\": \"支付管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 32,\n" +
            "        \"moduleUri\": \"/module/joint/mall/shop_literature\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-joint-mall-shop_literature\",\n" +
            "        \"moduleNameCn\": \"文库商铺\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 34,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-user_catalog-user_info_manage\",\n" +
            "        \"moduleNameCn\": \"用户信息管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 36,\n" +
            "        \"moduleUri\": \"/module/mall/buyer/buyer_ripe_exchange_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-mall_exchange_center-buyer_ripe_exchange_manage\",\n" +
            "        \"moduleNameCn\": \"买家交易管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 37,\n" +
            "        \"moduleUri\": \"/module/mall/seller/seller_ripe_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-mall_exchange_center-seller_ripe_self_catalog_manage\",\n" +
            "        \"moduleNameCn\": \"卖家商品管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 38,\n" +
            "        \"moduleUri\": \"/module/mall/seller/seller_ripe_exchange_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-mall_exchange_center-seller_ripe_exchange_manage\",\n" +
            "        \"moduleNameCn\": \"卖家交易管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 39,\n" +
            "        \"moduleUri\": \"/module/mall/manage/coin_data_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-mall_exchange_center-coin_exchange_manage\",\n" +
            "        \"moduleNameCn\": \"知识币交易管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 42,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-setup_center-user_parameter_manage\",\n" +
            "        \"moduleNameCn\": \"用户参数管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 43,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-system-setup-xpp_fg_login\",\n" +
            "        \"moduleNameCn\": \"登录通行证前台\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 45,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-setup_center-user_logout_kbm_fg\",\n" +
            "        \"moduleNameCn\": \"用户帐号退出\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 49,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='crafts_image_lib'\",\n" +
            "        \"moduleNameCn\": \"工艺图片库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 50,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='crafts_courseware_lib'\",\n" +
            "        \"moduleNameCn\": \"工艺课件库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 51,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='project_video_lib'\",\n" +
            "        \"moduleNameCn\": \"项目视频库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 52,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='brand_show_lib'\",\n" +
            "        \"moduleNameCn\": \"品牌展示库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 53,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='technical_bid_lib'\",\n" +
            "        \"moduleNameCn\": \"技术标书库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 54,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='technical_solution_lib'\",\n" +
            "        \"moduleNameCn\": \"技术方案库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 55,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='management_performance_lib'\",\n" +
            "        \"moduleNameCn\": \"管理成果库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 56,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='security_environment_lib'\",\n" +
            "        \"moduleNameCn\": \"安全环境库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 57,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='ci_image_lib'\",\n" +
            "        \"moduleNameCn\": \"CI形象库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 58,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='bim_technology_lib'\",\n" +
            "        \"moduleNameCn\": \"BIM技术库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 59,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='standard_lib'\",\n" +
            "        \"moduleNameCn\": \"规范标准库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 60,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='enterprsie_standard'\",\n" +
            "        \"moduleNameCn\": \"企业标准库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 61,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='science_achievements_lib'\",\n" +
            "        \"moduleNameCn\": \"科技成果库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 63,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='mechanical_equipment_lib'\",\n" +
            "        \"moduleNameCn\": \"机械设备库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 64,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='building_materials_lib'\",\n" +
            "        \"moduleNameCn\": \"建筑材料库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 65,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='labor_contractor_lib'\",\n" +
            "        \"moduleNameCn\": \"分包劳务库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 66,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='exam_lib'\",\n" +
            "        \"moduleNameCn\": \"培训考试库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 67,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='enterprise_expert_lib'\",\n" +
            "        \"moduleNameCn\": \"企业专家库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 68,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='other_information_lib'\",\n" +
            "        \"moduleNameCn\": \"其他档案库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 69,\n" +
            "        \"moduleUri\": \"/module/statistic/rank/rank_dossier\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_statistic_rank\",\n" +
            "        \"moduleNameCn\": \"知识库排行榜\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 70,\n" +
            "        \"moduleUri\": \"/module/statistic/rank/rank_literature\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-literature-literature_statistic_rank\",\n" +
            "        \"moduleNameCn\": \"文库排行榜\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 71,\n" +
            "        \"moduleUri\": \"/module/statistic/rank/company_coterie_literature\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-literature-literature_company_coterie\",\n" +
            "        \"moduleNameCn\": \"公司圈子\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 72,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-widget-homepage-theme\",\n" +
            "        \"moduleNameCn\": \"皮肤中心\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 74,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-user_center-setup_center-user_logout_kbm_bg\",\n" +
            "        \"moduleNameCn\": \"帐号退出\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 76,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 78,\n" +
            "        \"moduleUri\": \"/module/operation/mall/ripe_data_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-product-mall-ripe_catalog_manage\",\n" +
            "        \"moduleNameCn\": \"商品字典管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 79,\n" +
            "        \"moduleUri\": \"/module/product/netdisk/workflow_data_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-product-mall-workflow_catalog_manage\",\n" +
            "        \"moduleNameCn\": \"工作流字典管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 80,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-product-netdisk-file_data_manage\",\n" +
            "        \"moduleNameCn\": \"文件数据管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 81,\n" +
            "        \"moduleUri\": \"/module/operation/mall/ripe_journal_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-operation-mall-ripe_exchange_journal_manage\",\n" +
            "        \"moduleNameCn\": \"商品流水管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 82,\n" +
            "        \"moduleUri\": \"/module/operation/mall/coin_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-product-mall-coin_exchange_journal_manage\",\n" +
            "        \"moduleNameCn\": \"知识币流水管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 83,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-system-parameter-parameter_normal_manage\",\n" +
            "        \"moduleNameCn\": \"通用参数管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 40,\n" +
            "        \"moduleUri\": \"/module/netdisk/manage/archive_data_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-mall_business_center-archive_plan_manage\",\n" +
            "        \"moduleNameCn\": \"文案计划管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 41,\n" +
            "        \"moduleUri\": \"/module/mall/manage/ripe_qc_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-nall_business_center-ripe_qc_manage\",\n" +
            "        \"moduleNameCn\": \"商品质检管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 62,\n" +
            "        \"moduleUri\": \"/module/mall/ripe/dossier_classify_query\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-mall-dossier-dossier_classify_query?ripe_grp_name_en='project_data_lib'\",\n" +
            "        \"moduleNameCn\": \"项目资料库\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 77,\n" +
            "        \"moduleUri\": \"/module/product/netdisk/archive_template_manage\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-product-netdisk-archive_template_catalog_manage\",\n" +
            "        \"moduleNameCn\": \"文案模板字典管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 75,\n" +
            "        \"moduleUri\": \"/module/system/main/a\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-homepage-main-home\",\n" +
            "        \"moduleNameCn\": \"首页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 46,\n" +
            "        \"moduleUri\": \"/module/system/main/home\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-homepage-main-home\",\n" +
            "        \"moduleNameCn\": \"首页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 48,\n" +
            "        \"moduleUri\": \"/module/joint/mall/dossier_search_fulltext\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-homepage-main-dossier_search_fulltext\",\n" +
            "        \"moduleNameCn\": \"商品全文检索\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 33,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-widget-homepage-theme\",\n" +
            "        \"moduleNameCn\": \"皮肤中心\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 73,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-user_center-setup_center-user_login_kbm_fg\",\n" +
            "        \"moduleNameCn\": \"登录知识库前台\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 84,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_bg-system-task-schedule\",\n" +
            "        \"moduleNameCn\": \"定时管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 47,\n" +
            "        \"moduleUri\": \"/module/system/main/guide\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 35,\n" +
            "        \"moduleUri\": \"/module/netdisk/manage/netdisk_mydisk\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-user_center-netdisk_center-netdisk_mydisk\",\n" +
            "        \"moduleNameCn\": \"我的云盘\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 44,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"kbm_web_fg-system-setup-kbm_bg_login\",\n" +
            "        \"moduleNameCn\": \"登录知识库后台\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 11,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 5,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-joint-startup-unit_set_choice\",\n" +
            "        \"moduleNameCn\": \"单位帐套选择\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 8,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user_center-setup_center-user_logout_xpp_fg\",\n" +
            "        \"moduleNameCn\": \"帐号退出\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 10,\n" +
            "        \"moduleUri\": \"/function/home_page\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-homepage-main-home\",\n" +
            "        \"moduleNameCn\": \"首页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 14,\n" +
            "        \"moduleUri\": \"/function/user_license_manage\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-license-user_license_manage\",\n" +
            "        \"moduleNameCn\": \"用户系统执照管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 15,\n" +
            "        \"moduleUri\": \"/function/account_password_login\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-security-password_login_manage\",\n" +
            "        \"moduleNameCn\": \"用户登录密码管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 16,\n" +
            "        \"moduleUri\": \"/function/account_password_safe\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-security-password_safe_manage\",\n" +
            "        \"moduleNameCn\": \"用户安全密码管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 18,\n" +
            "        \"moduleUri\": \"/function/account_bind_mobile\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-protect-account_protect_telephone_manage\",\n" +
            "        \"moduleNameCn\": \"用户密保手机管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 19,\n" +
            "        \"moduleUri\": \"/function/account_bind_email\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-protect-account_protect_email_manage\",\n" +
            "        \"moduleNameCn\": \"用户密保邮箱管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 20,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-widget-homepage-theme\",\n" +
            "        \"moduleNameCn\": \"皮肤中心\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 22,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-user_center-setup-user_logout_xpp_bg\",\n" +
            "        \"moduleNameCn\": \"帐号退出\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 23,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-homepage-main-home\",\n" +
            "        \"moduleNameCn\": \"首页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 24,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 27,\n" +
            "        \"moduleUri\": \"/module/system/organize/purview_manage\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-system-purview-purview_manage\",\n" +
            "        \"moduleNameCn\": \"权限分配管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 28,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-system-parameter-parameter_normal_manage\",\n" +
            "        \"moduleNameCn\": \"通用参数管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 17,\n" +
            "        \"moduleUri\": \"/function/account_security_qa\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-protect-account_protect_qa_manage\",\n" +
            "        \"moduleNameCn\": \"用户密保问题管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 9,\n" +
            "        \"moduleUri\": \"/boot_page\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-login-main-index\",\n" +
            "        \"moduleNameCn\": \"启始页\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 12,\n" +
            "        \"moduleUri\": \"/function/ori_user_info\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-catalog-user_info_manage\",\n" +
            "        \"moduleNameCn\": \"用户信息管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 4,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-joint-startup-loading\",\n" +
            "        \"moduleNameCn\": \"启动加载\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 7,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-system-setup-xpp_bg_login\",\n" +
            "        \"moduleNameCn\": \"登录通行证后台\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 25,\n" +
            "        \"moduleUri\": \"/module/system/organize/user_manage\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-system-purview-user_manage\",\n" +
            "        \"moduleNameCn\": \"用户综合管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 21,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-user_center-setup-user_login_xpp_fg\",\n" +
            "        \"moduleNameCn\": \"登录通行证前台\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 26,\n" +
            "        \"moduleUri\": \"/module/system/organize/module_manage\",\n" +
            "        \"moduleNameEn\": \"xpp_web_bg-system-purview-module_manage\",\n" +
            "        \"moduleNameCn\": \"模块综合管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 3,\n" +
            "        \"moduleUri\": \"/init\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-joint-account-user_initialize\",\n" +
            "        \"moduleNameCn\": \"帐号初始管理\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 2,\n" +
            "        \"moduleUri\": \"/forget_password\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-joint-account-user_forget_password\",\n" +
            "        \"moduleNameCn\": \"忘记密码\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 1,\n" +
            "        \"moduleUri\": \"/register\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-joint-account-user_register\",\n" +
            "        \"moduleNameCn\": \"帐号注册\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 11,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-homepage-main-guide\",\n" +
            "        \"moduleNameCn\": \"操作指南\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 92,\n" +
            "        \"moduleUri\": \"\",\n" +
            "        \"moduleNameEn\": \"12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678\",\n" +
            "        \"moduleNameCn\": \"12345678901234567890123456789012\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"userPurviewFeature\": \"C,R,U,D\",\n" +
            "        \"idModule\": 13,\n" +
            "        \"moduleUri\": \"/function/auth_identity_remote\",\n" +
            "        \"moduleNameEn\": \"xpp_web_fg-user-certification-user_certification_permit_manage\",\n" +
            "        \"moduleNameCn\": \"用户实名认证管理\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"idUserUnit\": 11,\n" +
            "    \"idUnitLicenseLoginProject\": \"1\",\n" +
            "    \"userMobileCaptchaSource\": null,\n" +
            "    \"userEmailCaptchaSource\": null,\n" +
            "    \"sessionChecksumCode\": \"9ea29557edae985c566ae6949109d7adacf0fd3b25d4c842f295ba16abcacf33\"\n" +
            "  },\n" +
            "  \"total\": null\n" +
            "}";
}
