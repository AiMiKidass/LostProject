package com.example.alex.newtestproject.test.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.newtestproject.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * 基础Fragment
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 后退用
     */
    public abstract void onBacked();

    /**
     * 添加的View
     */
    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = onInitView(inflater);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);

        afterCreateView();

        return view;
    }

    protected abstract View onInitView(LayoutInflater inflater);

    protected abstract void afterCreateView();

    protected abstract void onclickEvent(View view);

    protected void showMsg(String message){
        ToastUtils.showToast(getActivity(),message);
    }
}
