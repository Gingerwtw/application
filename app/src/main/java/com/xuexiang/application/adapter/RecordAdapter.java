package com.xuexiang.application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xuexiang.application.R;
import com.xuexiang.application.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends ArrayAdapter<Record> {
    public RecordAdapter(Context context, int resource, List<Record> objects) {
        super(context,resource,objects);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record record = getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(R.layout.record_item, parent, false);

        TextView record_ID = view.findViewById(R.id.record_item_id);
        TextView record_health_index = view.findViewById(R.id.record_item_health_index);
        TextView record_date = view.findViewById(R.id.record_item_date);
        TextView record_name = view.findViewById(R.id.record_item_name);

        record_ID.setText(Integer.toString(record.getID())+".");
        record_health_index.setText(record.getResult());
        record_date.setText(record.getDate());
        record_name.setText(record.getName());

        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        //获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            // pre-condition
            return;

        }
        int totalHeight = 0;

        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {  //listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);  //计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        //listView.getDividerHeight()获取子项间分隔符占用的高度

        //params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }



}
