package com.xuexiang.application.fragment.Diagnosis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.xuexiang.application.R;
import com.xuexiang.application.activity.DiagnosisActivity;
import com.xuexiang.application.activity.FaceActivity;
import com.xuexiang.application.activity.LoginActivity;
import com.xuexiang.application.activity.MainActivity;
import com.xuexiang.application.activity.PulseActivity;
import com.xuexiang.application.activity.TongueActivity;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.database.URLInfo;
import com.xuexiang.application.fragment.LungVolume.LungVolumeActivity;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;


/**
 * URL修改
 */
@Page(anim = CoreAnim.none)
//public class ChangeURLFragment extends Fragment<FragmentChangeUrlBinding> implements View.OnClickListener {
    public class DiagnosisFragment extends Fragment implements View.OnClickListener {

    ImageButton face,tongue,lung_volume,all, pulse;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_diagnosis, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        face = getActivity().findViewById(R.id.diagnosis_btn_face);
        tongue = getActivity().findViewById(R.id.diagnosis_btn_tongue);
        lung_volume = getActivity().findViewById(R.id.diagnosis_btn_lung_volume);
        all = getActivity().findViewById(R.id.diagnosis_btn_all);
        pulse = getActivity().findViewById(R.id.diagnosis_btn_pulse);

        face.setOnClickListener(this);
        tongue.setOnClickListener(this);
        lung_volume.setOnClickListener(this);
        all.setOnClickListener(this);
        pulse.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);

    }

    @SuppressLint("NonConstantResourceId")
    @SingleClick
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.diagnosis_btn_face:
                intent = new Intent(getActivity(), FaceActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.diagnosis_btn_tongue:
                intent = new Intent(getActivity(), TongueActivity.class);
                startActivityForResult(intent,2);
                break;
            case R.id.diagnosis_btn_lung_volume:
                intent = new Intent(getActivity(), LungVolumeActivity.class);
                startActivityForResult(intent,3);
                break;
            case R.id.diagnosis_btn_pulse:
                intent = new Intent(getActivity(), PulseActivity.class);
                startActivityForResult(intent,4);
                break;
            case R.id.diagnosis_btn_all:
                intent = new Intent(getActivity(), DiagnosisActivity.class);
                startActivityForResult(intent,5);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

}
