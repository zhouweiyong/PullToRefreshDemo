package com.vst.pulltorefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vst.pulltorefresh.recyclerview.CommonAdapter;
import com.vst.pulltorefresh.recyclerview.PullUpAdapter;
import com.vst.pulltorefresh.recyclerview.base.ViewHolder;
import com.vst.pulltorefresh.ui.PullRefreshRecyclerView;
import com.vst.pulltorefresh.ui.PullToRefreshBase;

import java.util.ArrayList;

/**
 * Created by zwy on 2017/5/12.
 * email:16681805@qq.com
 */

public class PullRefreshRecyclerViewActivity extends Activity {

    private PullRefreshRecyclerView rv;
    private RecyclerView mRecyclerView;
    private CommonAdapter<String> commonAdapter;
    private ArrayList<String> list = new ArrayList<>();
    private PullUpAdapter<String> pullUpAdapter;
    private Handler handler = new Handler();
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_recyclerview);
        initView();
    }

    private void initView() {
        rv = (PullRefreshRecyclerView) findViewById(R.id.rv);
        mRecyclerView = rv.getRefreshableView();
        rv.setPullLoadEnabled(true);
        rv.setScrollLoadEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        getData();
        pullUpAdapter = new PullUpAdapter<String>(mRecyclerView, R.layout.item_rv, list) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_item, s);
            }
        };
        mRecyclerView.setAdapter(pullUpAdapter);

        rv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                index = 0;
                list.clear();
                getData();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv.onPullDownRefreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                rv.setHasMoreData(true);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        rv.onPullUpRefreshComplete();
                    }
                }, 2000);
            }
        });

    }


    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    list.add("内容" + index++);
                }
            }
        }).start();

    }
}
