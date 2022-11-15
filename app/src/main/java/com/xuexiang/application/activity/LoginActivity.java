package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.database.UserDBHelper;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.display.Colors;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements OnClickListener, ClickUtils.OnClick2ExitListener {

    private EditText editText_username; // 声明一个编辑框对象
    private EditText editText_password; // 声明一个编辑框对象
    private Button btn_forget; // 声明一个按钮控件对象
    private Button btn_login;
    private Button btn_register;
    private LinearLayout linearLayout_login, linearLayout_change;
    private boolean bRemember = false; // 是否记住密码

    private String phone;
    private String password;

    private UserDBHelper mHelper; // 声明一个用户数据库的帮助器对象
    private SharedPreferences mShared; // 声明一个共享参数对象
    private UrlUtils UrlInfo = new UrlUtils();
    private MaterialDialog.Builder warningDialog;

    private final int SUBMIT_LOGIN = 1;
    private int SUBMIT_LOGIN_FAIL = 0;
    private final int SUBMIT_LOGIN_WRONG_SERVER = 2;

    private UrlUtils urlUtils = new UrlUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        openPage(LoginFragment.class, getIntent().getExtras());
        setContentView(R.layout.activity_login);
        mShared = getSharedPreferences("information", MODE_PRIVATE);

            editText_username = findViewById(R.id.et_username);
            editText_password = findViewById(R.id.et_password);
            btn_forget = (Button) findViewById(R.id.tv_forget_password);
            btn_register = (Button)findViewById(R.id.tv_register);
            btn_login = findViewById(R.id.btn_login);
            Button btn_change_url = findViewById(R.id.btn_change_url);
            btn_change_url.setOnClickListener(this);

            editText_username.setText(getIntent().getStringExtra("phone"));

//        editText_phone.addTextChangedListener(new HideTextWatcher(editText_phone));
//        editText_password.addTextChangedListener(new HideTextWatcher(editText_password));

            btn_forget.setOnClickListener(this);
            btn_login.setOnClickListener(this);
            btn_register.setOnClickListener(this);

            btn_forget.setVisibility(View.INVISIBLE);
//        btn_skip.setVisibility(View.INVISIBLE);

            warningDialog = new MaterialDialog.Builder(LoginActivity.this)
                    .title("错误")
                    .positiveText("确认");

    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == SUBMIT_LOGIN){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("information_result",message.getData().getString("content"));
//                bundle.putInt("select",message.getData().getInt("select"));
//                intent.putExtras(bundle);

                startActivity(intent);
            }
            else if (message.what == SUBMIT_LOGIN_FAIL){
//                warningDialog.content("登录失败").show();
                XToastUtils.error("用户名不存在或密码错误");
            }
            else if (message.what == SUBMIT_LOGIN_WRONG_SERVER){
//                warningDialog.content("登录失败").show();
                XToastUtils.error("服务器地址错误");
            }
        }
    };

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.tv_forget_password){
            phone = editText_username.getText().toString();
            Intent intent = new Intent(LoginActivity.this, LoginForgetActivity.class);
            intent.putExtra("phone", phone);
            startActivityForResult(intent, 1);
        }
        if (view.getId() == R.id.tv_register){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_login){
            phone = editText_username.getText().toString();
            if (urlUtils.getLogin() != null){
                login();
            }
            else{
                warningDialog.content("请先设置服务器地址").show();
            }
        }
        if (view.getId() == R.id.btn_change_url){
            Intent intent = new Intent(LoginActivity.this, ChangeUrlActivity.class);
            startActivity(intent);
        }
    }

    private void login(){
        password = editText_password.getText().toString();

        String result = ToJSON();

        new Thread() {
            @Override
            public void run() {
                super.run();

                HttpReqData req = new HttpReqData();
                req.url = UrlInfo.getLogin();
                req.params = new StringBuffer(result);

                HttpRespData resp_data = HttpRequestUtil.postData(req);
                Log.d("login result", String.valueOf(resp_data));
                String content = resp_data.content;
                Log.d("login result", content);


                Message message = Message.obtain();
                if (content.equals("")){
                    message.what = SUBMIT_LOGIN_WRONG_SERVER;
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(content);
                        String status = obj.getString("login_status");

                        SharedPreferences.Editor editor = mShared.edit();
                        editor.putString("phone",obj.getString("phone"));
                        editor.putString("name", obj.getString("name"));
                        editor.putString("gender", obj.getString("gender"));
                        editor.putString("birthday", obj.getString("birthday"));
                        editor.putString("profession", obj.getString("profession"));
                        editor.putString("medical_history", obj.getString("medical_history"));
                        editor.apply();

                        if (status.equals("success")){
                            message.what = SUBMIT_LOGIN;
                        }else{
                            message.what = SUBMIT_LOGIN_FAIL;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        message.what = SUBMIT_LOGIN_FAIL;
                    }
                }
                mhandler.sendMessage(message);
            }
        }.start();

//            UserInfo userInfo = mHelper.queryByPhone(phone);
//            if (userInfo == null){
//                XToastUtils.error("请输入正确的手机号");
//            }
//            else if (!userInfo.password.equals(password)) {
//                Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
//            } else { // 密码校验通过
//                Toast.makeText(this, "正确的密码", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
    }

    public String ToJSON(){
        String result = "";
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",phone);
            jsonObject.put("password",password);

            result = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("login",result);
        return result;
    }

    public void refresh(){
        onCreate(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 获得用户数据库帮助器的一个实例
        mHelper = UserDBHelper.getInstance(this, 2);
        // 恢复页面，则打开数据库连接
        mHelper.openWriteLink();
    }

    // 从修改密码页面返回登录页面，要清空密码的输入框
    @Override
    protected void onRestart() {
        editText_password.setText("");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    @Override
    protected void initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
//    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.exitApp();
    }

}
