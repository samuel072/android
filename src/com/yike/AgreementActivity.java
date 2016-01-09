package com.yike;

import com.yike.R;
import com.yike.action.ServiceAction;
import com.yike.utils.LogUtils;
import com.yike.utils.Utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class AgreementActivity extends BaseActivity {

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.img_back:
			onBackPressed();
			break;

		default:
			break;
		}

	}

	@Override
	public void setContentView() {
		// TODO Auto-generated method stub

		setContentView(R.layout.fragment_webview);
		inintView();
	}

	private WebView mWebView;
	private TextView tileView;
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	private String mWebUrl = "http://182.92.80.2/pages/agreement.html";

	private Handler mHandler = new Handler() {
	};

	// private Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case SDK_PAY_FLAG: {
	// PayResult payResult = new PayResult((String) msg.obj);
	//
	// // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
	// String resultInfo = payResult.getResult();
	//
	// String resultStatus = payResult.getResultStatus();
	//
	// // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
	// if (TextUtils.equals(resultStatus, "9000")) {
	// Toast.makeText(getActivity(), "支付成功",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// // 判断resultStatus 为非“9000”则代表可能支付失败
	// // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
	// if (TextUtils.equals(resultStatus, "8000")) {
	// Toast.makeText(getActivity(), "支付结果确认中",
	// Toast.LENGTH_SHORT).show();
	//
	// } else {
	// // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
	// Toast.makeText(getActivity(), "支付失败",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// }
	// break;
	// }
	// case SDK_CHECK_FLAG: {
	// Toast.makeText(getActivity(), "检查结果为：" + msg.obj,
	// Toast.LENGTH_SHORT).show();
	// break;
	// }
	// default:
	// break;
	// }
	// }
	// };

	@SuppressLint("JavascriptInterface")
	public void inintView() {
		findViewById(R.id.img_back).setOnClickListener(this);
		tileView = (TextView) findViewById(R.id.tv_video_name);
		mWebView = (WebView) findViewById(R.id.webWiew);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new webViewClient() {
		});
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				tileView.setText(title);
			}
		});
		mWebView.loadUrl(mWebUrl);

		// mWebView.addJavascriptInterface(new Object() {
		// @JavascriptInterface
		// public void clickOnAndroid(final String name, final String detail,
		// final String money) {
		// mHandler.post(new Runnable() {
		// public void run() {
		// // PayDemoActivity.aliPay(getActivity(), mHandler, name,
		// // detail, money);
		// // LogUtils.d("clickOnAndroid------" + name + "---" +
		// // detail + "---" + money);
		// }
		// });
		// }
		//
		// }, "pay");

		mWebView.addJavascriptInterface(new Object() {
			@JavascriptInterface
			public void clickOnAndroid(final boolean argree) {
				LogUtils.d("argreement==" + argree);
				mHandler.post(new Runnable() {
					public void run() {
						Utils.toast(AgreementActivity.this, "您已经同意了本协议");
						LogUtils.d("argreement==" + argree);
					}
				});
			}

		}, "argreement");

		// 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
		mWebView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK
							&& mWebView.canGoBack()) { // 表示按返回键时的操作
						mWebView.goBack(); // 后退
						// webview.goForward();//前进
						return true; // 已处理
					}
				}
				return false;
			}
		});

	}

	class webViewClient extends WebViewClient {

		// 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false

			return true;

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

	}

	@Override
	public void findViewById() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.modernsky.istv.listener.CommonListener#onStart(com.modernsky.istv.action.ServiceAction, java.lang.Object)
	 */
	@Override
	public void onStart(ServiceAction service, Object action) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.modernsky.istv.listener.CommonListener#onFinish(com.modernsky.istv.action.ServiceAction, java.lang.Object)
	 */
	@Override
	public void onFinish(ServiceAction service, Object action) {
		// TODO Auto-generated method stub
		
	}

}
