package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.utils.BitmapUtil;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class FaceActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton btn_get, btn_reget, btn_album, btn_ana;
    private ImageButton btn_back;
    private Button btn_change_url;
    private ImageView image;
    private Bitmap bitmap = null;

    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象
    private final int GET_FACE_IMAGE = 0;
    private final int ANA_FACE_IMAGE = 1;
    private final int GET_FACE_IMAGE_FAILED = 2;
    private final int ANA_FACE_IMAGE_FAILED = 3;
    private final int ANA_FACE_IMAGE_ADD_SUCCESS = 4;
    private final int ANA_FACE_IMAGE_ADD_FAILED = 5;
    private final int ANA_FACE_IMAGE_SERVER_ERROR = 6;
    private final int OPEN_ALBUM = 101;

    private int init = 0;

    private MaterialDialog.Builder editDialog;
    private MaterialDialog.Builder waitingDialog;
    private MaterialDialog.Builder warningDialog;

    private DialogLoader mDialogLoader;
    private UrlUtils urlUtils = new UrlUtils();
    private SharedPreferences mShared, imageShared, faceShared;

    private JSONObject middle_top, middle_middle, middle_bottom, left, right;
    private String health_index, correct_face_img;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        mDialogLoader = DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());
        mShared = getSharedPreferences("information", MODE_PRIVATE);
        imageShared = getSharedPreferences("image", MODE_PRIVATE);
        faceShared = getSharedPreferences("face", MODE_PRIVATE);
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        warningDialog = new MaterialDialog.Builder(FaceActivity.this)
                .title("错误")
                .positiveText("确认");

        editDialog = new MaterialDialog.Builder(FaceActivity.this)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        XToast.normal(FaceActivity.this,input).show();
                    }
                })
                .positiveText("确认")
                .negativeText("取消");

        waitingDialog = new MaterialDialog.Builder(FaceActivity.this)
