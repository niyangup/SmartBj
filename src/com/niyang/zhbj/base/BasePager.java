package com.niyang.zhbj.base;

import com.niyang.zhbj.R;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

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

		return view;
	}

	public void initData() {
		
	}
}
