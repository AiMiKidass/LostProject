package com.example.alex.newtestproject.ui.adapter.viewholder;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.model.ApkModel;
import com.example.alex.newtestproject.ui.adapter.DownloadAdapter;
import com.example.alex.newtestproject.ui.view.NumberProgressBar;
import com.example.alex.newtestproject.utils.ApkUtils;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DownloadViewHolder extends BaseViewHolder {
    private DownloadAdapter adapter;
    private Context mContext;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.priority)
    TextView priority;
    @BindView(R.id.downloadSize)
    TextView downloadSize;
    @BindView(R.id.tvProgress)
    TextView tvProgress;
    @BindView(R.id.netSpeed)
    TextView netSpeed;
    @BindView(R.id.pbProgress)
    NumberProgressBar pbProgress;
    @BindView(R.id.start)
    Button download;
    private DownloadTask task;
    private String tag;
    private NumberFormat numberFormat;

    public DownloadViewHolder(View itemView, Context context, DownloadAdapter adapter) {
        super(itemView);
        this.mContext = context;
        numberFormat = NumberFormat.getPercentInstance();
        this.adapter = adapter;
        ButterKnife.bind(this, itemView);
    }

    public void setTask(DownloadTask task) {
        this.task = task;
    }

    public void bind() {
        Progress progress = task.progress;
        ApkModel apk = (ApkModel) progress.extra1;
        if (apk != null) {
            Glide.with(mContext).load(apk.iconUrl).error(R.mipmap.ic_launcher).into(icon);
            name.setText(apk.name);
            priority.setText(String.format("优先级：%s", progress.priority));
        } else {
            name.setText(progress.fileName);
        }
    }

    public void refresh(Progress progress) {
        String currentSize = Formatter.formatFileSize(mContext, progress.currentSize);
        String totalSize = Formatter.formatFileSize(mContext, progress.totalSize);
        downloadSize.setText(currentSize + "/" + totalSize);
        priority.setText(String.format("优先级：%s", progress.priority));
        switch (progress.status) {
            case Progress.NONE:
                netSpeed.setText("停止");
                download.setText("下载");
                break;
            case Progress.PAUSE:
                netSpeed.setText("暂停中");
                download.setText("继续");
                break;
            case Progress.ERROR:
                netSpeed.setText("下载出错");
                download.setText("出错");
                break;
            case Progress.WAITING:
                netSpeed.setText("等待中");
                download.setText("等待");
                break;
            case Progress.FINISH:
                netSpeed.setText("下载完成");
                download.setText("完成");
                break;
            case Progress.LOADING:
                String speed = Formatter.formatFileSize(mContext, progress.speed);
                netSpeed.setText(String.format("%s/s", speed));
                download.setText("暂停");
                break;
        }
        tvProgress.setText(numberFormat.format(progress.fraction));
        pbProgress.setMax(10000);
        pbProgress.setProgress((int) (progress.fraction * 10000));
    }

    @OnClick(R.id.start)
    public void start() {
        Progress progress = task.progress;
        switch (progress.status) {
            case Progress.PAUSE:
            case Progress.NONE:
            case Progress.ERROR:
                task.start();
                break;
            case Progress.LOADING:
                task.pause();
                break;
            case Progress.FINISH:
                if (ApkUtils.isAvailable(mContext, new File(progress.filePath))) {
                    ApkUtils.uninstall(mContext, ApkUtils.getPackageName(mContext, progress.filePath));
                } else {
                    ApkUtils.install(mContext, new File(progress.filePath));
                }
                break;
        }
        refresh(progress);
    }

    @OnClick(R.id.remove)
    public void remove() {
        task.remove(true);
       // this.adapter.updateData(adapter.getType());
    }

    @OnClick(R.id.restart)
    public void restart() {
        task.restart();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
