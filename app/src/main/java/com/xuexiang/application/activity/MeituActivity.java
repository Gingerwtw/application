package com.xuexiang.application.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.filedialog.lib.dialog.FileSaveFragment;
import com.example.filedialog.lib.dialog.FileSaveFragment.FileSaveCallbacks;
import com.example.filedialog.lib.dialog.FileSelectFragment;
import com.example.filedialog.lib.dialog.FileSelectFragment.FileSelectCallbacks;
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
import com.xuexiang.application.widget.BitmapView;
import com.xuexiang.application.widget.MeituView;
import com.xuexiang.application.widget.MeituView.ImageChangetListener;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;


public class MeituActivity extends BaseActivity implements
        FileSelectCallbacks, FileSaveCallbacks, ImageChangetListener {
    private final static String TAG = "MeituActivity";
    private MeituView mv_content; // 声明一个美图视图对象
    private TextView tv_intro;
    private BitmapView bv_content; // 声明一个位图视图对象
    private Bitmap mBitmap = null; // 声明一个位图对象
    private Button btn_return;
    private Button btn_confirm;

    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象

    private Bitmap bitmap_content;

    private final int ANA_TONGUE_IMAGE = 0;
    private final int ANA_TONGUE_IMAGE_FAILED = 1;
    private final int ANA_TONGUE_IMAGE_ADD_SUCCESS = 2;
    private final int ANA_TONGUE_IMAGE_ADD_FAILED = 3;

    private String originTongueImagePath;
    private String cutTongueImagePath;

//    private MaterialDialog.Builder confirmDialog;
    private MaterialDialog.Builder editDialog;
    private MaterialDialog.Builder waitingDialog;
    private MaterialDialog.Builder warningDialog;

    private UrlUtils UrlInfo = new UrlUtils();
    SharedPreferences mShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meitu);

        mShared = getSharedPreferences("information", MODE_PRIVATE);
        warningDialog = new MaterialDialog.Builder(this)
                .title("错误")
                .content("向舌象分析设备发送网络请求失败")
                .positiveText("确认");


//        confirmDialog = new MaterialDialog.Builder(this)
//                .positiveText(R.string.lab_yes)
//                .negativeText(R.string.lab_no);

        editDialog = new MaterialDialog.Builder(this)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        XToast.normal(MeituActivity.this,input).show();
                    }
                })
                .positiveText("确认")
                .negativeText("取消");


        waitingDialog = new MaterialDialog.Builder(this)
//                .iconRes(R.drawable.icon_sex_man)
                .limitIconToDefaultSize()
                .title(R.string.waiting_dialog_title)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .negativeText(R.string.waiting_dialog_negativeText)
                .autoDismiss(true);

        // 从布局文件中获取名叫mv_content的美图视图
        mv_content = findViewById(R.id.mv_content);
        // 设置美图视图的图像变更监听器
        mv_content.setImageChangetListener(this);
//        tv_intro = findViewById(R.id.tv_intro);
        // 从布局文件中获取名叫bv_content的位图视图
        bv_content = findViewById(R.id.bv_content);
        // 开启位图视图bv_content的绘图缓存
        bv_content.setDrawingCacheEnabled(true);

