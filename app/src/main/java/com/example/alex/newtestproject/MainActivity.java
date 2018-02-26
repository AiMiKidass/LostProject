package com.example.alex.newtestproject;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.newtestproject.eventbus.EventBusMsg;
import com.example.alex.newtestproject.eventbus.EventBusMsg22;
import com.example.alex.newtestproject.eventbus.TestEvent;
import com.example.alex.newtestproject.gaode.LocationSourceActivity;
import com.example.alex.newtestproject.gaode.MineMapActivity;
import com.example.alex.newtestproject.gaode.RoutePlanningActivity;
import com.example.alex.newtestproject.rxJava.RxJavaDemoTestActivity;
import com.example.alex.newtestproject.rxJava.permission.CheckPermissionUtils;
import com.example.alex.newtestproject.serverapi.XApiActivity;
import com.example.alex.newtestproject.test.XTestActivity;
import com.example.alex.newtestproject.utils.NewPermissionUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class  MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSION_SD = 101;
    private static final int REQUEST_CODE_SETTING = 400;
    @BindView(R.id.tv_testcontent)
    TextView tv_testcontent;

    @BindView(R.id.btn_click)
    Button btn_click;
    @BindView(R.id.btn_openlist)
    Button btn_openlist;

    private TestEvent testEvent;

    @BindView(R.id.lv_list)
    public ListView list;
    private MineItem[] mDataSoucre = new MineItem[]{
            new MineItem("小蓝点定位", LocationSourceActivity.class),
            new MineItem("高德地图定位+高级蓝点自定义", MineMapActivity.class),
            new MineItem("高德地图定位+导航", RoutePlanningActivity.class),
            new MineItem("RxJavaDemo", RxJavaDemoTestActivity.class),
            new MineItem("Api测试专用", XApiActivity.class),
            new MineItem("各种测试效果", XTestActivity.class),
    };
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_CONTACTS,
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        // eventbus是以事件类型,来确定最终发送对应的对象方法,若有多个方法注册了同一个类型的事件,他们在发送对应事件后,都会执行
        // EventBusMsg就是事件的具体类型,注意post中的发送对象类型
        testEvent = new TestEvent(new TestEvent.Event1(), new TestEvent.Event2(), new TestEvent.Event3());
        list.setAdapter(new CustomArrayAdapter(mDataSoucre));

        countdown();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void request2() {

        if (CheckPermissionUtils.isNeedAddPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            requestPermissions(permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

        }
    }

    void countdown(){
        CountDownTimer timer = new CountDownTimer(10000, 1000) {// 第一个参数是总共时间，第二个参数是间隔触发时间
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e(TAG, "ontick...." + millisUntilFinished / 1000 + "s后结束");

            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onfinish...." + "结束了");
            }
        };
        timer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {

        }
    }

    private void request1() {
        NewPermissionUtils.checkPermission(this, permissions);

    }

    @OnItemClick({R.id.lv_list})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MineItem mineItem = mDataSoucre[position];
        startActivity(new Intent(MainActivity.this, mineItem.clazz));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(EventBusMsg event) {
        Log.d(TAG, "接收到信息");
        tv_testcontent.setText("EventBus 数据 : " + event.name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent2(EventBusMsg22 event) {
        Log.d(TAG, "接收到信息222");
        tv_testcontent.setText("EventBus 数据 : " + event.name);
    }

    @OnClick({R.id.btn_click, R.id.btn_openlist})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:
                /*
                 EventBus.getDefault().post(new EventBusMsg("sss"));
                testEvent.post();
                 */


                request3();

                break;
            case R.id.btn_openlist:
                startActivity(new Intent(MainActivity.this, ListItemActivity.class));
                break;
        }
    }

    private void request3() {

        // 申请单个权限。
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_PERMISSION_SD)
                .permission(Manifest.permission.WRITE_CALENDAR)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestcode, @NonNull List<String> permissions) {
                        if (requestcode == 101) {
                            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();

                            if (AndPermission.hasPermission(MainActivity.this, permissions)) {
                                // do something

                            }else{
                                Toast.makeText(MainActivity.this, "系统提示授权成功,但实际没有权限", Toast.LENGTH_SHORT).show();

                                // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_SETTING).show();
                            }

                        }
                    }

                    @Override
                    public void onFailed(int requestcode, @NonNull List<String> deniedPermissions) {
                        if (requestcode == 101) {
                            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                            if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                                // 第一种：用默认的提示语。
                                AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_SETTING).show();
                            }
                        }
                    }
                })
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                        AndPermission.rationaleDialog(MainActivity.this, rationale).
                                show();
                    }
                })
                .start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    private class MineItem {
        String title;
        Class<?> clazz;

        MineItem(String title, Class<?> clazz) {
            this.title = title;
            this.clazz = clazz;
        }
    }

    private class CustomArrayAdapter extends BaseAdapter {

        private MineItem[] mDataSoucre;

        public CustomArrayAdapter(MineItem[] mDataSoucre) {
            this.mDataSoucre = mDataSoucre;
        }

        @Override
        public int getCount() {
            return mDataSoucre.length;
        }

        @Override
        public Object getItem(int position) {
            return mDataSoucre[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.customer_item, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.tv_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MineItem item = (MineItem) getItem(position);
            holder.textView.setText(item.title);
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
