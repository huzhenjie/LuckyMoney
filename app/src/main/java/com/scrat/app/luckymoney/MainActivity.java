package com.scrat.app.luckymoney;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        mWebview = (WebView) findViewById(R.id.webview);
    }

    private void initData() {
        mWebview.loadUrl("https://huzhenjie.github.io/LuckyMoney/");
    }

    public void openAccessibilitySetting(View v) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }

}