package com.panburikat.busscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.view.MenuItemCompat;
import androidx.core.view.ViewCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CashIn extends AppCompatActivity {
    private WebView webView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cash In");
        getSupportActionBar().setHomeActionContentDescription("");

        String id = getIntent().getStringExtra("accID");

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //ProgressDialog pd = ProgressDialog.show(CashIn.this, "Loading", "Please Wait", true);
        //pd.setCancelable(false);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //pd.show();
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //pd.dismiss();
                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                if (url.equals("https://jamora.leon.svdphs.ph/complete.php")) {
                    finish();
                }
            }
        });
        webView.loadUrl("https://jamora.leon.svdphs.ph/topup.php?accID=" + id);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
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