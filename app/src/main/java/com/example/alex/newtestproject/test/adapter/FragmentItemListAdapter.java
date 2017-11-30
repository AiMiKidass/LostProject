package com.example.alex.newtestproject.test.adapter;


import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.XTestActivity;
import com.example.alex.newtestproject.test.base.BaseAdapter;

import java.util.List;

/**
 * 子元素Button
 */

public class FragmentItemListAdapter extends BaseAdapter<XTestActivity.FragmengItem> {
    public FragmentItemListAdapter(Context context, List<XTestActivity.FragmengItem> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_button;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        XTestActivity.FragmengItem item = getItem(position);
        if (item == null) {
            return convertView;
        }

        Button button = holder.getView(R.id.btn_button);
        button.setText(item.getName());

        return convertView;
    }
}
