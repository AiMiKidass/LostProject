package com.example.alex.newtestproject.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.application.AppConstants;
import com.example.alex.newtestproject.model.ImageItem;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.upload.UploadTask;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * https://github.com/jeasonlzy
 */
@SuppressWarnings("WeakerAccess")
public class TestUploadAdapter extends BaseQuickAdapter<UploadTask<?>, BaseViewHolder> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;

    private List<UploadTask<?>> values;
    private List<ImageItem> images;
    private NumberFormat numberFormat;
    private Context context;
    private int type = -1;

    public TestUploadAdapter(Context context) {
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

    public void unRegister() {
        Map<String, UploadTask<?>> taskMap = OkUpload.getInstance().getTaskMap();
        for (UploadTask<?> task : taskMap.values()) {
            task.unRegister(createTag(task));
        }
    }

    private String createTag(UploadTask task) {
        return type + "_" + task.progress.tag;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void convert(BaseViewHolder helper, UploadTask<?> item) {
        UploadTask<String> task = (UploadTask<String>) item;
        String tag = createTag(task);

        // 取消订单 立即支付
        helper.addOnClickListener(R.id.upload);

    }

}

