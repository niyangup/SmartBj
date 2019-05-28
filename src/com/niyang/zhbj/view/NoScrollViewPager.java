package com.niyang.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**不允许滑动的ViewPager
 * @author niyang
 *
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		//重写此方法,触摸时,什么都不做,从而试下对华东时间的禁用
		return true;
	}

}
