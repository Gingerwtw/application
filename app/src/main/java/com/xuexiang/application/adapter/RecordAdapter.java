package com.xuexiang.application.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

}
