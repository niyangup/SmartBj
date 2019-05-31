package com.niyang.zhbj.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.niyang.zhbj.MainAcitivity;
import com.niyang.zhbj.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class BasePager {

	public Activity mActivity;
	public TextView mTvTitle;
	public ImageButton mBtnMenu;
	public FrameLayout mFlContent;
	public View mRootView;

	public BasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		mTvTitle = (TextView) view.findViewById(R.id.tv_title);
		mBtnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		mFlContent = (FrameLayout) view.findViewById(R.id.fl_content);

		mBtnMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toggle();
			}
		});
		
		return view;
	}

	protected void toggle() {
		//打开或者关闭侧边栏
		MainAcitivity mainUI=(MainAcitivity) mActivity;
		
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();//如果当前状态是开,调用后就关
	}

	public void initData() {
		
	}
}
