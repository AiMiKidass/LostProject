package com.example.alex.newtestproject.test.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.XTestActivityXTest;
import com.example.alex.newtestproject.test.base.BaseAdapter;

import java.util.List;

/**
 * 子元素Button
 */

public class FragmentItemListAdapter extends BaseAdapter<XTestActivityXTest.FragmengItem> {
    public FragmentItemListAdapter(Context context, List<XTestActivityXTest.FragmengItem> data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_textview;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        XTestActivityXTest.FragmengItem item = getItem(position);
        if (item == null) {
            return convertView;
        }

        TextView textview = holder.getView(R.id.tv_text);
        textview.setText(item.getName());

        return convertView;
    }
}
