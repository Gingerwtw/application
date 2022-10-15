package com.xuexiang.application.activity;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseActivity;
import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.application.databinding.ActivityMainBinding;
import com.xuexiang.application.fragment.Adviser.AdviserFragment;
import com.xuexiang.application.fragment.ChangeURL.ChangeURLFragment;
import com.xuexiang.application.fragment.Diagnosis.DiagnosisFragment;
import com.xuexiang.application.fragment.Face.FaceFragment;
import com.xuexiang.application.fragment.KnowledgeGraph.KnowledgeGraphFragment;
import com.xuexiang.application.fragment.LungVolume.LungVolumeFragment;
import com.xuexiang.application.fragment.Personal.PersonalFragment;
import com.xuexiang.application.fragment.other.AboutFragment;
import com.xuexiang.application.fragment.other.SettingsFragment;
import com.xuexiang.application.fragment.Tongue.TongueFragment;
import com.xuexiang.application.utils.Utils;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.application.utils.sdkinit.XUpdateInit;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.utils.WidgetUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.display.Colors;

/**
 * 程序主页面
 */
public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, ClickUtils.OnClick2ExitListener, Toolbar.OnMenuItemClickListener {

    private String[] mTitles;
    public static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;
    private Button takephoto;
    private TextView toolbar_title;

    @Override
    protected ActivityMainBinding viewBindingInflate(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initData();
        initListeners();

    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    private void initViews() {
        WidgetUtils.clearActivityBackground(this);

        mTitles = ResUtils.getStringArray(R.array.home_titles);
//        toolbar_title = findViewById(R.id.main_toolbar_title);
//        toolbar_title.setText(mTitles[0]);
        binding.includeMain.toolbar.setTitle(mTitles[0]);

        binding.includeMain.toolbar.inflateMenu(R.menu.menu_main);
        binding.includeMain.toolbar.setOnMenuItemClickListener(this);
//        binding.includeMain.toolbar.setNavigationIcon(R.drawable.check_choose);

        initHeader();

        //主页内容填充
        Fragment[] fragments = new Fragment[]{

                new DiagnosisFragment(),
                new AdviserFragment(),
                new KnowledgeGraphFragment(),
                new PersonalFragment(),
        };
        FragmentAdapter<Fragment> adapter = new FragmentAdapter<>(getSupportFragmentManager(), fragments);
        binding.includeMain.viewPager.setOffscreenPageLimit(mTitles.length - 1);
        binding.includeMain.viewPager.setAdapter(adapter);
    }

    private void initData() {
//        GuideTipsDialog.showTips(this);               打开app时的弹窗
        XUpdateInit.checkUpdate(this, false);
    }

    private void initHeader() {
        binding.navView.setItemIconTintList(null);
        View headerView = binding.navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        RadiusImageView ivAvatar = headerView.findViewById(R.id.iv_avatar);
        TextView tvAvatar = headerView.findViewById(R.id.tv_avatar);
        TextView tvSign = headerView.findViewById(R.id.tv_sign);

        if (Utils.isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar.setTextColor(Colors.WHITE);
            tvSign.setTextColor(Colors.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_white));
            }
        } else {
            tvAvatar.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text));
            tvSign.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_explain_text));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_gray_3));
            }
        }
//
//        // TODO: 2019-10-09 初始化数据
        ivAvatar.setImageResource(R.drawable.ic_default_head);
        tvAvatar.setText(R.string.app_name);
        tvSign.setText("这个家伙很懒，什么也没有留下～～");
        navHeader.setOnClickListener(this);
    }

    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.includeMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏点击事件
        binding.navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isCheckable()) {
                binding.drawerLayout.closeDrawers();
                return handleNavigationItemSelected(menuItem);
            } else {
                int id = menuItem.getItemId();
                if (id == R.id.nav_settings) {
                    openNewPage(SettingsFragment.class);
                } else if (id == R.id.nav_about) {
                    openNewPage(AboutFragment.class);
                } else {
                    XToastUtils.toast("点击了:" + menuItem.getTitle());
                }
            }
            return true;
        });
        //主页事件监听
        binding.includeMain.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                XToastUtils.toast(""+position);
                MenuItem item = binding.includeMain.bottomNavigation.getMenu().getItem(position);
                binding.includeMain.toolbar.setTitle(item.getTitle());
//                toolbar_title.setText(item.getTitle());
                item.setChecked(true);
                updateSideNavStatus(item);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.includeMain.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * 处理侧边栏点击事件
     *
     * @param menuItem
     * @return
     */
    private boolean handleNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
//        XToastUtils.toast(""+index);
        if (index != -1) {
            binding.includeMain.toolbar.setTitle(menuItem.getTitle());
//            toolbar_title.setText(menuItem.getTitle());
            binding.includeMain.viewPager.setCurrentItem(index, false);
            return true;
        }
        return false;
    }

    @Override
    // 头部右侧两个图标的点击事件
    public boolean onMenuItemClick(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_privacy) {
//            GuideTipsDialog.showTipsForce(this);
//        } else if (id == R.id.action_about) {
//            openNewPage(AboutFragment.class);
//        }
//        if (id == R.id.action_about) {
//            openNewPage(AboutFragment.class);
//        }
        return false;
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.nav_header) {
            XToastUtils.toast("点击头部！");
        }
    }

    //================Navigation================//

    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            binding.includeMain.toolbar.setTitle(menuItem.getTitle());
//            toolbar_title.setText(menuItem.getTitle());
            binding.includeMain.viewPager.setCurrentItem(index, false);

            updateSideNavStatus(menuItem);
            return true;
        }
        return false;
    }

    /**
     * 更新侧边栏菜单选中状态
     *
     * @param menuItem
     */
    private void updateSideNavStatus(MenuItem menuItem) {
        MenuItem side = binding.navView.getMenu().findItem(menuItem.getItemId());
        if (side != null) {
            side.setChecked(true);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        XToastUtils.toast("再按一次退出程序");
    }

    @Override
    public void onExit() {
        XUtil.exitApp();
    }

}
