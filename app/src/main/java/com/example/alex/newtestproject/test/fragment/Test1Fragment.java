package com.example.alex.newtestproject.test.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.base.BaseFragment;
import com.example.alex.newtestproject.utils.LogUtils;
import com.example.alex.newtestproject.utils.PermissionUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试1
 */

public class Test1Fragment extends BaseFragment {
    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.btn_countdown)
    Button btnCountdown;

    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;

    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindView(R.id.ll_test)
    LinearLayout ll_test;

    @BindView(R.id.iv_qr)
    ImageView iv_qr;

    LinkedList<String> data;
    @BindView(R.id.et_input)
    EditText etInput;
    private Dialog dialog;
    private boolean isBreakFlag;
    private ScheduledExecutorService singleThreadScheduledPool;


    @Override
    public void onBacked() {

    }

    @Override
    protected View onInitView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_test_t01, null);
        return inflate;
    }

    @Override
    protected void afterCreateView() {
        initSpinner();

        addRepaymentViews();

        LogUtils.d("" + getScreenWidth(getActivity()));
        LogUtils.d("" + getScreenHeight(getActivity()));

        PermissionUtils.requestPermission(getActivity(), 101, new String[]{Manifest.permission.CAMERA}, new PermissionUtils.OnPermissionResultListener() {
            @Override
            public void onSuccess() {
                showMsg("授权成功!");
            }

            @Override
            public void onFailure() {

            }
        });


        test001();
    }

    /**
     * 返回屏幕宽(px)
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 返回屏幕高(px)
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private void initSpinner() {
        data = new LinkedList<>(Arrays.asList("Zhang", "Phil", "@", "CSDN"));

        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectItemstr = data.get(position);

                showMsg(selectItemstr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getFragmentManager();
            }
        });

        niceSpinner.post(new Runnable() {
            @Override
            public void run() {
                niceSpinner.attachDataSource(data);
            }
        });
    }

    @OnClick({R.id.btn_click, R.id.btn_countdown, R.id.btn_showdialog})
    protected void onclickEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
                showPopwin();
                break;
            case R.id.btn_countdown:
                //
                countDown(3 * 1000);

//                String s = etInput.getText().toString();
//                //showMsg(createTecNumber(s));
//
//                String text = tv_test.getText().toString();
//
//                if (singleThreadScheduledPool != null) {
//                    singleThreadScheduledPool.shutdownNow();
//                    LogUtils.d("isshutdow" + singleThreadScheduledPool.isShutdown());
//                }
                // setSpanText(tv_test, text, 3, 4, getResources().getColor(R.color.ffdba2), 50);
                break;
            case R.id.btn_showdialog:
                /*


                Uri uri = Uri.parse("http://192.168.5.68:8088/Contents/android/11.rar");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
 */
                /*
                final String[] items = {"1", "2", "3", "4"};
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("单选对话框")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addRepaymentViews();
                            }
                        }).create();
                dialog.show(); */

//                isPassword("123");
//                isPassword("sdaskdjaksjdk@111.com");


                startPolling();
