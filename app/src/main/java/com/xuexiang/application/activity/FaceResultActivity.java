

package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.application.MyApp;
import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.utils.ImageSaveUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FaceResultActivity extends BaseActivity implements View.OnClickListener {
//    private JSONObject middle_top;
//    private JSONObject middle_middle;
//    private JSONObject middle_bottom;
//    private JSONObject left;
//    private JSONObject right;
    private String health_index;
    private String correct_face_img;
    private LinearLayout report;

    private String middle_top,middle_middle, middle_bottom, left, right;

    private MaterialDialog.Builder warningDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_result);
        SharedPreferences imageShared = getSharedPreferences("image", MODE_PRIVATE);

        initResult();
        initMiddleTop();
        initMiddleMiddle();
        initMiddleBottom();
        initLeft();
        initRight();

        ImageView face_result_image = findViewById(R.id.face_image);
//        Bitmap bitmap = Base64tobitmep(correct_face_img);
        Bitmap bitmap = Base64tobitmep(imageShared.getString("image",""));
        face_result_image.setImageBitmap(bitmap);
        face_result_image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        ImageButton btn_back = findViewById(R.id.face_result_toolbar_back);
        report = findViewById(R.id.face_result_report);
        Button btn_save = findViewById(R.id.btn_face_result_save);
        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        TextView tv_face_result_health_index = findViewById(R.id.face_result_health_index);
        tv_face_result_health_index.setText(health_index);

        warningDialog = new MaterialDialog.Builder(FaceResultActivity.this).positiveText("确认");
    }

    private void initResult(){
        Bundle bundle=getIntent().getExtras();
        middle_top = bundle.getString("middle_top");
        middle_middle = bundle.getString("middle_middle");
        middle_bottom = bundle.getString("middle_bottom");
        left = bundle.getString("left");
        right = bundle.getString("right");
        health_index = bundle.getString("health_index");
//
//        String respondJson = bundle.getString("face_result");
//        try {
//            JSONObject obj = new JSONObject(respondJson);
//            middle_top = obj.getJSONObject("middle_top");
//            middle_middle = obj.getJSONObject("middle_middle");
//            middle_bottom = obj.getJSONObject("middle_bottom");
//            left = obj.getJSONObject("left");
//            right = obj.getJSONObject("right");
//            health_index = obj.getString("health_index");
//            correct_face_img = obj.getString("correct_face_img");
//
//            Log.d("health_index",health_index);
//            Log.d("color_middle_top", String.valueOf(middle_top));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @SuppressLint("SetTextI18n")
    private void initMiddleTop(){
        TextView tv_face_middle_top_color1 = findViewById(R.id.tv_face_middle_top_color1);
        TextView tv_face_middle_top_color2 = findViewById(R.id.tv_face_middle_top_color2);
        TextView tv_face_middle_top_color3 = findViewById(R.id.tv_face_middle_top_color3);
        TextView tv_face_middle_top_color4 = findViewById(R.id.tv_face_middle_top_color4);
        TextView tv_face_middle_top_color5 = findViewById(R.id.tv_face_middle_top_color5);
        TextView tv_face_middle_top_color6 = findViewById(R.id.tv_face_middle_top_color6);

        try {
            JSONObject obj = new JSONObject(middle_top);
            tv_face_middle_top_color1.setText((int)obj.getDouble("black") +"%");
            tv_face_middle_top_color2.setText((int)obj.getDouble("yellow") +"%");
            tv_face_middle_top_color3.setText((int)obj.getDouble("light_yellow") +"%");
            tv_face_middle_top_color4.setText((int)obj.getDouble("gray") +"%");
            tv_face_middle_top_color5.setText((int)obj.getDouble("deep_red") +"%");
            tv_face_middle_top_color6.setText((int)obj.getDouble("red") +"%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initMiddleMiddle(){
        TextView tv_face_middle_middle_color1 = findViewById(R.id.tv_face_middle_middle_color1);
        TextView tv_face_middle_middle_color2 = findViewById(R.id.tv_face_middle_middle_color2);
        TextView tv_face_middle_middle_color3 = findViewById(R.id.tv_face_middle_middle_color3);
        TextView tv_face_middle_middle_color4 = findViewById(R.id.tv_face_middle_middle_color4);
        TextView tv_face_middle_middle_color5 = findViewById(R.id.tv_face_middle_middle_color5);
        TextView tv_face_middle_middle_color6 = findViewById(R.id.tv_face_middle_middle_color6);

        try {
            JSONObject obj = new JSONObject(middle_middle);
            tv_face_middle_middle_color1.setText((int)obj.getDouble("black") +"%");
            tv_face_middle_middle_color2.setText((int)obj.getDouble("yellow") +"%");
            tv_face_middle_middle_color3.setText((int)obj.getDouble("light_yellow") +"%");
            tv_face_middle_middle_color4.setText((int)obj.getDouble("gray") +"%");
            tv_face_middle_middle_color5.setText((int)obj.getDouble("deep_red") +"%");
            tv_face_middle_middle_color6.setText((int)obj.getDouble("red") +"%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initMiddleBottom(){
        TextView tv_face_middle_bottom_color1 = findViewById(R.id.tv_face_middle_bottom_color1);
        TextView tv_face_middle_bottom_color2 = findViewById(R.id.tv_face_middle_bottom_color2);
        TextView tv_face_middle_bottom_color3 = findViewById(R.id.tv_face_middle_bottom_color3);
        TextView tv_face_middle_bottom_color4 = findViewById(R.id.tv_face_middle_bottom_color4);
        TextView tv_face_middle_bottom_color5 = findViewById(R.id.tv_face_middle_bottom_color5);
        TextView tv_face_middle_bottom_color6 = findViewById(R.id.tv_face_middle_bottom_color6);

        try {
            JSONObject obj = new JSONObject(middle_bottom);
            tv_face_middle_bottom_color1.setText((int)obj.getDouble("black") +"%");
            tv_face_middle_bottom_color2.setText((int)obj.getDouble("yellow") +"%");
            tv_face_middle_bottom_color3.setText((int)obj.getDouble("light_yellow") +"%");
            tv_face_middle_bottom_color4.setText((int)obj.getDouble("gray") +"%");
            tv_face_middle_bottom_color5.setText((int)obj.getDouble("deep_red") +"%");
            tv_face_middle_bottom_color6.setText((int)obj.getDouble("red") +"%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initLeft(){
        TextView tv_face_left_color1 = findViewById(R.id.tv_face_left_color1);
        TextView tv_face_left_color2 = findViewById(R.id.tv_face_left_color2);
        TextView tv_face_left_color3 = findViewById(R.id.tv_face_left_color3);
        TextView tv_face_left_color4 = findViewById(R.id.tv_face_left_color4);
        TextView tv_face_left_color5 = findViewById(R.id.tv_face_left_color5);
        TextView tv_face_left_color6 = findViewById(R.id.tv_face_left_color6);

        try {
            JSONObject obj = new JSONObject(left);
            tv_face_left_color1.setText((int)obj.getDouble("black") +"%");
            tv_face_left_color2.setText((int)obj.getDouble("yellow") +"%");
            tv_face_left_color3.setText((int)obj.getDouble("light_yellow") +"%");
            tv_face_left_color4.setText((int)obj.getDouble("gray") +"%");
            tv_face_left_color5.setText((int)obj.getDouble("deep_red") +"%");
            tv_face_left_color6.setText((int)obj.getDouble("red") +"%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initRight(){
        TextView tv_face_right_color1 = findViewById(R.id.tv_face_right_color1);
        TextView tv_face_right_color2 = findViewById(R.id.tv_face_right_color2);
        TextView tv_face_right_color3 = findViewById(R.id.tv_face_right_color3);
        TextView tv_face_right_color4 = findViewById(R.id.tv_face_right_color4);
        TextView tv_face_right_color5 = findViewById(R.id.tv_face_right_color5);
        TextView tv_face_right_color6 = findViewById(R.id.tv_face_right_color6);

        try {
            JSONObject obj = new JSONObject(right);
            tv_face_right_color1.setText((int)obj.getDouble("black") +"%");
            tv_face_right_color2.setText((int)obj.getDouble("yellow") +"%");
            tv_face_right_color3.setText((int)obj.getDouble("light_yellow") +"%");
            tv_face_right_color4.setText((int)obj.getDouble("gray") +"%");
            tv_face_right_color5.setText((int)obj.getDouble("deep_red") +"%");
            tv_face_right_color6.setText((int)obj.getDouble("red") +"%");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Bitmap Base64tobitmep(String base){
        Bitmap bitmap = null;
        try {
            byte[] bitmapByte = Base64.decode(base, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.face_result_toolbar_back){
//            Intent intent = new Intent(FaceResultActivity.this, FaceActivity.class);
//            startActivityForResult(intent,200);
            finish();
        }
        else if (view.getId() == R.id.btn_face_result_save){
            Boolean save_result = screenshotView();
            if (save_result){
                warningDialog.title("成功").content("分析报告保存成功，可在相册中查看").show();
            }else{
                warningDialog.title("错误").content("分析报告保存失败").show();
            }
        }
    }

    private Boolean screenshotView() {
//        View view = getWindow().getDecorView(); //截取当前屏幕
        View view = report;
        view.setBackgroundResource(R.drawable.lung_background);
//        view.setDrawingCacheEnabled(true); // 设置缓存，可用于实时截图
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        view.setBackgroundResource(0);
//        view.setDrawingCacheEnabled(false); // 清空缓存，可用于实时截图

        return ImageSaveUtil.saveAlbum(this, bitmap, Bitmap.CompressFormat.JPEG, 80, true);
    }
}