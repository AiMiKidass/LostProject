package com.example.alex.newtestproject.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.ui.activity.CustomAdapterActivity;

import java.text.NumberFormat;
import java.util.List;

/**
 * Item类型不同 的一种写法
 */
@SuppressWarnings("WeakerAccess")
public class TestMultItemAdapter extends BaseMultiItemQuickAdapter<CustomAdapterActivity.EnityBean, BaseViewHolder> {

    private NumberFormat numberFormat;
    private Context context;

    private int index = 0;

    public TestMultItemAdapter(Context context, List<CustomAdapterActivity.EnityBean> datas) {
        super(datas);
        this.context = context;
        numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);

        // 类型与调用一定要匹配
        addItemType(CustomAdapterActivity.EnityBean.TYPE_TITLE, R.layout.item_multitem_task_title);
        addItemType(CustomAdapterActivity.EnityBean.TYPE_CONTROL, R.layout.item_multitem_task_control);
        addItemType(CustomAdapterActivity.EnityBean.TYPE_WHOLEINFO, R.layout.item_multitem_task_wholeinfo);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void convert(BaseViewHolder helper, CustomAdapterActivity.EnityBean item) {
        int type = helper.getItemViewType();
        // 三种不同的布局
        switch (type) {
            case CustomAdapterActivity.EnityBean.TYPE_TITLE: {
                helper.setText(R.id.view_task_runing, "您正在下载(3)条");
                helper.addOnClickListener(R.id.view_task_runing);
                break;
            }
            case CustomAdapterActivity.EnityBean.TYPE_CONTROL: {
                helper.setText(R.id.act_start_tasks, "全部暂停");
                helper.addOnClickListener(R.id.act_start_tasks);
                break;
            }
            case CustomAdapterActivity.EnityBean.TYPE_WHOLEINFO: {
                helper.setText(R.id.view_task_name, item.getName());
                helper.setText(R.id.view_task_progress, item.getDateTime());
                helper.addOnClickListener(R.id.view_task_name);
                break;
            }
            default:
                break;
        }

    }

}

