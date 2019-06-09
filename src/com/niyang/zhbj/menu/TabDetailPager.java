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
import com.niyang.zhbj.NewsDetailAcitvity;
import com.niyang.zhbj.R;
import com.niyang.zhbj.base.BaseMenuDetailPager;
import com.niyang.zhbj.domain.NewsMenu.NewsTabData;
import com.niyang.zhbj.domain.NewsTabBean;
import com.niyang.zhbj.domain.NewsTabBean.NewsData;
import com.niyang.zhbj.domain.NewsTabBean.TopNews;
import com.niyang.zhbj.global.GlobalConstants;
import com.niyang.zhbj.util.CacheUtil;
import com.niyang.zhbj.util.SpUtil;
import com.niyang.zhbj.view.PullToRefreshListView;
import com.niyang.zhbj.view.PullToRefreshListView.OnRefreshListener;
import com.niyang.zhbj.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签页面对象
 * 
 * @author niyang
 *
 */
public class TabDetailPager extends BaseMenuDetailPager {

	private NewsTabData mTabData;// 单个页签的网络数据
	private TextView view;

	@ViewInject(R.id.tv_title)
	private TextView mTvTitle;

	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager mViewPager;

	@ViewInject(R.id.lv_list)
	private PullToRefreshListView mLvList;

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;

	private String mUrl;
	private ArrayList<TopNews> mTopNews;
	private ArrayList<NewsData> mNewsList;
	private String mMoreUrl;// 下一页数据的连接
	private NewsAdapter mNewsAdapter;

	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		this.mTabData = newsTabData;

		mUrl = GlobalConstants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		// view = new TextView(mActivity);
		//
		//// view.setText(mTabData.title);
		//
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);

		View headView = View.inflate(mActivity, R.layout.list_item_head, null);
		ViewUtils.inject(this, headView);

		mLvList.addHeaderView(headView);

		/**
		 * 5.实现回调
		 */
		mLvList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void OnRefresh() {
				// 刷新数据
				getDataFromServer();
			}

			@Override
			public void OnLoadMore() {
				// 判断是否有下一页
				if (mMoreUrl != null) {
					// 有下一页
					getMoreDataFromServer();
				} else {
					// 没有下一页
					Toast.makeText(mActivity, "没有更多数据了", 1).show();
					// 没有数据时,也要收起控件
					mLvList.onRefreshComplete(true);
				}
			}
		});

		mLvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int positon, long id) {
				int headerViewsCount = mLvList.getHeaderViewsCount();
				positon = positon - headerViewsCount;
				Log.v("TAG", "第" + positon + "个被点击了");

				NewsData news = mNewsList.get(positon);

				String readIds = SpUtil.getString(mActivity, "read_ids", "");
				// 只要不包含当前id,才需要添加,避免重复
				if (!readIds.contains("" + news.id)) {
					readIds = readIds + news.id + ",";
					SpUtil.putString(mActivity, "read_ids", readIds);
				}

				// 将被点击的item的文字改为灰色
				TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
				tvTitle.setTextColor(Color.GRAY);

				Intent intent = new Intent(mActivity, NewsDetailAcitvity.class);

				// 由于服务器文件问题,修改成一下路径
				String mUrl = GlobalConstants.HTML_URL;
				intent.putExtra("url", mUrl);
				mActivity.startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void initData() {
		// view.setText(mTabData.title);
		String cache = CacheUtil.getCache(mActivity, mUrl);
		if (!TextUtils.isEmpty(cache)) {
			ProcessedData(cache, false);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				ProcessedData(result, false);
				// 设置缓存
				CacheUtil.setCache(mActivity, mUrl, result);

				// 收起下拉刷新的控件
				mLvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();

				// 收起下拉刷新的控件
				mLvList.onRefreshComplete(false);
			}
		});
	}

	/**
	 * 加载下一页数据
	 */
	protected void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				ProcessedData(result, true);

				// 收起下拉刷新的控件
				mLvList.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, 1).show();

				// 收起下拉刷新的控件
				mLvList.onRefreshComplete(false);
			}
		});
	}

	protected void ProcessedData(String result, boolean isMore) {
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
		mTopNews = newsTabBean.data.topnews;

		String moreUrl = newsTabBean.data.more;
		if (!TextUtils.isEmpty(mUrl)) {
			mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
		} else {
			mMoreUrl = null;
		}
		if (!isMore) {
			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter());

				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);// 快照方式展示

				// 事件要设置给mIndicator
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						// 更新头条新闻的标题
						TopNews topNews = mTopNews.get(position);
						mTvTitle.setText(topNews.title);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});
				// 手动更新第一个头条新闻的标题
				mTvTitle.setText(mTopNews.get(0).title);
				mIndicator.onPageSelected(0);// 默认让第一个选中(解决页面销毁后重新初始化时,仍然保持当前选中的位置)
			}
			mNewsList = newsTabBean.data.news;
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				mLvList.setAdapter(mNewsAdapter);
			}

			if (mHandler == null) {
				mHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						int currentItem = mViewPager.getCurrentItem();
						currentItem++;
						if (currentItem > mTopNews.size() - 1) {
							// 如果已经到了最后一个页面,跳到第一页
							currentItem = 0;
						}
						mViewPager.setCurrentItem(currentItem);

						mHandler.sendEmptyMessageDelayed(0, 3000);// 发送延时3秒的消息
					}
				};
				// 保证只启动一次
				mHandler.sendEmptyMessageDelayed(0, 3000);// 发送延时3秒的消息

				mViewPager.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View view, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							// 停止广告自动轮播
							// 删除handler的所有消息
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// //主线程运行
							// }
							// });
							break;

						case MotionEvent.ACTION_UP:
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;

						case MotionEvent.ACTION_CANCEL:
							//点击viewpager后,直接滑动到listview,导致此事件被调用
							mHandler.sendEmptyMessageDelayed(0, 3000);
							break;
						}
						return false;
					}
				});
			}
		} else {
			// 加载更多数据,将数据追加到原来的集合中
			ArrayList<NewsData> moreNews = newsTabBean.data.news;
			mNewsList.addAll(moreNews);
			// 刷新ListView
			mNewsAdapter.notifyDataSetChanged();
		}

	}

	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			// 设置加载失败时默认的图片
			// mBitmapUtils.configDefaultLoadFailedImage(R.drawable.topnews_item_default);
			// 设置加载中 默认的图片
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			// view.setImageResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY);// 设置图片缩放方式,宽高填充父控件
			String imageUrl = mTopNews.get(position).topimage;
			// 下载图片-将图片设置给imageView-避免内存溢出-缓存
			mBitmapUtils.display(view, imageUrl);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	class NewsAdapter extends BaseAdapter {

		private BitmapUtils mBitmapUtils;

		public NewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			ViewHolder holder;
			if (converView == null) {
				converView = View.inflate(mActivity, R.layout.list_item_news, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) converView.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) converView.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) converView.findViewById(R.id.tv_date);

				converView.setTag(holder);
			} else {
				holder = (ViewHolder) converView.getTag();
			}

			NewsData news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);

			String readIds = SpUtil.getString(mActivity, "read_ids", "");
			if (readIds.contains("" + news.id)) {
				holder.tvTitle.setTextColor(Color.GRAY);
			} else {
				holder.tvTitle.setTextColor(Color.BLACK);
			}

			mBitmapUtils.display(holder.ivIcon, news.listimage);

			return converView;
		}

	}

	static class ViewHolder {
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
