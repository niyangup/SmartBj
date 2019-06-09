package com.niyang.zhbj.menu;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.niyang.zhbj.MainAcitivity;
import com.niyang.zhbj.R;
import com.niyang.zhbj.base.BaseMenuDetailPager;
import com.niyang.zhbj.domain.NewsMenu.NewsTabData;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

/**菜单详情页-新闻
 * @author niyang
 *
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager {

	@ViewInject(R.id.vp_news_menu_detail)
	private ViewPager mViewPager;
	@ViewInject(R.id.indicator)
	private TabPageIndicator mIndicator;
	
	private ArrayList<NewsTabData> mTabData;
	private ArrayList<TabDetailPager> mPagers;
	
	public NewsMenuDetailPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		this.mTabData = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
		ViewUtils.inject(this, view);
		return view;
	}
	
	@Override
	public void initData() {
		//初始化页签
		mPagers=new ArrayList<TabDetailPager>();
		for (int i = 0; i < mTabData.size(); i++) {
			TabDetailPager pager = new TabDetailPager(mActivity,mTabData.get(i));
			mPagers.add(pager);
		}
		
		mViewPager.setAdapter(new NewsMenuDetailAdapter());
		//将ViewPager绑定在一起,必须在viewpager.setAdapter之后
		mIndicator.setViewPager(mViewPager);
		
		
		//设置页面滑动监听,必须给 指示器 设置监听
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if (position==0) {
					//开启侧边栏
					setSlidingMenuEnable(true);
				}else {
					//关闭侧边栏
					setSlidingMenuEnable(false);
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
			
			/**
			 * 开启或禁用侧边栏
			 * 
			 * @param enable
			 */
			protected void setSlidingMenuEnable(boolean enable) {
				// 获取侧边栏对象
				MainAcitivity mainUI = (MainAcitivity) mActivity;
				SlidingMenu slidingMenu = mainUI.getSlidingMenu();
				if (enable) {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
			}
		});
	}
	
	class NewsMenuDetailAdapter extends PagerAdapter{
		
		//指定指示器的标题
		@Override
		public CharSequence getPageTitle(int position) {
			
			NewsTabData data = mTabData.get(position);
			
			return data.title;
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);
			
			View view=pager.mRootView;
			container.addView(pager.mRootView);
			pager.initData();
			
			return view;
		}
		

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	@OnClick(R.id.ib_next)
	public void nextItem(View view) {
		mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
	}
}
