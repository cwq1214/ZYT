package com.zyt.bdccx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyt.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/8/30.
 */
public class BrowseActivity extends AppCompatActivity {

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.backBtn)
    ImageView backBtn;
    @InjectView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @InjectView(R.id.webView)
    WebView webView;

    static String url;
    @butterknife.InjectView(R.id.loadingView)
    RelativeLayout loadingView;

    public static void launch(Context context, String url) {
        context.startActivity(new Intent(context, BrowseActivity.class));
        BrowseActivity.url = url;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        url = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);
        ButterKnife.inject(this);

        setupWebView();

        webView.loadUrl(url);
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
// 设置可以支持缩放
        settings.setSupportZoom(true);
// 设置出现缩放工具
        settings.setBuiltInZoomControls(true);
//扩大比例的缩放
        settings.setUseWideViewPort(true);
//自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("");
                loadingView.setVisibility(View.GONE);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                BrowseActivity.this.title.setText(title);
            }

        });
    }

    @OnClick(R.id.backBtn)
    public void onBackBtnClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
