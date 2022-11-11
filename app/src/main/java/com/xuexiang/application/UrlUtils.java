

package com.xuexiang.application;

import android.content.SharedPreferences;
import com.xuexiang.application.MyApp;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.database.URLInfo;

import java.util.Objects;

public class UrlUtils {
    SharedPreferences mShared = MyApp.getContext().getSharedPreferences("information", MyApp.getContext().MODE_PRIVATE);
    String collect = mShared.getString("collect","");
    String analyze = mShared.getString("analyze","");

    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象
    private URLInfo collect_info, analyze_info;

//    public String get_calibrate() {
////        String url = null;
////        if (!Objects.equals(mShared.getString("collect", ""), ""))
////            url = mShared.getString("collect","");
////        return url;
//
//        mHelper = URLDBHelper.getInstance(MyApp.getContext(), 2);
//        mHelper.openWriteLink();
//
//        collect_info = mHelper.queryByUsage("0");
//        if (checkURLInfo(collect_info)){
//            return collect_info.User_URL;
//        }else {
//            return null;
//        }
//    }

//    public String get_analyze() {
////        String url = null;
////        if (!Objects.equals(mShared.getString("analyze", ""), ""))
////            url = mShared.getString("analyze","");
////        return url;
//
//        mHelper = URLDBHelper.getInstance(MyApp.getContext(), 2);
//        mHelper.openWriteLink();
//
//        analyze_info = mHelper.queryByUsage("1");
//
//        if (checkURLInfo(analyze_info)){
//            return analyze_info.User_URL;
//        }else {
//            return null;
//        }
//    }

    public String getFace_calibrate() {
//        return "http://10.249.45.227:8000/calibrate_face_1/";
        if (Boolean.TRUE.equals(checkURLInfo("0"))){
            return collect_info.User_URL+"calibrate_face_1/";
        }else {
            return null;
        }
    }
    public String getFace_analyze() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"analyze_face_1/";
        }else {
            return null;
        }
    }
    public String getTongue_calibrate() {
        if (checkURLInfo("0")){
            return collect_info.User_URL+"calibrate_tongue_1/";
        }else {
            return null;
        }
    }
    public String getTongue_analyze() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"analyze_tongue_1/";
        }else {
            return null;
        }
    }
    public String getPulse_analyze() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"analyze_pulse_1/";
        }else {
            return null;
        }
    }
    public String getLogin() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"login/";
        }else {
            return null;
        }
    }
    public String getRegister() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"register/";
        }else {
            return null;
        }
    }
    public String getAddRecord() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"add_diagnostic_record/";
        }else {
            return null;
        }
    }
    public String getRecord() {
        if (checkURLInfo("1")){
            return analyze_info.User_URL+"get_diagnostic_record/";
        }else {
            return null;
        }
    }

    private Boolean checkURLInfo(String usage){
        mHelper = URLDBHelper.getInstance(MyApp.getContext(), 2);
        mHelper.openWriteLink();

        if (Objects.equals(usage, "0")){
            collect_info = mHelper.queryByUsage("0");
            return collect_info != null;
        }
        else if(Objects.equals(usage, "1")){
            analyze_info = mHelper.queryByUsage("1");
            return analyze_info != null;
        }
        return false;
    }
}
