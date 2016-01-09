package com.yike;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yike.R;
import com.yike.utils.Contansts;

/**
 * @author： fengqingyun2008
 * @Email： fengqingyun2008@gmail.com
 * @version：1.0
 * @创建时间：2015-4-25 下午2:51:40
 * @类说明：
 */
public class WebActivity extends BaseActivity {

	private String title = "演出时间表";
	private WebView mWebView;
	private String url;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.modernsky.istv.BaseActivity#setContentView()
	 */
	@Override
	public void setContentView() {
		setContentView(R.layout.fragment_webview);
		title = getIntent().getStringExtra(Contansts.TITLE);
		url = getIntent().getStringExtra(Contansts.URL);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.modernsky.istv.BaseActivity#findViewById()
	 */
	@Override
	public void findViewById() {
		findViewById(R.id.img_back).setOnClickListener(this);
		TextView tv_title = (TextView) findViewById(R.id.tv_video_name);
		tv_title.setText(title);
		mWebView = (WebView) findViewById(R.id.webWiew);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setDomStorageEnabled(true);
		// mWebView.loadUrl("file:///android_asset/index.html");
		// wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");

		mWebView.loadUrl(url);
		// mWebView.setWebChromeClient(new WebChromeClient() {
		// @Override
		// public void onReceivedTitle(WebView view, String title) {
		// super.onReceivedTitle(view, title);
		// topBar.setTitle(title);
		// }
		// });

	}

	class MyWebViewClient extends WebViewClient {

		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false

			return true;

		}

	}

}
