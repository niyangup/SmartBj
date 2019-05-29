package com.niyang.zhbj.impl;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.niyang.zhbj.base.BasePager;
import com.niyang.zhbj.domain.NewsMenu;
import com.niyang.zhbj.global.GlobalConstants;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**新闻中心
 * @author niyang
 *
 */
public class NewsPager extends BasePager {

	public NewsPager(Activity activity) {
		super(activity);
	}


	@Override
	public void initData() {
		//给帧布局填充布局对象
		TextView view=new TextView(mActivity);
		view.setText("新闻中心");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		
		mFlContent.addView(view);
		mTvTitle.setText("新闻");
		
		mBtnMenu.setVisibility(View.VISIBLE);
		
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
				System.out.println("请求成功"+result);
				processData(result);
			}
		});
	}


	/**
	 * 解析json数据
	 */
	protected void processData(String json) {
		Gson gson=new Gson();
		NewsMenu data = gson.fromJson(json, NewsMenu.class);
		
		System.out.println("解析结果:"+data);
	}
}
