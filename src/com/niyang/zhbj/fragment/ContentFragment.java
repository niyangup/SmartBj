package com.niyang.zhbj.fragment;

import java.util.ArrayList;

import com.niyang.zhbj.R;
import com.niyang.zhbj.base.BasePager;
import com.niyang.zhbj.impl.GovAffairsPager;
import com.niyang.zhbj.impl.HomePager;
import com.niyang.zhbj.impl.NewsPager;
import com.niyang.zhbj.impl.SettingPager;
import com.niyang.zhbj.impl.SmartServicePager;
import com.niyang.zhbj.view.NoScrollViewPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**主页面Fragment
 * @author niyang
 *
 */
public class ContentFragment extends BaseFragment {

	private NoScrollViewPager mVpContnent;
	private ArrayList<BasePager> mPagers;//5个标签页的集合

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		mVpContnent =  (NoScrollViewPager) view.findViewById(R.id.vp_content);
		return view;
	}

	@Override
	public void initData() {
		mPagers=new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsPager(mActivity));
		mPagers.add(new	SmartServicePager(mActivity));
		mPagers.add(new GovAffairsPager(mActivity));
		mPagers.add(new SettingPager(mActivity));
		
		mVpContnent.setAdapter(new ContentAdapater());
	}

	class ContentAdapater extends PagerAdapter{

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagers.get(position);
			View view=pager.mRootView;//获取当前页面对象的布局
			
			pager.initData();
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
	}
}
