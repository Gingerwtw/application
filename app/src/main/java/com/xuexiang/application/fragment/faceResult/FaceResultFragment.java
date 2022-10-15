package com.xuexiang.application.fragment.faceResult;

import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

public class FaceResultFragment extends BaseFragment {
    private static final String TAG = "TabThirdFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    @Override
    protected void initViews() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_third.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_face_result, container, false);

        TextView face_result_health_index = mView.findViewById(R.id.face_result_health_index);
        HorizontalProgressView progressBar = mView.findViewById(R.id.face_result_progress_xui);

        String health_index = getArguments().getString("health_index");
        face_result_health_index.setText(health_index);
        progressBar.setProgress((int) (Double.parseDouble(health_index)*100));

        return mView;
    }

    @NonNull
    @Override
    protected ViewBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return null;
    }
}
