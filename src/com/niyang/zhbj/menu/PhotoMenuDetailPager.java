package com.niyang.zhbj.menu;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.niyang.zhbj.R;
import com.niyang.zhbj.base.BaseMenuDetailPager;
import com.niyang.zhbj.domain.PhotosBean;
import com.niyang.zhbj.domain.PhotosBean.PhotoNews;
import com.niyang.zhbj.global.GlobalConstants;
import com.niyang.zhbj.util.CacheUtil;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 菜单详情页-组图
 * 
 * @author niyang
 *
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager implements OnClickListener{

	@ViewInject(R.id.lv_photo)
	private ListView mLvPhoto;

	@ViewInject(R.id.gv_photo)
	private GridView mGvhoto;

	private ArrayList<PhotoNews> mNewsData;

	private ImageButton mBtnPhoto;

	public PhotoMenuDetailPager(Activity activity, ImageButton mBtnPhoto) {
		super(activity);
		this.mBtnPhoto = mBtnPhoto;
		
		mBtnPhoto.setOnClickListener(this);
	}

	@Override
	public View initView() {
		// TextView view=new TextView(mActivity);
		// view.setText("菜单详情页-组图");
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);

		View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		String cache = CacheUtil.getCache(mActivity, GlobalConstants.PHOTOS_URL);
		if (!TextUtils.isEmpty(cache)) {
			ProcessedData(cache);
		}

		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				ProcessedData(result);
				CacheUtil.setCache(mActivity, GlobalConstants.PHOTOS_URL, result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// 请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();
			}
		});
	}

	protected void ProcessedData(String json) {
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(json, PhotosBean.class);
		mNewsData = photosBean.data.news;

		mLvPhoto.setAdapter(new PhotoAdapter());
		mGvhoto.setAdapter(new PhotoAdapter());
	}

	class PhotoAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public PhotoAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		
		@Override
		public int getCount() {
			return mNewsData.size();
		}

		@Override
		public Object getItem(int positon) {

			return mNewsData.get(positon);
		}

		@Override
		public long getItemId(int positon) {
			return positon;
		}

		@Override
		public View getView(int positon, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_photos, null);

				viewHolder = new ViewHolder();
				viewHolder.ivPic=(ImageView) convertView.findViewById(R.id.iv_pic);
				viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
				
				convertView.setTag(viewHolder);
			}else {
				viewHolder=(ViewHolder) convertView.getTag();
			}
			
			viewHolder.tvTitle.setText(mNewsData.get(positon).title);
			mBitmapUtils.display(viewHolder.ivPic, mNewsData.get(positon).listimage);
			
			return convertView;
		}

	}

	static class ViewHolder {
		public ImageView ivPic;
		public TextView tvTitle;
	}

	
	//点击事件
	private boolean islistView=true;//标记是否是ListView
	
	@Override
	public void onClick(View view) {
		if (islistView) {
			//切换成gridview
			mLvPhoto.setVisibility(View.GONE);
			mGvhoto.setVisibility(View.VISIBLE);
			mBtnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
			
			islistView=false;
		}else {
			mLvPhoto.setVisibility(View.VISIBLE);
			mGvhoto.setVisibility(View.GONE);
			mBtnPhoto.setImageResource(R.drawable.icon_pic_list_type);
			
			islistView=true;
		}
	}

}
