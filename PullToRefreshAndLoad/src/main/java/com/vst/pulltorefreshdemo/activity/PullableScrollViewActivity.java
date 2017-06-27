package com.vst.pulltorefreshdemo.activity;


import android.app.Activity;
import android.os.Bundle;

import com.vst.pulltorefreshdemo.MyListener;
import com.vst.pulltorefreshdemo.PullToRefreshLayout;
import com.vst.pulltorefreshdemo.R;

public class PullableScrollViewActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrollview);
		((PullToRefreshLayout) findViewById(R.id.refresh_view))
		.setOnRefreshListener(new MyListener());
	}
}
