package com.xuexiang.application.fragment.Tongue;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xuexiang.application.R;
import com.xuexiang.application.activity.TongueSeparationActivity;
import com.xuexiang.application.database.URLInfo;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.utils.BitmapUtil;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xrouter.utils.TextUtils;
import com.xuexiang.application.dialog.URLEditDialog;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 舌象fragment
 */
@Page(anim = CoreAnim.none)
public class TongueFragment extends Fragment implements OnClickListener {

    private Button btn_ana;
    private Button btn_get;
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

//    @NonNull
//    @Override
//    protected FragmentTongueBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
//        return FragmentTongueBinding.inflate(inflater, container, false);
//    }
//
//    /**
//     * @return 返回为 null意为不需要导航栏
//     */
//    @Override
//    protected TitleBar initTitle() {
//        return null;
//    }
//
//    /**
//     * 初始化控件
//     */
//    @Override
//    protected void initViews() {
//
//    }

//    @Override
//    protected void initListeners() {
//        binding.menuSettings.setOnSuperTextViewClickListener(this);
//        binding.menuAbout.setOnSuperTextViewClickListener(this);
//
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tongue, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_ana = getActivity().findViewById(R.id.btn_tongue_ana);
        btn_get = getActivity().findViewById(R.id.btn_tongue_get);
//        btn_change_url = getActivity().findViewById(R.id.btn_tongue_change_url);
        image = getActivity().findViewById(R.id.tongue_image);
        btn_ana.setOnClickListener(this);
        btn_get.setOnClickListener(this);
//        btn_change_url.setOnClickListener(this);

        warningDialog = new MaterialDialog.Builder(getContext())
                .title("错误")
                .content("向采集设备发送网络请求失败")
                .positiveText("确认");

        waitingDialog = new MaterialDialog.Builder(getContext())
//                .iconRes(R.drawable.icon_sex_man)
                .limitIconToDefaultSize()
                .title(R.string.waiting_dialog_title)
                .content(R.string.waiting_dialog_content)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .negativeText(R.string.waiting_dialog_negativeText)
                .autoDismiss(true);

        editDialog = new MaterialDialog.Builder(getContext())
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        XToast.normal(getActivity(),input).show();
                    }
                })
                .positiveText("确认")
                .negativeText("取消");
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获得URL数据库帮助器的一个实例
        mHelper = URLDBHelper.getInstance(getActivity(), 3);
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
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if(init == 0){
                init = 1;
            }
            else{
                image.setImageBitmap(bitmap);
            }
        }
        else {
            bitmap = null;
        }
    }

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
        if (id == R.id.btn_tongue_ana) {
            if (bitmap == null){
                XToastUtils.error("请先获取图像");
            }
            else {

//                BitmapUtil instance = BitmapUtil.getInstance();
//                instance.setEditBitmap(bitmap);

                String image_path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
                String image_name = "ana_face.png";
                String full_path = String.format("%s/%s", image_path, image_name);
                BitmapUtil.saveBitmap(full_path, bitmap, "jpg", 80);

                Intent intent = new Intent(getActivity(), TongueSeparationActivity.class);
                intent.putExtra("path",full_path);

//                ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                byte [] bitmapByte =baos.toByteArray();
//                intent.putExtra("bitmap", bitmapByte);
//                startActivityForResult(intent, 1);
                startActivityForResult(intent,200);
//                getActivity().setResult(1,intent);
            }

        }
        // 从采集设备获取图像
        else if (id == R.id.btn_tongue_get) {
//            image.setImageResource(R.drawable.test_picture);
            URLInfo urlInfo = mHelper.queryByUsage("3");

            if (urlInfo == null) {
                XToastUtils.info("请先设置采集设备地址！");
//                showDialog(false);
                editDialog.title("请输入采集地址")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String url = ((MaterialDialog)dialog).getInputEditText().getText().toString();
                                if (TextUtils.isEmpty(url)) {
                                    XToastUtils.error("请输入url地址");
                                } else {
                                    URLInfo urlInfo = new URLInfo();

                                    urlInfo.User_URL = url;
                                    urlInfo.usage = "0";
                                    mHelper.insert(urlInfo);
                                }
                            }
                        }).show();
            }
            else {
                waitingDialog.showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        final MaterialDialog dialog = (MaterialDialog)dialogInterface;
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Message message = Message.obtain();


                                // 创建一个HTTP请求对象
                                HttpReqData req_data = new HttpReqData(urlInfo.User_URL);
                                // 发送HTTP请求信息，并获得HTTP应答对象
                                HttpRespData resp_data = HttpRequestUtil.getImage(req_data);

                                if (resp_data.bitmap == null){
                                    message.what = GET_TONGUE_IMAGE_FAILED;
                                }
                                else {
                                    message.what = GET_TONGUE_IMAGE;
                                    Bundle bundle = new Bundle();

                                    String image_path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
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

//            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_picture);
//            if (bitmap == null){
//                XToastUtils.error("获取图像失败，请重试");
//            }
//            else{
//                image.setImageBitmap(bitmap);
//            }

        }
    }

    private void showDialog(Boolean isURLExited) {
        final URLEditDialog editDialog = new URLEditDialog(getActivity());
        editDialog.setTitle("请输入采集设备的网络地址");
        editDialog.setYesOnclickListener("确定", new URLEditDialog.onYesOnclickListener() {
            @Override
            public void onYesClick(String url) {
                if (TextUtils.isEmpty(url)) {
                    XToastUtils.error("请输入url地址");
                } else {
                    if (isURLExited){
                        URLInfo urlInfo = mHelper.queryByUsage("0");
                        urlInfo.User_URL = url;
                        mHelper.update(urlInfo);
                    }
                    else {
                        URLInfo urlInfo = new URLInfo();

                        urlInfo.User_URL = url;
                        urlInfo.usage = "0";
                        mHelper.insert(urlInfo);
                    }
                    XToastUtils.info(""+url);

                    editDialog.dismiss();
                    //让软键盘隐藏
                    InputMethodManager imm = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    }
                    imm.hideSoftInputFromWindow(getView().getApplicationWindowToken(), 0);
                }
            }
        });
        editDialog.setNoOnclickListener("取消", new URLEditDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }
}
