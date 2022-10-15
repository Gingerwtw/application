package com.xuexiang.application.fragment.Personal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.xuexiang.application.R;
import com.xuexiang.application.activity.PersonalInformationActivity;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;

import java.util.Objects;


@Page(anim = CoreAnim.none)
public class PersonalFragment extends Fragment implements View.OnClickListener {

    RelativeLayout personal_more_information;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences mShared = getActivity().getSharedPreferences("information", getActivity().MODE_PRIVATE);

        TextView username = getActivity().findViewById(R.id.personal_name);
        ImageView gender = getActivity().findViewById(R.id.personal_gender_icon);
        TextView phone = getActivity().findViewById(R.id.personal_phone);
        personal_more_information = getActivity().findViewById(R.id.personal_more_information);

        username.setText(mShared.getString("name",""));
        phone.setText(mShared.getString("phone",""));
        if (Objects.equals(mShared.getString("gender", ""), "女")){
            gender.setImageResource(R.drawable.female);
        }

        personal_more_information.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.personal_more_information){
            Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
            startActivity(intent);
        }
    }
}
