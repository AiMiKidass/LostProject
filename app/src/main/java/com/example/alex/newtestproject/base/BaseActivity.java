package com.example.alex.newtestproject.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.utils.LogUtils;
import com.example.alex.newtestproject.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity框架
 *
 * @author arthur
 * @date 2018/05/28
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final int VALUE_NO_CONTENTVIEW = 0;
    // 状态栏的颜色
    private static int colorRes = R.color.red;
    public static Context mContext;
    private Unbinder mUnbinder;

    public static final String KEY_SAVEDATA_USERINFO = "key_savedata_userinfo";
    private static final String KEY_SAVEDATA_USERSESSIONINFO = "key_savedata_usersessioninfo";

    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (getRootViewResId() != VALUE_NO_CONTENTVIEW) {
            super.setContentView(getRootViewResId());
        }

        if (savedInstanceState != null) {
            // 恢复应用时从之前缓存的数据中恢复
        }

        mContext = this;
        mUnbinder = ButterKnife.bind(this);
        mLoadingDialog = getProgressDialog();

        afterOnCreate(savedInstanceState);
    }

    protected abstract int getRootViewResId();

    protected abstract void afterOnCreate(Bundle savedInstanceState);

    /**
     * View初始化 本方法需要自行调用
     */
    protected abstract void initView();

    /**
     * data初始化 本方法需要自行调用
     */
    protected abstract void initData();

    /**
     * 预留:网络加载错误时,显示的消息和界面效果
     */
    protected void afterLoadingError() {

    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    /**
     * 显示或者隐藏读取框
     *
     * @param isShow true=显示
     */
    protected void showOrDismissLoadingDialog(boolean isShow) {
        if (mLoadingDialog == null) {
            mLoadingDialog = getProgressDialog();
        }

        if (isShow) {
            // showDialog
            mLoadingDialog.show();
        } else {
            // dismissDialog
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 当发生网络错误的时候
     */
    protected void handleLoadingError() {
        afterLoadingError();
    }

    /**
     * 打印日志
     *
     * @param message
     */
    protected void log(String message) {
        LogUtils.d(message);
    }

    public void displayImage(String url, ImageView imageView) {
        Glide.with(getApplicationContext())//
                .load(url)//
                .error(R.mipmap.ic_launcher)//
                .into(imageView);
    }

    /**
     * 是否为空
     *
     * @param trim string value
     * @return isEmpty
     */
    public boolean isEmpty(String trim) {
        return TextUtils.isEmpty(trim);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            // 在每次返回桌面的时候，将特定信息(如用户信息)保存起来。防止应用长时间在后台，保存在application中的信息丢失
            // 保存用户资料进程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用dialog
     */
    private ProgressDialog getProgressDialog() {
        if (mLoadingDialog != null) {
            return mLoadingDialog;
        }

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setTitle("请稍候...");
        mLoadingDialog.setMessage("正在读取数据...");
        mLoadingDialog.setIndeterminate(false);
        mLoadingDialog.setCancelable(true);
        return mLoadingDialog;
    }

    /**
     * 显示Toast(自定义)
     *
     * @param msgResId R.string.xxx
     */
    public void showToastMessage(int msgResId) {
        String messageText = getResources().getString(msgResId);
        if (messageText.length() > 0) {
            showToastMessage(messageText);
        }
    }

    /**
     * 显示Toast
     *
     * @param msg string text
     */
    @SuppressWarnings("ConstantConditions")
    public void showToastMessage(final String msg) {
        if (TextUtils.isEmpty(msg)) {
            LogUtils.d("toast消息为null--" + msg);
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(BaseActivity.this, msg);
            }
        });
    }


    /**
     * 显示一个简单的Android风格的提示框 确定取消
     *
     * @param message
     */
    protected void showAlertDialog(String message) {
        message = message == null ? "" : message;
        AlertDialog.Builder builder = new AlertDialog
                .Builder(this)
                .setMessage(message)
                .setPositiveButton("确定", null);
        builder.create().show();
    }

    protected String stringformatStr(String str, Object... values) {
        return String.format(str, values);
    }


    protected void onDestroy() {
        super.onDestroy();
        // 清空
        mContext = null;

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

}
