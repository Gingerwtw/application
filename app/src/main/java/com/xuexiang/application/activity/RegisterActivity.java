package com.xuexiang.application.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.database.UserDBHelper;
import com.xuexiang.application.database.UserInfo;
import com.xuexiang.application.utils.DateUtil;
import com.xuexiang.application.utils.XToastUtils;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_register;
    private EditText editText_phone;
//    private EditText editText_name;
    private EditText editText_password;
    private EditText editText_password_confirm;

    private SharedPreferences mShared; // 声明一个共享参数对象
    private UserDBHelper mHelper; // 声明一个用户数据库的帮助器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_phone = findViewById(R.id.et_phone_number);
        editText_password = findViewById(R.id.et_password);
//        editText_name = findViewById(R.id.et_name);
        btn_register = findViewById(R.id.btn_register);
        editText_password_confirm = findViewById(R.id.et_password_confirm);

        btn_register.setOnClickListener(RegisterActivity.this);

        mShared = getSharedPreferences("share_login", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获得用户数据库帮助器的一个实例
        mHelper = UserDBHelper.getInstance(this, 2);
        // 恢复页面，则打开数据库连接
        mHelper.openWriteLink();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    public void onClick(View view){

        String phone = editText_phone.getText().toString();
//        String name = editText_name.getText().toString();
        String password = editText_password.getText().toString();
        String password_confirm = editText_password_confirm.getText().toString();

//        UserInfo checkInfo = mHelper.queryByPhone(phone);
//        if (checkInfo != null){
////            Toast.makeText(this, "该手机号已注册", Toast.LENGTH_SHORT).show();
//            XToastUtils.info("该手机号已注册");
//        }
//        else {
//            if (name.length() == 0) {
//                XToastUtils.error("用户名不能为空");
//            }
//            else
            if (phone.length() == 0){
                XToastUtils.error("手机号不能为空");
            }
            else if (phone.length() != 11){
                XToastUtils.error("请输入正确的11位手机号");
            }
            else if (password.length() == 0){
                XToastUtils.error("请输入密码");
            }
            else if (password_confirm.length() == 0){
                XToastUtils.error("请再次输入密码");
            }
            else if (!password_confirm.equals(password)){
                XToastUtils.error("两次输入的密码不一致");
            }
            else {
                UserInfo info = new UserInfo();

//                info.name = name;
                info.phone = phone;
                info.password = password;
                info.update_time = DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss");
                mHelper.insert(info);

                Intent intent = new Intent(RegisterActivity.this, InformationActivity.class);
                intent.putExtra("phone", phone);
//                intent.putExtra("username",name);
                intent.putExtra("password",password);

                startActivityForResult(intent, 1);
            }
//        }
    }
}