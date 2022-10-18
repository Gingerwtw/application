package com.xuexiang.application.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.application.R;
import com.xuexiang.application.Record;
import com.xuexiang.application.adapter.RecordAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    SharedPreferences mShared;
    ImageButton btn_back;

    int record_size;
    String []recordStringList = new String[10];
    String []dateStringList = new String[10];
    String []nameStringList = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mShared = getSharedPreferences("information", MODE_PRIVATE);


        boolean haveRecord = initResult();
        if (!haveRecord){
            TextView record_hint = findViewById(R.id.record_hint);
            record_hint.setVisibility(View.VISIBLE);
        }else{
            ListView listView = findViewById(R.id.record_list);
            List<Record> recordList = new ArrayList<>();

            for (int i = 0; i  < record_size; i++){
                Record record = new Record(i+1, recordStringList[i],dateStringList[i], nameStringList[i]);
                recordList.add(record);
            }

            RecordAdapter adapter = new RecordAdapter(RecordActivity.this, R.layout.record_item, recordList);
            listView.setAdapter(adapter);
        }

        btn_back = findViewById(R.id.record_toolbar_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean initResult(){
        Bundle bundle=getIntent().getExtras();
        String respondJson = bundle.getString("content");
        JSONArray recordArray = null, dateArray = null;
        boolean haveRecord = false;
        try {
            JSONObject obj = new JSONObject(respondJson);
            record_size = obj.getInt("record_size");
            recordArray = obj.getJSONArray("record");
            dateArray = obj.getJSONArray("date");

            Log.d("recordArray", String.valueOf(recordArray));
            Log.d("dateArray", String.valueOf(dateArray));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (recordArray != null & dateArray != null){
            for(int i=0;i<record_size;i++) {
                try {
                    String[] strarray=recordArray.getString(i).split("[,]");
                    recordStringList[i] = strarray[0];
                    nameStringList[i] = strarray[1];
                    dateStringList[i] = dateArray.getString(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            haveRecord = true;
        }
        return haveRecord;
    }
}