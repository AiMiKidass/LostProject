package com.example.alex.newtestproject.rxJava.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.net.FakeApi;
import com.example.alex.newtestproject.rxJava.Network;
import com.example.alex.newtestproject.rxJava.fragment.view.FakeThing;
import com.example.alex.newtestproject.rxJava.fragment.view.FakeToken;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/8.
 */
public class TokenAdvancedFragment extends BaseZhuangBiFragment {

    @BindView(R.id.tokenTv)
    TextView tokenTv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    final FakeToken cachedFakeToken = new FakeToken(true);
    boolean tokenUpdated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_token_advanced, null);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @OnClick(R.id.invalidateTokenBt)
    void invalidateToken() {
        cachedFakeToken.expired = true;
        Toast.makeText(getActivity(), R.string.token_destroyed, Toast.LENGTH_SHORT).show();
    }

    /**
     * 本文是一个常见null异常重试范例
     *  通过Observable.just(null) 创建一个Observable,然后通过flatmap判断,当cachedFakeToken.token == null时
     *  ,返回Observable.error对象,这是一个用来专门处理error的Observable,
     *  然后在重试retryWhen函数中,通过处理bservable<Throwable>对象,重试fakeApi.getFakeToken("fake_auth_code")函数,
     *  获得cachedFakeToken.token;
     *
     */
    @OnClick(R.id.requestBt)
    void upload() {
        tokenUpdated = false;
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final FakeApi fakeApi = Network.getFakeApi();
        subscription = null;
        subscription = Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cachedFakeToken.token == null ?
                                Observable.<FakeThing>error(new NullPointerException("Token is null"))
                                : fakeApi.getFakeData(cachedFakeToken);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        Observable<Object> resultObservable = observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                // 如果throwable 属于 参数异常类型 或者 空指针异常类型  那么
                                if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
                                    return fakeApi.getFakeToken("fake_auth_code").doOnNext(new Action1<FakeToken>() {
                                        @Override
                                        public void call(FakeToken fakeToken) {
                                            tokenUpdated = true;
                                            cachedFakeToken.token = fakeToken.token;
                                            cachedFakeToken.expired = fakeToken.expired;
                                        }
                                    });
                                }
                                return Observable.error(throwable);
                            }
                        });
                        return resultObservable;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cachedFakeToken.token;
                        if (tokenUpdated) {
                            token += "(" + getString(R.string.updated) + ")";
                        }
                        tokenTv.setText(getString(R.string.got_token_and_data, token, fakeThing.id, fakeThing.name));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }
}