//                showDialog();

                break;
        }
    }

    long counttime = 0;
    final Timer timer = new Timer();
    private TimerTask task;

    private void countDown(final long time) {
        counttime = time;

        btnCountdown.setText(time / 1000 + "秒");
        task = new TimerTask() {
            @Override
            public void run() {
                if (counttime > 0) {
                    counttime -= 1000;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (counttime == 0) {
                                LogUtils.d("counttime=" + counttime);
                                btnCountdown.setText("重新发送");
                                btnCountdown.setClickable(true);
                                task.cancel();
                            } else {
                                LogUtils.d("counttime=" + counttime);
                                btnCountdown.setText(counttime / 1000 + "秒");
                                btnCountdown.setClickable(false);
                            }
                        }
                    });
                }
            }
        };

        timer.schedule(task, 1000, 1000);
    }

    private void startPolling() {
        singleThreadScheduledPool = Executors.newScheduledThreadPool(1);
        //延迟1秒后，每隔2秒执行一次该任务
        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String threadName = Thread.currentThread().getName();
                LogUtils.d("zxy 线程：" + threadName + ",正在执行");
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * showDialog
     */
    private void showDialog() {
        if (dialog == null) {
            dialog = new Dialog(getActivity(), R.style.loading_dialog);
            View dialogview = View.inflate(getActivity(), R.layout.dialog_pcode, null);
            View ivbg = dialogview.findViewById(R.id.iv_bg);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isBreakFlag = true;
                }
            });
            dialog.setContentView(dialogview, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局

            ivbg.setBackgroundResource(R.drawable.amap_ride);
        }
        dialog.show();
    }

    public static boolean isPassword(String pwd) {
        Pattern p = Pattern
                .compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }


    /**
     * 添加还款计划View
     * 遍历元素
     */
    private void addRepaymentViews() {
        LinearLayout llRepaymentsContainer = ll_test;

        if (llRepaymentsContainer.getChildCount() > 0) {
            llRepaymentsContainer.removeAllViews();
        }

        TextView textView;
        String textdesc = "";
        for (int i = 0; i < 3; i++) {
            textView = new TextView(getActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 60));
            textdesc = "333" + " ";
            textView.setText(textdesc);
            textView.setBackgroundColor(getResources().getColor(R.color.white));
            textView.setTextSize(35, TypedValue.COMPLEX_UNIT_PX);
            textView.setTextColor(getResources().getColor(R.color.black));

            // AutoUtils.autoSize(textView);
            llRepaymentsContainer.addView(textView);
        }

    }


    private String createTecNumber(String number) {
        int length = number.length();

        if (length <= 3) {
            return number;
        }

        String[] split = number.split("");
        StringBuilder text = new StringBuilder();
        int index = 1;
        for (int i = split.length - 1; i > 0; i--) {
            text.insert(0, split[i]);
            if ((index) % 3 == 0 && (index) != (split.length - 1)) {
                text.insert(0, ",");
            }
            index++;
        }
        return text.toString();
    }

    private void showPopwin() {


    }

    /**
     * 举例:setSpanText(view,str,start,end,colorRes)
     *
     * @param view
     * @param str
     * @param start
     * @param end
     * @param colorRes
     */
    public static void setSpanText(final TextView view, CharSequence str, int start, int end, final int colorRes) {
        setSpanText(view, str, start, end, colorRes);
    }

    /**
     * 更改部分样式 特殊颜色
     * 举例:setSpanText(view,str,start,end,colorRes)
     *
     * @param view
     * @param str
     * @param start
     * @param end
     * @param colorRes
     */
    public static void setSpanText(final TextView view, CharSequence str, int start, int end, final int colorRes, final int textSize) {
        //设置部分字体样式，但是不可点击
        SpannableString spString = new SpannableString(str);
        spString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(colorRes);//设置字体颜色

                if (view.getTextSize() != textSize) {
                    ds.setTextSize(textSize);
                }
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 表示不影响前后文字
        view.setText(spString);
    }


    private void test001() {
        iv_qr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View viewm) {
                LogUtils.d("开始");

                Bitmap obmp = ((BitmapDrawable) (iv_qr).getBackground()).getBitmap();
                int width = obmp.getWidth();
                int height = obmp.getHeight();
                int[] data = new int[width * height];
                obmp.getPixels(data, 0, width, 0, 0, width, height);
                com.google.zxing.RGBLuminanceSource sourc2e = new com.google.zxing.RGBLuminanceSource(width, height, data);
                BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(sourc2e));
                QRCodeReader reader = new QRCodeReader();
                Result re = null;
                try {
                    re = reader.decode(bitmap1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (re == null) {
                    showAlert(obmp);
                } else {
                    LogUtils.d("qrcode url=" + re.getText());
                    showSelectAlert(obmp, re.getText());
                }
                return false;
            }
        });
    }

    private void showAlert(final Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("保存图片")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterfacem, int i) {
                        //  saveImageToGallery(bitmap);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterfacem, int i) {
                    }
                });
        builder.show();
    }


    private void showSelectAlert(final Bitmap bitmap, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择");
        String str[] = {"保存图片", "扫二维码"};
        builder.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterfacem, int i) {
                switch (i) {
                    case 0: {
                        LogUtils.d("0");
                        //	saveImageToGallery(bitmap);
                    }
                    break;
                    case 1: {
                        LogUtils.d("1");
//						Intent n = new Intent(EnlargeimagevActivity.this, DetailActivity.class);
//						n.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.WEBVIEW_DETAIL);
//						n.putExtra(DetailwebFragment.WEB_URL, url);
//						startActivity(n);
                    }
                    break;
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterfacem, int i) {
                LogUtils.d("cancel");
            }
        });
        builder.show();
    }

}
