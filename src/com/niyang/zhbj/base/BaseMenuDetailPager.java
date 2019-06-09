package com.niyang.zhbj.base;

import android.app.Activity;
import android.view.View;

/**菜单详情页基类
 * @author niyang
 *
 */
public abstract class BaseMenuDetailPager {
	public Activity mActivity;
	public View mRootView;//菜单详情页根布局

	public abstract View initView();
	
	public BaseMenuDetailPager(Activity activity) {
		mActivity=activity;
		mRootView = initView();
	}
	
	public void initData() {
		
	}
}
