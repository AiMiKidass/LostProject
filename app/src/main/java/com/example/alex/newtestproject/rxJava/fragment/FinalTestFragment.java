package com.example.alex.newtestproject.rxJava.fragment;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.rxJava.net.SubscriberOnNextListener;

import java.util.List;

import butterknife.BindView;
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


    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }
}
