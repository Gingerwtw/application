package com.xuexiang.application.fragment.ChangeURL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xuexiang.application.core.BaseFragment;
import com.xuexiang.application.database.URLDBHelper;
import com.xuexiang.application.database.URLInfo;
import com.xuexiang.application.databinding.FragmentChangeUrlBinding;
import com.xuexiang.application.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.enums.CoreAnim;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.application.R;


/**
 * URL修改
 */
@Page(anim = CoreAnim.none)
//public class ChangeURLFragment extends Fragment<FragmentChangeUrlBinding> implements View.OnClickListener {
    public class ChangeURLFragment extends Fragment implements View.OnClickListener {

    private EditText editText_face_collect;
    private EditText editText_tongue_collect;
    private EditText editText_face_ana;
    private EditText editText_tongue_ana;
    private URLDBHelper mHelper; // 声明一个用户数据库的帮助器对象

    URLInfo face_collect_info;
    URLInfo tongue_collect_info;
    URLInfo face_ana_info ;
    URLInfo tongue_ana_info ;

    private int init = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_url, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn_confirm = getActivity().findViewById(R.id.btn_change_url_confirm);
        editText_face_collect = getActivity().findViewById(R.id.et_face_collect_url);
        editText_tongue_collect = getActivity().findViewById(R.id.et_tongue_collect_url);
        editText_face_ana = getActivity().findViewById(R.id.et_face_ana_url);
        editText_tongue_ana = getActivity().findViewById(R.id.et_tongue_ana_url);


        initEditText();

        btn_confirm.setOnClickListener(this);
    }

    private void initEditText(){
        mHelper = URLDBHelper.getInstance(getActivity(), 2);
        mHelper.openWriteLink();

        face_collect_info = mHelper.queryByUsage("0");
        face_ana_info = mHelper.queryByUsage("1");
        tongue_ana_info = mHelper.queryByUsage("2");
        tongue_collect_info = mHelper.queryByUsage("3");

        if (face_collect_info != null){
            editText_face_collect.setText(face_collect_info.User_URL);
        }
        if (tongue_collect_info != null){
            editText_tongue_collect.setText(tongue_collect_info.User_URL);
        }
        if (face_ana_info != null){
            editText_face_ana.setText(face_ana_info.User_URL);
        }
        if (tongue_ana_info != null){
            editText_tongue_ana.setText(tongue_ana_info.User_URL);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // 获得用户数据库帮助器的一个实例
        mHelper = URLDBHelper.getInstance(getActivity(), 1);
        // 恢复页面，则打开数据库连接
        mHelper.openWriteLink();
//        initEditText();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停页面，则关闭数据库连接
        mHelper.closeLink();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
//        if (!isVisibleToUser){
//            if(init == 0){
//                init = 1;
//            }
//            else{
//                XToastUtils.info("invisible");
//                initEditText();
//            }
//        }
//        else{
//            if(init == 1){
//                initEditText();
//            }
//            XToastUtils.info("visible");
//        }
        if (isVisibleToUser){
            if(init == 0){
                init = 1;
            }
            else{
                initEditText();
            }
        }
    }



    @SingleClick
    @Override
    public void onClick(View view) {
        XToastUtils.info("修改成功");
        String face_collect = editText_face_collect.getText().toString();
        String tongue_collect = editText_tongue_collect.getText().toString();
        String face_ana = editText_face_ana.getText().toString();
        String tongue_ana = editText_tongue_ana.getText().toString();

        updateURL(face_collect_info,face_collect,"0");
        updateURL(face_ana_info, face_ana, "1");
        updateURL(tongue_ana_info, tongue_ana, "2");
        updateURL(tongue_collect_info, tongue_collect, "3");
    }

    private void updateURL(URLInfo urlInfo, String url, String usage) {
        if (urlInfo == null){
            urlInfo = new URLInfo();
            urlInfo.User_URL = url;
            urlInfo.usage = usage;
            mHelper.insert(urlInfo);
        }else{
            urlInfo.User_URL = url;
            mHelper.update(urlInfo);
        }
    }
}
