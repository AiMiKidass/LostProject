package com.example.alex.newtestproject.ui.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.application.AppConstants;
import com.example.alex.newtestproject.listener.LogUploadListener;
import com.example.alex.newtestproject.model.ImageItem;
import com.example.alex.newtestproject.ui.view.NumberProgressBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * https://github.com/jeasonlzy
 */
@SuppressWarnings("WeakerAccess")
public class UploadAdapter extends BaseQuickAdapter<UploadTask<?>, UploadAdapter.UploadViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private List<UploadTask<?>> values;
    private List<ImageItem> images;
    private NumberFormat numberFormat;
    private Context context;
    private int type = -1;

    public UploadAdapter(Context context) {
        super(R.layout.item_upload_manager);
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
    }

    public void updateData(int type) {
        //这里是将数据库的数据恢复
        this.type = type;
        if (type == TYPE_ALL)
            values = OkUpload.restore(UploadManager.getInstance().getAll());
        if (type == TYPE_FINISH)
            values = OkUpload.restore(UploadManager.getInstance().getFinished());
        if (type == TYPE_ING)
            values = OkUpload.restore(UploadManager.getInstance().getUploading());

        //由于Converter是无法保存下来的，所以这里恢复任务的时候，需要额外传入Converter，否则就没法解析数据
        //至于数据类型，统一就行，不一定非要是String
        for (UploadTask<?> task : values) {
            //noinspection unchecked
            Request<String, ? extends Request> request = (Request<String, ? extends Request>) task.progress.request;
            request.converter(new StringConvert());
        }

        replaceData(values);
    }

    public List<UploadTask<?>> updateData(List<ImageItem> images) {
        this.type = -1;
        this.images = images;
        values = new ArrayList<>();
        if (images != null) {
            Random random = new Random();
            for (int i = 0; i < images.size(); i++) {
                ImageItem imageItem = images.get(i);
                //这里是演示可以传递任何数据
                PostRequest<String> postRequest = OkGo.<String>post(AppConstants.URL_FORM_UPLOAD)//
                        .headers("aaa", "111")
                        .params("idDirectoryLogic", "109895")
                        .params("filePhysicsChecksumCode", "0eedd9a17e79a4b70bffd400531221427740ef7dd2fa5fef0883b40f2cb7bf9b")
                        .params("idUserFileLogicOwner", "11")
                        .params("fileLogicContent", new File(imageItem.path))
                        .converter(new StringConvert());

                UploadTask<String> task = OkUpload.request(imageItem.path, postRequest)//
                        .priority(random.nextInt(100))//
                        .extra1(imageItem)//
                        .save();
                values.add(task);
            }
        }
        replaceData(values);
        return values;
    }

    @Override
    protected void convert(UploadViewHolder holder, UploadTask item) {
        //noinspection unchecked
        UploadTask<String> task = (UploadTask<String>) item;
        String tag = createTag(task);
        task.register(new ListUploadListener(tag, holder))
                .register(new LogUploadListener<String>());
        holder.setTag(tag);
        holder.setTask(task);
        holder.bind();
        holder.refresh(task.progress);
    }

    public void unRegister() {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        for (UploadTask<?> task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(UploadTask task) {
        return type + "_" + task.progress.tag;
    }

    public class UploadViewHolder extends BaseViewHolder {

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
        @BindView(R.id.upload)
        Button upload;
        private UploadTask<?> task;
        private String tag;

        public UploadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTask(UploadTask<?> task) {
            this.task = task;
        }

        public void bind() {
            Progress progress = task.progress;
            ImageItem item = (ImageItem) progress.extra1;
            Glide.with(context).load(item.path).error(R.mipmap.ic_launcher).into(icon);
            name.setText(item.name);
            priority.setText(String.format("优先级：%s", progress.priority));
        }

        public void refresh(Progress progress) {
            String currentSize = Formatter.formatFileSize(context, progress.currentSize);
            String totalSize = Formatter.formatFileSize(context, progress.totalSize);
            downloadSize.setText(currentSize + "/" + totalSize);
            priority.setText(String.format("优先级：%s", progress.priority));
            switch (progress.status) {
                case Progress.NONE:
                    netSpeed.setText("停止");
                    upload.setText("上传");
                    break;
                case Progress.PAUSE:
                    netSpeed.setText("暂停中");
                    upload.setText("继续");
                    break;
                case Progress.ERROR:
                    netSpeed.setText("上传出错");
                    upload.setText("出错");
                    break;
                case Progress.WAITING:
                    netSpeed.setText("等待中");
                    upload.setText("等待");
                    break;
                case Progress.FINISH:
                    upload.setText("完成");
                    netSpeed.setText("上传成功");
                    break;
                case Progress.LOADING:
                    String speed = Formatter.formatFileSize(context, progress.speed);
                    netSpeed.setText(String.format("%s/s", speed));
                    upload.setText("停止");
                    break;
            }
            tvProgress.setText(numberFormat.format(progress.fraction));
            pbProgress.setMax(10000);
            pbProgress.setProgress((int) (progress.fraction * 10000));
        }

        @OnClick(R.id.upload)
        public void upload() {
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
                    break;
            }
            refresh(progress);
        }

        @OnClick(R.id.remove)
        public void remove() {
            task.remove();
            if (type == -1) {
                int removeIndex = -1;
                for (int i = 0; i < images.size(); i++) {
                    if (images.get(i).path.equals(task.progress.tag)) {
                        removeIndex = i;
                        break;
                    }
                }
                if (removeIndex != -1) {
                    images.remove(removeIndex);
                }
                updateData(images);
            } else {
                updateData(type);
            }
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

    private class ListUploadListener extends UploadListener<String> {

        private UploadViewHolder holder;

        ListUploadListener(Object tag, UploadViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null)
                throwable.printStackTrace();
        }

        @Override
        public void onFinish(String s, Progress progress) {
            Toast.makeText(context, "上传完成", Toast.LENGTH_SHORT).show();
            if (type != -1)
                updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }
}

