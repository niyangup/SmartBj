package com.niyang.zhbj.fragment;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.niyang.zhbj.MainAcitivity;
import com.niyang.zhbj.R;
import com.niyang.zhbj.base.BasePager;
import com.niyang.zhbj.impl.GovAffairsPager;
import com.niyang.zhbj.impl.HomePager;
import com.niyang.zhbj.impl.NewsPager;
import com.niyang.zhbj.impl.SettingPager;
import com.niyang.zhbj.impl.SmartServicePager;
import com.niyang.zhbj.view.NoScrollViewPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页面Fragment
 * 
 * @author niyang
 *
 */
public class ContentFragment extends BaseFragment {

	private NoScrollViewPager mVpContnent;
	private ArrayList<BasePager> mPagers;// 5个标签页的集合
	private RadioGroup mRgGroup;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		mVpContnent = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		mRgGroup = (RadioGroup) view.findViewById(R.id.rg_group);

		return view;
	}

	@Override
	public void initData() {
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsPager(mActivity));
		mPagers.add(new SmartServicePager(mActivity));
		mPagers.add(new GovAffairsPager(mActivity));
		mPagers.add(new SettingPager(mActivity));

		mVpContnent.setAdapter(new ContentAdapater());

		// 为底栏标签切换监设置监听
		mRgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					mVpContnent.setCurrentItem(0, false);

					break;

				case R.id.rb_news:
					mVpContnent.setCurrentItem(1, false);

					break;

				case R.id.rb_smart:
					mVpContnent.setCurrentItem(2, false);

					break;

				case R.id.rb_gov:
					mVpContnent.setCurrentItem(3, false);

					break;

				case R.id.rb_setting:
					mVpContnent.setCurrentItem(4, false);

					break;

				}
			}
		});
		// 手动加载第一页
		mPagers.get(0).initData();
		setSlidingMenuEnable(false);

		mVpContnent.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (position == 0 || position == mPagers.size() - 1) {
					// 首页和设置页面禁用侧边栏
					setSlidingMenuEnable(false);
				} else {
					// 开启侧边栏
					setSlidingMenuEnable(true);
				}
				BasePager pager = mPagers.get(position);
				pager.initData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**开启或禁用侧边栏
	 * @param enable
	 */
	protected void setSlidingMenuEnable(boolean enable) {
		//获取侧边栏对象
		MainAcitivity mainUI=(MainAcitivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}
			else {
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}

	class ContentAdapater extends PagerAdapter {

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagers.get(position);
			View view = pager.mRootView;// 获取当前页面对象的布局

			// pager.initData();//viewpager会默认加载下一个页面为了节省流量,不在此调用

			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
}