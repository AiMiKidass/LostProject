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
import com.example.alex.newtestproject.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/8.
 */
public class TokenFragment extends BaseZhuangBiFragment {

    @BindView(R.id.tokenTv)
    TextView tokenTv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @OnClick(R.id.requestBt)
    public void upload() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();


        Action1<FakeThing> actionA = new Action1<FakeThing>() {
            @Override
            public void call(FakeThing fakeThing) {
                swipeRefreshLayout.setRefreshing(false);
                tokenTv.setText(getString(R.string.got_data, fakeThing.id, fakeThing.name));
            }
        };
        Action1<Throwable> actionB = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
            }
        };

        final FakeApi fakeApi = Network.getFakeApi();
        subscription = fakeApi.getFakeToken("fake_auth_code")
                .flatMap(new Func1<FakeToken, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(FakeToken fakeToken) {
                        return fakeApi.getFakeData(fakeToken);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtils.d("doOnSubscribe" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(actionA, actionB);


    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token;
    }
}
