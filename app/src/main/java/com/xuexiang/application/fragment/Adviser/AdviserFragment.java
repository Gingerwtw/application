package com.xuexiang.application.fragment.Adviser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.xuexiang.application.R;
import com.xuexiang.application.activity.FaceActivity;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

@Page(anim = CoreAnim.none)
public class AdviserFragment extends Fragment implements View.OnClickListener {

    private MaterialDialog.Builder warningDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_adviser, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn_160 = getActivity().findViewById(R.id.adviser_160);
        btn_160.setOnClickListener(this);
        Button btn_chunyu = getActivity().findViewById(R.id.adviser_chunyu);
        btn_chunyu.setOnClickListener(this);

        warningDialog = new MaterialDialog.Builder(getActivity())
                .title("错误")
                .positiveText("确认");
    }


    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()){
            case R.id.adviser_160:
                intent = getActivity().getPackageManager().getLaunchIntentForPackage("cn.kidyn.qdmedical160");
                if (intent != null) {
                    intent.putExtra("type", "110");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    warningDialog.content("未检测到手机上的健康160软件").show();
                }
                break;
            case R.id.adviser_chunyu:
                intent = getActivity().getPackageManager().getLaunchIntentForPackage("me.chunyu.ChunyuDoctor");
                if (intent != null) {
                    intent.putExtra("type", "110");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    warningDialog.content("未检测到手机上的春雨医生软件").show();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }


    }
}
