package com.yike;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommonWebViewActivity extends Activity implements OnClickListener {

    private WebView mWebView;
    private TextView mTitleTv;
    private ImageView mBackView;
    private WebChromeClient chromeClient = null;
    private String mUrl;
    private LinearLayout frameLayout = null;
    private View myView = null;
    private WebChromeClient.CustomViewCallback myCallBack = null;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview);
        frameLayout = (LinearLayout) findViewById(R.id.framelayout);
        mBackView = (ImageView) findViewById(R.id.img_back);
        mBackView.setOnClickListener(this);
        mWebView = (WebView) findViewById(R.id.webWiew);
        mTitleTv = (TextView) findViewById(R.id.tv_title_name);
        mUrl = this.getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(mUrl)) mWebView.loadUrl(mUrl);
        mWebView.getSettings().setJavaScriptEnabled(true);// 设置使用够执行JS脚本
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setBuiltInZoomControls(true);// 设置使支持缩放
        chromeClient = new MyChromeClient();

        mWebView.setWebChromeClient(chromeClient);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);

        final String USER_AGENT_STRING = mWebView.getSettings().getUserAgentString() + " Rong/2.0";
        mWebView.getSettings().setUserAgentString(USER_AGENT_STRING);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                mTitleTv.setText(view.getTitle());
                return true;// true表示此事件在此处被处理，不需要再广播
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                    String url) {
                WebResourceResponse response = null;
                response = super.shouldInterceptRequest(view, url);
                return response;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mTitleTv.setText(view.getTitle());
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mTitleTv.setText(view.getTitle());
                super.onPageStarted(view, url, favicon);
            }

            @Override
            // 转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                Toast.makeText(CommonWebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }
        });
        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
    }

    public void addJavaScriptMap(Object obj, String objName) {
        mWebView.addJavascriptInterface(obj, objName);
    }

    public class MyChromeClient extends WebChromeClient {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (mWebView != null) {
                callback.onCustomViewHidden();
                return;
            }
            frameLayout.removeView(mWebView);
            frameLayout.addView(view);
            myView = view;
            myCallBack = callback;
        }

        @Override
        public void onHideCustomView() {
            if (mWebView == null) {
                return;
            }
            frameLayout.removeView(myView);
            myView = null;
            frameLayout.addView(mWebView);
            myCallBack.onCustomViewHidden();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }

    }

}
