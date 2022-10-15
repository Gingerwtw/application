

package com.xuexiang.application.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xuexiang.application.R;
import com.xuexiang.application.UrlUtils;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.database.URLInfo;
import com.xuexiang.application.utils.BitmapUtil;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xrouter.utils.TextUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.ByteArrayOutputStream;

public class FaceActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_get;
    private Button btn_reget;
    private ImageButton btn_back;
    private Button btn_change_url;
    private ImageView image;
    Bitmap bitmap = null;

    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象
    private final int GET_FACE_IMAGE = 0;
    private final int GET_FACE_IMAGE_FAILED = 2;
    private final int ANA_FACE_IMAGE = 1;
    private final int ANA_FACE_IMAGE_FAILED = 3;

    private int init = 0;

    private MaterialDialog.Builder editDialog;
    private MaterialDialog.Builder waitingDialog;
    private MaterialDialog.Builder warningDialog;

    private DialogLoader mDialogLoader;
    private UrlUtils UrlInfo = new UrlUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        mDialogLoader = DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());

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
        btn_back = findViewById(R.id.face_toolbar_back);
        btn_reget = findViewById(R.id.btn_face_reget);
//        btn_change_url = getActivity().findViewById(R.id.btn_face_change_url);
        image = findViewById(R.id.face_image);

        btn_get.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_reget.setOnClickListener(this);
        btn_reget.setVisibility(View.INVISIBLE);
//        btn_change_url.setOnClickListener(this);
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
    }


    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if (message.what == GET_FACE_IMAGE){
                String path = message.getData().getString("path");
                bitmap = BitmapUtil.openBitmap(path);
                image.setImageBitmap(bitmap);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn_get.setText("分析");
                btn_reget.setVisibility(View.VISIBLE);
            }
            else if(message.what == ANA_FACE_IMAGE){
                Log.i("respond", message.getData().getString("content"));//打印返回的结果

                Intent intent = new Intent(FaceActivity.this, FaceResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("face_result",message.getData().getString("content"));
                intent.putExtras(bundle);
                Log.i("face","start activity");
                startActivity(intent);
            }
            else if (message.what == GET_FACE_IMAGE_FAILED){
                warningDialog.content("向采集设备发送网络请求失败").show();
            }
            else if (message.what == ANA_FACE_IMAGE_FAILED){
                warningDialog.content("向面象分析设备发送网络请求失败").show();
            }
        }
    };

    @SingleClick
    @Override
    public void onClick(View view) {
        int id = view.getId();
        // 从采集设备获取图像
        if (id == R.id.btn_face_get) {
            if (btn_get.getText().equals("采集")){
                getFace();
            }
            else{
                analyseFace();
            }
        }
        else if (id == R.id.face_toolbar_back){
//            Intent intent = new Intent(FaceActivity.this, MainActivity.class);
//            startActivityForResult(intent,200);
            FaceActivity.this.finish();
        }
        else if (id == R.id.btn_face_reget){
            getFace();
        }
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
                        String res = HttpRequestUtil.postImage(UrlInfo.getFace_analyze(),data);
                        Log.d("respond",res);

                        if (res.length() < 200){
                            message.what = ANA_FACE_IMAGE_FAILED;
                        }
                        else{
                            message.what = ANA_FACE_IMAGE;
                            Bundle bundle = new Bundle();
                            bundle.putString("content",res);

                            message.setData(bundle);
                        }
                        mhandler.sendMessage(message);
                        dialog.dismiss();
                    }
                }.start();
            }
        }).show();
    }

    private void getFace(){
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

                        HttpReqData req_data = new HttpReqData(UrlInfo.getFace_calibrate());
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
                    }
                }.start();
            }
        }).show() ;
    }
}