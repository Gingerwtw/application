package com.xuexiang.application.fragment.Personal;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.activity.LoginActivity;
import com.xuexiang.application.activity.MainActivity;
import com.xuexiang.application.activity.PersonalInformationActivity;
import com.xuexiang.application.activity.RecordActivity;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


@Page(anim = CoreAnim.none)
public class PersonalFragment extends Fragment implements View.OnClickListener {
    RelativeLayout personal_more_information, personal_record, logout;

    SharedPreferences mShared;
    private UrlUtils UrlInfo = new UrlUtils();

    private int GET_RECORD_SUCCESS = 0;
    private int GET_RECORD_FAILED = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShared = getActivity().getSharedPreferences("information", getActivity().MODE_PRIVATE);

        TextView username = getActivity().findViewById(R.id.personal_name);
        ImageView gender = getActivity().findViewById(R.id.personal_gender_icon);
        TextView phone = getActivity().findViewById(R.id.personal_phone);
        personal_more_information = getActivity().findViewById(R.id.personal_more_information);
        personal_record = getActivity().findViewById(R.id.personal_record);
        logout = getActivity().findViewById(R.id.logout);

        username.setText(mShared.getString("name",""));
        phone.setText(mShared.getString("phone",""));
        if (Objects.equals(mShared.getString("gender", ""), "女")){
            gender.setImageResource(R.drawable.female);
        }

        personal_more_information.setOnClickListener(this);
        personal_record.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == GET_RECORD_SUCCESS){
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("content",message.getData().getString("content"));
                intent.putExtras(bundle);

                startActivityForResult(intent, 100);
            }
            else if (message.what == GET_RECORD_FAILED){
                XToastUtils.error("获取诊查记录失败");
            }
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.personal_more_information){
            Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.personal_record){
            getRecord();
        }
        else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void getRecord(){
        String requestData = ToJSON();
        new Thread() {
            @Override
            public void run() {
                super.run();

                HttpReqData req = new HttpReqData();
                req.url = UrlInfo.getRecord();
                req.params = new StringBuffer(requestData);

                HttpRespData resp_data = HttpRequestUtil.postData(req);
                Log.d("login result", String.valueOf(resp_data));
                String content = resp_data.content;
                Log.d("login result", content);

                Message message = Message.obtain();
                try {
                    JSONObject obj = new JSONObject(content);
                    String status = obj.getString("status");

                    if (status.equals("success")){
                        message.what = GET_RECORD_SUCCESS;
                    }else{
                        message.what = GET_RECORD_FAILED;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = GET_RECORD_FAILED;
                }
                Bundle bundle = new Bundle();
                bundle.putString("content",content);
                message.setData(bundle);
                mhandler.sendMessage(message);
            }
        }.start();
    }

    public String ToJSON(){
        String result = "";
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",mShared.getString("phone",""));

            result = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("login",result);
        return result;
    }
}
