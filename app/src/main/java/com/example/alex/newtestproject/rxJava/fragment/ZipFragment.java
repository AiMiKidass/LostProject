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
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.Network;
import com.example.alex.newtestproject.rxJava.fragment.adapter.ItemListAdapter;
import com.example.alex.newtestproject.rxJava.fragment.view.Item;
import com.example.alex.newtestproject.rxJava.fragment.view.ZhuangbiImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/7.
 */
public class ZipFragment extends BaseZhuangBiFragment {

    @BindView(R.id.gridRv)
    RecyclerView gridRv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    ItemListAdapter adapter = new ItemListAdapter();

    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> itemList) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setItems(itemList);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @OnClick(R.id.zipLoadBt)
    void load() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        Observable<List<Item>> observable1 = Network.getGankApi().getBeauties(200, 1).map(MapFragment.GankBeautyResultToItemsMapper.getInstance());
        Observable<List<ZhuangbiImage>> observable2 = Network.getZhuangbiApi().search("装逼");
        /**
         * Func2中的 <> 中的参数为 T1,T2,R1 分别为 observable1,observable2,result(callback return type)
         */
        Observable<List<Item>> zipObservable = Observable.zip(observable1, observable2, new Func2<List<Item>, List<ZhuangbiImage>, List<Item>>() {
            @Override
            public List<Item> call(List<Item> gankItems, List<ZhuangbiImage> zhuangbiImages) {
                ArrayList<Item> items = new ArrayList<>();
                for (int i = 0; i < gankItems.size() /2 && i < zhuangbiImages.size(); i++) {
                    items.add(gankItems.get(i * 2));
                    items.add(gankItems.get(i * 2 + 1));
                    Item zhuangbiItem = new Item();
                    ZhuangbiImage zhuangbiImage = zhuangbiImages.get(i);
                    zhuangbiItem.description = zhuangbiImage.description;
                    zhuangbiItem.imageUrl = zhuangbiImage.image_url;
                    items.add(zhuangbiItem);
                }
                return items;
            }
        });
        Observable<List<Item>> subscribeOn = zipObservable.subscribeOn(Schedulers.io());
        Observable<List<Item>> observeOn = subscribeOn.observeOn(AndroidSchedulers.mainThread());
        observeOn.subscribe(observer);


        //subscription =
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_zip;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_zip;
    }
}
