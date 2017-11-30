package com.example.alex.newtestproject.rxJava.fragment.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.fragment.view.ZhuangbiImage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/7.
 * 装逼测试Adapter
 */
public class ZhunagBiListAdapter extends RecyclerView.Adapter {

    List<ZhuangbiImage> images;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new DebounceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DebounceViewHolder debounceViewHolder = (DebounceViewHolder) holder;
        ZhuangbiImage image = images.get(position);
        // show In Glide
        Glide.with(holder.itemView.getContext()).load(image.image_url).into(debounceViewHolder.imageIv);
        // 配置文字
        debounceViewHolder.descriptionTv.setText(image.description);

        if(mZhuangBiClickListener != null){
            holder.itemView.setOnClickListener(mZhuangBiClickListener);
        }
    }

    public void setImages(List<ZhuangbiImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    static class DebounceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageIv)
        ImageView imageIv;
        @BindView(R.id.descriptionTv)
        TextView descriptionTv;

        public DebounceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private ZhuangBiClickListener mZhuangBiClickListener;
    public void setZhuangBiClickListener(ZhuangBiClickListener mZhuangBiClickListener) {
        this.mZhuangBiClickListener = mZhuangBiClickListener;
    }

    public interface ZhuangBiClickListener extends View.OnClickListener{

    }
}
