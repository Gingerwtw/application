

package com.xuexiang.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuexiang.application.R;

import java.util.Objects;

public class RecordDetailActivity extends AppCompatActivity {

    TextView tx_tongue_constitution, tx_tongue_health_index, tx_face_health_index,
            record_detail_tongue_hint, record_detail_face_hint, record_detail_lung_volume_hint;
    LinearLayout detail_tongue;
    RelativeLayout detail_face, detail_lung_volume;
    String tongue, face, lung, tongue_health_index, tongue_constitution, face_health_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        tx_tongue_constitution = findViewById(R.id.record_detail_tongue_constitution_data);
        tx_tongue_health_index = findViewById(R.id.record_detail_tongue_health_index_data);
        tx_face_health_index = findViewById(R.id.record_detail_face_health_index_data);
        record_detail_tongue_hint = findViewById(R.id.record_detail_tongue_hint);
        record_detail_face_hint = findViewById(R.id.record_detail_face_hint);
        record_detail_lung_volume_hint = findViewById(R.id.record_detail_lung_volume_hint);
        detail_tongue = findViewById(R.id.record_detail_tongue);
        detail_face = findViewById(R.id.record_detail_face);
        detail_lung_volume = findViewById(R.id.record_detail_lung_volume);

        init();
    }

    private void init(){
        Bundle bundle=getIntent().getExtras();
        tongue = bundle.getString("tongue");
        face = bundle.getString("face");
        lung = bundle.getString("lung");
        Log.d("tongue", tongue);
        Log.d("face", face);
        Log.d("lung", lung);

        tongue = replaceIllegalCharacter(tongue);
        face = replaceIllegalCharacter(face);
        lung = replaceIllegalCharacter(lung);

        Log.d("tongue after replace", tongue);
        Log.d("face after replace", face);
        Log.d("lung after replace", lung);


        if (Objects.equals(tongue, "暂无舌象数据")){
            detail_tongue.setVisibility(View.INVISIBLE);
        }else {
            record_detail_tongue_hint.setVisibility(View.INVISIBLE);
            String[] strarray=tongue.split("[+]");
            tongue_health_index = strarray[0];
            tongue_constitution = strarray[1];

            tx_tongue_constitution.setText(tongue_constitution);
            tx_tongue_health_index.setText(tongue_health_index);
        }
        if (Objects.equals(face, "暂无面象数据")){
            detail_face.setVisibility(View.INVISIBLE);
        }else {
            record_detail_face_hint.setVisibility(View.INVISIBLE);
            String[] strarray=face.split("[+]");
            face_health_index = strarray[0];

            tx_face_health_index.setText(face_health_index);
        }
        if (Objects.equals(lung, "暂无肺音数据")){
            detail_lung_volume.setVisibility(View.INVISIBLE);
        }else {
            record_detail_lung_volume_hint.setVisibility(View.INVISIBLE);
        }
    }

    private String replaceIllegalCharacter(String string){
        string = string.replace("[","");
        string = string.replace("]","");
        string = string.replace("\"","");

        return string;
    }
}