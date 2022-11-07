

package com.xuexiang.application.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xuexiang.application.R;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.database.URLInfo;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;

import java.util.Objects;

public class ChangeUrlActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText collect;
    private EditText analyse;
    private ImageButton btn_back;
    private SharedPreferences mShared;

    String collect_text, analyse_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_url);
        mShared = getSharedPreferences("information", MODE_PRIVATE);

        Button btn_confirm = findViewById(R.id.change_url_confirm);
        btn_back = findViewById(R.id.change_url_toolbar_back);
        collect = findViewById(R.id.collect_url);
        analyse = findViewById(R.id.analyse_url);

        initEditText();
        btn_confirm.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void initEditText(){
        if (!Objects.equals(mShared.getString("collect", ""), "")){
            collect.setText(mShared.getString("collect",""));
        }
        if (!Objects.equals(mShared.getString("analyze", ""), "")){
            analyse.setText(mShared.getString("analyze",""));
        }
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.change_url_toolbar_back) {
            XToastUtils.info("设置成功");

            collect_text = collect.getText().toString();
            analyse_text = analyse.getText().toString();

            SharedPreferences.Editor editor = mShared.edit();
            editor.putString("collect", collect_text);
            editor.putString("analyze", analyse_text);

            editor.apply();
        }
        finish();
    }
}