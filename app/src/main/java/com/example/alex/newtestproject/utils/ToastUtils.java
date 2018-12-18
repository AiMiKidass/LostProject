package com.example.alex.newtestproject.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.XApplication;


@SuppressWarnings("ALL")
public class ToastUtils {
    public final static int ONLY_TEXE = 1;
    public final static int TEXT_AND_IMAGE = 2;
    public final static int TEXT_AND_PROCESSBAR = 3;

    private static Toast bigToast;
    private static Toast toast;
    private static WindowManager wm;
    private static View view;

    public static void showToast(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
        View view = inflater.inflate(R.layout.toast_global, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_right);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb);

        tv.setText(msg);
        if (bigToast == null) {
            bigToast = new Toast(context.getApplicationContext());
        }
        bigToast.setGravity(Gravity.CENTER, 0, 0);
        bigToast.setDuration(Toast.LENGTH_SHORT);
        bigToast.setView(view);
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        bigToast.show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, Spanned msg, int dur) {
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
        View view = inflater.inflate(R.layout.toast_global, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_right);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb);

        tv.setText(msg);
        if (bigToast == null) {
            bigToast = new Toast(context.getApplicationContext());
        }
        bigToast.setGravity(Gravity.CENTER, 0, 0);
        bigToast.setDuration(dur);
        bigToast.setView(view);
        imageView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        bigToast.show();
    }

    public static void showNetErrorToast(Context context) {
        if (toast == null && context != null) {
            toast = Toast.makeText(context.getApplicationContext(), "网络请求失败，请检查网络设置", Toast.LENGTH_SHORT);
        }
        if (toast != null) {
            toast.setText("网络请求失败，请检查网络设置");
            toast.show();
        }
    }

    /**
     * 显示自定义土司
     *
     * @param
     */
    public static void showTextToast(String text, Context context, int kind) {
        if (wm == null) {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        view = View.inflate(context,
                R.layout.toast_global, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_right);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb);
        tv.setText(text);
        if (kind == ONLY_TEXE) {
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else if (kind == TEXT_AND_IMAGE) {
            progressBar.setVisibility(View.GONE);
        } else if (kind == TEXT_AND_PROCESSBAR) {
            imageView.setVisibility(View.GONE);
        }
        // ************mparams参数设置*******************
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.height = 250;
        mParams.width = 400;
        // 修改完左上角对其,便于边界问题的处理
        mParams.gravity = Gravity.CENTER;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //自定义的土司需要用户触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.toast_anim_style;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST; //土司窗体天生不响应触摸事件
        mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        // **********************************
        wm.addView(view, mParams);
    }

    /**
     * 显示自定义土司,可以设置显示时间
     *
     * @param
     */
    public static void showTextToast(String text, final Context context, int kind, final int time) {
        if (wm == null) {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        view = View.inflate(context,
                R.layout.toast_global, null);
        TextView tv = (TextView) view.findViewById(R.id.tv);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_right);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb);
        tv.setText(text);
        if (kind == ONLY_TEXE) {
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else if (kind == TEXT_AND_IMAGE) {
            progressBar.setVisibility(View.GONE);
        } else if (kind == TEXT_AND_PROCESSBAR) {
            imageView.setVisibility(View.GONE);
        }
        // ************mparams参数设置*******************
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.height = 250;
        mParams.width = 400;
        // 修改完左上角对其,便于边界问题的处理
        mParams.gravity = Gravity.CENTER;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //自定义的土司需要用户触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.toast_anim_style;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST; //土司窗体天生不响应触摸事件
        mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        // **********************************
        wm.addView(view, mParams);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    wm.removeView(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, time);
    }

    public static void closeToast() {
        if (wm != null) {
            wm.removeView(view);
        }
    }

    public static void showApplicationToastMessage(String message) {
        showToast(XApplication.getInstance(), message);
    }
}
