package com.niyang.zhbj.impl;

import com.niyang.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**首页
 * @author niyang
 *
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
	}
	
	@Override
	public void initData() {
		//给帧布局填充布局对象
		TextView view=new TextView(mActivity);
		view.setText("智慧服务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		mFlContent.addView(view);
		mTvTitle.setText("生活");
		
		mBtnMenu.setVisibility(View.VISIBLE);
	}

}
