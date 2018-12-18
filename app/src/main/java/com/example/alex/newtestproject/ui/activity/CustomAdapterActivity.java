package com.example.alex.newtestproject.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.alex.newtestproject.R;
import com.example.alex.newtestproject.base.BaseActivity;
import com.example.alex.newtestproject.ui.adapter.TestMultItemAdapter;
import com.example.alex.newtestproject.utils.DateUtils;
import com.example.alex.newtestproject.view.CustomScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 测试:
 */
public class CustomAdapterActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private TestMultItemAdapter adapter;
    private List<EnityBean> beanArrayList;

    @BindView(R.id.vp_viewpager)
    CustomScrollViewPager pager;
    @BindView(R.id.btn_change_viewpmode)
    Button btnChangeViewMode;
    private boolean mChange;

    @Override
    protected int getRootViewResId() {
        return R.layout.activity_custom_adapter;
    }

    @Override
    protected void afterOnCreate(Bundle savedInstanceState) {

        initView();
        initData();

        btnChangeViewMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChange = !mChange;
                pager.setScrollable(mChange);

                showToastMessage("是否开启=" + mChange);
            }
        });

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new FragmentA();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "标题";
            }
        });
    }

    public static class FragmentA extends Fragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return View.inflate(getActivity(),R.layout.fragment_map,null);
        }
    }

    @Override
    protected void initView() {
        adapter = new TestMultItemAdapter(this, new ArrayList<EnityBean>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.view_task_runing: {
                        showToastMessage("R.id.view_task_runing");
                        break;
                    }
                    case R.id.act_start_tasks: {
                        showToastMessage("R.id.act_start_tasks");
                        break;
                    }
                    case R.id.view_task_name: {
                        showToastMessage("R.id.view_task_name");
                        break;
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        beanArrayList = new ArrayList<>();
        EnityBean enityBean1 = new EnityBean();
        enityBean1.setName("EnityBean.TYPE_TITLE");
        enityBean1.setDateTime(DateUtils.getCurrentTime());
        enityBean1.setType(EnityBean.TYPE_TITLE);
        beanArrayList.add(enityBean1);

        EnityBean enityBean2 = new EnityBean();
        enityBean2.setName("EnityBean.TYPE_CONTROL");
        enityBean2.setDateTime(DateUtils.getCurrentTime());
        enityBean2.setType(EnityBean.TYPE_CONTROL);
        beanArrayList.add(enityBean2);

        EnityBean enityBean3 = new EnityBean();
        enityBean3.setName("EnityBean.TYPE_WHOLEINFO");
        enityBean3.setDateTime(DateUtils.getCurrentTime());
        enityBean3.setType(EnityBean.TYPE_WHOLEINFO);
        beanArrayList.add(enityBean3);

        EnityBean enityBean4 = new EnityBean();
        enityBean4.setName("EnityBean.TYPE_TITLE");
        enityBean4.setDateTime(DateUtils.getCurrentTime());
        enityBean4.setType(EnityBean.TYPE_TITLE);
        beanArrayList.add(enityBean4);

        adapter.replaceData(beanArrayList);
    }

    public static class EnityBean implements MultiItemEntity {
        public static final int TYPE_TITLE = 1;
        public static final int TYPE_WHOLEINFO = 3;
        public static final int TYPE_CONTROL = 2;

        private int type = TYPE_TITLE;

        private String name;
        private String dateTime;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        public int getItemType() {
            return type;
        }
    }
}
