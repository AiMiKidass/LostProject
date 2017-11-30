package com.example.alex.newtestproject.test.fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.base.BaseFragment;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
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


    LinkedList<String> data;
    @BindView(R.id.et_input)
    EditText etInput;

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
                String s = etInput.getText().toString();
                showMsg(createTecNumber(s));

                String text = tv_test.getText().toString();

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


                isPassword("123");
                isPassword("sdaskdjaksjdk@111.com");

                break;
        }

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

}
