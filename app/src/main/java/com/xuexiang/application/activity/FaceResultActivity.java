

package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class FaceResultActivity extends BaseActivity implements View.OnClickListener {
    private JSONObject middle_top;
    private JSONObject middle_middle;
    private JSONObject middle_bottom;
    private JSONObject left;
    private JSONObject right;
    private String health_index;
    private String correct_face_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_result);

        initResult();
        initMiddleTop();
        initMiddleMiddle();
        initMiddleBottom();
        initLeft();
        initRight();

        ImageView face_result_image = findViewById(R.id.face_image);
        Bitmap bitmap = Base64tobitmep(correct_face_img);
        face_result_image.setImageBitmap(bitmap);
        face_result_image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        ImageButton btn_back = findViewById(R.id.face_result_toolbar_back);
        btn_back.setOnClickListener(this);

        TextView tv_face_result_health_index = findViewById(R.id.face_result_health_index);
        tv_face_result_health_index.setText(health_index);
    }

    private void initResult(){
        Bundle bundle=getIntent().getExtras();

        String respondJson = bundle.getString("face_result");
        try {
            JSONObject obj = new JSONObject(respondJson);
            middle_top = obj.getJSONObject("middle_top");
            middle_middle = obj.getJSONObject("middle_middle");
            middle_bottom = obj.getJSONObject("middle_bottom");
            left = obj.getJSONObject("left");
            right = obj.getJSONObject("right");
            health_index = obj.getString("health_index");
            correct_face_img = obj.getString("correct_face_img");

            Log.d("health_index",health_index);
            Log.d("color_middle_top", String.valueOf(middle_top));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            tv_face_middle_top_color1.setText((int)middle_top.getDouble("black") +"%");
            tv_face_middle_top_color2.setText((int)middle_top.getDouble("yellow") +"%");
            tv_face_middle_top_color3.setText((int)middle_top.getDouble("light_yellow") +"%");
            tv_face_middle_top_color4.setText((int)middle_top.getDouble("gray") +"%");
            tv_face_middle_top_color5.setText((int)middle_top.getDouble("deep_red") +"%");
            tv_face_middle_top_color6.setText((int)middle_top.getDouble("red") +"%");
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
            tv_face_middle_middle_color1.setText((int)middle_middle.getDouble("black") +"%");
            tv_face_middle_middle_color2.setText((int)middle_middle.getDouble("yellow") +"%");
            tv_face_middle_middle_color3.setText((int)middle_middle.getDouble("light_yellow") +"%");
            tv_face_middle_middle_color4.setText((int)middle_middle.getDouble("gray") +"%");
            tv_face_middle_middle_color5.setText((int)middle_middle.getDouble("deep_red") +"%");
            tv_face_middle_middle_color6.setText((int)middle_middle.getDouble("red") +"%");
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
            tv_face_middle_bottom_color1.setText((int)middle_bottom.getDouble("black") +"%");
            tv_face_middle_bottom_color2.setText((int)middle_bottom.getDouble("yellow") +"%");
            tv_face_middle_bottom_color3.setText((int)middle_bottom.getDouble("light_yellow") +"%");
            tv_face_middle_bottom_color4.setText((int)middle_bottom.getDouble("gray") +"%");
            tv_face_middle_bottom_color5.setText((int)middle_bottom.getDouble("deep_red") +"%");
            tv_face_middle_bottom_color6.setText((int)middle_bottom.getDouble("red") +"%");
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
            tv_face_left_color1.setText((int)left.getDouble("black") +"%");
            tv_face_left_color2.setText((int)left.getDouble("yellow") +"%");
            tv_face_left_color3.setText((int)left.getDouble("light_yellow") +"%");
            tv_face_left_color4.setText((int)left.getDouble("gray") +"%");
            tv_face_left_color5.setText((int)left.getDouble("deep_red") +"%");
            tv_face_left_color6.setText((int)left.getDouble("red") +"%");
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
            tv_face_right_color1.setText((int)right.getDouble("black") +"%");
            tv_face_right_color2.setText((int)right.getDouble("yellow") +"%");
            tv_face_right_color3.setText((int)right.getDouble("light_yellow") +"%");
            tv_face_right_color4.setText((int)right.getDouble("gray") +"%");
            tv_face_right_color5.setText((int)right.getDouble("deep_red") +"%");
            tv_face_right_color6.setText((int)right.getDouble("red") +"%");
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
    }
}