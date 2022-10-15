package com.xuexiang.application.fragment.ChangeURL;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.xuexiang.application.R;
import com.xuexiang.application.activity.TakePictureActivity;
import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.application.databinding.FragmentTrendingBinding;
import com.xuexiang.application.widget.CameraView;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;


/**

 */
@Page(anim = CoreAnim.none)
public class CameraFragment extends BaseFragment<FragmentTrendingBinding> {

    private FrameLayout fl_content; // 声明一个框架布局对象
    private ImageView iv_photo; // 声明一个图像视图对象
    private GridView gv_shooting; // 声明一个网格视图对象
    private Button take_photo;
    private static int requestCodeCamera = 101;

    @NonNull
    @Override
    protected FragmentTrendingBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentTrendingBinding.inflate(inflater, container, false);
    }

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 从布局文件中获取名叫fl_content的框架布局
        fl_content = getActivity().findViewById(R.id.fl_content);
        // 从布局文件中获取名叫iv_photo的图像视图
        iv_photo = getActivity().findViewById(R.id.iv_photo);
        // 从布局文件中获取名叫gv_shooting的网格视图
        gv_shooting = getActivity().findViewById(R.id.gv_shooting);

        take_photo = getActivity().findViewById(R.id.btn_catch_behind);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请权限
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA};
                checkPermission(getActivity(),permissions, requestCodeCamera);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle resp = intent.getExtras(); // 获取返回的包裹
        String is_null = resp.getString("is_null");
        if (!TextUtils.isEmpty(is_null) && !is_null.equals("yes")) { // 有发生拍照动作
            int type = resp.getInt("type");
            if (type == 0) { // 单拍。一次只拍一张
                iv_photo.setVisibility(View.VISIBLE);
                gv_shooting.setVisibility(View.GONE);
                String path = resp.getString("path");
                fillBitmap(BitmapFactory.decodeFile(path, null));
            }
//            else if (type == 1) { // 连拍。一次连续拍了好几张
//                iv_photo.setVisibility(View.GONE);
//                gv_shooting.setVisibility(View.VISIBLE);
//                ArrayList<String> pathList = resp.getStringArrayList("path_list");
//                Log.d(TAG, "pathList.size()=" + pathList.size());
//                // 通过网格视图展示连拍的数张照片
//                ShootingAdapter adapter = new ShootingAdapter(this, pathList);
//                gv_shooting.setAdapter(adapter);
//            }
        }
    }

    public boolean checkPermission(Activity activity, String[] permissions, int resultCode){
        boolean result = true;
        // 只对Android6.0以上系统进行权限检查
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int check = PackageManager.PERMISSION_GRANTED;
            for (String permission:permissions){
                check = ContextCompat.checkSelfPermission(activity,permission);
                if(check != PackageManager.PERMISSION_GRANTED){
                    break;
                }
            }
            if(check != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(activity,permissions,resultCode);
                result = false;
                requestPermissions(permissions, resultCode);
            }else {
                doTakePicture();
//                camera_view.doTakePicture();
            }
        }
        return result;
    }

    public void doTakePicture(){
        // 打开后置摄像头（未指定摄像头编号的话，默认就是打开后置摄像头）
        Camera mCamera = Camera.open();
        if (mCamera != null) {
            mCamera.release(); // 释放摄像头
            // 前往Camera的拍照页面
            Intent intent = new Intent(getActivity(), TakePictureActivity.class);
            // 类型为后置摄像头
            intent.putExtra("type", CameraView.CAMERA_BEHIND);
            // 需要处理拍照页面的返回结果
            startActivityForResult(intent, 1);
        } else {
            Toast.makeText(getActivity(),"当前设备不支持后置摄像头", Toast.LENGTH_SHORT).show();
        }
    }

    // 以合适比例显示照片
    private void fillBitmap(Bitmap bitmap) {
        // 位图的高度大于框架布局的高度，则按比例调整图像视图的宽高
        if (bitmap.getHeight() > fl_content.getMeasuredHeight()) {
            ViewGroup.LayoutParams params = iv_photo.getLayoutParams();
            params.height = fl_content.getMeasuredHeight();
            params.width = bitmap.getWidth() * fl_content.getMeasuredHeight() / bitmap.getHeight();
            // 设置iv_photo的布局参数
            iv_photo.setLayoutParams(params);
        }
        // 设置iv_photo的拉伸类型为居中
        iv_photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        // 设置iv_photo的位图对象
        iv_photo.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCodeCamera) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                doTakePicture();
//                camera_view.doTakePicture();
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "访问被拒绝！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