//        bitmap_content = BitmapFactory.decodeResource(getResources(), R.drawable.test_picture);

        originTongueImagePath = getIntent().getStringExtra("path");
        bitmap_content = BitmapUtil.openBitmap(originTongueImagePath);
        bv_content.setImageBitmap(bitmap_content);

        bv_content.post(new Runnable(){
            @Override
            public void run() {
                refreshImage(true);
            }
        });

        btn_return = findViewById(R.id.btn_return);
        btn_confirm = findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBitmap = mv_content.getCropBitmap();
                String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
                String image_name = "result_tongue.png";
                cutTongueImagePath = String.format("%s/%s", image_path, image_name);
                BitmapUtil.saveBitmap(cutTongueImagePath, mBitmap, "jpg", 80);

                //将位图转为字节数组后再转为base64
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);   //切割图
//                bitmap_content.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);     //原图

                waitingDialog.content("舌象分析中，请稍等")
                        .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        final MaterialDialog dialog = (MaterialDialog)dialogInterface;
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Message message = Message.obtain();

                                String data = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
                                //发起网络请求，传入base64数据
                                String res = HttpRequestUtil.postImage(UrlInfo.getTongue_analyze(),data);
                                Log.d("res",res+res.length());
                                if (res.length() < 100){
                                    message.what = ANA_TONGUE_IMAGE_FAILED;
                                }
                                else{
                                    message.what = ANA_TONGUE_IMAGE;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("content",res);
                                    message.setData(bundle);
                                }
                                mhandler.sendMessage(message);
                                dialog.dismiss();
                            }
                        }.start();
                    }
                }).show() ;
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isReturn",true);
                bundle.putString("path","");
                intent.putExtras(bundle);

                setResult(200,intent);
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        public void handleMessage(Message message){
            if (message.what == ANA_TONGUE_IMAGE){
                Log.i("respond", message.getData().getString("content"));//打印返回的结果
                String analyzeResult = message.getData().getString("content");
                new Thread(){
                    @Override
                    public void run(){
                        super.run();
                        Message recordmessage = Message.obtain();
                        Log.i("respond", analyzeResult);
                        String record = TOJSON(analyzeResult);
                        HttpReqData req = new HttpReqData();
                        req.url = UrlInfo.getAddRecord();
                        req.params = new StringBuffer(record);

                        HttpRespData resp_data = HttpRequestUtil.postData(req);
                        String content = resp_data.content;
                        Log.d("add record result", content);
                        String status = null;
                        try {
                            JSONObject obj = new JSONObject(content);
                            status = obj.getString("status");
                        }catch (JSONException e) {
                            recordmessage.what = ANA_TONGUE_IMAGE_ADD_FAILED;
                            e.printStackTrace();
                        }
                        Log.d("tongue add record res", status);
                        if (Objects.equals(status, "success!")){
                            recordmessage.what = ANA_TONGUE_IMAGE_ADD_SUCCESS;
                        }else{
                            recordmessage.what = ANA_TONGUE_IMAGE_ADD_FAILED;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("content",analyzeResult);

                        recordmessage.setData(bundle);
                        recordHandler.sendMessage(recordmessage);
                    }
                }.start();
            }
            else if (message.what == ANA_TONGUE_IMAGE_FAILED){
                warningDialog.show();
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler recordHandler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == ANA_TONGUE_IMAGE_ADD_FAILED){
                XToastUtils.error("上传诊疗记录失败");
            }
            Intent intent = new Intent(MeituActivity.this, TongueResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("tongue_result",message.getData().getString("content"));
            bundle.putString("path",cutTongueImagePath);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // 获得URL数据库帮助器的一个实例
        mHelper = URLDBHelper.getInstance(this, 2);
        // 恢复页面，则打开数据库连接
        mHelper.openWriteLink();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    protected void onDestroy() {
//        if(confirmDialog != null) {
//            confirmDialog.dismiss();
//        }
//        if (editDialog != null) {
//            editDialog.dismiss();
//        }
        super.onDestroy();
    }

    public Bitmap getMagicDrawingCache(View view) {

        Bitmap.Config bitmap_quality = Bitmap.Config.RGB_565;
        boolean quick_cache = false;

        Bitmap bitmap = (Bitmap) view.getTag(R.id.cacheBitmapKey);
        Boolean dirty = (Boolean) view.getTag(R.id.cacheBitmapDirtyKey);
        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();

        if (bitmap == null || bitmap.getWidth() != viewWidth || bitmap.getHeight() != viewHeight) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bitmap = Bitmap.createBitmap(viewWidth, viewHeight, bitmap_quality);
            view.setTag(R.id.cacheBitmapKey, bitmap);
            dirty = true;
        }

        if (dirty == true || !quick_cache) {
//            bitmap.eraseColor(color_background);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            view.setTag(R.id.cacheBitmapDirtyKey, false);
        }
        return bitmap;
    }

    // 在判断文件能否保存时触发
    public boolean onCanSave(String absolutePath, String fileName) {
        return true;
    }

    // 点击文件保存对话框的确定按钮后触发
    public void onConfirmSave(String absolutePath, String fileName) {
        // 拼接文件的完整路径
        String path = String.format("%s/%s", absolutePath, fileName);
        // 把位图数据保存为图片文件
        BitmapUtil.saveBitmap(path, mBitmap, "jpg", 80);
        Toast.makeText(this, "成功保存图片文件：" + path, Toast.LENGTH_LONG).show();
    }

    // 点击文件选择对话框的确定按钮后触发
    public void onConfirmSelect(String absolutePath, String fileName, Map<String, Object> map_param) {
//        tv_intro.setVisibility(View.GONE);
//        // 拼接文件的完整路径
//        String path = String.format("%s/%s", absolutePath, fileName);
//        // 从指定路径的图片文件中获取位图数据
//        Bitmap bitmap = BitmapUtil.openBitmap(path);
//        // 设置位图视图的位图对象
//        bv_content.setImageBitmap(bitmap);
//        refreshImage(true);
    }

    // 检查文件是否合法时触发
    public boolean isFileValid(String absolutePath, String fileName, Map<String, Object> map_param) {
        return true;
    }

    // 刷新图像展示
    private void refreshImage(boolean is_first) {
        // 从位图视图bv_content的绘图缓存中获取位图对象
//        Bitmap bitmap = bv_content.getDrawingCache();
        Bitmap bitmap = getMagicDrawingCache(bv_content);
        // 设置美图视图的原始位图
        mv_content.setOrigBitmap(bitmap);
        if (is_first) { // 首次打开
            int left = bitmap.getWidth() / 4;
            int top = bitmap.getHeight() / 4;
            // 设置美图视图的位图边界
            boolean result = mv_content.setBitmapRect(new Rect(left, top, left * 2, top * 2));
        } else { // 非首次打开
            // 设置美图视图的位图边界
            mv_content.setBitmapRect(mv_content.getBitmapRect());
        }
    }

    // 在图片平移时触发
    public void onImageTraslate(int offsetX, int offsetY, boolean bReset) {
        // 设置位图视图的偏移距离
        bv_content.setOffset(offsetX, offsetY, bReset);
        refreshImage(false);
    }

    // 在图片缩放时触发
    public void onImageScale(float ratio) {
        // 设置位图视图的缩放比率
        bv_content.setScaleRatio(ratio, false);
        refreshImage(false);
    }

    // 在图片旋转时触发
    public void onImageRotate(int degree) {
        // 设置位图视图的旋转角度
        bv_content.setRotateDegree(degree, false);
        refreshImage(false);
    }

    // 在图片点击时触发
    public void onImageClick() {}

    // 在图片长按时触发
    public void onImageLongClick() {
        // 给美图视图注册上下文菜单
        registerForContextMenu(mv_content);
        // 为美图视图打开上下文菜单
        openContextMenu(mv_content);
        // 给美图视图注销上下文菜单
        unregisterForContextMenu(mv_content);
    }

    // 在创建上下文菜单时调用
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_meitu, menu);
    }

    // 在选中菜单项时调用
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_file_open) {
            // 打开文件选择对话框
            FileSelectFragment.show(this, new String[]{"jpg", "png"}, null);
        } else if (id == R.id.menu_file_save) {
            // 获取美图视图处理后的位图
            mBitmap = mv_content.getCropBitmap();
            // 打开文件保存对话框
            FileSaveFragment.show(this, "jpg");
        }
        return true;
    }

    private String TOJSON(String respond){
        String result = "";
        try{
            JSONObject obj = new JSONObject(respond);
            String health_index = obj.getString("health_index");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",mShared.getString("phone",""));
            jsonObject.put("record",health_index+",舌象分析");

            result = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("face record",result);
        return result;
    }

        private void showInputURLDialog(Boolean isURLExited) {
//            editDialog.setTitle("请输入舌像分析设备的网络地址");
//            editDialog.setYesOnclickListener("确定", new URLEditDialog.onYesOnclickListener() {
//                @Override
//                public void onYesClick(String url) {
//                    if (TextUtils.isEmpty(url)) {
//                        XToastUtils.error("请输入url地址");
//                    } else {
//                        if (isURLExited){
//                            URLInfo urlInfo = mHelper.queryByUsage("2");
//                            urlInfo.User_URL = url;
//                            mHelper.update(urlInfo);
//                        }
//                        else {
//                            URLInfo urlInfo = new URLInfo();
//
//                            urlInfo.User_URL = url;
//                            urlInfo.usage = "2";
//                            mHelper.insert(urlInfo);
//                        }
//                        XToastUtils.info(""+url);
//
//                        editDialog.dismiss();
//                        //让软键盘隐藏
////                        InputMethodManager imm = null;
////                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
////                            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
////                        }
////                        imm.hideSoftInputFromWindow(getView().getApplicationWindowToken(), 0);
//                    }
//                }
//            });
//            editDialog.setNoOnclickListener("取消", new URLEditDialog.onNoOnclickListener() {
//                @Override
//                public void onNoClick() {
//                    editDialog.dismiss();
//                }
//            });
//            editDialog.show();
        }

    private void showConfirmDialog(URLInfo urlInfo) {
//        confirmDialog.setTitle("将向以下分析设备地址发送请求");
//        confirmDialog.setConfirmURL(urlInfo.User_URL);
//        confirmDialog.setYesOnclickListener("确定", new URLConfirmDialog.onYesOnclickListener() {
//            @Override
//            public void onYesClick() {
//                // 获取美图视图处理后的位图
//                mBitmap = mv_content.getCropBitmap();
//                String image_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
//                String image_name = "result_tongue.png";
//                String full_path = String.format("%s/%s", image_path, image_name);
//                BitmapUtil.saveBitmap(full_path, mBitmap, "jpg", 80);
//
////                    String cut_face = bitmaptoString(mBitmap,100);
//
//                //将位图转为字节数组后再转为base64
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);   //切割图
////                bitmap_content.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);     //原图
//
//                waitingDialog.showListener(new DialogInterface.OnShowListener() {
//                    @Override
//                    public void onShow(DialogInterface dialogInterface) {
//                        final MaterialDialog dialog = (MaterialDialog)dialogInterface;
//                        new Thread(){
//                            @Override
//                            public void run() {
//                                super.run();
//                                Message message = Message.obtain();
//                                message.what = RESPONSE;
//                                Bundle bundle = new Bundle();
//
//                                String data = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
//
//                                //发起网络请求，传入base64数据
//                                String res = HttpRequestUtil.postImage(urlInfo.User_URL,data);
//
//                                bundle.putString("content",res);
//
//                                message.setData(bundle);
//                                mhandler.sendMessage(message);
//
//                                dialog.dismiss();
//                            }
//                        }.start();
//                    }
//                }).show() ;
//            }
//        });
//        confirmDialog.setNoOnclickListener("取消", new URLConfirmDialog.onNoOnclickListener() {
//            @Override
//            public void onNoClick() {
//                confirmDialog.dismiss();
//            }
//        });
//        confirmDialog.show();
    }
}
