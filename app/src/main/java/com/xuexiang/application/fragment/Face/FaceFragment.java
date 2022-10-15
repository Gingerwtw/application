package com.xuexiang.application.fragment.Face;

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
import com.xuexiang.application.activity.FaceResultFragmentActivity;
import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.application.database.URLInfo;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.databinding.FragmentFaceBinding;
import com.xuexiang.application.utils.BitmapUtil;
import com.xuexiang.application.utils.HttpRequestUtil;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.http.HttpReqData;
import com.xuexiang.application.utils.http.HttpRespData;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xrouter.utils.TextUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.application.dialog.URLEditDialog;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;
import com.xuexiang.xui.widget.dialog.strategy.impl.MaterialDialogStrategy;
import com.xuexiang.xui.widget.toast.XToast;

import java.io.ByteArrayOutputStream;

/**
 * 面像
 */
@Page(anim = CoreAnim.none)
public class FaceFragment extends Fragment implements OnClickListener {

    private Button btn_ana;
    private Button btn_get;
    private Button btn_change_url;
    private ImageView image;
    Bitmap bitmap = null;

    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象
    private final int GET_FACE_IMAGE = 0;
    private final int GET_FACE_IMAGE_FAILED = 2;
    private final int ANA_FACE_IMAGE = 1;
    private final int ANA_FACE_IMAGE_FAILED = 3;

    private int init = 0;

    private MaterialDialog.Builder confirmDialog;
    private MaterialDialog.Builder editDialog;
    private MaterialDialog.Builder waitingDialog;
    private MaterialDialog.Builder warningDialog;

    private DialogLoader mDialogLoader;

//    @NonNull
//    @Override
//    protected FragmentFaceBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
//        return FragmentFaceBinding.inflate(inflater, container, false);
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
//
//    }

//    @Override
//    protected void initListeners() {
//        binding.menuSettings.setOnSuperTextViewClickListener(this);
//        binding.menuAbout.setOnSuperTextViewClickListener(this);
//
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_face, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        confirmDialog = new URLConfirmDialog(getActivity());
//        editDialog = new URLEditDialog(getActivity());
        mDialogLoader = DialogLoader.getInstance().setIDialogStrategy(new MaterialDialogStrategy());

        warningDialog = new MaterialDialog.Builder(getContext())
                .title("错误")
                .positiveText("确认");

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

        confirmDialog = new MaterialDialog.Builder(getContext())
                .positiveText(R.string.lab_yes)
                .negativeText(R.string.lab_no);

        waitingDialog = new MaterialDialog.Builder(getContext())
//                .iconRes(R.drawable.icon_sex_man)
                    .limitIconToDefaultSize()
                    .title(R.string.waiting_dialog_title)
                    .content(R.string.waiting_dialog_content)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .negativeText(R.string.waiting_dialog_negativeText)
                    .autoDismiss(true);

