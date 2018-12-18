package com.example.alex.newtestproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ListItemActivity extends AppCompatActivity {

    public ListView list;

    private MineItem[] mDataSoucre = new MineItem[]{
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);


        list = (ListView) findViewById(R.id.lv_list);
        list.setAdapter(new CustomArrayAdapter(mDataSoucre));
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
            return 99;
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
            View view = View.inflate(ListItemActivity.this, R.layout.customer_item, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_item);
            // MineItem item = (MineItem) getItem(position);
            textView.setText("3333");
            return view;
        }
    }
}
