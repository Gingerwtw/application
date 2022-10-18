package com.xuexiang.application.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.databinding.ActivityTongueResultFragmentBinding;
import com.xuexiang.application.fragment.tongueResult.TabForthFragment;
import com.xuexiang.application.fragment.tongueResult.TabSecondFragment;
import com.xuexiang.application.fragment.tongueResult.TabThirdFragment;
import com.xuexiang.application.fragment.tongueResult.TongueColorFragment;
import com.xuexiang.application.fragment.tongueResult.TongueResultFragment;
import com.xuexiang.application.utils.BitmapUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TongueResultFragmentActivity extends BaseActivity<ActivityTongueResultFragmentBinding> implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TongueResultFragmentActivity";
    private FragmentTabHost tabHost; // 声明一个碎片标签栏对象

    private String[] mTitles;
    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    List<Fragment> list;

    private ImageView tongue_result_image;
    private Button show_coating;
    private Button hide_coating;

    private JSONObject substance;
    private JSONObject coating;
    private String health_index;
    private String coating_img;

    private Bitmap originBitmap;
    private Bitmap resultBitmap;

    private Bundle TongueColorBundle;
    private Bundle faceVeinBundle;
    private Bundle faceResultBundle;

    @Override
    protected ActivityTongueResultFragmentBinding viewBindingInflate(LayoutInflater inflater) {
        return ActivityTongueResultFragmentBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_result_fragment);

        tongue_result_image = findViewById(R.id.tongue_result_image);
        show_coating = (Button)findViewById(R.id.tongue_result_image_show_coating);
        hide_coating = (Button)findViewById(R.id.tongue_result_image_hide_coating);
        show_coating.setOnClickListener(this);
        hide_coating.setOnClickListener(this);

        initResult();
        initBundle();
//        initViews();
//        initFragment();
//        initViewPager();
//        initNavigation();

        faceResultBundle = new Bundle(); // 创建一个包裹对象
        faceResultBundle.putString("health_index", health_index);

        tabHost = findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//        tabHost.addTab(getTabView(R.string.title_color, R.drawable.ic_home_black_24dp),
