package com.example.alex.newtestproject.ui.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.base.BaseActivity;
import com.example.alex.newtestproject.model.ImageItem;
import com.example.alex.newtestproject.ui.adapter.UploadAdapter;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.task.XExecutor;
import com.lzy.okserver.upload.UploadTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * OKGo上传范例
 * Github地址：https://github.com/jeasonlzy
 */
public class UploadAllActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.upload)
    Button upload;
    @BindView(R.id.deleteAll)
    Button deleteAll;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private OkUpload okUpload;
    private UploadAdapter adapter;
    private List<UploadTask<?>> tasks;

    private static final String filePath = "/storage/emulated/0/DCIM/300M.rar";
    private List<ImageItem> images;

    @Override
    protected int getRootViewResId() {
        return R.layout.activity_upload_all;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        initToolBar(toolbar, true, "所有任务");

        select.setVisibility(View.VISIBLE);
        upload.setVisibility(View.VISIBLE);
        deleteAll.setVisibility(View.VISIBLE);

        okUpload = OkUpload.getInstance();
        okUpload.getThreadPool().setCorePoolSize(1);

        adapter = new UploadAdapter(this);
        adapter.updateData(UploadAdapter.TYPE_ALL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        okUpload.addOnAllTaskEndListener(this);
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {


    }

    @OnClick(R.id.select)
    public void select(View view) {
        //noinspection unchecked
        images = new ArrayList<>();

        ImageItem imageItem = new ImageItem();
        imageItem.name = "300M.rar";
        imageItem.path = "/storage/emulated/0/DCIM/300M.rar";
        images.add(imageItem);

        ImageItem imageItem2 = new ImageItem();
        imageItem2.name = "magazine-unlock-hi1051601.jpg";
        imageItem2.path = "/storage/emulated/0/Huawei/MagazineUnlock/magazine-unlock-hi1051601.jpg";
        images.add(imageItem2);

        tasks = adapter.updateData(images);
    }

    @OnClick(R.id.upload)
    public void upload(View view) {
        if (tasks == null) {
            showToastMessage("请先选择图片");
            return;
        }
        for (UploadTask<?> task : tasks) {
            task.start();
        }
    }

    @OnClick(R.id.removeonefile)
    public void removeonefile(View view) {
        ImageItem imageItem = this.images.get(0);
        String tag = imageItem.path;
        OkUpload.getInstance().removeTask(tag);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        okUpload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }

    @Override
    public void onAllTaskEnd() {
        showToastMessage("所有上传任务已结束");
    }

    @OnClick(R.id.deleteAll)
    public void deleteAll(View view) {
        OkUpload.getInstance().removeAll();
        adapter.updateData(UploadAdapter.TYPE_ALL);
    }

}
