

package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.utils.BitmapUtil;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.ByteArrayOutputStream;

public class ComprehensiveAnalyzeActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_questionnaire, btn_face, btn_tongue, btn_lung, btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_analyze);


        btn_questionnaire = findViewById(R.id.comprehensive_btn_questionnaire);
        btn_face = findViewById(R.id.comprehensive_btn_face);
        btn_tongue = findViewById(R.id.comprehensive_btn_tongue);
        btn_lung = findViewById(R.id.comprehensive_btn_lung_volume);
        btn_submit = findViewById(R.id.comprehensive_btn_submit);


        btn_questionnaire.setOnClickListener(this);
        btn_face.setOnClickListener(this);
        btn_tongue.setOnClickListener(this);
        btn_lung.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;

        switch (id){
            case R.id.comprehensive_btn_questionnaire:
                intent = new Intent(ComprehensiveAnalyzeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.comprehensive_btn_face:
                intent = new Intent(ComprehensiveAnalyzeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.comprehensive_btn_tongue:
                intent = new Intent(ComprehensiveAnalyzeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.comprehensive_btn_lung_volume:
                intent = new Intent(ComprehensiveAnalyzeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.comprehensive_btn_submit:
                intent = new Intent(ComprehensiveAnalyzeActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}