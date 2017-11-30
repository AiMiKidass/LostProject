package com.example.alex.newtestproject.rxJava.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class TestFragment1 extends BaseZhuangBiFragment {

    private static final int MSG_COUNTDOWN = 11;
    private static final int MSG_COUNTBREAK = 12;

    int max = 50000;
    int min = 1000;

    private static final int TIMEOUT_MAX = 5000;
    @BindView(R.id.tv_showvalue)
    TextView tvShowvalue;
    @BindView(R.id.sb_seekbar)
    SeekBar sbSeekbar;

    private Handler mHandler = new Handler() {

        private boolean isContinue = false;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_COUNTDOWN:
                    if (isContinue) {
                        mHandler.sendEmptyMessageDelayed(MSG_COUNTDOWN, 1000);
                        long time = TIMEOUT_MAX - 1000;
                        LogUtils.d("" + time);
                        btn_countdown.setText("倒计时,现在还有" + (time / 1000) + "秒");
                    }
                    break;
                case MSG_COUNTBREAK:
                    isContinue = false;
                    break;
            }
        }
    };

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }

    @BindView(R.id.btn_click)
    Button btn_click;
    @BindView(R.id.btn_countdown)
    Button btn_countdown;
    @BindView(R.id.btn_click222)
    Button btn_click222;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_test1, null);
        ButterKnife.bind(this, view);
        init();


        long currentTime = System.currentTimeMillis();

        long currentSSS = currentTime + 1000000;


        int hour = 1000 * 60 * 12;
        if (currentTime - currentSSS > hour) {
            // set
            // put
        } else {
            //donothing
        }
        return view;
    }

    private void init() {
        sbSeekbar.setMax(max / min);
        sbSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //MapToastUtil.show(getActivity(), "progress = " + progress);
                tvShowvalue.setText((min * progress) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @OnClick(R.id.btn_countdown)
    void countdown() {

        btn_countdown.setSelected(true);

        mHandler.sendEmptyMessageDelayed(MSG_COUNTDOWN, 1000);

        CountDownTimer timer = new CountDownTimer(5000, 1000) {// 第一个参数是总共时间，第二个参数是间隔触发时间
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtils.d("millisUntilFinished" + millisUntilFinished);

                btn_countdown.setText("倒计时,现在还有" + ((millisUntilFinished + 100) / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                LogUtils.d("onFinish");
                btn_countdown.setText("倒计时结束");
                btn_countdown.setEnabled(false);
            }
        };


    }


    AlertDialog alertDialog;

    @OnClick(R.id.btn_click)
    void buttonClick() {


        View dialogView = View.inflate(getActivity(), R.layout.dialog_regist_protocol, null);

        LinearLayout rootView = (LinearLayout) dialogView.findViewById(R.id.ll_root);

        android.app.AlertDialog mRegisterProtocolDialog = new android.app.AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .create();

        Dialog alertDialog2 = new Dialog(getActivity(), R.style.dialog);
        alertDialog2.setContentView(dialogView);

        // 一种全屏dialog的方法,dialog这个自带边距
        Window window = alertDialog2.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
        alertDialog2.show();
    }
    String[] itemsStri = {"1", "2", "3"};
    @OnClick(R.id.btn_click222)
    void dialogClickStyleTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.custom_info_bubble);
        builder.setTitle("使用Resource ID");
        builder.setItems(itemsStri, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class DropDownDataAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getActivity(), R.layout.item_dropdown, null);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
            tv_item.setText("请选择");
            return view;
        }
    }
}
