package com.easemob.easeui.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cnpc.zhibo.app.R;
import com.easemob.easeui.widget.EaseTitleBar;

public abstract class EaseBaseFragment extends Fragment{
//    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        setUpView();
    }
    
	public void set_title_text(String str) {
		TextView title = (TextView)  getView().findViewById(R.id.textView_myactity_titlebar);
		title.setText(str);
		
		View view = getView().findViewById(R.id.imageView_myacitity_zuo);
		view.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});//设置返回
		view.setVisibility(View.VISIBLE);
		
	}
    
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 初始化控件
     */
    protected abstract void initView();
    
    /**
     * 设置属性，监听等
     */
    protected abstract void setUpView();
    
    
}
