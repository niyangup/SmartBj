package com.niyang.zhbj;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.niyang.zhbj.fragment.ContentFragment;
import com.niyang.zhbj.fragment.LeftMenuFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

/**
 * 使用slidingmenu 1.引入slidingmenu库 2.继承SlidingFragmentActivity 3.onCreate改为public
 * 4.调用相关api
 * 
 * @author niyang
 *
 */
public class MainAcitivity extends SlidingFragmentActivity {
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// 设置左侧边栏
		setBehindContentView(R.layout.sliding_left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();

		// 设置右边栏
//		slidingMenu.setSecondaryMenu(R.layout.sliding_right_menu);
		slidingMenu.setMode(SlidingMenu.LEFT);// 设置模式,左右都有侧边栏

		// 设置全屏触摸,默认划动边界
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// 这只侧边栏高度
		slidingMenu.setBehindOffset(300);

		initFragment();
	}

	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
		transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);

		transaction.commit();
	}

	// 获取侧边栏fragment对象
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);

		return fragment;
	}

	// 获取主页fragment对象
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);

		return fragment;
	}
}
