package com.sca.immense;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView clearImg;
    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    WebView webView;
    EditText inputUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        relativeLayout = findViewById(R.id.relative);
        clearImg = findViewById(R.id.clear_img);
        progressBar = findViewById(R.id.progress_bar);
        webView = findViewById(R.id.web_view);
        inputUrl = findViewById(R.id.input_url);

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(true);
        setting.setDisplayZoomControls(false);

        if (!inputUrl.getText().toString().isEmpty()) {
            clearImg.setVisibility(View.VISIBLE);
        } else {
            clearImg.setVisibility(View.GONE);
        }
        webView.setWebViewClient(new MyViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        LoadUrl("https://google.com");

        inputUrl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputUrl.getWindowToken(), 0);
                    LoadUrl(inputUrl.getText().toString());
                    return true;
                }
                return false;
            }
        });

        clearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputUrl.setText("");
            }
        });

    }

    public void LoadUrl(String url) {

        boolean isUrl = Patterns.WEB_URL.matcher(url).matches();
        if (isUrl) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("https://google.com/search?q=" + url);
            relativeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public class MyViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


//            if (inputUrl == null || inputUrl.getText().toString().startsWith("http://") || inputUrl.getText().toString().startsWith("https://"))
//                return false;

            return false;

        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            inputUrl.setText(webView.getUrl());
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

//    private boolean appInstalledOrNot(String uri) {
//
//        PackageManager pm = getPackageManager();
//        try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//        }
//
//        return false;
//    }
}

