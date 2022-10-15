package com.xuexiang.application.fragment.faceResult;


import com.xuexiang.application.FaceColor;
import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.application.dialog.URLConfirmDialog;
import com.xuexiang.application.dialog.URLEditDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

public class ColorFragment extends BaseFragment {
    private static final String TAG = "TabFirstFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    public final static String FACE_COLOR_MIDDLE_TOP = "FACE_COLOR_MIDDLE_TOP";
    public final static String FACE_COLOR_MIDDLE_MIDDLE = "FACE_COLOR_MIDDLE_MIDDLE";
    public final static String FACE_COLOR_MIDDLE_BOTTOM = "FACE_COLOR_MIDDLE_BOTTOM";
    public final static String FACE_COLOR_LEFT = "FACE_COLOR_LEFT";
    public final static String FACE_COLOR_RIGHT = "FACE_COLOR_RIGHT";

    @Override
    protected void initViews() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_face_color, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initMiddleTop();
        initMiddleMiddle();
        initMiddleBottom();
        initLeft();
        initRight();

    }

    @NonNull
    @Override
    protected ViewBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @SuppressLint("SetTextI18n")
    private void initMiddleTop(){
        TextView tv_face_middle_top_color1 = getActivity().findViewById(R.id.tv_face_middle_top_color1);
        TextView tv_face_middle_top_color2 = getActivity().findViewById(R.id.tv_face_middle_top_color2);
        TextView tv_face_middle_top_color3 = getActivity().findViewById(R.id.tv_face_middle_top_color3);
        TextView tv_face_middle_top_color4 = getActivity().findViewById(R.id.tv_face_middle_top_color4);
        TextView tv_face_middle_top_color5 = getActivity().findViewById(R.id.tv_face_middle_top_color5);
        TextView tv_face_middle_top_color6 = getActivity().findViewById(R.id.tv_face_middle_top_color6);

        FaceColor faceColor = (FaceColor) getArguments().getSerializable(FACE_COLOR_MIDDLE_TOP);

        tv_face_middle_top_color1.setText(faceColor.getBlack() +"%");
        tv_face_middle_top_color2.setText(String.valueOf(faceColor.getYellow()) +"%");
        tv_face_middle_top_color3.setText(String.valueOf(faceColor.getLight_yellow()) +"%");
        tv_face_middle_top_color4.setText(String.valueOf(faceColor.getGrey()) +"%");
        tv_face_middle_top_color5.setText(String.valueOf(faceColor.getDeep_red()) +"%");
        tv_face_middle_top_color6.setText(String.valueOf(faceColor.getRed()) +"%");
    }

    @SuppressLint("SetTextI18n")
    private void initMiddleMiddle(){
        TextView tv_face_middle_middle_color1 = getActivity().findViewById(R.id.tv_face_middle_middle_color1);
        TextView tv_face_middle_middle_color2 = getActivity().findViewById(R.id.tv_face_middle_middle_color2);
        TextView tv_face_middle_middle_color3 = getActivity().findViewById(R.id.tv_face_middle_middle_color3);
        TextView tv_face_middle_middle_color4 = getActivity().findViewById(R.id.tv_face_middle_middle_color4);
        TextView tv_face_middle_middle_color5 = getActivity().findViewById(R.id.tv_face_middle_middle_color5);
        TextView tv_face_middle_middle_color6 = getActivity().findViewById(R.id.tv_face_middle_middle_color6);

        FaceColor faceColor = (FaceColor) getArguments().getSerializable(FACE_COLOR_MIDDLE_MIDDLE);

        tv_face_middle_middle_color1.setText(String.valueOf(faceColor.getBlack()) +"%");
        tv_face_middle_middle_color2.setText(String.valueOf(faceColor.getYellow()) +"%");
        tv_face_middle_middle_color3.setText(String.valueOf(faceColor.getLight_yellow()) +"%");
        tv_face_middle_middle_color4.setText(String.valueOf(faceColor.getGrey()) +"%");
        tv_face_middle_middle_color5.setText(String.valueOf(faceColor.getDeep_red()) +"%");
        tv_face_middle_middle_color6.setText(String.valueOf(faceColor.getRed()) +"%");
    }

    @SuppressLint("SetTextI18n")
    private void initMiddleBottom(){
        TextView tv_face_middle_bottom_color1 = getActivity().findViewById(R.id.tv_face_middle_bottom_color1);
        TextView tv_face_middle_bottom_color2 = getActivity().findViewById(R.id.tv_face_middle_bottom_color2);
        TextView tv_face_middle_bottom_color3 = getActivity().findViewById(R.id.tv_face_middle_bottom_color3);
        TextView tv_face_middle_bottom_color4 = getActivity().findViewById(R.id.tv_face_middle_bottom_color4);
        TextView tv_face_middle_bottom_color5 = getActivity().findViewById(R.id.tv_face_middle_bottom_color5);
        TextView tv_face_middle_bottom_color6 = getActivity().findViewById(R.id.tv_face_middle_bottom_color6);

        FaceColor faceColor = (FaceColor) getArguments().getSerializable(FACE_COLOR_MIDDLE_BOTTOM);

        tv_face_middle_bottom_color1.setText(String.valueOf(faceColor.getBlack()) +"%");
        tv_face_middle_bottom_color2.setText(String.valueOf(faceColor.getYellow()) +"%");
        tv_face_middle_bottom_color3.setText(String.valueOf(faceColor.getLight_yellow()) +"%");
        tv_face_middle_bottom_color4.setText(String.valueOf(faceColor.getGrey()) +"%");
        tv_face_middle_bottom_color5.setText(String.valueOf(faceColor.getDeep_red()) +"%");
        tv_face_middle_bottom_color6.setText(String.valueOf(faceColor.getRed()) +"%");
    }

    @SuppressLint("SetTextI18n")
    private void initLeft(){
        TextView tv_face_left_color1 = getActivity().findViewById(R.id.tv_face_left_color1);
        TextView tv_face_left_color2 = getActivity().findViewById(R.id.tv_face_left_color2);
        TextView tv_face_left_color3 = getActivity().findViewById(R.id.tv_face_left_color3);
        TextView tv_face_left_color4 = getActivity().findViewById(R.id.tv_face_left_color4);
        TextView tv_face_left_color5 = getActivity().findViewById(R.id.tv_face_left_color5);
        TextView tv_face_left_color6 = getActivity().findViewById(R.id.tv_face_left_color6);

        FaceColor faceColor = (FaceColor) getArguments().getSerializable(FACE_COLOR_LEFT);

        tv_face_left_color1.setText(String.valueOf(faceColor.getBlack()) +"%");
        tv_face_left_color2.setText(String.valueOf(faceColor.getYellow()) +"%");
        tv_face_left_color3.setText(String.valueOf(faceColor.getLight_yellow()) +"%");
        tv_face_left_color4.setText(String.valueOf(faceColor.getGrey()) +"%");
        tv_face_left_color5.setText(String.valueOf(faceColor.getDeep_red()) +"%");
        tv_face_left_color6.setText(String.valueOf(faceColor.getRed()) +"%");
    }

    @SuppressLint("SetTextI18n")
    private void initRight(){
        TextView tv_face_right_color1 = getActivity().findViewById(R.id.tv_face_right_color1);
        TextView tv_face_right_color2 = getActivity().findViewById(R.id.tv_face_right_color2);
        TextView tv_face_right_color3 = getActivity().findViewById(R.id.tv_face_right_color3);
        TextView tv_face_right_color4 = getActivity().findViewById(R.id.tv_face_right_color4);
        TextView tv_face_right_color5 = getActivity().findViewById(R.id.tv_face_right_color5);
        TextView tv_face_right_color6 = getActivity().findViewById(R.id.tv_face_right_color6);

        FaceColor faceColor = (FaceColor) getArguments().getSerializable(FACE_COLOR_RIGHT);

        tv_face_right_color1.setText(String.valueOf(faceColor.getBlack()) +"%");
        tv_face_right_color2.setText(String.valueOf(faceColor.getYellow()) +"%");
        tv_face_right_color3.setText(String.valueOf(faceColor.getLight_yellow()) +"%");
        tv_face_right_color4.setText(String.valueOf(faceColor.getGrey()) +"%");
        tv_face_right_color5.setText(String.valueOf(faceColor.getDeep_red()) +"%");
        tv_face_right_color6.setText(String.valueOf(faceColor.getRed()) +"%");
    }
}
