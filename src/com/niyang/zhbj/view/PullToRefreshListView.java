package com.niyang.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.niyang.zhbj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	private int startY = -1;
	private int measuredHeight;
	private View mHeaderView;

	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;

	private int mCurrentState = STATE_PULL_TO_REFRESH;
	private TextView mTvTitle;
	private TextView mTvTime;
	private ImageView mIvArrow;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	private ProgressBar mPbLoading;

	public PullToRefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeaderView();
		initFooterView();
	}

	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);
		mTvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		mTvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		mIvArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		mPbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

		this.addHeaderView(mHeaderView);

		mHeaderView.measure(0, 0);
		measuredHeight = mHeaderView.getMeasuredHeight();

		mHeaderView.setPadding(0, -measuredHeight, 0, 0);
		initAnim();

		setcurrentTime();
	}

	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_foot, null);
		this.addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeigth = mFooterView.getMeasuredHeight();

		mFooterView.setPadding(0, -mFooterViewHeigth, 0, 0);

		this.setOnScrollListener(this);
	}

	// 设置刷新时间
	private void setcurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
		String time = format.format(new Date());

		mTvTime.setText(time);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {
				startY = (int) ev.getY();
			}

			if (mCurrentState == STATE_REFRESHING) {
				// 如果是正在刷新,跳出循环
				break;
			}

			int endY = (int) ev.getY();

			int dy = (int) (endY - startY);

			int firstVisiblePosition = getFirstVisiblePosition();

			if (dy > 0 && firstVisiblePosition == 0) {
				int padding = (int) (dy - measuredHeight);

				mHeaderView.setPadding(0, padding, 0, 0);

				if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					// 改为松开刷新
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
					// 改为下拉刷新
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				return true;
			}
			break;

		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				refreshState();

				// 完整展示下拉刷新的头布局
				mHeaderView.setPadding(0, 0, 0, 0);

				/**
				 * 3.在合适的地方调用
				 */
				if (mListener != null) {
					mListener.OnRefresh();
				}
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头部具
				mHeaderView.setPadding(0, -measuredHeight, 0, 0);
			}

			break;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 初始化箭头动画
	 */
	private void initAnim() {
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);

		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(200);
		animDown.setFillAfter(true);

	}

	/**
	 * 根据当前状态刷新页面
	 */
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			mTvTitle.setText("下拉刷新");
			mIvArrow.startAnimation(animDown);

			mPbLoading.setVisibility(View.INVISIBLE);
			mIvArrow.setVisibility(View.VISIBLE);
			break;

		case STATE_RELEASE_TO_REFRESH:
			mTvTitle.setText("松开刷新");
			mIvArrow.startAnimation(animUp);

			mPbLoading.setVisibility(View.INVISIBLE);
			mIvArrow.setVisibility(View.VISIBLE);
			break;

		case STATE_REFRESHING:
			mTvTitle.setText("正在刷新");
			// 清除动画后才能隐藏
			mIvArrow.clearAnimation();
			// 显示进度条,隐藏箭头
			mPbLoading.setVisibility(View.VISIBLE);
			mIvArrow.setVisibility(View.INVISIBLE);

			break;
		}
	}

	/**
	 * 刷新结束,收起控件
	 * 
	 * @param success
	 */
	public void onRefreshComplete(boolean success) {

		if (!isLoadMore) {

			mHeaderView.setPadding(0, -measuredHeight, 0, 0);

			mCurrentState = STATE_PULL_TO_REFRESH;
			mTvTitle.setText("下拉刷新");
			mPbLoading.setVisibility(View.INVISIBLE);
			mIvArrow.setVisibility(View.VISIBLE);

			if (success) {
				setcurrentTime();
			}
		}else {
			//加载更多
			mFooterView.setPadding(0, -mFooterViewHeigth, 0, 0);//隐藏布局
			isLoadMore=false;
		}

	}

	private OnRefreshListener mListener;
	private View mFooterView;

	/**
	 * 2.暴露接口,设置监听
	 * 
	 * @param listener
	 *            实现接口方法的对象
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {

		this.mListener = listener;

	}

	/**
	 * 1.下拉刷新的回调接口
	 * 
	 * @author niyang
	 *
	 */
	public interface OnRefreshListener {
		public void OnRefresh();

		public void OnLoadMore();
	}

	// 滑动过程的回调
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	private boolean isLoadMore;// 标记是否正在加载更多
	private int mFooterViewHeigth;

	// 滑动状态发生变化
	@Override
	public void onScrollStateChanged(AbsListView view, int scollState) {
		if (scollState == SCROLL_STATE_IDLE) {// 空闲状态
			int lastVisiblePosition = getLastVisiblePosition();

			// 当前显示最后一个item,并且当前没有加载更多
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
				// 到底了
				System.out.println("加载更多...");

				isLoadMore=true;
				
				mFooterView.setPadding(0, 0, 0, 0);

				// 将listView的位置显示在最后一个item上,从而加载更多出来,无需手动滑动
				// setSelection(getCount()-1);

				// 通知主界面加载下一页数据
				if (mListener != null) {
					mListener.OnLoadMore();
				}

			}
		}
	}

}
