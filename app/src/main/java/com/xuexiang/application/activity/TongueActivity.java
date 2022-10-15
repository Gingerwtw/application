package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 舌象Activity
 */
@Page(anim = CoreAnim.none)
public class TongueActivity extends BaseActivity implements OnClickListener {

//    private Button btn_ana;
    private Button btn_get;
    private Button btn_reget;
    private ImageButton btn_back;

    private Button btn_change_url;
    private ImageView image;
    Bitmap bitmap = null;

    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象

    private final int GET_TONGUE_IMAGE = 0;
    private final int GET_TONGUE_IMAGE_FAILED = 1;
    private int init = 0;

    private MaterialDialog.Builder waitingDialog;
    private MaterialDialog.Builder editDialog;
    private MaterialDialog.Builder warningDialog;

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_tongue, container, false);
//    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue);

//        btn_ana = findViewById(R.id.btn_tongue_ana);
        btn_get = findViewById(R.id.btn_tongue_get);
        btn_back = findViewById(R.id.tongue_toolbar_back);
        btn_reget = findViewById(R.id.btn_tongue_reget);

        image = findViewById(R.id.tongue_image);
//        btn_ana.setOnClickListener(this);
        btn_get.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_reget.setOnClickListener(this);
        btn_reget.setVisibility(View.INVISIBLE);

        warningDialog = new MaterialDialog.Builder(TongueActivity.this)
                .title("错误")
                .content("向采集设备发送网络请求失败")
                .positiveText("确认");

        waitingDialog = new MaterialDialog.Builder(TongueActivity.this)
//                .iconRes(R.drawable.icon_sex_man)
                .limitIconToDefaultSize()
                .title(R.string.waiting_dialog_title)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .negativeText(R.string.waiting_dialog_negativeText)
                .autoDismiss(true);

        editDialog = new MaterialDialog.Builder(TongueActivity.this)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        XToast.normal(TongueActivity.this,input).show();
                    }
                })
                .positiveText("确认")
                .negativeText("取消");
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获得URL数据库帮助器的一个实例
//        mHelper = URLDBHelper.getInstance(TongueActivity.this, 3);
        // 恢复页面，则打开数据库连接
//        mHelper.openWriteLink();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
//        mHelper.closeLink();
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser){
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            if(init == 0){
//                init = 1;
//            }
//            else{
//                image.setImageBitmap(bitmap);
//            }
//        }
//        else {
//            bitmap = null;
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==200 && resultCode == 200)
        {
           Bundle bundle  = data.getExtras();
           boolean isReturn =  bundle.getBoolean("isReturn");

           if (!isReturn){
               String respondJson = bundle.getString("respond");
               try {
                   JSONObject obj = new JSONObject(respondJson);
                   JSONObject substance = obj.getJSONObject("substance");
                   JSONObject coating = obj.getJSONObject("coating");
                   String health_index = obj.getString("health_index");
                   String coating_image = obj.getString("coating_img");

                   Log.d("health_index",health_index);

               } catch (JSONException e) {
                   e.printStackTrace();
               }


//               bitmap = BitmapUtil.openBitmap(path);
//               image.setImageBitmap(bitmap);
           }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if (message.what == GET_TONGUE_IMAGE){
                String path = message.getData().getString("path");
                bitmap = BitmapUtil.openBitmap(path);
                image.setImageBitmap(bitmap);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                btn_get.setText("分析");
                btn_reget.setVisibility(View.VISIBLE);
            }
            else if (message.what == GET_TONGUE_IMAGE_FAILED){
                warningDialog.show();
            }
        }
    };

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public Bitmap stringToBitmap(String string) {
// 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.URL_SAFE);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        int id = view.getId();
        // 分割图像
//        if (id == R.id.btn_tongue_ana) {
//            if (bitmap == null){
//                XToastUtils.error("请先获取图像");
//            }
//            else {
//
////                BitmapUtil instance = BitmapUtil.getInstance();
////                instance.setEditBitmap(bitmap);
//
//                String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
//                String image_name = "ana_face.png";
//                String full_path = String.format("%s/%s", image_path, image_name);
//                BitmapUtil.saveBitmap(full_path, bitmap, "jpg", 80);
//
//                Intent intent = new Intent(TongueActivity.this, MeituActivity.class);
//                intent.putExtra("path",full_path);
//
////                ByteArrayOutputStream baos=new ByteArrayOutputStream();
////                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
////                byte [] bitmapByte =baos.toByteArray();
////                intent.putExtra("bitmap", bitmapByte);
////                startActivityForResult(intent, 1);
//                startActivityForResult(intent,200);
////                getActivity().setResult(1,intent);
//            }
//
//        }
        // 从采集设备获取图像
        if (id == R.id.btn_tongue_get) {
            if (btn_get.getText().equals("采集")){
                getTongue();
            }
            else{
                String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
                String image_name = "ana_face.png";
                String full_path = String.format("%s/%s", image_path, image_name);
                BitmapUtil.saveBitmap(full_path, bitmap, "jpg", 80);

                Intent intent = new Intent(TongueActivity.this, MeituActivity.class);
                intent.putExtra("path",full_path);
                startActivityForResult(intent,200);

            }
        }
        else if (id == R.id.tongue_toolbar_back){
//            Intent intent = new Intent(TongueActivity.this, MainActivity.class);
////            startActivityForResult(intent,200);
            TongueActivity.this.finish();
        }
        else if (id == R.id.btn_tongue_reget){
            getTongue();
        }
    }

    private void getTongue(){
        waitingDialog.content("舌象采集中，请稍候")
        .showListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final MaterialDialog dialog = (MaterialDialog)dialogInterface;
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message message = Message.obtain();


                        // 创建一个HTTP请求对象
//                                HttpReqData req_data = new HttpReqData(urlInfo.User_URL);

                        HttpReqData req_data = new HttpReqData(new UrlUtils().getTongue_calibrate());
                        // 发送HTTP请求信息，并获得HTTP应答对象
                        HttpRespData resp_data = HttpRequestUtil.getImage(req_data);

                        if (resp_data.bitmap == null){
                            message.what = GET_TONGUE_IMAGE_FAILED;
                        }
                        else {
                            message.what = GET_TONGUE_IMAGE;
                            Bundle bundle = new Bundle();

                            String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
                            String image_name = "get_face.png";
                            String full_path = String.format("%s/%s", image_path, image_name);
                            BitmapUtil.saveBitmap(full_path, resp_data.bitmap, "jpg", 80);
                            bundle.putString("path", full_path);

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
