package com.niyang.zhbj.impl;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.niyang.zhbj.MainAcitivity;
import com.niyang.zhbj.base.BaseMenuDetailPager;
import com.niyang.zhbj.base.BasePager;
import com.niyang.zhbj.domain.NewsMenu;
import com.niyang.zhbj.fragment.LeftMenuFragment;
import com.niyang.zhbj.global.GlobalConstants;
import com.niyang.zhbj.menu.InteractMenuDetailPager;
import com.niyang.zhbj.menu.NewsMenuDetailPager;
import com.niyang.zhbj.menu.PhotoMenuDetailPager;
import com.niyang.zhbj.menu.TopicMenuDetailPager;
import com.niyang.zhbj.util.CacheUtil;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 新闻中心
 * 
 * @author niyang
 *
 */
public class NewsCenterPager extends BasePager {
	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
	private NewsMenu mNewsData;//分类信息网络数据

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		// 给帧布局填充布局对象
//		TextView view = new TextView(mActivity);
//		view.setText("新闻中心");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//
//		mFlContent.addView(view);
		mTvTitle.setText("新闻");

		mBtnMenu.setVisibility(View.VISIBLE);

		// 判断本地是否在缓存,如有则直接加载
		String cache = CacheUtil.getCache(mActivity, GlobalConstants.CATEGORY_URL);
		if (!TextUtils.isEmpty(cache)) {
			Log.v("TAG", "发现缓存");
			processData(cache);
		}

		getDataFromService();
	}

	/**
	 * 从服务器获取数据
	 */
	private void getDataFromService() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println("请求成功" + result);
				processData(result);

				// 写缓存
				CacheUtil.setCache(mActivity, GlobalConstants.CATEGORY_URL, result);
			}
		});
	}

	/**
	 * 解析json数据
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(json, NewsMenu.class);

		System.out.println("解析结果:" + mNewsData);
		
		MainAcitivity mainAcitivity=(MainAcitivity) mActivity;
		
		LeftMenuFragment fragment = mainAcitivity.getLeftMenuFragment();
		//给侧边栏设置数据
		fragment.setMenuData(mNewsData.data);
		
		//初始化四个菜单页面
		mMenuDetailPagers=new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotoMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
		
		//将新闻菜单详情页
		setCurrentDetailPager(0);
	}
	
	//获取当前
	public void setCurrentDetailPager(int position) {
		//重新给framelayout添加内容
		BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
		View view = pager.mRootView;
		
		mFlContent.removeAllViews();
		mFlContent.addView(view);
		pager.initData();
		
		//更新标题
		mTvTitle.setText(mNewsData.data.get(position).title);
	}
}
