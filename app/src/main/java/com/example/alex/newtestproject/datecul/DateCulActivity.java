package com.example.alex.newtestproject.datecul;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;


public class DateCulActivity extends BaseActivity {

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_select_date)
    TextView tvSelectDate;
    @BindView(R.id.tv_duty)
    TextView tvDuty;
    @BindView(R.id.sp_duty)
    Spinner spDuty;

    private MineDate mCurrentDate;
    private MineDate mSelectDate = new MineDate();
    private int mSelectPosition;
    private ArrayList<String> mDutyDescArray;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_date_cul;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {
        mCurrentDate = getCurrentTime();

        mDutyDescArray = new ArrayList<>();
        mDutyDescArray.add("早班");
        mDutyDescArray.add("夜班");
        mDutyDescArray.add("休息");

//        LinkedList<String> strings2 = new LinkedList<>();
//        mDutyDescArray.add("早班");
//        mDutyDescArray.add("夜班");
//        mDutyDescArray.add("休息");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                DateCulActivity.this, android.R.layout.simple_list_item_1, mDutyDescArray);
        spDuty.setAdapter(arrayAdapter);
        spDuty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvDate.setText(
                String.format("今天是%s年%s月%s日", mCurrentDate.year, mCurrentDate.month, mCurrentDate.day));
    }

    /**
     * 弹出日期选择器
     */
    public void getEndDate() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                mSelectDate.year = year;
                mSelectDate.month = month + 1;
                mSelectDate.day = day;
                setDateText(year, month + 1, day);
            }
        }, mCurrentDate.year, mCurrentDate.month - 1, mCurrentDate.day).show();
    }

    private void setDateText(int year, int month, int day) {
        tvSelectDate.setText(
                String.format("选择的日期是%s年%s月%s日", year, month, day));

        tvDuty.setText(String.format("该日期是%s日", getDudyText()));
    }

    private String getDudyText() {
        Calendar currentTime = Calendar.getInstance();
        Calendar selectTime = Calendar.getInstance();

        currentTime.set(Calendar.YEAR, mCurrentDate.year);
        currentTime.set(Calendar.MONTH, mCurrentDate.month - 1);
        currentTime.set(Calendar.DAY_OF_MONTH, mCurrentDate.day);

        selectTime.set(Calendar.YEAR, mSelectDate.year);
        selectTime.set(Calendar.MONTH, mSelectDate.month - 1);
        selectTime.set(Calendar.DAY_OF_MONTH, mSelectDate.day);

        int days = daysBetween(currentTime.getTime(), selectTime.getTime()) + mSelectPosition;

        int count = Math.abs(days % 3);

        String dutyStr = "早";
        switch (count) {
            case 0:
                dutyStr = mDutyDescArray.get(0);
                break;
            case 1:
                dutyStr = mDutyDescArray.get(1);
                break;
            case 2:
                dutyStr = mDutyDescArray.get(2);
                break;
        }
        return dutyStr;
    }

    public static int daysBetween(Date early, Date late) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                .getTime().getTime() / 1000)) / 3600 / 24;

        return days;
    }


    @OnClick({R.id.tv_select_date})
    void onClick() {
        getEndDate();
    }

    private MineDate getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        MineDate mineDate = new MineDate();
        mineDate.year = calendar.get(Calendar.YEAR);
        mineDate.month = calendar.get(Calendar.MONTH) + 1;
        mineDate.day = calendar.get(Calendar.DATE);
        return mineDate;
    }

    private class MineDate {
        int year;
        int month;
        int day;
    }
}
