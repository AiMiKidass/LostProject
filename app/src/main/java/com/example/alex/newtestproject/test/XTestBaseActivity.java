package com.example.alex.newtestproject.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.base.BaseFragment;
import com.example.alex.newtestproject.utils.AutoUtils;
import com.example.alex.newtestproject.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import butterknife.ButterKnife;

/**
 * Created by Alk on 2017/5/23.
 * alk
 */
public abstract class XTestBaseActivity extends AppCompatActivity {

    private static final String KEY_SAVEINSTANCE_TOKEN = "token";
    private static final String KEY_SAVEINSTANCE_USERMSG = "usermsg";
    protected ProgressDialog mDialog;
    private boolean hasActionBar = true;
    private boolean hasAutoSize = true;
    protected Activity context;

    protected static Gson mGson = new Gson();
    private TitleActionBarLayout mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = XTestBaseActivity.this;
        beforeOnCreate();
        super.onCreate(savedInstanceState);
        // 每次必须在启动activity中初始化一次
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  // 竖屏
//        if (hasAutoSize) {
//            AutoUtils.setSize(this, true, APP.SCREEN_WIDTH, APP.SCREEN_HEIGHT);//false为,没有状态栏,设计尺寸的宽
//        }
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        ButterKnife.bind(this);
        //  initActionBar();
        AutoUtils.auto(this);//适配实际屏幕

        if (savedInstanceState != null) {
//            GlobalConfig.USERMSG = (UserMessage) savedInstanceState.get(KEY_SAVEINSTANCE_USERMSG);
//            GlobalConfig.ACCESSTOKEN = (String) savedInstanceState.get(KEY_SAVEINSTANCE_TOKEN);
        }

        initProgressBar();
        afterOnCreate(savedInstanceState);

        mGson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        if (GlobalConfig.USERMSG != null) {
//            outState.putSerializable(KEY_SAVEINSTANCE_USERMSG, GlobalConfig.USERMSG);
//            outState.putSerializable(KEY_SAVEINSTANCE_TOKEN, GlobalConfig.ACCESSTOKEN);
//        }
    }

    /**
     * 为调整actionbar所做出的方法
     */
    protected void beforeOnCreate() {

    }

    /**
     * 设置title
     */
    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar == null) {
            return;
        } else if (!hasActionBar) {
            supportActionBar.hide();
            return;
        }
        // init
        supportActionBar.setDisplayShowCustomEnabled(true);
        supportActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        mTitleView = getTitileView();

        AutoUtils.auto(mTitleView);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        supportActionBar.setCustomView(mTitleView, layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//21
            supportActionBar.setElevation(0);//5.0以上去阴影
        }
        // title
        String title = getTitle().toString();
        mTitleView.textview.setText(title);
        mTitleView.backedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            BaseFragment fragment = (BaseFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.ll_container);
            if (fragment != null) {
                fragment.onBacked();
            }
        }
    }

    /**
     * 设置title
     *
     * @return title
     */
    protected String getTitleText() {
        return "";
    }

    protected void setActionBarVisible(boolean hasActionBar) {
        this.hasActionBar = hasActionBar;
    }

    protected void setHasAutoSize(boolean hasAutoSize) {
        this.hasAutoSize = hasAutoSize;
    }

    private void initProgressBar() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("读取数据中,请稍候");
        mDialog.setMessage("正在读取...");
        mDialog.setIndeterminate(false);
        mDialog.setCancelable(true);
        mDialog.setOnCancelListener(null);
    }

    protected abstract int getLayoutId();

    protected abstract void afterOnCreate(Bundle savedInstanceState);


    private void handleError() {
        ToastUtils.showToast(XTestBaseActivity.this, "发生了未知错误");
    }

    protected void dismissProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    protected void showLoadingProgress() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    protected void showToastMessage(String msg) {
        ToastUtils.showToast(XTestBaseActivity.this, msg);
    }

    protected void showToastMessage(String msg, int mode) {
        Toast.makeText(context, msg, mode).show();
    }

    protected void showToastMessage(int resId) {
        ToastUtils.showToast(XTestBaseActivity.this, resId);
    }

    protected class TitleActionBarLayout extends RelativeLayout {
        public ImageView backedView;
        public TextView textview;
        public TextView rightView;

        public TitleActionBarLayout(Context context) {
            super(context);
        }
    }

    protected String getText(EditText text) {
        return text.getText().toString().trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected HashMap<String, String> createParamsHashMap() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }

    /**
     * 验证字符串是否为空
     *
     * @param text
     * @return
     */
    protected boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    protected void showError(String msg) {
        showToastMessage("提示:" + msg);
    }

    protected void showError(int msgId) {
        showError(getString(msgId));
    }


    public class RequestMethod {
        public final static int GET = 1;
        public final static int POST = 2;
    }

    /**
     * 弹出提示对话框
     *
     * @return
     */
    protected AlertDialog.Builder createAlertMessage() {
        return new AlertDialog.Builder(context);
    }

}

