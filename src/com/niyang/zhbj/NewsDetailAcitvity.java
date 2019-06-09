package com.niyang.zhbj;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class NewsDetailAcitvity extends Activity implements OnClickListener {
	@ViewInject(R.id.ll_control)
	private LinearLayout mLlControl;

	@ViewInject(R.id.btn_back)
	private ImageButton mBtnBack;

	@ViewInject(R.id.btn_share)
	private ImageButton mBtnShare;

	@ViewInject(R.id.btn_menu)
	private ImageButton mBtnMenu;

	@ViewInject(R.id.btn_textsize)
	private ImageButton mBtnTextSize;

	@ViewInject(R.id.wb_news_detail)
	private WebView mWebView;

	@ViewInject(R.id.pb_loading)
	private ProgressBar mPbLoading;

	private String mUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);

		ViewUtils.inject(this);

		mLlControl.setVisibility(View.VISIBLE);
		mBtnBack.setVisibility(View.VISIBLE);
		mBtnMenu.setVisibility(View.GONE);

		mBtnBack.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		mBtnTextSize.setOnClickListener(this);
		// mWebView.goBack();//跳到上个页面
		// mWebView.goForward();//跳到下一页

		WebSettings settings = mWebView.getSettings();
		settings.setBuiltInZoomControls(true);// 滑动后显示缩放按钮(wap网页不支持)
		settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)

		settings.setJavaScriptEnabled(true);// 支持js功能

		mUrl = getIntent().getStringExtra("url");
		Log.v("TAG", "url地址:" + mUrl);
		mWebView.loadUrl(mUrl);

		mWebView.setWebViewClient(new WebViewClient() {

			// 开始加载网页时调用
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mPbLoading.setVisibility(View.VISIBLE);
			}

			// 网页加载结束调用
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mPbLoading.setVisibility(View.INVISIBLE);
			}

			// 所有连接跳转会走此方法
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);// 在跳转页面时强制在webView中加载

				return true;
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			// 进度发生变化
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				Log.v("tag", "进度:" + newProgress);
			}

			// 网页标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				Log.v("tag", "网页标题:" + title);
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_share:
			// TODO
			break;

		case R.id.btn_textsize:
			// 修改网页字体大小
			showChooseDialog();
			break;

		}
	}

	private int mTempWhich;// 临时记录字体大小的which

	private int mCurrentWhich = 2;// 记录当前字体大小的which

	/**
	 * 展示选择字体大小的弹窗
	 */
	public void showChooseDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体设置");
		String[] items = { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };
		builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempWhich = which;
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings settings = mWebView.getSettings();
				switch (mTempWhich) {
				case 0:
					// 超大字体
					settings.setTextSize(TextSize.LARGEST);
					// settings.setTextZoom(arg0);
					break;

				case 1:
					// 大字体
					settings.setTextSize(TextSize.LARGER);
					break;

				case 2:
					// 正常字体
					settings.setTextSize(TextSize.NORMAL);
					break;

				case 3:
					// 小号字体
					settings.setTextSize(TextSize.SMALLER);
					break;

				case 4:
					// 超小号字体
					settings.setTextSize(TextSize.SMALLEST);
					break;
				}
				mCurrentWhich = mTempWhich;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
}
