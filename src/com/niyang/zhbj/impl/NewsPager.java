package com.niyang.zhbj.impl;

import com.niyang.zhbj.base.BasePager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**新闻中心
 * @author niyang
 *
 */
public class NewsPager extends BasePager {

	public NewsPager(Activity activity) {
		super(activity);
	}


	@Override
	public void initData() {
		//给帧布局填充布局对象
		TextView view=new TextView(mActivity);
		view.setText("新闻中心");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		mFlContent.addView(view);
		mTvTitle.setText("新闻");
	}
}