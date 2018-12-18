package com.example.alex.newtestproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.base.BaseActivity;
import com.example.alex.newtestproject.utils.LogUtils;

import java.io.File;

import butterknife.OnClick;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

/**
 * 文件选择器demo
 * github: https://github.com/bartwell/ExFilePicker
 */
public class ExFilePickerTestActivity extends BaseActivity {

    private static final int EX_FILE_PICKER_RESULT = 565;

    @Override
    protected int getRootViewResId() {
        return R.layout.activity_test_ex_file_picker;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.tv_click1)
    void click001() {
        ExFilePicker exFilePicker = new ExFilePicker();
        // 设置禁止新建文件夹 禁用排序 启动退出按钮 显示第一行为回退
        // 设置只能选择文件(不能选文件夹)
        exFilePicker.setCanChooseOnlyOneItem(true);
        exFilePicker.setNewFolderButtonDisabled(true);
        exFilePicker.setSortButtonDisabled(true);
        exFilePicker.setQuitButtonEnabled(true);
        exFilePicker.setUseFirstItemAsUpEnabled(true);
        exFilePicker.setHideHiddenFilesEnabled(true);
        exFilePicker.setChoiceType(ExFilePicker.ChoiceType.FILES);
        exFilePicker.start(this, EX_FILE_PICKER_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EX_FILE_PICKER_RESULT) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result == null || result.getCount() == 0) {
                return;
            }
            File[] files = new File[result.getCount()];
            for (int i = 0; i < result.getCount(); i++) {
                File file = new File(result.getPath() + result.getNames().get(i));
                files[i] = file;
            }

            for (File f : files) {
                LogUtils.d(stringformatStr("name=%s,exists=%s", f.getName(), f.exists()));
            }
        }
    }
}
