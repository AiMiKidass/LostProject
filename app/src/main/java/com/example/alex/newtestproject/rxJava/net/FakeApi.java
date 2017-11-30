package com.example.alex.newtestproject.rxJava.net;

import com.example.alex.newtestproject.rxJava.fragment.view.FakeThing;
import com.example.alex.newtestproject.rxJava.fragment.view.FakeToken;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/6/8.
 * fake API
 * remember
 */
public class FakeApi {
    Random random = new Random();

    public Observable<FakeToken> getFakeToken(String fakeAuth) {

        Observable<FakeToken> map = Observable.just(fakeAuth).map(new Func1<String, FakeToken>() {
            @Override
            public FakeToken call(String fakeAuth) {
                // Add some random delay to mock the network delay
                int fakeNetworkTimeCost = random.nextInt(500) + 500;
                try {
                    Thread.sleep(fakeNetworkTimeCost);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FakeToken fakeToken = new FakeToken(createToken(), false);
                return fakeToken;
            }
        });

        return map;
    }

    private String createToken() {
        return "fake_token_" + System.currentTimeMillis() % 1000;
    }

    void sss() {


    }

    /**
     * ??? 虚假的
     * @param fakeToken
     * @return
     */
    public Observable<FakeThing> getFakeData(FakeToken fakeToken) {
        Observable<FakeThing> map = Observable.just(fakeToken).map(new Func1<FakeToken, FakeThing>() {
            @Override
            public FakeThing call(FakeToken fakeToken) {
                // Add some random delay to mock to network delay
                int fakeNetworkTimeCost = random.nextInt(500) + 500;
                try {
                    Thread.sleep(fakeNetworkTimeCost);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(fakeToken.expired){
                    throw new IllegalArgumentException("Token expired");
                }

                FakeThing fakeThing = new FakeThing();
                fakeThing.id = (int) (System.currentTimeMillis() % 1000);
                fakeThing.name = "FAKE_USER_" + fakeThing.id;
                return fakeThing;
            }
        });
        return map;
    }
}