//                TongueColorFragment.class, TongueColorBundle);
//        tabHost.addTab(getTabView(R.string.title_vein, R.drawable.ic_dashboard_black_24dp),
//                TabSecondFragment.class, faceResultBundle);
//        tabHost.addTab(getTabView(R.string.title_tangible_material, R.drawable.ic_dashboard_black_24dp),
//                TabThirdFragment.class, faceResultBundle);
//        tabHost.addTab(getTabView(R.string.title_geometry, R.drawable.ic_dashboard_black_24dp),
//                TabForthFragment.class, faceResultBundle);
//        tabHost.addTab(getTabView(R.string.title_result, R.drawable.ic_notifications_black_24dp),
//                TongueResultFragment.class, faceResultBundle);

        tabHost.addTab(getTabView(R.string.title_color),
                TongueColorFragment.class, TongueColorBundle);
        tabHost.addTab(getTabView(R.string.title_vein),
                TabSecondFragment.class, faceResultBundle);
        tabHost.addTab(getTabView(R.string.title_tangible_material),
                TabThirdFragment.class, faceResultBundle);
        tabHost.addTab(getTabView(R.string.title_geometry),
                TabForthFragment.class, faceResultBundle);
        tabHost.addTab(getTabView(R.string.title_result),
                TongueResultFragment.class, faceResultBundle);
        tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        resultBitmap = Base64tobitmep(coating_img);
        originBitmap = BitmapUtil.openBitmap(getIntent().getStringExtra("path"));
        tongue_result_image.setImageBitmap(resultBitmap);
    }

    private void initNavigation(){
//        bottomNavigationView = findViewById(R.id.tongue_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @SuppressLint("NonConstantResourceId")
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                // 获得点击的item
//                String title = item.getTitle().toString();
//                // 根据点击的item项目切换对应的Fragment
//                switch (item.getItemId()) {
//                    case R.id.tongue_nav_color:
//                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.tongue_nav_vein:
//                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.tongue_nav_tangible_material:
//                        viewPager.setCurrentItem(2);
//                        break;
//                    case R.id.tongue_nav_geometry:
//                        viewPager.setCurrentItem(3);
//                        break;
//                    case R.id.tongue_nav_result:
//                        viewPager.setCurrentItem(4);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void initViewPager(){
//        viewPager = findViewById(R.id.tongue_view_pager);
//        viewPager.setAdapter(new MyViewPager2Adapter (this, list));
//
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                bottomNavigationView.getMenu().getItem(position).setChecked(true);
//            }
//        });
    }

    private void initFragment(){
//        list = new ArrayList<>();
//        list.add(new TongueColorFragment());
//        list.add(new TabSecondFragment());
//        list.add(new TabThirdFragment());
//        list.add(new TabForthFragment());
//        list.add(new TongueResultFragment());
    }

    private  void initBundle(){
        TongueColorBundle = new Bundle();
        setTongueColorBundle(TongueColorBundle);
    }

    private void setTongueColorBundle(Bundle bundle){
        String[] tongue_substance_color = new String[8];
        String[] tongue_substance_color_name = new String[8];
        String[] tongue_coating_color = new String[4];
        String[] tongue_coating_color_name = new String[4];
        int tongue_substance_color_count = 0;
        int tongue_coating_color_count = 0;

        try {
            if (substance.has("Cyan")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Cyan")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "青色";
                tongue_substance_color_count++;
            }
            if (substance.has("Red")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Red")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "青色";
                tongue_substance_color_count++;
            }
            if (substance.has("Light purple")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Light purple")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "淡紫色";
                tongue_substance_color_count++;
            }
            if (substance.has("Deep red")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Deep red")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "绛红色";
                tongue_substance_color_count++;
            }
            if (substance.has("Light red")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Light red")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "淡红色";
                tongue_substance_color_count++;
            }
            if (substance.has("Blue")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Blue")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "蓝色";
                tongue_substance_color_count++;
            }
            if (substance.has("Light blue")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Light blue")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "淡蓝色";
                tongue_substance_color_count++;
            }
            if (substance.has("Purple")){
                tongue_substance_color[tongue_substance_color_count] = (int)substance.getDouble("Purple")+"%";
                tongue_substance_color_name[tongue_substance_color_count] = "紫色";
                tongue_substance_color_count++;
            }

            if(coating.has("Black")){
                tongue_coating_color[tongue_coating_color_count] = (int)coating.getDouble("Black")+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "黑色";
                tongue_coating_color_count++;
            }
            if(coating.has("Gray")){
                tongue_coating_color[tongue_coating_color_count] = (int)coating.getDouble("Gray")+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "灰色";
                tongue_coating_color_count++;
            }
            if(coating.has("White")){
                tongue_coating_color[tongue_coating_color_count] = (int)coating.getDouble("White")+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "白色";
                tongue_coating_color_count++;
            }
            if(coating.has("Yellow")){
                tongue_coating_color[tongue_coating_color_count] = (int)coating.getDouble("Yellow")+"%";
                tongue_coating_color_name[tongue_coating_color_count] = "黄色";
                tongue_coating_color_count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bundle.putStringArray("substance",tongue_substance_color);
        bundle.putStringArray("coating",tongue_coating_color);
        bundle.putStringArray("substance_name",tongue_substance_color_name);
        bundle.putStringArray("coating_name",tongue_coating_color_name);
        bundle.putInt("substance_length",tongue_substance_color_count);
        bundle.putInt("coating_length",tongue_coating_color_count);
    }

    private void initResult(){
        Bundle bundle=getIntent().getExtras();

        String respondJson = bundle.getString("tongue_result");
        try {
            JSONObject obj = new JSONObject(respondJson);
            substance = obj.getJSONObject("substance");
            coating = obj.getJSONObject("coating");
            health_index = obj.getString("health_index");
            coating_img = obj.getString("coating_img");

            Log.d("health_index",health_index);

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
//    private TabHost.TabSpec getTabView(int textId, int imgId) {
//        // 根据资源编号获得字符串对象
//        String text = getResources().getString(textId);
//        // 根据资源编号获得图形对象
//        Drawable drawable = getResources().getDrawable(imgId);
//        // 设置图形的四周边界。这里必须设置图片大小，否则无法显示图标
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        // 根据布局文件item_tabbar.xml生成标签按钮对象
//        View item_tabbar = getLayoutInflater().inflate(R.layout.face_item_tabbar, null);
//        TextView tv_item = item_tabbar.findViewById(R.id.tv_item_tabbar);
//        tv_item.setText(text);
//        // 在文字上方显示标签的图标
//        tv_item.setCompoundDrawables(null, drawable, null, null);
//        // 生成并返回该标签按钮对应的标签规格
//        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
//    }

    private TabHost.TabSpec getTabView(int textId) {
        // 根据资源编号获得字符串对象
        String text = getResources().getString(textId);

        // 根据布局文件item_tabbar.xml生成标签按钮对象
        View item_tabbar = getLayoutInflater().inflate(R.layout.face_item_tabbar, null);
        TextView tv_item = item_tabbar.findViewById(R.id.tv_item_tabbar);
        tv_item.setText(text);
        // 在文字上方显示标签的图标
        tv_item.setCompoundDrawables(null, null, null, null);
        // 生成并返回该标签按钮对应的标签规格
        return tabHost.newTabSpec(text).setIndicator(item_tabbar);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tongue_result_image_hide_coating) {
            tongue_result_image.setImageBitmap(originBitmap);
        }
        else if (id == R.id.tongue_result_image_show_coating) {
            tongue_result_image.setImageBitmap(resultBitmap);
        }
    }

    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
//        Log.d("tongue",""+index);
//        if (index != -1) {
//            binding.tongueViewPager.setCurrentItem(index, false);
//
//            return true;
//        }
        return false;
    }
}