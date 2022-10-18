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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.utils.BitmapUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class TongueResultActivity extends BaseActivity implements View.OnClickListener {

    private Button show_coating;
    private Button hide_coating;

    private JSONObject substance;
    private JSONObject coating;
    private String health_index;
    private String coating_img;
    private String constitution;
    private String advice;

    private Bitmap originBitmap;

    private Bundle TongueColorBundle;
    private Bundle faceVeinBundle;
    private Bundle faceResultBundle;

    String[] tongue_substance_color = new String[8];
    String[] tongue_substance_color_name = new String[8];
    String[] tongue_coating_color = new String[4];
    String[] tongue_coating_color_name = new String[4];
    int tongue_substance_color_count = 0;
    int tongue_coating_color_count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_result);

        initResult();
        initView();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tongue_result_toolbar_back){
//            Intent intent = new Intent(TongueResultActivity.this, TongueActivity.class);
//            startActivityForResult(intent,200);
            finish();
        }
    }

    private void initResult(){
        Bundle bundle=getIntent().getExtras();

        String respondJson = bundle.getString("tongue_result");
        try {
            JSONObject obj = new JSONObject(respondJson);
            substance = obj.getJSONObject("substance");
            coating = obj.getJSONObject("coating");
            health_index = obj.getString("health_index");
            coating_img = obj.getString("coating_img");
            constitution = obj.getString("constitution");
            advice = obj.getString("advice");

            Log.d("health_index",health_index);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        ImageButton btn_back = findViewById(R.id.tongue_result_toolbar_back);
        btn_back.setOnClickListener(this);

        ImageView tongue_result_image = findViewById(R.id.tongue_image);
        Bitmap resultBitmap = Base64tobitmep(coating_img);
        originBitmap = BitmapUtil.openBitmap(getIntent().getStringExtra("path"));
        tongue_result_image.setImageBitmap(resultBitmap);

        TextView tv_tongue_substance_color_type1 = findViewById(R.id.tongue_substance_color_type_value1);
        TextView tv_tongue_substance_color_type2 = findViewById(R.id.tongue_substance_color_type_value2);
        TextView tv_tongue_substance_color_type3 = findViewById(R.id.tongue_substance_color_type_value3);
        TextView tv_tongue_substance_color_value1 = findViewById(R.id.tongue_substance_color_percent_value1);
        TextView tv_tongue_substance_color_value2 = findViewById(R.id.tongue_substance_color_percent_value2);
        TextView tv_tongue_substance_color_value3 = findViewById(R.id.tongue_substance_color_percent_value3);

        TextView tv_tongue_coating_color_type1 = findViewById(R.id.tongue_coating_color_type_value1);
        TextView tv_tongue_coating_color_type2 = findViewById(R.id.tongue_coating_color_type_value2);
        TextView tv_tongue_coating_color_type3 = findViewById(R.id.tongue_coating_color_type_value3);
        TextView tv_tongue_coating_color_value1 = findViewById(R.id.tongue_coating_color_percent_value1);
        TextView tv_tongue_coating_color_value2 = findViewById(R.id.tongue_coating_color_percent_value2);
        TextView tv_tongue_coating_color_value3 = findViewById(R.id.tongue_coating_color_percent_value3);

        handleJSON();

        tv_tongue_substance_color_type1.setText(tongue_substance_color_name[0]);
        tv_tongue_substance_color_value1.setText(tongue_substance_color[0]);
        if (tongue_substance_color_count > 1){
            tv_tongue_substance_color_type2.setText(tongue_substance_color_name[1]);
            tv_tongue_substance_color_value2.setText(tongue_substance_color[1]);
        }
        if (tongue_substance_color_count > 2){
            tv_tongue_substance_color_type3.setText(tongue_substance_color_name[2]);
            tv_tongue_substance_color_value3.setText(tongue_substance_color[2]);
        }

        tv_tongue_coating_color_type1.setText(tongue_coating_color_name[0]);
        tv_tongue_coating_color_value1.setText(tongue_coating_color[0]);
        if (tongue_coating_color_count > 1){
            tv_tongue_coating_color_type2.setText(tongue_coating_color_name[1]);
            tv_tongue_coating_color_value2.setText(tongue_coating_color[1]);
        }
        if (tongue_coating_color_count > 2){
            tv_tongue_coating_color_type3.setText(tongue_coating_color_name[2]);
            tv_tongue_coating_color_value3.setText(tongue_coating_color[2]);
        }

        TextView tv_tongue_health_index = findViewById(R.id.tongue_result_health_index);
        tv_tongue_health_index.setText(health_index);

        TextView tv_tongue_constitution = findViewById(R.id.tongue_result_constitution);
        tv_tongue_constitution.setText(constitution);

        TextView tv_tongue_advice = findViewById(R.id.tongue_result_advice);
        tv_tongue_advice.setText(advice);
    }

    @SuppressLint("DefaultLocale")
    private void handleJSON(){
        try {
            if (substance.has("Cyan")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Cyan")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "青色";
                tongue_substance_color_count++;
            }
            if (substance.has("Red")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Red")) +"%";
//                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Red")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "青色";
                tongue_substance_color_count++;
            }
            if (substance.has("Light purple")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Light purple")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "淡紫色";
                tongue_substance_color_count++;
            }
            if (substance.has("Deep red")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Deep red")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "绛红色";
                tongue_substance_color_count++;
            }
            if (substance.has("Light red")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Light red")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "淡红色";
                tongue_substance_color_count++;
            }
            if (substance.has("Blue")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Blue")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "蓝色";
                tongue_substance_color_count++;
            }
            if (substance.has("Light blue")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Light blue")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "淡蓝色";
                tongue_substance_color_count++;
            }
            if (substance.has("Purple")){
                tongue_substance_color[tongue_substance_color_count] = String.format("%.2f",substance.getDouble("Purple")) +"%";
                tongue_substance_color_name[tongue_substance_color_count] = "紫色";
                tongue_substance_color_count++;
            }

            if(coating.has("Black")){
                tongue_coating_color[tongue_coating_color_count] = String.format("%.2f",coating.getDouble("Black"))+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "黑色";
                tongue_coating_color_count++;
            }
            if(coating.has("Gray")){
                tongue_coating_color[tongue_coating_color_count] = String.format("%.2f",coating.getDouble("Gray"))+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "灰色";
                tongue_coating_color_count++;
            }
            if(coating.has("White")){
                tongue_coating_color[tongue_coating_color_count] = String.format("%.2f",coating.getDouble("White"))+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "白色";
                tongue_coating_color_count++;
            }
            if(coating.has("Yellow")){
                tongue_coating_color[tongue_coating_color_count] = String.format("%.2f",coating.getDouble("Yellow"))+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "黄色";
                tongue_coating_color_count++;
            }
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
}
