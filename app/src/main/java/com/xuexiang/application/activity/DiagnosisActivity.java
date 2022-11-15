

package com.xuexiang.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.application.R;

import java.util.Objects;

public class DiagnosisActivity extends AppCompatActivity {

    TextView health_index, constitution, hint;
    LinearLayout face_tongue;
    ImageButton btn_back;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        SharedPreferences tongueShared = getSharedPreferences("tongue", MODE_PRIVATE);
        SharedPreferences faceShared = getSharedPreferences("face", MODE_PRIVATE);

        health_index = findViewById(R.id.diagnosis_health_index_data);
        constitution = findViewById(R.id.diagnosis_constitution_data);
        hint = findViewById(R.id.diagnosis_face_tongue_hint);
        face_tongue = findViewById(R.id.diagnosis_face_tongue);
        btn_back = findViewById(R.id.diagnosis_toolbar_back);

        String tongue_health_index = tongueShared.getString("health_index","");
        String face_health_index = faceShared.getString("health_index","");
        Log.d("diagnosis", tongue_health_index+face_health_index);
        if (!Objects.equals(tongue_health_index, "") && !Objects.equals(face_health_index, "")){
            float health_index_data = (Float.parseFloat(tongue_health_index)+
                    Float.parseFloat(face_health_index))/2;

            health_index.setText(Float.toString(health_index_data));
            constitution.setText(tongueShared.getString("constitution",""));
            hint.setVisibility(View.INVISIBLE);
        }else{
            face_tongue.setVisibility(View.INVISIBLE);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}