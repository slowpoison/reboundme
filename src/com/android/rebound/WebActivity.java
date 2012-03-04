package com.android.rebound;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends Activity {
//	private Uri authUrl = Uri.parse("https://runkeeper.com/apps/authorize?client_id=bdf6d860d6964992a766c4405d7e5638&response_type=code&redirect_uri=javascript:window.close()");
	private String authUrl = "https://runkeeper.com/apps/authorize?client_id=bdf6d860d6964992a766c4405d7e5638&response_type=code&redirect_uri=javascript:close()";
//	private String authUrl = "http://www.google.com";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		final WebView wv = (WebView) findViewById(R.id.webview);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(authUrl);
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				Toast.makeText(WebActivity.this, "Starting with url " + url, Toast.LENGTH_SHORT).show();
				wv.stopLoading();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Toast.makeText(WebActivity.this, "Done with url " + url, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
