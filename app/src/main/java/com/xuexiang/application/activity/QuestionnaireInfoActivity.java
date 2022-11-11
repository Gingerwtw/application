

package com.xuexiang.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xuexiang.application.R;

public class QuestionnaireInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_info);

        Button btn_confirm = findViewById(R.id.questionnaire_info_confirm);
        Button btn_jump = findViewById(R.id.questionnaire_info_jump);
        btn_confirm.setOnClickListener(this);
        btn_jump.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.questionnaire_info_confirm){
            Intent intent = new Intent(this, QuestionnaireActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}