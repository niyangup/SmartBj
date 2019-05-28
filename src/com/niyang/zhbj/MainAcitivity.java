package com.niyang.zhbj;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.view.Window;

/**使用slidingmenu
 * 1.引入slidingmenu库
 * 2.继承SlidingFragmentActivity 
 * 3.onCreate改为public
 * 4.调用相关api
 * 
 * @author niyang
 *
 */
public class MainAcitivity extends SlidingFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// 设置左侧边栏
		setBehindContentView(R.layout.sliding_left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();

		// 设置右边栏
		slidingMenu.setSecondaryMenu(R.layout.sliding_right_menu);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 设置模式,左右都有侧边栏

		// 设置全屏触摸,默认划动边界
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// 这只侧边栏高度
		slidingMenu.setBehindOffset(200);
	}
}
