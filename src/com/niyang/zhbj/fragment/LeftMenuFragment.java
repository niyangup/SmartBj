package com.niyang.zhbj.fragment;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.niyang.zhbj.MainAcitivity;
import com.niyang.zhbj.R;
import com.niyang.zhbj.domain.NewsMenu.NewsMenuData;
import com.niyang.zhbj.impl.NewsCenterPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends BaseFragment {

	private ListView mLvList;
	private ArrayList<NewsMenuData> mNewsMenuData;
	private int mCurrentPos=0;//当前被选中的item的位置
	private LeftMenuAdapter mAdapter;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		mLvList = (ListView) view.findViewById(R.id.lv_list);
		return view;
	}

	@Override
	public void initData() {

	}

	// 给侧边栏设置数据
	public void setMenuData(ArrayList<NewsMenuData> data) {
		mCurrentPos=0;//当前选中的位置归零
		
		// 更新页面
		mNewsMenuData = data;
		mAdapter = new LeftMenuAdapter();
		mLvList.setAdapter(mAdapter);
		
		mLvList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				mCurrentPos=position;//更新当前被选中的位置
				mAdapter.notifyDataSetChanged();//刷新ListView
				
				//收起侧边栏
				toggle();
				
				//侧边栏点击后,要修改新闻中心的Framelayout中的内容
				setCurrentDataliPager(position);
			}
		});
	}

	/**设置当前的菜单详情页
	 * @param position
	 */
	protected void setCurrentDataliPager(int position) {
		MainAcitivity mainAcitivity=(MainAcitivity) mActivity;
		ContentFragment fragment = mainAcitivity.getContentFragment();
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
		newsCenterPager.setCurrentDetailPager(position);
	}

	protected void toggle() {
		//打开或者关闭侧边栏
		MainAcitivity mainUI=(MainAcitivity) mActivity;
		
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();//如果当前状态是开,调用后就关
	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mNewsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View converView, ViewGroup arg2) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);

			TextView mTvMenu = (TextView) view.findViewById(R.id.tv_menu);

			NewsMenuData item = getItem(position);
			mTvMenu.setText(item.title);
			
			if (position==mCurrentPos) {
				mTvMenu.setEnabled(true);
			}else {
				mTvMenu.setEnabled(false);
			}
			return view;
		}

	}

}
