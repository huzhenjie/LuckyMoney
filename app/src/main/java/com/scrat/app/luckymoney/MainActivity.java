package com.scrat.app.luckymoney;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

public class MainActivity extends AppCompatActivity {

    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        mWebview.removeAllViews();
        mWebview.destroy();
        super.onDestroy();
    }

    private void initView() {
        mWebview = (WebView) findViewById(R.id.webview);
    }

    private void initData() {
        mWebview.loadUrl("https://huzhenjie.github.io/LuckyMoney/");
    }

    public void openAccessibilitySetting(View v) {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(v.getContext(), "请到系统设置中打开辅助功能", Toast.LENGTH_LONG).show();
        }
    }

}
