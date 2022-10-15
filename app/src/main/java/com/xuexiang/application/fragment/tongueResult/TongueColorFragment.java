package com.xuexiang.application.fragment.tongueResult;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.xuexiang.application.FaceColor;
import com.xuexiang.application.R;
import com.xuexiang.application.core.BaseFragment;

public class TongueColorFragment extends BaseFragment {
    private static final String TAG = "TabFirstFragment";
    protected View mView; // 声明一个视图对象
    protected Context mContext; // 声明一个上下文对象

    private TextView tv_tongue_substance_color_type1;
    private TextView tv_tongue_substance_color_type2;
    private TextView tv_tongue_substance_color_type3;
    private TextView tv_tongue_substance_color_value1;
    private TextView tv_tongue_substance_color_value2;
    private TextView tv_tongue_substance_color_value3;
    private TextView tv_tongue_coating_color_type1;
    private TextView tv_tongue_coating_color_type2;
    private TextView tv_tongue_coating_color_type3;
    private TextView tv_tongue_coating_color_value1;
    private TextView tv_tongue_coating_color_value2;
    private TextView tv_tongue_coating_color_value3;

    @Override
    protected void initViews() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity(); // 获取活动页面的上下文
        // 根据布局文件fragment_tab_first.xml生成视图对象
        mView = inflater.inflate(R.layout.fragment_tongue_color, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initMiddleTop();
//        initMiddleMiddle();
//        initMiddleBottom();
//        initLeft();
//        initRight();
        init();
    }

    @NonNull
    @Override
    protected ViewBinding viewBindingInflate(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        TextView tv_tongue_substance_color_type1 = getActivity().findViewById(R.id.tongue_substance_color_type_value1);
        TextView tv_tongue_substance_color_type2 = getActivity().findViewById(R.id.tongue_substance_color_type_value2);
        TextView tv_tongue_substance_color_type3 = getActivity().findViewById(R.id.tongue_substance_color_type_value3);
        TextView tv_tongue_substance_color_value1 = getActivity().findViewById(R.id.tongue_substance_color_percent_value1);
        TextView tv_tongue_substance_color_value2 = getActivity().findViewById(R.id.tongue_substance_color_percent_value2);
        TextView tv_tongue_substance_color_value3 = getActivity().findViewById(R.id.tongue_substance_color_percent_value3);

        TextView tv_tongue_coating_color_type1 = getActivity().findViewById(R.id.tongue_coating_color_type_value1);
        TextView tv_tongue_coating_color_type2 = getActivity().findViewById(R.id.tongue_coating_color_type_value2);
        TextView tv_tongue_coating_color_type3 = getActivity().findViewById(R.id.tongue_coating_color_type_value3);
        TextView tv_tongue_coating_color_value1 = getActivity().findViewById(R.id.tongue_coating_color_percent_value1);
        TextView tv_tongue_coating_color_value2 = getActivity().findViewById(R.id.tongue_coating_color_percent_value2);
        TextView tv_tongue_coating_color_value3 = getActivity().findViewById(R.id.tongue_coating_color_percent_value3);

        String[] tongue_substance_color = getArguments().getStringArray("substance");
        String[] tongue_substance_color_name = getArguments().getStringArray("substance_name");
        int substance_length = getArguments().getInt("substance_length");

        tv_tongue_substance_color_type1.setText(tongue_substance_color_name[0]);
        tv_tongue_substance_color_value1.setText(tongue_substance_color[0]);
        if (substance_length > 1){
            tv_tongue_substance_color_type2.setText(tongue_substance_color_name[1]);
            tv_tongue_substance_color_value2.setText(tongue_substance_color[1]);
        }
        if (substance_length > 2){
            tv_tongue_substance_color_type3.setText(tongue_substance_color_name[2]);
            tv_tongue_substance_color_value3.setText(tongue_substance_color[2]);
        }

        String[] tongue_coating_color = getArguments().getStringArray("coating");
        String[] tongue_coating_color_name = getArguments().getStringArray("coating_name");
        int coating_length = getArguments().getInt("coating_length");

        tv_tongue_coating_color_type1.setText(tongue_coating_color_name[0]);
        tv_tongue_coating_color_value1.setText(tongue_coating_color[0]);
        if (coating_length > 1){
            tv_tongue_coating_color_type2.setText(tongue_coating_color_name[1]);
            tv_tongue_coating_color_value2.setText(tongue_coating_color[1]);
        }
        if (coating_length > 2){
            tv_tongue_coating_color_type3.setText(tongue_coating_color_name[2]);
            tv_tongue_coating_color_value3.setText(tongue_coating_color[2]);
        }
    }


}
