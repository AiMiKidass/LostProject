package com.example.alex.newtestproject.gaode.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.gaode.util.AMapUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 * 公交adapter?
 */
public class BusResultListAdapter extends BaseAdapter {
    private List<BusPath> mBusPathList;
    private Context mContext;
    private BusRouteResult mBusRouteResult;

    public BusResultListAdapter(Context context, BusRouteResult busRouteResult) {
        this.mContext = context;
        this.mBusRouteResult = busRouteResult;
        this.mBusPathList = busRouteResult.getPaths();
    }

    @Override
    public int getCount() {
        return mBusPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBusPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_bus_result, null);
            holder.title = (TextView) convertView.findViewById(R.id.bus_path_title);
            holder.des = (TextView) convertView.findViewById(R.id.bus_path_des);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BusPath item = mBusPathList.get(position);
        holder.title.setText(AMapUtil.getBusPathTitle(item));
        holder.des.setText(AMapUtil.getBusPathDes(item));

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView des;
    }
}
