

package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.application.R;
import com.xuexiang.application.utils.XToastUtils;

public class QuestionnaireActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private String first = null, second, third, forth, fifth, sixth, seventh, eighth, ninth, tenth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        RadioGroup radioGroup_first = findViewById(R.id.radioGroup_first);
        RadioGroup radioGroup_second = findViewById(R.id.radioGroup_second);
        RadioGroup radioGroup_third = findViewById(R.id.radioGroup_third);
        RadioGroup radioGroup_forth = findViewById(R.id.radioGroup_forth);
        RadioGroup radioGroup_fifth = findViewById(R.id.radioGroup_fifth);
        RadioGroup radioGroup_sixth = findViewById(R.id.radioGroup_sixth);
        RadioGroup radioGroup_seventh = findViewById(R.id.radioGroup_seventh);
        RadioGroup radioGroup_eighth = findViewById(R.id.radioGroup_eighth);
        RadioGroup radioGroup_ninth = findViewById(R.id.radioGroup_ninth);
        RadioGroup radioGroup_tenth = findViewById(R.id.radioGroup_tenth);

        radioGroup_first.setOnCheckedChangeListener(this);
        radioGroup_second.setOnCheckedChangeListener(this);
        radioGroup_third.setOnCheckedChangeListener(this);
        radioGroup_forth.setOnCheckedChangeListener(this);
        radioGroup_fifth.setOnCheckedChangeListener(this);
        radioGroup_sixth.setOnCheckedChangeListener(this);
        radioGroup_seventh.setOnCheckedChangeListener(this);
        radioGroup_eighth.setOnCheckedChangeListener(this);
        radioGroup_ninth.setOnCheckedChangeListener(this);
        radioGroup_tenth.setOnCheckedChangeListener(this);

        ImageButton btn_back = findViewById(R.id.questionnaire_toolbar_back);
        Button btn_submit = findViewById(R.id.btn_questionnaire_submit);

        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.radio_first_yes:
                first = "yes";
                break;
            case R.id.radio_first_no:
                first = "no";
                break;
            case R.id.radio_second_yes:
                second = "yes";
                break;
            case R.id.radio_second_no:
                second = "no";
                break;
            case R.id.radio_third_yes:
                third = "yes";
                break;
            case R.id.radio_third_no:
                third = "no";
                break;
            case R.id.radio_forth_yes:
                forth = "yes";
                break;
            case R.id.radio_forth_no:
                forth = "no";
                break;
            case R.id.radio_fifth_yes:
                fifth = "yes";
                break;
            case R.id.radio_fifth_no:
                fifth = "no";
                break;
            case R.id.radio_sixth_yes:
                sixth = "yes";
                break;
            case R.id.radio_sixth_no:
                sixth = "no";
                break;
            case R.id.radio_seventh_yes:
                seventh = "yes";
                break;
            case R.id.radio_seventh_no:
                seventh = "no";
                break;
            case R.id.radio_eighth_yes:
                eighth = "yes";
                break;
            case R.id.radio_eighth_no:
                eighth = "no";
                break;
            case R.id.radio_ninth_yes:
                ninth = "yes";
                break;
            case R.id.radio_ninth_no:
                ninth = "no";
                break;
            case R.id.radio_tenth_yes:
                tenth = "yes";
                break;
            case R.id.radio_tenth_no:
                tenth = "no";
                break;
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.questionnaire_toolbar_back){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_questionnaire_submit){
//            XToastUtils.info("提交");
            if (first == null || second == null || third == null  || forth == null || fifth == null
                    || sixth == null || seventh == null || eighth == null || ninth == null || tenth == null){
                XToastUtils.info("请将问卷填写完毕");
            }
            else {
                Log.d("questionnaire", first+second+third+forth+fifth+sixth+seventh+eighth+ninth+tenth);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}