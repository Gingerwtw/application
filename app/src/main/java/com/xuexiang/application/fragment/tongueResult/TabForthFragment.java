package com.xuexiang.application.fragment.tongueResult;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseFragment;

public class TabForthFragment extends BaseFragment {
    private static final String TAG = "TabSecondFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    @Override
    protected void initViews() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_second.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_tab_second, container, false);
        // 根据碎片标签栏传来的参数拼接文本字符串
        String desc = String.format("%s页面", "几何");
        TextView tv_second = mView.findViewById(R.id.tv_second);
        tv_second.setText(desc);

        return mView;
    }

    @NonNull
    @Override
    protected ViewBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return null;
    }
}
