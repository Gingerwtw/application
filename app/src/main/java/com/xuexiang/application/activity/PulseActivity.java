package com.xuexiang.application.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.utils.BitmapUtil;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PulseActivity extends AppCompatActivity {

    private DialogLoader mDialogLoader;

    private MaterialDialog.Builder SingleChoiceDialog;

    private UrlUtils UrlInfo = new UrlUtils();

    private final int ANA_PULSE = 1;
    private final int ANA_PULSE_FAILED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);

        Button btn_pulse = findViewById(R.id.btn_pulse);
        btn_pulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
//                SingleChoiceDialog.show();
            }
        });

        SingleChoiceDialog = new MaterialDialog.Builder(PulseActivity.this)
                .title("选择您要分析的脉冲数据")
                .items(new String[]{"脉象数据1", "脉象数据2", "脉象数据3", "脉象数据4"})
                .itemsCallbackSingleChoice(
                        -1,
                        (dialog, view, which, text) -> {
//                            switch (which){
//                                case 0:
//                                    XToastUtils.info("点击了"+which);
//                                    break;
//                                case 1:
//                                    XToastUtils.info("点击了"+which);
//                                    break;
//                                case 2:
//                                    XToastUtils.info("点击了"+which);
//                                    break;
//                                case 3:
//                                    XToastUtils.info("点击了"+which);
//                                    break;
//                            }
//                            XToastUtils.info(which + ": " + text);
                            XToastUtils.info("点击了"+which);
                            return true; // allow selection
                        })
                .alwaysCallSingleChoiceCallback()
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        XToastUtils.info("点击了确认");
                    }
                })
                .negativeText("关闭");

        mDialogLoader = DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());

        ImageButton btn_back = findViewById(R.id.pulse_toolbar_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == ANA_PULSE){
                Intent intent = new Intent(PulseActivity.this, PulseResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("pulse_result",message.getData().getString("content"));
                bundle.putInt("select",message.getData().getInt("select"));
                intent.putExtras(bundle);

                startActivity(intent);
            }
            else if (message.what == ANA_PULSE_FAILED){
//                warningDialog.content("向面象分析设备发送网络请求失败").show();
            }
        }
    };

    private void showSingleChoiceDialog(){
        final String[] items = {"脉象数据1","脉象数据2","脉象数据3", "脉象数据4"};
        mDialogLoader.showSingleChoiceDialog(PulseActivity.this
                ,"选择您要分析的脉冲数据"
                ,items
                ,0
                ,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //点击确认按钮后，执行该方法
                        String pulse1, pulse2, pulse3;
                        List<List<Float>> pulse = new ArrayList<>();
                        String result ;
                        List<Float> list1 = new ArrayList<>(), list2 = new ArrayList<>(), list3 = new ArrayList<>();

                        pulse1 = getRawTxtFileContent(PulseActivity.this, i, R.raw.pulse1);
                        pulse2 = getRawTxtFileContent(PulseActivity.this, i, R.raw.pulse2);
                        pulse3 = getRawTxtFileContent(PulseActivity.this, i, R.raw.pulse3);

                        String[] strs1 = pulse1.split("\t");
                        for (String s : strs1){
                            list1.add(Float.parseFloat(s)) ;
                        }
                        pulse.add(list1);

                        String[] strs2 = pulse2.split("\t");
                        for (String s : strs2){
                            list2.add(Float.parseFloat(s)) ;
                        }
                        pulse.add(list2);

                        String[] strs3 = pulse3.split("\t");
                        for (String s : strs3){
                            list3.add(Float.parseFloat(s)) ;
                        }
                        pulse.add(list3);

                        Log.d("pulse", String.valueOf(pulse));

                        result = ToJSON(pulse);
                        Log.d("json",result);

                        float[] array = new float[list1.size()];

                        new Thread(){
                            @Override
                            public void run() {
                                super.run();

                                HttpReqData req = new HttpReqData();
                                req.url = UrlInfo.getPulse_analyze();
                                req.params = new StringBuffer(result);

                                HttpRespData resp_data = HttpRequestUtil.postData(req);
                                Log.d("pulse result", String.valueOf(resp_data));
                                String content = resp_data.content;
                                Log.d("pulse result", content);

                                Message message = Message.obtain();
//                                if (content.length() < 200){
//                                    message.what = ANA_PULSE_FAILED;
//                                }
//                                else{
//                                    message.what = ANA_PULSE;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("content",content);
//
//                                    message.setData(bundle);
//                                }
                                message.what = ANA_PULSE;
                                Bundle bundle = new Bundle();
                                bundle.putString("content",content);
                                bundle.putInt("select", i);

                                message.setData(bundle);
                                mhandler.sendMessage(message);
                            }
                        }.start();

                    }
                },"确认"
                ,"关闭");
    }

    /**
     * 获取raw文件夹下的文件内容
     * @param context
     * @return
     */
    public static String getRawTxtFileContent(Context context, int select, int rawResId) {
        String pulse = "";
        int count;
        try {
            InputStream inputStream = context.getResources().openRawResource(rawResId);
            if (inputStream != null){
                InputStreamReader reader = new InputStreamReader(inputStream,"GB2312");
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                count = 0;
                while ((line = bufferedReader.readLine()) != null){
                    if (count == select){
                        pulse = line;
                        break;
                    }else{
                        count += 1;
                    }

                }
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.d("text", "getFileContent: i = "+ select +"content = " + pulse);
        return pulse;
    }

    public String ToJSON(List<List<Float>> pulse){
        String result = "";
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pulse",pulse);

            result = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}