package com.example.alex.newtestproject.rxJava.data;

import android.support.annotation.IntDef;

import com.example.alex.newtestproject.App;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.Network;
import com.example.alex.newtestproject.rxJava.fragment.MapFragment;
import com.example.alex.newtestproject.rxJava.fragment.view.Item;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by Administrator on 2017/6/8.
 *
 */
public class Data {

    private static final int DATA_SOURCE_MEMORY = 1;
    private static final int DATA_SOURCE_DISK = 2;
    private static final int DATA_SOURCE_NETWORK = 3;

    public void clearMemoryCache() {
        cache = null;
    }

    public void clearMemoryAndDiskCache() {
        clearMemoryCache();
    }

    public Subscription subscribeData(Observer<List<Item>> observer) {
        if (cache == null) {
            cache = BehaviorSubject.create();
            Observable
                    .create(new Observable.OnSubscribe<List<Item>>() {
                        @Override
                        public void call(Subscriber<? super List<Item>> subscriber) {
                            List<Item> items = Database.getInstance().readItems();
                            if (items == null) {
                                setDataSource(DATA_SOURCE_NETWORK);
                                loadFromNetwork();
                            } else {
                                setDataSource(DATA_SOURCE_DISK);
                                subscriber.onNext(items);
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);
        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    /**
     * 枚举的写法 android官方推荐
     */
    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK})
    @interface DataSource {
    }

    BehaviorSubject<List<Item>> cache;

    private int dataSource;

    private Data() {
    }

    private void setDataSource(@DataSource int dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceText() {
        int dataTextResId;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataTextResId = R.string.data_source_memory;
                break;
            case DATA_SOURCE_NETWORK:
                dataTextResId = R.string.data_source_network;
                break;
            case DATA_SOURCE_DISK:
                dataTextResId = R.string.data_source_disk;
                break;
            default:
                dataTextResId = R.string.data_source_network;
                break;
        }
        return App.getInstance().getString(dataTextResId);
    }

    public void loadFromNetwork() {
        Network.getGankApi()
                .getBeauties(100, 1)
                .subscribeOn(Schedulers.io())
                .map(MapFragment.GankBeautyResultToItemsMapper.getInstance())
                .doOnNext(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        Database.getInstance().writeItems(items);
                    }
                })
                .subscribe(new Action1<List<Item>>() {
                    @Override
                    public void call(List<Item> items) {
                        cache.onNext(items);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public static Data getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static Data instance;

        static {
            instance = new Data();
        }
    }
}
