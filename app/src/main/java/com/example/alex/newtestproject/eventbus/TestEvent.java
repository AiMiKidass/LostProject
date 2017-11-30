package com.example.alex.newtestproject.eventbus;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/5/19.
 */
public class TestEvent {
    private static final String TAG = TestEvent.class.getSimpleName();
    private Event1 e1;
    private Event2 e2;
    private Event3 e3;

    public TestEvent(Event1 e1, Event2 e2, Event3 e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public void post() {
        EventBus.getDefault().post(new EventBusMsg("sss"));
        EventBus.getDefault().post(new Event1());
        EventBus.getDefault().post(new Event2());
        EventBus.getDefault().post(new Event3());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(Event1 e1) {
        Log.d(TAG, "e1");
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(Event2 e2) {
        Log.d(TAG, "e2");
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(Event3 e3) {
        Log.d(TAG, "e3");
    }


    public static class Event1 {
        String value = "";
    }

    public static class Event2 {
        String value = "";
    }

    public static class Event3 {
        String value = "";
    }
}
