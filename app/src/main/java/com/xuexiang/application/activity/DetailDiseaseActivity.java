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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuexiang.application.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DetailDiseaseActivity extends AppCompatActivity {

    String []JsonTag = new String[18];
    TextView[]textViews = new TextView[18];
    LinearLayout[]linearLayouts = new LinearLayout[18];

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_disease);

        initView();

        Bundle bundle=getIntent().getExtras();
        String []disease_info = new String[18];
        String request_content = bundle.getString("tongue_result_constitution");
        String disease = getRawTxtFileContent(this, request_content);
        try {
            JSONObject obj = new JSONObject(disease);

            for (int i = 0; i < JsonTag.length; i++) {
                disease_info[i] = obj.getString(JsonTag[i]);
                if (disease_info[i] != null){
                    disease_info[i] = replaceIllegalCharacter(disease_info[i], i);
                    textViews[i].setText(disease_info[i]);
                    linearLayouts[i].setVisibility(View.VISIBLE);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(){
        textViews[0] = findViewById(R.id.knowledge_graph_name);
        textViews[1] = findViewById(R.id.knowledge_graph_description);
        textViews[2] = findViewById(R.id.knowledge_graph_category);
        textViews[3] = findViewById(R.id.knowledge_graph_prevent);
        textViews[4] = findViewById(R.id.knowledge_graph_cause);
        textViews[5] = findViewById(R.id.knowledge_graph_symptom);
        textViews[6] = findViewById(R.id.knowledge_graph_yibao_status);
        textViews[7] = findViewById(R.id.knowledge_graph_get_prob);
        textViews[8] = findViewById(R.id.knowledge_graph_get_way);
        textViews[9] = findViewById(R.id.knowledge_graph_accompany);
        textViews[10] = findViewById(R.id.knowledge_graph_cure_department);
        textViews[11] = findViewById(R.id.knowledge_graph_cure_way);
        textViews[12] = findViewById(R.id.knowledge_graph_cure_last_time);
        textViews[13] = findViewById(R.id.knowledge_graph_cured_prob);
        textViews[14] = findViewById(R.id.knowledge_graph_cost_money);
        textViews[15] = findViewById(R.id.knowledge_graph_check);
        textViews[16] = findViewById(R.id.knowledge_graph_recmmand_drug);
        textViews[17] = findViewById(R.id.knowledge_graph_drug_detail);

        linearLayouts[0] = findViewById(R.id.knowledge_graph_name_Linear);
        linearLayouts[1] = findViewById(R.id.knowledge_graph_description_Linear);
        linearLayouts[2] = findViewById(R.id.knowledge_graph_category_Linear);
        linearLayouts[3] = findViewById(R.id.knowledge_graph_prevent_Linear);
        linearLayouts[4] = findViewById(R.id.knowledge_graph_cause_Linear);
        linearLayouts[5] = findViewById(R.id.knowledge_graph_symptom_Linear);
        linearLayouts[6] = findViewById(R.id.knowledge_graph_yibao_status_Linear);
        linearLayouts[7] = findViewById(R.id.knowledge_graph_get_prob_Linear);
        linearLayouts[8] = findViewById(R.id.knowledge_graph_get_way_Linear);
        linearLayouts[9] = findViewById(R.id.knowledge_graph_accompany_Linear);
        linearLayouts[10] = findViewById(R.id.knowledge_graph_cure_department_Linear);
        linearLayouts[11] = findViewById(R.id.knowledge_graph_cure_way_Linear);
        linearLayouts[12] = findViewById(R.id.knowledge_graph_cure_last_time_Linear);
        linearLayouts[13] = findViewById(R.id.knowledge_graph_cured_prob_Linear);
        linearLayouts[14] = findViewById(R.id.knowledge_graph_cost_money_Linear);
        linearLayouts[15] = findViewById(R.id.knowledge_graph_check_Linear);
        linearLayouts[16] = findViewById(R.id.knowledge_graph_recmmand_drug_Linear);
        linearLayouts[17] = findViewById(R.id.knowledge_graph_drug_detail_Linear);

        for (LinearLayout linearLayout : linearLayouts) {
            linearLayout.setVisibility(View.INVISIBLE);
        }

        JsonTag[0] = "name";
        JsonTag[1] = "desc";
        JsonTag[2] = "category";
        JsonTag[3] = "prevent";
        JsonTag[4] = "cause";
        JsonTag[5] = "symptom";
        JsonTag[6] = "yibao_status";
        JsonTag[7] = "get_prob";
        JsonTag[8] = "get_way";
        JsonTag[9] = "acompany";
        JsonTag[10] = "cure_department";
        JsonTag[11] = "cure_way";
        JsonTag[12] = "cure_lasttime";
        JsonTag[13] = "cured_prob";
        JsonTag[14] = "cost_money";
        JsonTag[15] = "check";
        JsonTag[16] = "recommand_drug";
        JsonTag[17] = "drug_detail";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRawTxtFileContent(Context context, String inputSearch) {
        String disease = "";
        String pString = "\"name\" : \""+inputSearch+"\"";
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.medical_graph_w);
            if (inputStream != null){
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                while ((line = bufferedReader.readLine()) != null){
                    int start = line.indexOf(pString);
                    if (start != -1){
                        disease = line;
                        break;
                    }
                }
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("text", "getFileContent: content = " + disease);
        return disease;
    }

    private String replaceIllegalCharacter(String string, int count){
        string = string.replace("[","");
        string = string.replace("]","");
        string = string.replace(",","，");
        string = string.replace("(","（");
        string = string.replace(")","）");
        if (count!=1 && count!=3 && count!= 4){
            string = string.replace("\"","");
        }

        return string;
    }
}