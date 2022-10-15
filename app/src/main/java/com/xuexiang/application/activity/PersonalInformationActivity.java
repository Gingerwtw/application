

package com.xuexiang.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xuexiang.application.R;

public class PersonalInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        SharedPreferences mShared = getSharedPreferences("information", MODE_PRIVATE);

        TextView name, phone, gender, birthday, profession, medical_history;
        ImageButton btn_back = findViewById(R.id.personal_information_toolbar_back);
        name = findViewById(R.id.personal_information_name);
        phone = findViewById(R.id.personal_information_phone);
        gender = findViewById(R.id.personal_information_gender);
        birthday = findViewById(R.id.personal_information_birthday);
        profession = findViewById(R.id.personal_information_profession);
        medical_history = findViewById(R.id.personal_information_medical_history);

        name.setText(mShared.getString("name",""));
        phone.setText(mShared.getString("phone",""));
        gender.setText(mShared.getString("gender",""));
        birthday.setText(mShared.getString("birthday",""));
        profession.setText(mShared.getString("profession",""));
        medical_history.setText(mShared.getString("medical_history",""));

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}