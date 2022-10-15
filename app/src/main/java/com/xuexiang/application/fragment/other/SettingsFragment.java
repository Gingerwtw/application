

package com.xuexiang.application.fragment.other;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.application.databinding.FragmentSettingsBinding;
import com.xuexiang.application.utils.TokenUtils;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.XUtil;

/**

 */
@Page(name = "设置")
public class SettingsFragment extends BaseFragment<FragmentSettingsBinding> implements SuperTextView.OnSuperTextViewClickListener {

    @NonNull
    @Override
    protected FragmentSettingsBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return FragmentSettingsBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initViews() {
        binding.menuCommon.setOnSuperTextViewClickListener(this);
        binding.menuPrivacy.setOnSuperTextViewClickListener(this);
        binding.menuPush.setOnSuperTextViewClickListener(this);
        binding.menuHelper.setOnSuperTextViewClickListener(this);
        binding.menuChangeAccount.setOnSuperTextViewClickListener(this);
        binding.menuLogout.setOnSuperTextViewClickListener(this);
    }

    @SingleClick
    @Override
    public void onClick(SuperTextView superTextView) {
        int id = superTextView.getId();
        if (id  == R.id.menu_common || id  == R.id.menu_privacy || id == R.id.menu_push || id  == R.id.menu_helper) {
            XToastUtils.toast(superTextView.getLeftString());
        } else if (id == R.id.menu_change_account) {
            XToastUtils.toast(superTextView.getCenterString());
        } else if (id == R.id.menu_logout) {
            DialogLoader.getInstance().showConfirmDialog(
                    getContext(),
                    getString(R.string.lab_logout_confirm),
                    getString(R.string.lab_yes),
                    (dialog, which) -> {
                        dialog.dismiss();
                        XUtil.getActivityLifecycleHelper().exit();
                        TokenUtils.handleLogoutSuccess();
                    },
                    getString(R.string.lab_no),
                    (dialog, which) -> dialog.dismiss()
            );
        }
    }

}
