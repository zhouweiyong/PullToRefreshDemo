package com.vst.pulltorefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.listview).setOnClickListener(this);
        findViewById(R.id.webview).setOnClickListener(this);
        findViewById(R.id.scrollview).setOnClickListener(this);
        findViewById(R.id.listview2).setOnClickListener(this);
        findViewById(R.id.recyclerview).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listview:
                startActivity(new Intent(this, PullRefreshListViewActivity.class));
                break;
            case R.id.scrollview:
                startActivity(new Intent(this, PullRefreshScrollViewActivity.class));
                break;
            case R.id.webview:
                startActivity(new Intent(this, PullRefreshWebViewActivity.class));
                break;
            case R.id.listview2:
                startActivity(new Intent(this, PullRefreshListViewActivity2.class));
                break;
            case R.id.recyclerview:
                startActivity(new Intent(this, PullRefreshRecyclerViewActivity.class));
                break;

        }
    }
}