        btn_ana = getActivity().findViewById(R.id.btn_face_ana);
        btn_get = getActivity().findViewById(R.id.btn_face_get);
//        btn_change_url = getActivity().findViewById(R.id.btn_face_change_url);
        image = getActivity().findViewById(R.id.face_image);
        btn_ana.setOnClickListener(this);
        btn_get.setOnClickListener(this);
//        btn_change_url.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获得URL数据库帮助器的一个实例
        mHelper = URLDBHelper.getInstance(getActivity(), 2);
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
    public void onDestroy() {
//        if(confirmDialog != null) {
//            confirmDialog.dismiss();
//        }
//        if (editDialog != null) {
//            editDialog.dismiss();
//        }
        super.onDestroy();
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
            }
            else if(message.what == ANA_FACE_IMAGE){
                Log.i("respond", message.getData().getString("content"));//打印返回的结果

                Intent intent = new Intent(getActivity(), FaceResultFragmentActivity.class);
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
        // 分析图像
        if (id == R.id.btn_face_ana) {
            if (bitmap == null){
                XToastUtils.error("请先获取图像");
            }
            else {
                URLInfo urlInfo = mHelper.queryByUsage("1");
                if (urlInfo == null) {
                    XToastUtils.info("请先设置面像分析设备地址！");
//                    showInputURLDialog(false, "1", "请输入面像分析设备地址");
                    editDialog.title("请输入面部分析地址")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String url = ((MaterialDialog)dialog).getInputEditText().getText().toString();
                            if (TextUtils.isEmpty(url)) {
                                XToastUtils.error("请输入url地址");
                            } else {
                                URLInfo urlInfo = new URLInfo();

                                urlInfo.User_URL = url;
                                urlInfo.usage = "1";
                                mHelper.insert(urlInfo);
                            }
                        }
                    }).show();
                }
                else {
//                    showConfirmDialog(urlInfo);
                    confirmDialog.title(R.string.confirm_dialog_title)
                            .content(urlInfo.User_URL)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //将位图转为字节数组后再转为base64
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                                    waitingDialog.showListener(new DialogInterface.OnShowListener() {
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
                                                    String res = HttpRequestUtil.postImage(urlInfo.User_URL,data);
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
                            }).show();
                }
            }
        }
        // 从采集设备获取图像
        else if (id == R.id.btn_face_get) {
            URLInfo urlInfo = mHelper.queryByUsage("0");
            if (urlInfo == null) {
                XToastUtils.info("请先设置采集设备地址！");
//                showInputURLDialog(false, "0", "请输入采集设备地址");
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

                                HttpReqData req_data = new HttpReqData(urlInfo.User_URL);
                                HttpRespData resp_data = HttpRequestUtil.getImage(req_data);
                                Log.d("respond", String.valueOf(resp_data.bitmap));

                                if (resp_data.bitmap == null){
                                    message.what = GET_FACE_IMAGE_FAILED;
                                }
                                else{
                                    Bundle bundle = new Bundle();
                                    message.what = GET_FACE_IMAGE;

                                    String image_path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
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
    }

    private void showInputURLDialog(Boolean isURLExited, String usage, String title) {
//        editDialog.setTitle(title);
//        editDialog.setYesOnclickListener("确定", new URLEditDialog.onYesOnclickListener() {
//            @Override
//            public void onYesClick(String url) {
//                if (TextUtils.isEmpty(url)) {
//                    XToastUtils.error("请输入url地址");
//                } else {
//                    if (isURLExited){
//                        URLInfo urlInfo = mHelper.queryByUsage(usage);
//                        urlInfo.User_URL = url;
//                        mHelper.update(urlInfo);
//                    }
//                    else {
//                        URLInfo urlInfo = new URLInfo();
//
//                        urlInfo.User_URL = url;
//                        urlInfo.usage = usage;
//                        mHelper.insert(urlInfo);
//                    }
//                    XToastUtils.info(""+url);
//
//                    editDialog.dismiss();
//                    //让软键盘隐藏
//                    InputMethodManager imm = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
//                    }
//                    imm.hideSoftInputFromWindow(getView().getApplicationWindowToken(), 0);
//                }
//            }
//        });
//        editDialog.setNoOnclickListener("取消", new URLEditDialog.onNoOnclickListener() {
//            @Override
//            public void onNoClick() {
//                editDialog.dismiss();
//            }
//        });
//        editDialog.show();
    }

    private void showInputDialog(){
        InputInfo inputInfo = new InputInfo(InputType.TYPE_CLASS_TEXT);
        inputInfo.setHint("hint");

//        mDialogLoader.showInputDialog(getContext(),)
    }

    private void showConfirmDialog(URLInfo urlInfo) {
//        confirmDialog.setTitle("将向以下分析设备地址发送请求");
//        confirmDialog.setConfirmURL(urlInfo.User_URL);
//        confirmDialog.setYesOnclickListener("确定", new URLConfirmDialog.onYesOnclickListener() {
//            @Override
//            public void onYesClick() {
//                //将位图转为字节数组后再转为base64
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//
//                new Thread() {
//                    @Override
//                    public void run () {
//                        super.run();
//
//                        Message message = Message.obtain();
//                        message.what = ANA_FACE_IMAGE;
//                        Bundle bundle = new Bundle();
//
//                        String data = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
//                        //发起网络请求，传入base64数据
//                        String res = HttpRequestUtil.postImage(urlInfo.User_URL,data);
//
//                        bundle.putString("content",res);
//
//                        message.setData(bundle);
//                        mhandler.sendMessage(message);
//                    }
//                }.start();
//                waitingDialog.show();
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
