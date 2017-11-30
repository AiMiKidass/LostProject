package com.example.alex.newtestproject.rxJava.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.Network;
import com.example.alex.newtestproject.rxJava.fragment.adapter.ItemListAdapter;
import com.example.alex.newtestproject.rxJava.fragment.view.GankBeauty;
import com.example.alex.newtestproject.rxJava.fragment.view.GankBeautyResult;
import com.example.alex.newtestproject.rxJava.fragment.view.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/7.
 *
 */
public class MapFragment extends BaseZhuangBiFragment {
    private int page = 0;

    @BindView(R.id.pageTv)
    TextView pageTv;
    @BindView(R.id.previousPageBt)
    Button previousPageBt;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.gridRv)
    RecyclerView gridRv;

    private ItemListAdapter adapter = new ItemListAdapter();
    Observer<List<Item>> observer = new Observer<List<Item>>(){
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(),R.string.loading_failed,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(List<Item> images) {
            swipeRefreshLayout.setRefreshing(false);
            pageTv.setText(getString(R.string.page_with_number, String.valueOf(page)));
            adapter.setItems(images);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @OnClick(R.id.previousPageBt)
    void previousPage() {
        loadPage(--page);
        if (page == 1) {
            previousPageBt.setEnabled(false);
        }
    }

    @OnClick(R.id.nextPageBt)
    void nextPage() {
        loadPage(++page);
        if (page == 2) {
            previousPageBt.setEnabled(true);
        }
    }

    private void loadPage(int page) {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Network.getGankApi()
                .getBeauties(10, page)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .map(new Func1<List<Item>, List<Item>>() {
                    @Override
                    public List<Item> call(List<Item> items) {
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }

    public static class GankBeautyResultToItemsMapper implements Func1<GankBeautyResult, List<Item>> {
        private static GankBeautyResultToItemsMapper INSTANCE = new GankBeautyResultToItemsMapper();

        private GankBeautyResultToItemsMapper() {
        }

        public static GankBeautyResultToItemsMapper getInstance() {
            return INSTANCE;
        }

        @Override
        public List<Item> call(GankBeautyResult gankBeautyResult) {
            List<GankBeauty> gankBeauties = gankBeautyResult.beauties;
            List<Item> items = new ArrayList<>(gankBeauties.size());
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            for (GankBeauty gankBeauty : gankBeauties) {
                Item item = new Item();
                try {
                    Date date = inputFormat.parse(gankBeauty.createdAt);
                    item.description = outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    item.description = "unknown date";
                }
                item.imageUrl = gankBeauty.url;
                items.add(item);
            }
            return items;
        }
    }
}
