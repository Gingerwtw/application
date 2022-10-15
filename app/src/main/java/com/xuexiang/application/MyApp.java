package com.xuexiang.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;

import com.xuexiang.application.utils.sdkinit.ANRWatchDogInit;
import com.xuexiang.application.utils.sdkinit.UMengInit;
import com.xuexiang.application.utils.sdkinit.XBasicLibInit;
import com.xuexiang.application.utils.sdkinit.XUpdateInit;

/**

 */
public class MyApp extends Application {
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLibs();

        mContext = getApplicationContext();
    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        // X系列基础库初始化
        XBasicLibInit.init(this);
        // 版本更新初始化
        XUpdateInit.init(this);
        // 运营统计数据
        UMengInit.init(this);
        // ANR监控
        ANRWatchDogInit.init();
    }

    // 在工具类中获取context
    public static Context getContext() {
        return mContext;
    }



    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }


}
