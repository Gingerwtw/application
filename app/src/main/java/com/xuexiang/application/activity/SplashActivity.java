package com.xuexiang.application.activity;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;

import com.xuexiang.application.R;
import com.xuexiang.application.utils.SettingUtils;
import com.xuexiang.application.utils.TokenUtils;
import com.xuexiang.application.utils.Utils;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;
import com.xuexiang.xutil.app.ActivityUtils;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * 启动页【无需适配屏幕大小】
 *
 */
public class SplashActivity extends BaseSplashActivity implements CancelAdapt {

    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }
    private SharedPreferences mShared = getSharedPreferences("Url", MODE_PRIVATE);;

    /**
     * activity启动后的初始化
     */
    @Override
    protected void onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash);
        startSplash(false);
    }


    /**
     * 启动页结束后的动作
     */
    @Override
    protected void onSplashFinished() {
        if (SettingUtils.isAgreePrivacy()) {
            loginOrGoMainPage();
        } else {
            Utils.showPrivacyDialog(this, (dialog, which) -> {
                dialog.dismiss();
                SettingUtils.setIsAgreePrivacy(true);
                loginOrGoMainPage();
            });
        }
    }

    private void loginOrGoMainPage() {
        if (TokenUtils.hasToken()) {
            ActivityUtils.startActivity(LoginActivity.class);
        } else {
            ActivityUtils.startActivity(LoginActivity.class);
        }
//        if (mShared.getString("Opened","") == null){
//            Log.d("splash", "loginOrGoMainPage: Opened==null");
//            ActivityUtils.startActivity(ChangeUrlActivity.class);
//        }
//        else{
//            Log.d("splash", "loginOrGoMainPage: Opened=="+mShared.getString("Opened",""));
//            SharedPreferences.Editor editor = mShared.edit();
//            editor.putString("Opened","yes");
//            editor.apply();
//
//            ActivityUtils.startActivity(LoginActivity.class);
//        }
        finish();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
