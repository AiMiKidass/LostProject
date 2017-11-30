package com.example.alex.newtestproject.rxJava.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.example.alex.newtestproject.R;

import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Administrator on 2017/6/7.
 * zhuangbi base Fragment
 */
public abstract class BaseZhuangBiFragment extends Fragment {
    /**
     * ???
     * Subscription是所有Rx启动的基本类,继承于Observer?
     */
    protected Subscription subscription;
    private ProgressDialog progressDialog;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //@OnClick(R.id.tipBt)
    void tip() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    protected void makeProcessShow() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(getActivity(), "", "读取中");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        } else if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void makeProcessDissmiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
