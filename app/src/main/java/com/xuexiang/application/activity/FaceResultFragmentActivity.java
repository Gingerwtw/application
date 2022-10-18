package com.xuexiang.application.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import androidx.fragment.app.FragmentTabHost;

import com.xuexiang.application.FaceColor;
import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.fragment.faceResult.ColorFragment;
import com.xuexiang.application.fragment.faceResult.FaceResultFragment;
import com.xuexiang.application.fragment.faceResult.TabSecondFragment;

import org.json.JSONException;
import org.json.JSONObject;


public class FaceResultFragmentActivity extends BaseActivity {
    private static final String TAG = "FaceResultFragmentActivity";
    private FragmentTabHost tabHost; // 声明一个碎片标签栏对象

    private ImageView face_result_image;

    private JSONObject middle_top;
    private JSONObject middle_middle;
    private JSONObject middle_bottom;
    private JSONObject left;
    private JSONObject right;
    private String health_index;
    private String correct_face_img;
//    private Bundle faceColorMiddleTopBundle;
//    private Bundle faceColorMiddleMiddleBundle;
//    private Bundle faceColorMiddleBottomBundle;
//    private Bundle faceColorLeftBundle;
//    private Bundle faceColorRightBundle;
    private Bundle faceColorBundle;
    private Bundle faceVeinBundle;
    private Bundle faceResultBundle;

    public final static String FACE_COLOR_MIDDLE_TOP = "FACE_COLOR_MIDDLE_TOP";
    public final static String FACE_COLOR_MIDDLE_MIDDLE = "FACE_COLOR_MIDDLE_MIDDLE";
    public final static String FACE_COLOR_MIDDLE_BOTTOM = "FACE_COLOR_MIDDLE_BOTTOM";
    public final static String FACE_COLOR_LEFT = "FACE_COLOR_LEFT";
    public final static String FACE_COLOR_RIGHT = "FACE_COLOR_RIGHT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("faceresult","start activity");
        setContentView(R.layout.activity_face_result_fragment);
        face_result_image = findViewById(R.id.face_result_image);

        Log.d("faceresult","start init");

        initResult();
        initBundle();

        Log.d("faceresult","finish init");

        faceResultBundle = new Bundle(); // 创建一个包裹对象
        faceResultBundle.putString("health_index", health_index);

        // 从布局文件中获取名叫tabhost的碎片标签栏
        tabHost = findViewById(android.R.id.tabhost);
        // 把实际的内容框架安装到碎片标签栏
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 往标签栏添加第一个标签，其中内容视图展示TabFirstFragment
        tabHost.addTab(getTabView(R.string.title_color, R.drawable.ic_home_black_24dp),
                ColorFragment.class, faceColorBundle);
        // 往标签栏添加第二个标签，其中内容视图展示TabSecondFragment
        tabHost.addTab(getTabView(R.string.title_vein, R.drawable.ic_dashboard_black_24dp),
                TabSecondFragment.class, faceResultBundle);
        // 往标签栏添加第三个标签，其中内容视图展示TabThirdFragment
        tabHost.addTab(getTabView(R.string.title_result, R.drawable.ic_notifications_black_24dp),
                FaceResultFragment.class, faceResultBundle);
        // 不显示各标签之间的分隔线
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        Bitmap bitmap = Base64tobitmep(correct_face_img);
        face_result_image.setImageBitmap(bitmap);
        face_result_image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Log.d("faceresult","all finish");
    }

    private  void initBundle(){
        faceColorBundle = new Bundle();
        setFaceColorBundle(faceColorBundle, middle_top, FACE_COLOR_MIDDLE_TOP);
        setFaceColorBundle(faceColorBundle, middle_middle, FACE_COLOR_MIDDLE_MIDDLE);
        setFaceColorBundle(faceColorBundle, middle_bottom, FACE_COLOR_MIDDLE_BOTTOM);
        setFaceColorBundle(faceColorBundle, left, FACE_COLOR_LEFT);
        setFaceColorBundle(faceColorBundle, right, FACE_COLOR_RIGHT);
    }

    private void setFaceColorBundle(Bundle bundle, JSONObject jsonObject,String key){
        FaceColor faceColor = new FaceColor();
        try {
            faceColor.setBlack((int)jsonObject.getDouble("black"));
            faceColor.setYellow((int)jsonObject.getDouble("yellow"));
            faceColor.setLight_yellow((int)jsonObject.getDouble("light_yellow"));
            faceColor.setGrey((int)jsonObject.getDouble("gray"));
            faceColor.setDeep_red((int)jsonObject.getDouble("deep_red"));
            faceColor.setRed((int)jsonObject.getDouble("red"));
            Log.d("colorred", String.valueOf(jsonObject.getDouble("red")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle.putSerializable(key, faceColor);
    }

    private void initResult(){
        Bundle bundle=getIntent().getExtras();

        String respondJson = bundle.getString("face_result");
        try {
            JSONObject obj = new JSONObject(respondJson);
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
    }

    private Bitmap Base64tobitmep(String base){
        Bitmap bitmap = null;
        try {
            byte[] bitmapByte = Base64.decode(base, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // 根据字符串和图标的资源编号，获得对应的标签规格
    private TabSpec getTabView(int textId, int imgId) {
        // 根据资源编号获得字符串对象
        String text = getResources().getString(textId);
        // 根据资源编号获得图形对象
        Drawable drawable = getResources().getDrawable(imgId);
        // 设置图形的四周边界。这里必须设置图片大小，否则无法显示图标
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        // 根据布局文件item_tabbar.xml生成标签按钮对象
        View item_tabbar = getLayoutInflater().inflate(R.layout.face_item_tabbar, null);
        TextView tv_item = item_tabbar.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        // 在文字上方显示标签的图标
        tv_item.setCompoundDrawables(null, drawable, null, null);
        // 生成并返回该标签按钮对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
    }
}
