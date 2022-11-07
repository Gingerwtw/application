

package com.xuexiang.application;

import android.content.SharedPreferences;
import com.xuexiang.application.MyApp;

import java.util.Objects;

public class UrlUtils {
    SharedPreferences mShared = MyApp.getContext().getSharedPreferences("information", MyApp.getContext().MODE_PRIVATE);
    String collect = mShared.getString("collect","");
    String analyze = mShared.getString("analyze","");

    public String get_calibrate() {
        String url = null;
        if (!Objects.equals(mShared.getString("collect", ""), ""))
            url = mShared.getString("collect","");
        return url;
    }

    public String get_analyze() {
        String url = null;
        if (!Objects.equals(mShared.getString("analyze", ""), ""))
            url = mShared.getString("analyze","");
        return url;
    }

    public String getFace_calibrate() {
//        return "http://10.249.45.227:8000/calibrate_face_1/";
        return  collect+"calibrate_face_1/";
    }
    public String getFace_analyze() {
        return analyze+"analyze_face_1/";
    }
    public String getTongue_calibrate() {
        return collect+"calibrate_tongue_1/";
    }
    public String getTongue_analyze() {
        return analyze+"analyze_tongue_1/";}
    public String getPulse_analyze() {
        return analyze+"analyze_pulse_1/";}
    public String getLogin() {
        return analyze+"login/";}
    public String getRegister() {
        return analyze+"register/";}
    public String getAddRecord() {
        return analyze+"add_diagnostic_record/";}
    public String getRecord() {
        return analyze+"get_diagnostic_record/";}
}
