package com.niyang.zhbj;

import java.util.ArrayList;
import java.util.List;

import com.niyang.zhbj.util.SpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideAcitivity extends Activity {
	private ViewPager mVp;
	private List<ImageView> imagelist;
	private LinearLayout mLlPointer;
	private int mPointDis;
	private View ivRedPoint;
	private Button mBtnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		mVp = (ViewPager) findViewById(R.id.vp_pager);
		ivRedPoint = findViewById(R.id.view_red_point);
		mLlPointer = (LinearLayout) findViewById(R.id.ll_point);
		mBtnStart = (Button) findViewById(R.id.btn_start);
		initData();
		initAdapter();
		mVp.setAdapter(new MyAdapter());
		
		mBtnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpUtil.putBoolean(getApplicationContext(), "ISFIRST", false);
				startActivity(new Intent(getApplicationContext(), MainAcitivity.class));
				finish();
			}
		});
	}

	private void initAdapter() {
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 某个item被选中
				if (position==imagelist.size()-1) {
					mBtnStart.setVisibility(View.VISIBLE);
				}else {
					mBtnStart.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 当页面滑动过程中的回调
//				int leftMargin = (int) (mPointDis * positionOffset) + position * mPointDis;
//				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivRedPoint
//						.getLayoutParams();
//				layoutParams.leftMargin = 20;
//				ivRedPoint.setLayoutParams(layoutParams);

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 页面状态发生改变的回调
			}
		});

		// 小圆点之间的间距
		mLlPointer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				mLlPointer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mPointDis = mLlPointer.getChildAt(1).getLeft() - mLlPointer.getChildAt(0).getLeft();
				System.out.println("mPointDis:" + mPointDis);
			}
		});

	}

	private void initData() {
		int[] imgs = { R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3 };
		imagelist = new ArrayList<ImageView>();
		ivRedPoint = new ImageView(this);
		for (int i = 0; i < imgs.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgs[i]);
			imagelist.add(imageView);

			// 初始化小圆点
			ImageView point = new ImageView(getApplicationContext());
			// 设置shape形状
			point.setImageResource(R.drawable.shape_point_gray);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				// 从第二个点开始设置左边距
				params.leftMargin = 10;
			}
			// 将布局参数设置给小圆点
			point.setLayoutParams(params);

			mLlPointer.addView(point);
		}

	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imagelist.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = imagelist.get(position);
			container.addView(imageView);
			return imageView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
