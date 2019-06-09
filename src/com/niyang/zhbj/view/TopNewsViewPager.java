package com.niyang.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {

	private float startY;
	private float startX;

	public TopNewsViewPager(Context context) {
		super(context);
	}

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 1.上下滑动拦截 
	 * 2.向右滑动并且当前是第一个页面,需要拦截 
	 * 3.向左滑动,并且当前是最后一个页面,需要拦截
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		getParent().requestDisallowInterceptTouchEvent(true);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = ev.getX();
			startY = ev.getY();

			break;
		case MotionEvent.ACTION_MOVE:
			float endX = ev.getX();
			float endY = ev.getY();

			float dx = endX - startX;
			float dy = endY - startY;

			if (Math.abs(dy) >= Math.abs(dx)) {
				// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);

			} else {
				// 左右滑动
				int currentItem = getCurrentItem();
				if (dx > 0) {
					// 向右滑
					if (currentItem == 0) {
						// 第一个页面
						getParent().requestDisallowInterceptTouchEvent(false);
					}

				} else {
					// 向左滑
					int count = getAdapter().getCount();// 总数
					if (currentItem == count - 1) {
						// 最后一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
