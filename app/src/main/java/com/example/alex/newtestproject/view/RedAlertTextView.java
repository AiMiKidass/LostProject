package com.example.alex.newtestproject.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.alex.newtestproject.R;

/**
 *
 */
public class RedAlertTextView extends TextView {
    public RedAlertTextView(Context context) {
        super(context);
    }

    public RedAlertTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RedAlertTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        String text = (String) getText();
        if (text == null || text.length() == 0) {
            return;
        }
        setSpanText(getContext(), RedAlertTextView.this, text, text.length() - 1, text.length(), R.color.red);
    }

    void setSpanText(final Context context, TextView view, String str, int start, int end, final int colorRes) {
        // //设置部分字体样式，但是不可点击
        SpannableString spString = new SpannableString(str);
        spString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.RED);//设置字体颜色
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 表示不影响前后文字
        view.setText(spString);
    }


}
