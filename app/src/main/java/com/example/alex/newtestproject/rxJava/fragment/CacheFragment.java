package com.example.alex.newtestproject.rxJava.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.data.Data;
import com.example.alex.newtestproject.rxJava.fragment.adapter.ItemListAdapter;
import com.example.alex.newtestproject.rxJava.fragment.view.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * Created by Administrator on 2017/6/8.
 * 缓存
 */
public class CacheFragment extends BaseZhuangBiFragment {

    @BindView(R.id.loadingTimeTv)
    TextView loadingTimeTv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.cacheRv)
    RecyclerView cacheRv;

    ItemListAdapter adapter = new ItemListAdapter();
    long startingTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_cache, null);
        ButterKnife.bind(this, view);

        cacheRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        cacheRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @OnClick(R.id.clearMemoryCacheBt)
    void clearMemoryCache() {
        Data.getInstance().clearMemoryCache();
        adapter.setItems(null);
        Toast.makeText(getActivity(), R.string.memory_and_disk_cache_cleared, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.clearMemoryAndDiskCacheBt)
    void clearMemoryAndDiskCache() {
        Data.getInstance().clearMemoryAndDiskCache();
        adapter.setItems(null);
        Toast.makeText(getActivity(), R.string.memory_and_disk_cache_cleared, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.loadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        startingTime = System.currentTimeMillis();
        unsubscribe();
        subscription = Data.getInstance().subscribeData(new Observer<List<Item>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Item> items) {
                swipeRefreshLayout.setRefreshing(false);
                int loadingTime = (int) (System.currentTimeMillis() - startingTime);
                loadingTimeTv.setText(getString(R.string.loading_time_and_source, String.valueOf(loadingTime), Data.getInstance().getDataSourceText()));
                adapter.setItems(items);
            }
        });


    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_cache;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }
}
