package com.xuexiang.application.database;

public class UserInfo {
    public long rowid;
    public String name;
//    public int age;
//    public long height;
//    public float weight;
//    public boolean married;
    public String update_time;
    public String phone;
    public String password;

    public UserInfo() {
        rowid = 0L;
        name = "";
        update_time = "";
        phone = "";
        password = "";
//        age = 0;
//        height = 0L;
//        weight = 0.0f;
//        married = false;
//        xuhao = 0;
    }
}
