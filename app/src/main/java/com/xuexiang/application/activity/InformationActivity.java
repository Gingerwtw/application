/*
 * Copyright (C) 2022 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class InformationActivity extends AppCompatActivity {

    private EditText editText_name, editText_sex, editText_birth, editText_occupation, editText_disease_history;
    private Button btn_submit;
    private String name, gender, birthday, profession, medical_history;

    private DialogLoader mDialogLoader;
    private UrlUtils UrlInfo = new UrlUtils();

    private int SUBMIT_INFORMATION = 1;
    private int SUBMIT_INFORMATION_FAIL = 0;

    private SharedPreferences mShared; // 声明一个共享参数对象
    private MaterialDialog.Builder warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mShared = getSharedPreferences("information", MODE_PRIVATE);
        warningDialog = new MaterialDialog.Builder(InformationActivity.this)
                .title("错误")
                .positiveText("确认");

        editText_name = findViewById(R.id.et_name);
        editText_sex = findViewById(R.id.et_sex);
        editText_birth = findViewById(R.id.et_birth);
        editText_occupation = findViewById(R.id.et_occupation);
        editText_disease_history = findViewById(R.id.et_disease_history);

        editText_sex.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                showSingleChoiceDialog();}
            }
        });

        editText_birth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                showDateDialog();}
            }
        });

        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText_name.getText().toString();
                gender = editText_sex.getText().toString();
                birthday = editText_birth.getText().toString();
                profession = editText_occupation.getText().toString();
                medical_history = editText_disease_history.getText().toString();

                if (Objects.equals(name, "") || Objects.equals(gender, "") || Objects.equals(birthday, "")
                        || Objects.equals(profession, "") || Objects.equals(medical_history, "")){
                    XToastUtils.info("请将个人资料填写完毕");
                }
                else {
                    Log.d("information submit", name+gender+birthday+profession+medical_history);
                    String url = UrlInfo.getRegister();
                    if(url == null){
                        warningDialog.content("请先设置服务器地址").show();
                    }
                    else{
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                String result = ToJSON();

                                HttpReqData req = new HttpReqData();
                                req.url = url;
                                req.params = new StringBuffer(result);

                                HttpRespData resp_data = HttpRequestUtil.postData(req);
                                Log.d("information result", String.valueOf(resp_data));
                                String content = resp_data.content;
                                Log.d("information result", content);

                                Message message = Message.obtain();
                                try {
                                    JSONObject obj = new JSONObject(content);
                                    String status = obj.getString("message");

                                    if (status.equals("注册成功！")){
                                        message.what = SUBMIT_INFORMATION;
                                    }else{
                                        message.what = SUBMIT_INFORMATION_FAIL;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    message.what = SUBMIT_INFORMATION_FAIL;
                                }
                                mhandler.sendMessage(message);
                            }
                        }.start();
                    }

                }
            }
        });

        mDialogLoader = DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == SUBMIT_INFORMATION){
                Intent intent = new Intent(InformationActivity.this, QuestionnaireInfoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("information_result",message.getData().getString("content"));
//                bundle.putInt("select",message.getData().getInt("select"));
//                intent.putExtras(bundle);

                startActivity(intent);
            }
            else if (message.what == SUBMIT_INFORMATION_FAIL){
                warningDialog.content("注册失败，请检查网络连接或服务器地址是否正确").show();
            }
        }
    };

    private void showSingleChoiceDialog(){
        final String[] items = {"男","女"};
        mDialogLoader.showSingleChoiceDialog(InformationActivity.this
                ,"选择您的性别"
                ,items
                ,0
                ,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //点击确认按钮后，执行该方法
//                        XToast.normal(InformationActivity.this,"您选择了 "+items[i]).show();
                        editText_sex.setText(items[i]);
                    }
                },"确认"
                ,"关闭");
    }

    private void showDatePicker(){
        TimePickerView mDatePicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelected(Date date, View v) {
                ToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setTitleText("日期选择")
                .build();

    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(InformationActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint({"CheckResult", "SetTextI18n"})
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int m = monthOfYear + 1;
                XToast.normal(InformationActivity.this, year + "-" + m + "-" + dayOfMonth);
                Log.i("date", year + "-" + m + "-" + dayOfMonth);
                editText_birth.setText(year + "-" + m + "-" + dayOfMonth);
            }
        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public String ToJSON(){
        String result = "";
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",getIntent().getStringExtra("username"));
            jsonObject.put("password",getIntent().getStringExtra("password"));
            jsonObject.put("phone",getIntent().getStringExtra("phone"));
            jsonObject.put("name",name);
            jsonObject.put("gender",gender);
            jsonObject.put("birthday",birthday);
            jsonObject.put("profession",profession);
            jsonObject.put("medical_history",medical_history);

            result = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = mShared.edit();
        editor.putString("username",getIntent().getStringExtra("username"));
        editor.putString("phone",getIntent().getStringExtra("phone"));
        editor.putString("name", name);
        editor.putString("gender", gender);
        editor.putString("birthday", birthday);
        editor.putString("profession", profession);
        editor.putString("medical_history", medical_history);
        editor.apply();

        Log.d("information",result);
        return result;
    }
}