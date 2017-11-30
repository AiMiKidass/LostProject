package com.example.alex.newtestproject.rxJava.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.Network;
import com.example.alex.newtestproject.rxJava.fragment.bean.HttpResult;
import com.example.alex.newtestproject.rxJava.fragment.bean.LoginVo;
import com.example.alex.newtestproject.rxJava.fragment.bean.StartPage;
import com.example.alex.newtestproject.rxJava.net.ProgressSubscriber;
import com.example.alex.newtestproject.rxJava.net.SubscriberOnNextListener;
import com.example.alex.newtestproject.rxJava.permission.RxPermissions;
import com.example.alex.newtestproject.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2017/6/13.
 * 用于测试之用途
 */
public class FinalTestFragment extends BaseZhuangBiFragment {

    @BindView(R.id.tv_result)
    TextView tv_result;

    @BindView(R.id.tv_login_status)
    Button tv_login_status;
    @BindView(R.id.tv_get_splash_data)
    Button tv_get_splash_data;

    @BindView(R.id.btn_1)
    Button btn_1;
    @BindView(R.id.btn_2)
    Button btn_2;

    @BindView(R.id.ll_container)
    LinearLayout ll_container;

    String DEFAULT = "";

    private SubscriberOnNextListener<List<Subject>> getTopMovieOnNext;
    private static final int VIEW_ID_ZERO = 89898989;
    private static final String[] names = {
            "测试1号",
            "练习-复杂链式RxJavaDemo",
            "练习-复杂链式RxJavaDemo2",
            "练习-复杂链式RxJavaDemo3",
    };
    private View.OnClickListener BtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case VIEW_ID_ZERO + 0: {
                    // 访问网络
                    Network.getInstance().getTestApi()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    // showProgress()
                                    makeProcessShow();
                                }
                            })
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override
                                public void onNext(String s) {
                                    LogUtils.d("result=" + s);


                                    makeProcessDissmiss();
                                }
                            });


                    break;
                }
                case VIEW_ID_ZERO + 1: {
                    rxtest001();
                    break;
                }
                case VIEW_ID_ZERO + 2: {
                    rxtest002();
                    break;
                }
                case VIEW_ID_ZERO + 3: {
                    rxtest003();
                    break;
                }
                case VIEW_ID_ZERO + 4: {
                    rxtest004();
                    break;
                }
                default:
                    // 访问网络
                    Network.getInstance().getStartPage()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(new Action0() {
                                @Override
                                public void call() {
                                    // showProgress()
                                    makeProcessShow();
                                }
                            })
                            .subscribe(new Subscriber<HttpResult<StartPage.ImageVo>>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    tv_result.setText("spalsh.get()出错了,错误原因:" + e.getMessage());
                                }

                                @Override
                                public void onNext(HttpResult<StartPage.ImageVo> result) {
                                    makeProcessDissmiss();

                                    LogUtils.d("code = " + result.code);
                                    if (result.code == 1) {
                                        // 获得结果并访问activity,携带参数授权ID
                                        tv_result.setText("spalsh.get()获得了数据");
                                    } else {
                                        onError(new Exception("spalsh.get()..出错了"));
                                    }
                                }
                            });
                    break;
            }
        }
    };

    @BindView(R.id.tv_text_many)
    TextView tv_text_many;

    private void setSpecilText() {
        // 设置
        String str = "测试部分123456789123";
        tv_text_many.setText("文本12345678911212315665"); //设置部分字体样式，但是不可点击

        // 新添加的部分
        SpannableString spString = new SpannableString(str);
        spString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showMsg("点击了部分文字");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setTextSize(35);//设置字体大小
                ds.setFakeBoldText(true);//设置粗体
                ds.setColor(Color.argb(255, 38, 157, 241));//设置字体颜色
                ds.setUnderlineText(false);//设置取消下划线
            }
        }, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 表示不影响前后文字

        spString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showMsg("点击了部分文字2222");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setFakeBoldText(true);//设置粗体
                ds.setColor(Color.argb(255, 38, 157, 241));//设置字体颜色
                ds.setUnderlineText(true);//设置取消下划线
            }
        }, 6, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 表示不影响前后文字

        // 将设置好的样式放置到
        tv_text_many.setText(spString);
        tv_text_many.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * PublishSubject
     * 与普通的Subject不同，在订阅时并不立即触发订阅事件，而是允许我们在任意时刻手动调用onNext(),onError(),onCompleted来触发事件。
     * 建议:多多使用
     * 更多Subject : http://www.jianshu.com/p/1257c8ba7c0c
     */
    private void rxtest004() {
        PublishSubject<Integer> ss = PublishSubject.create();
        ss.onNext(1);
        ss.subscribe();
        ss.onNext(2);
        ss.onNext(3);
        ss.onNext(4);
        ss.onCompleted();
        // 结果 2,3,4
    }

    /**
     * 练习如下知识点:
     * Observable.Transformer
     * Observable.buffer
     * flatMap
     * PublishSubject
     * Observable.concat
     * Observable.merge
     */
    private void rxtest003() {
        // concat 合并 连接 需要类型一致
        Observable<String> obs = Observable.from(new String[]{"", "", "", ""});
        Observable<String> obs2 = Observable.from(new String[]{""});
        Observable.concat(obs, obs2).doOnNext(new Action1<String>() {
            @Override
            public void call(String o) {
                LogUtils.d("");
            }
        }).subscribe();

        Observable.merge(obs, obs2).doOnNext(new Action1<String>() {
            @Override
            public void call(String o) {
                LogUtils.d("");
            }
        }).subscribe();

        // 练习003
        Observable.just(DEFAULT)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        showMsg("123");
                    }
                })
                .map(new Func1<String, List<String>>() {
                    @Override
                    public List<String> call(String s) {
                        ArrayList<String> strings = new ArrayList<>();
                        for (int i = -1; i < 5; i++) {
                            strings.add(String.valueOf(i));
                        }
                        return strings;
                    }
                })
                .compose(changeElement())
                .map(new Func1<Integer, List<String>>() {
                    @Override
                    public List<String> call(Integer integer) {
                        ArrayList<String> strings = new ArrayList<>();
                        for (int i = -1; i < 5; i++) {
                            strings.add(String.valueOf(i));
                        }
                        return strings;
                    }
                })
                .flatMap(new Func1<List<String>, Observable<?>>() { // map是1对1,flatmap是1对多
                    @Override
                    public Observable<?> call(List<String> strings) {
                        return Observable.from(strings);
                    }
                })
                .buffer(2)  // 缓存元素 注意缓存的是Observable的数量
                .doOnNext(new Action1<List<Object>>() {
                    @Override
                    public void call(List<Object> objects) {
                        LogUtils.d("");
                    }
                }).subscribe();

    }

    /**
     *
     */
    private void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void rxtest002() {
        // test1 最原始的形态
        Observable.just(new Object())
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object o) {
                        return new Object();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // do something


                    }
                });

        // test2 改进01形态
        // 缺点,不是链式,特别丑
        applySchedulers(Observable.just(new Object())
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object o) {
                        return new Object();
                    }
                })
        ).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                // do something
            }
        });

        // test3 最终变换形态
        // 支持链式,结构清晰
        Observable.just(new Object())
                .map(new Func1<Object, Object>() {
                    @Override
                    public Object call(Object o) {
                        return new Object();
                    }
                })
                .compose(applySchedulers()) // 注意这行 目的是达成一种封装
                .subscribe();

        RxPermissions s;

    }

    /**
     * flatMap() 是干啥的呢？
     * <p>
     * flatMap() 的返回值也是 Observable， 是不是就意味着 flatMap 也可以重用部分操作函数？
     * 他们的区别在于，compose() 是更高层的抽象： 他的操作是作用在整个事件流上的，而不是里面发射的单个数据。具体来讲就是：
     * 只能通过 compose() 来从数据流中获取源 Observable 。所以对于影响整个事件流的操作函数（例如 subscribeOn() 和 observeOn()）需要使用 compose()。
     * 如果你把 subscribeOn()/observeOn() 在 flatMap 内使用，则只会对在 flatMap 里面创建的 Observable 有用而不是整个数据流。
     * 一旦创建了事件流，compose() 就立刻开始执行了。而flatMap() 只有当每次 onNext() 调用的时候才开始执行。也就是说，flatMap() 转换的是每个单独的数据而 compose() 是作用在整个数据流上的。
     * 由于每次调用 Observable 的 onNext() 函数 flatMap() 都会创建一个新的 Observable。所以 flatMap() 的效率并不是很好。
     * 结论：
     * 如果你只是想把几个常用的操作函数封装为一个函数来复用代码，则请使用 compose()。 flatMap() 有很多种用法的，但是并不适合这种情况。
     * <p>
     * <p>
     * Read more: http://blog.chengyunfeng.com/?p=987#ixzz4kiI70yQG
     *
     * @param observable
     * @param <T>
     * @return
     */
    private <T> Observable<T> applySchedulers(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 主意,compse所达成的效果对整个事件流都起作用,而flatmap只对call方法中的Observable起作用
     *
     * @param
     * @return
     */
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 主意,compse所达成的效果对整个事件流都起作用,而flatmap只对call方法中的Observable起作用
     *
     * @param
     * @return
     */
    private <T> Observable.Transformer<T, Integer> changeElement() {
        return new Observable.Transformer<T, Integer>() {
            @Override
            public Observable<Integer> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<T, Integer>() {
                            @Override
                            public Integer call(T s) {
                                return new Integer(100);
                            }
                        });
            }
        };
    }


    /**
     * 复杂RxDemo 002
     * 要求:去网络获得ID
     * <p>
     * 根据ID访问数据
     * <p>
     * 数据处理为List泛型
     * <p>
     * 根据数据结果code=1返回正确值(否之?
     * <p>
     * 正确显示loading框和取消
     * <p>
     * 将得到的数据循环遍历并打印
     * <p>
     * 获得的每一条数据++ 并sleep 200ms
     * <p>
     * 基础上再 * 5倍 +3 同时sleep 50ms
     * <p>
     * 主线程TextVIEW显示结果
     */
    private void rxtest001() {
        Observable.just(0)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // showProgress()
                        makeProcessShow();
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer s) {
                        // 异步任务
                        return Network.getInstance().requestTest();
                    }
                })
                .map(new Func1<Integer, ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call(Integer i) {
                        // 异步任务
                        HttpResult<ArrayList<Integer>> arrayListHttpResult = Network.getInstance().requestTest02(i);
                        if (arrayListHttpResult.code == 0) {
                            // throw error exception
                        }
                        return arrayListHttpResult.obj;
                    }
                })
                .filter(new Func1<ArrayList<Integer>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<Integer> integers) {
                        // filter 操作符: 对满足条件的Observer进行返回,不满足则剔除掉
                        // true = 返回
                        return integers.size() > 0;
                    }
                })
                .flatMap(new Func1<ArrayList<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(ArrayList<Integer> integers) {
                        // 将得到的数据循环遍历并打印
                        // flatMap 和 map 都是对象转换 但是map是1对1,flatmap是1对多,注意两者的回调中的返回类型
                        return Observable.from(integers);
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtils.d("遍历消息:" + integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        // 切换为主线程并显示结果
                        tv_result.setText("循环遍历完成!!!");
                        LogUtils.d("循环遍历完成!!!");
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call(Integer integer) {
                        /* 获得的每一条数据++ 并sleep 200ms
                                * <p>
                        * 基础上再 * 5倍 +3 同时sleep 50ms
                                * <p>*/

                        ArrayList<Integer> integers = new ArrayList<>();

                        integer++;
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        integer = integer * 5 + 3;

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return integers;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Integer>>() {
                    @Override
                    public void call(ArrayList<Integer> integers) {
                    /*
                    主线程TextVIEW显示结果
                    */
                        tv_result.setText("所有任务均已完成");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable.getMessage());
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_test, null);
        ButterKnife.bind(this, view);
        init();

        getTopMovieOnNext = new SubscriberOnNextListener<List<Subject>>() {
            @Override
            public void onNext(List<Subject> subjects) {
                tv_result.setText(subjects.toString());
            }
        };

        setSpecilText();

        return view;
    }

    private void init() {
        for (int i = 0; i < names.length; i++) {
            Button btn = getButton();
            btn.setId(VIEW_ID_ZERO + i);
            btn.setText(names[i]);
            btn.setOnClickListener(BtnClickListener);
            ll_container.addView(btn, 1);
        }
    }

    private Button getButton() {
        Button button = new Button(getActivity());
        button.setTextAppearance(getActivity(), R.style.buttonstyle001);
        return button;
    }

    @OnClick(R.id.tv_login_status)
    void loginClickEvent() {
        login();
    }

    @OnClick(R.id.tv_get_splash_data)
    void getStartPageDataClickEvent() {
        getSplashData();
    }

    @OnClick(R.id.tv_get_data)
    void getData() {
        /**
         进化版写法,封装了部分逻辑,但还是不太美
         */
        Subscriber<LoginVo> subscriber = new Subscriber<LoginVo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                tv_result.setText("出错了,错误原因:" + e.getMessage());
            }

            @Override
            public void onNext(LoginVo result) {
                makeProcessDissmiss();

                LogUtils.d("code = " + result.code);
                if (result.code == 1) {
                    // 获得结果并访问activity,携带参数授权ID
                    tv_result.setText("登录了");
                } else {
                    tv_result.setText("出错了");
                }
            }
        };

        Network.getInstance().loginWithLaiDaiApiByMapParams(new Action0() {
            @Override
            public void call() {
                // showProgress()
                makeProcessShow();
            }
        }, subscriber);

        // 以map参数的形式访问网络
    }


    private void getSplashData() {
        // 访问网络

    }

    @OnClick(R.id.btn_2)
    void getSplashData2() {
        // 访问网络
        Network.getInstance().getStartPage2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // showProgress()
                        makeProcessShow();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        tv_result.setText("spalsh.get()出错了,错误原因:" + e.getMessage());
                    }

                    @Override
                    public void onNext(String result) {
                        makeProcessDissmiss();

                        // 获得结果并访问activity,携带参数授权ID
                        tv_result.setText("spalsh.get()获得了数据");
                    }
                });
    }

    /**
     * 18515531883(可在原版登录)
     * 123456
     */
    private void login() {

        // 访问网络
        Network.getInstance().loginWithLaiDaiApi("18515531883", "123456")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // showProgress()
                        makeProcessShow();
                    }
                })
                .subscribe(new Subscriber<LoginVo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        tv_result.setText("出错了,错误原因:" + e.getMessage());

                    }

                    @Override
                    public void onNext(LoginVo result) {
                        makeProcessDissmiss();

                        LogUtils.d("code = " + result.code);
                        if (result.code == 1) {
                            // 获得结果并访问activity,携带参数授权ID
                            tv_result.setText("登录了");
                        } else {
                            onError(new Exception("出错了"));
                        }

                        SubscriberOnNextListener s;
                        ProgressSubscriber<?> p;
                    }
                });
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }
}
