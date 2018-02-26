package com.example.alex.newtestproject.test.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.test.XTestActivity;
import com.example.alex.newtestproject.test.base.BaseAdapter;
import com.example.alex.newtestproject.test.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试: 上拉下拉刷新
 * 本Fragment为一个标准的上拉下拉刷新,修改自官方swipeRefreshView
 * 参考: https://www.jianshu.com/p/d23b42b6360b (简书)
 */
public class Test2Fragment extends BaseFragment {


//    @BindView(R.id.lv_content)
    ListView mListView;
    @BindView(R.id.srl_content)
    private SwipeRefreshLayout mRootSrlView;
    private ArrayList<String> mDataSource;
    private TestDataAdapter mAdapter;

    @Override
    public void onBacked() {

    }

    @Override
    protected View onInitView(LayoutInflater inflater) {
        View inflate = inflater.inflate(R.layout.fragment_test_t02, null);
        return inflate;
    }

    @Override
    protected void afterCreateView() {

        //  setListViewAdapter();


        String string = getString(R.string.test001);

        setSWRsetting();

    }

    private void setSWRsetting() {
        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        mRootSrlView.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.blue));
        // 设置下拉进度的主题颜色
        mRootSrlView.setColorSchemeColors(getResources().getColor(R.color.orarge)
                , getResources().getColor(R.color.red), getResources().getColor(R.color.white));
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mRootSrlView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 开始刷新 设置当前刷新状态为 doing
                mRootSrlView.setRefreshing(true);

                // 主线程操作(模拟)
                final Random random = new Random();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mDataSource.add(0, "我是天才" + random.nextInt(100) + "号");
//                        mAdapter.notifyDataSetChanged();

                        Toast.makeText(getActivity(), "刷新了一条数据", Toast.LENGTH_SHORT).show();

                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        mRootSrlView.setRefreshing(false);
                    }
                }, 1200);

                // 这个不能写在外边，不然会直接收起来
                //mRootSrlView.setRefreshing(false);
            }
        });
    }

    private void setListViewAdapter() {

        mDataSource = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            mDataSource.add("index" + i);
        }

        mAdapter = new TestDataAdapter(getActivity(), mDataSource);
        mListView.setAdapter(mAdapter);

    }

    @Override
    @OnClick({})
    protected void onclickEvent(View view) {

    }


    class TestDataAdapter extends BaseAdapter<String> {

        public TestDataAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public int getItemResource() {
            return R.layout.item_button;
        }

        @Override
        public View getItemView(int position, View convertView, ViewHolder holder) {

            String item = getItem(position);
            if (item == null) {
                return convertView;
            }

            Button button = holder.getView(R.id.btn_button);
            button.setText(item);

            return convertView;
        }

    }
}
