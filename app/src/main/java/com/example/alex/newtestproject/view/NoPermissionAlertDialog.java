package com.example.alex.newtestproject.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.alex.newtestproject.R;

public class NoPermissionAlertDialog extends Dialog {
    public NoPermissionAlertDialog(@NonNull Context context) {
        super(context);

        init();
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.dialog_nopermission_alert, null);
        setContentView(view);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        //  window.setAttributes(params);


    }


}
