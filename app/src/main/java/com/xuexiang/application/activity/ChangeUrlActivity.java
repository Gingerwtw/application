

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

public class ChangeUrlActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText collect;
    private EditText analyse;
    private ImageButton btn_back;
    private SharedPreferences mShared;
    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象

    private String collect_text, analyse_text;
    private URLInfo collect_info, analyze_info;

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
//        if (!Objects.equals(mShared.getString("collect", ""), "")){
//            collect.setText(mShared.getString("collect",""));
//        }
//        if (!Objects.equals(mShared.getString("analyze", ""), "")){
//            analyse.setText(mShared.getString("analyze",""));
//        }
        mHelper = URLDBHelper.getInstance(this, 2);
        mHelper.openWriteLink();

        collect_info = mHelper.queryByUsage("0");
        analyze_info = mHelper.queryByUsage("1");

        if (collect_info != null){
            collect.setText(collect_info.User_URL);
        }
        if (analyze_info != null){
            analyse.setText(analyze_info.User_URL);
        }

    }

    private void updateURL(URLInfo urlInfo, String url, String usage) {
        if (urlInfo == null){
            urlInfo = new URLInfo();
            urlInfo.User_URL = url;
            urlInfo.usage = usage;
            mHelper.insert(urlInfo);
        }else{
            urlInfo.User_URL = url;
            mHelper.update(urlInfo);
        }
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.change_url_toolbar_back) {
            XToastUtils.info("设置成功");

            collect_text = collect.getText().toString();
            analyse_text = analyse.getText().toString();

//            SharedPreferences.Editor editor = mShared.edit();
//            editor.putString("collect", collect_text);
//            editor.putString("analyze", analyse_text);
//            editor.apply();

            updateURL(collect_info,collect_text,"0");
            updateURL(analyze_info, analyse_text, "1");
        }
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获得用户数据库帮助器的一个实例
        mHelper = URLDBHelper.getInstance(this, 1);
        // 恢复页面，则打开数据库连接
        mHelper.openWriteLink();
//        initEditText();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
        mHelper.closeLink();
    }
}