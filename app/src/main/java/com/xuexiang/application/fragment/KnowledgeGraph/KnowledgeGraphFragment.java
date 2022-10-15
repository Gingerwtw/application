package com.xuexiang.application.fragment.KnowledgeGraph;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.xuexiang.application.R;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class KnowledgeGraphFragment extends Fragment implements View.OnClickListener {

    TextView hint;
    String []JsonTag = new String[18];
    TextView []textViews = new TextView[18];
    LinearLayout []linearLayouts = new LinearLayout[18];

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_knowledge_graph, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        SearchView search = getActivity().findViewById(R.id.knowledge_graph_search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String []disease_info = new String[18];
                String disease = getRawTxtFileContent(getContext(),s);

                if (Objects.equals(disease, "")){
                    hint.setVisibility(View.VISIBLE);
                    for (LinearLayout linearLayout : linearLayouts) {
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                    hint.setText("未查询到该疾病");
                }else{
                    try {
                        JSONObject obj = new JSONObject(disease);

                        for (int i = 0; i < JsonTag.length; i++) {
                            disease_info[i] = obj.getString(JsonTag[i]);
                            if (disease_info[i] != null){
                                disease_info[i] = replaceIllegalCharacter(disease_info[i], i);
                                textViews[i].setText(disease_info[i]);
                                linearLayouts[i].setVisibility(View.VISIBLE);
                                hint.setVisibility(View.INVISIBLE);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                XToastUtils.info(s);
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    private void initView(){
        hint = getActivity().findViewById(R.id.knowledge_graph_hint);
        textViews[0] = getActivity().findViewById(R.id.knowledge_graph_name);
        textViews[1] = getActivity().findViewById(R.id.knowledge_graph_description);
        textViews[2] = getActivity().findViewById(R.id.knowledge_graph_category);
        textViews[3] = getActivity().findViewById(R.id.knowledge_graph_prevent);
        textViews[4] = getActivity().findViewById(R.id.knowledge_graph_cause);
        textViews[5] = getActivity().findViewById(R.id.knowledge_graph_symptom);
        textViews[6] = getActivity().findViewById(R.id.knowledge_graph_yibao_status);
        textViews[7] = getActivity().findViewById(R.id.knowledge_graph_get_prob);
        textViews[8] = getActivity().findViewById(R.id.knowledge_graph_get_way);
        textViews[9] = getActivity().findViewById(R.id.knowledge_graph_accompany);
        textViews[10] = getActivity().findViewById(R.id.knowledge_graph_cure_department);
        textViews[11] = getActivity().findViewById(R.id.knowledge_graph_cure_way);
        textViews[12] = getActivity().findViewById(R.id.knowledge_graph_cure_last_time);
        textViews[13] = getActivity().findViewById(R.id.knowledge_graph_cured_prob);
        textViews[14] = getActivity().findViewById(R.id.knowledge_graph_cost_money);
        textViews[15] = getActivity().findViewById(R.id.knowledge_graph_check);
        textViews[16] = getActivity().findViewById(R.id.knowledge_graph_recmmand_drug);
        textViews[17] = getActivity().findViewById(R.id.knowledge_graph_drug_detail);

        linearLayouts[0] = getActivity().findViewById(R.id.knowledge_graph_name_Linear);
        linearLayouts[1] = getActivity().findViewById(R.id.knowledge_graph_description_Linear);
        linearLayouts[2] = getActivity().findViewById(R.id.knowledge_graph_category_Linear);
        linearLayouts[3] = getActivity().findViewById(R.id.knowledge_graph_prevent_Linear);
        linearLayouts[4] = getActivity().findViewById(R.id.knowledge_graph_cause_Linear);
        linearLayouts[5] = getActivity().findViewById(R.id.knowledge_graph_symptom_Linear);
        linearLayouts[6] = getActivity().findViewById(R.id.knowledge_graph_yibao_status_Linear);
        linearLayouts[7] = getActivity().findViewById(R.id.knowledge_graph_get_prob_Linear);
        linearLayouts[8] = getActivity().findViewById(R.id.knowledge_graph_get_way_Linear);
        linearLayouts[9] = getActivity().findViewById(R.id.knowledge_graph_accompany_Linear);
        linearLayouts[10] = getActivity().findViewById(R.id.knowledge_graph_cure_department_Linear);
        linearLayouts[11] = getActivity().findViewById(R.id.knowledge_graph_cure_way_Linear);
        linearLayouts[12] = getActivity().findViewById(R.id.knowledge_graph_cure_last_time_Linear);
        linearLayouts[13] = getActivity().findViewById(R.id.knowledge_graph_cured_prob_Linear);
        linearLayouts[14] = getActivity().findViewById(R.id.knowledge_graph_cost_money_Linear);
        linearLayouts[15] = getActivity().findViewById(R.id.knowledge_graph_check_Linear);
        linearLayouts[16] = getActivity().findViewById(R.id.knowledge_graph_recmmand_drug_Linear);
        linearLayouts[17] = getActivity().findViewById(R.id.knowledge_graph_drug_detail_Linear);

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