//                .iconRes(R.drawable.icon_sex_man)
                .limitIconToDefaultSize()
                .title(R.string.waiting_dialog_title)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .negativeText(R.string.waiting_dialog_negativeText)
                .autoDismiss(true);


        btn_get = findViewById(R.id.btn_face_get);
        btn_ana = findViewById(R.id.btn_face_ana);
        btn_back = findViewById(R.id.face_toolbar_back);
        btn_reget = findViewById(R.id.btn_face_reget);
        btn_album = findViewById(R.id.btn_face_album);
        image = findViewById(R.id.face_image);

        btn_get.setOnClickListener(this);
        btn_ana.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_reget.setOnClickListener(this);
        btn_album.setOnClickListener(this);
        btn_reget.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==200 && resultCode == 200)
        {
            Bundle bundle  = data.getExtras();
            boolean isReturn =  bundle.getBoolean("isReturn");

            if (!isReturn){
                String path = bundle.getString("path");
                bitmap = BitmapUtil.openBitmap(path);
                image.setImageBitmap(bitmap);
            }
        }
        else if (requestCode == OPEN_ALBUM && resultCode == RESULT_OK && data != null){
            Uri uris;
            uris = data.getData();
//            Bitmap bitmap = null;
            //Uri转化为Bitmap
            try {
                bitmap = getBitmapFromUri(uris);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            message.what = GET_FACE_IMAGE;

            String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
            String image_name = "get_face.png";
            String full_path = String.format("%s/%s", image_path, image_name);

            BitmapUtil.saveBitmap(full_path, bitmap, "jpg", 80);
            bundle.putString("path",full_path);
            message.setData(bundle);
            mhandler.sendMessage(message);
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if (message.what == GET_FACE_IMAGE){
                String path = message.getData().getString("path");
                bitmap = BitmapUtil.openBitmap(path);
                image.setImageBitmap(bitmap);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn_get.setVisibility(View.INVISIBLE);
                btn_ana.setVisibility(View.VISIBLE);
                btn_reget.setVisibility(View.VISIBLE);
            }
            else if(message.what == ANA_FACE_IMAGE){
                String analyzeResult = message.getData().getString("content");
                new Thread(){
                    @Override
                    public void run(){
                        super.run();
                        Message recordmessage = Message.obtain();
                        Log.i("respond", analyzeResult);
                        String record = TOJSON(analyzeResult);
                        HttpReqData req = new HttpReqData();
                        req.url = urlUtils.getAddRecord();
                        req.params = new StringBuffer(record);

                        HttpRespData resp_data = HttpRequestUtil.postData(req);
                        String content = resp_data.content;
                        Log.d("add record result", content);
                        String status = null;
                        try {
                            JSONObject obj = new JSONObject(content);
                            status = obj.getString("status");
                        }catch (JSONException e) {
                            recordmessage.what = ANA_FACE_IMAGE_ADD_FAILED;
                            e.printStackTrace();
                        }
                        Log.d("add record result", status);
                        if (Objects.equals(status, "success!")){
                            recordmessage.what = ANA_FACE_IMAGE_ADD_SUCCESS;
                        }else{
                            recordmessage.what = ANA_FACE_IMAGE_ADD_FAILED;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("content",analyzeResult);

                        recordmessage.setData(bundle);
                        recordHandler.sendMessage(recordmessage);
                    }
                }.start();
            }
            else if (message.what == GET_FACE_IMAGE_FAILED){
                warningDialog.content("向采集设备发送网络请求失败").show();
            }
            else if (message.what == ANA_FACE_IMAGE_SERVER_ERROR){
                warningDialog.content("向面象分析设备发送网络请求失败").show();
            }
            else if (message.what == ANA_FACE_IMAGE_FAILED){
                warningDialog.content("上传的面象照片中面部不清晰").show();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler recordHandler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == ANA_FACE_IMAGE_ADD_FAILED){
                XToastUtils.error("上传诊疗记录失败");
            }
            initResult(message.getData().getString("content"));
            Intent intent = new Intent(FaceActivity.this, FaceResultActivity.class);
            Bundle bundle = new Bundle();
//            bundle.putString("face_result",message.getData().getString("content"));
            bundle.putString("middle_top", String.valueOf(middle_top));
            bundle.putString("middle_middle", String.valueOf(middle_middle));
            bundle.putString("middle_bottom", String.valueOf(middle_bottom));
            bundle.putString("left", String.valueOf(left));
            bundle.putString("right", String.valueOf(right));
            bundle.putString("health_index", String.valueOf(health_index));
            intent.putExtras(bundle);
            Log.i("face","start activity");
            startActivity(intent);
        }
    };

    @SingleClick
    @Override
    public void onClick(View view) {
        int id = view.getId();
        // 从采集设备获取图像
        if (id == R.id.btn_face_get) {
            if (urlUtils.getFace_calibrate()!= null){
                getFace();
            }
            else{
                warningDialog.content("请先设置采集设备地址").show();
            }
        }
        else if (id == R.id.btn_face_ana){
            if (urlUtils.getFace_analyze() != null){
                analyseFace();
            }
            else{
                warningDialog.content("请先设置服务器地址").show();
            }
        }
        else if (id == R.id.face_toolbar_back){
            FaceActivity.this.finish();
        }
        else if (id == R.id.btn_face_reget){
            if (urlUtils.getFace_calibrate()!= null){
                getFace();
            }
            else{
                warningDialog.content("请先设置采集设备地址").show();
            }
        }
        else if (id == R.id.btn_face_album){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            //设置请求码，以便我们区分返回的数据
            startActivityForResult(intent, OPEN_ALBUM);
        }
    }

    private void initResult(String json){
        Bundle bundle=getIntent().getExtras();

        try {
            JSONObject obj = new JSONObject(json);
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
        SharedPreferences.Editor editor = imageShared.edit();
        editor.putString("image", correct_face_img);
        editor.apply();
    }

    private void analyseFace(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        waitingDialog.content("面象分析中，请稍候")
                .showListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final MaterialDialog dialog = (MaterialDialog)dialogInterface;
                new Thread() {
                    @Override
                    public void run () {
                        super.run();
                        Message message = Message.obtain();

                        String data = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
                        //发起网络请求，传入base64数据
                        String res = HttpRequestUtil.postImage(urlUtils.getFace_analyze(),data);
                        Log.d("respond",res);
                        Log.d("respond length", String.valueOf(res.length()));

//                        if (res.length() < 200){
//                            message.what = ANA_FACE_IMAGE_SERVER_ERROR;
//                        }else{
                            String status = null;
                            try {
                                JSONObject obj = new JSONObject(res);
                                status = obj.getString("status");
                            }catch (JSONException e) {
                                message.what = ANA_FACE_IMAGE_FAILED;
                                e.printStackTrace();
                            }

                            if (Objects.equals(status, "failed")){
                                message.what = ANA_FACE_IMAGE_FAILED;
                            }
                            else if (Objects.equals(status, "success")){
                                message.what = ANA_FACE_IMAGE;
                                Bundle bundle = new Bundle();
                                bundle.putString("content",res);
                                message.setData(bundle);
                            }
                            else{
                                message.what = ANA_FACE_IMAGE_SERVER_ERROR;
                            }
//                        }
                        mhandler.sendMessage(message);
                        dialog.dismiss();
                    }
                }.start();
            }
        }).show();
    }

    private void getFace(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();

        waitingDialog.content("面象采集中，请稍候")
                .showListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final MaterialDialog dialog = (MaterialDialog)dialogInterface;
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message message = Message.obtain();

                        HttpReqData req_data = new HttpReqData(urlUtils.getFace_calibrate());
                        HttpRespData resp_data = HttpRequestUtil.getImage(req_data);
                        Log.d("respond", String.valueOf(resp_data.bitmap));

                        if (resp_data.bitmap == null){
                            message.what = GET_FACE_IMAGE_FAILED;
                        }
                        else{
                            Bundle bundle = new Bundle();
                            message.what = GET_FACE_IMAGE;

                            String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
                            String image_name = "get_face.png";
                            String full_path = String.format("%s/%s", image_path, image_name);

                            BitmapUtil.saveBitmap(full_path, resp_data.bitmap, "jpg", 80);
                            bundle.putString("path",full_path);
                            message.setData(bundle);
                        }
                        mhandler.sendMessage(message);
                        dialog.dismiss();
                        long[] pattern = { 10L, 60L }; // An array of longs of times for which to turn the vibrator on or off.
                        vibrator.vibrate(pattern, -1); // The index into pattern at which to repeat, or -1 if you don't want to repeat.
                    }
                }.start();
            }
        }).show() ;
    }

    private String TOJSON(String respond){
        String result = "";
        try{
            JSONObject obj = new JSONObject(respond);
            String health_index = obj.getString("health_index");
            SharedPreferences.Editor editor = faceShared.edit();
            editor.putString("health_index", health_index);
            editor.apply();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",mShared.getString("phone",""));
            jsonObject.put("record",health_index+"+面象分析");

            result = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("face record",result);
        return result;
    }

    //Uri转化为Bitmap
    private Bitmap getBitmapFromUri(Uri uri) throws FileNotFoundException {
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
    }

}