package com.example.testapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
//    private static final String TAG = "MainActivity";
//    Context context;
//    ActivityMainBinding binding;
    ImageView imgArrowBack;
    WebView web;

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            int hasRecordPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            Log.d(TAG, "has record permission: " + hasRecordPermission);
            int hasAudioPermission = checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            Log.d(TAG, "has audio permission: " + hasAudioPermission);

            List<String> permissions = new ArrayList<>();

            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (hasRecordPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]),111);

            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.webview);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);

        webSettings.setMediaPlaybackRequiresUserGesture(false);

        String url = "https://youdsqubisa123.herokuapp.com/";

        web.setWebViewClient(new Callback());
//        web.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
//        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    request.grant(request.getResources());
//                }
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                        request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                        break;
                    }
                }
            }
        });
//        web.setWebChromeClient(new WebChromeClient() {
////            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                MainActivity.this.runOnUiThread(() -> request.grant(request.getResources()));
//            }
//
//            @Override
//            public void onPermissionRequestCanceled(PermissionRequest request) {
//                Log.d(TAG, "onPermissionRequestCanceled");
//            }
//        });
//        web.loadUrl(getIntent().getStringExtra("url"));
//        imgArrowBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        web.loadUrl(url);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(web, true);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }

    public class CustomWebClient extends WebViewClient {
        @Override
        public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showDialog();
            // refreshFooter(url);
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            super.onPageFinished(view, url);
            dismissDialog();
            // refreshFooter(url);
        }
    }

    Vector<ProgressDialog> progress = new Vector<>();
    public void showDialog(String message, boolean canCancelled) {
        try {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(message);
            dialog.setCancelable(canCancelled);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.show();
            progress.add(dialog);
        } catch (Exception ignored) {

        }
    }
    public void dismissDialog() {
        try {
            if (progress != null)
                for (ProgressDialog prog : progress)
                    prog.dismiss();
        } catch (Exception ignored) {
        }
    }
    public void showDialog() {
        showDialog("Loading please wait", true);
    }

}