package com.vst.pulltorefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vst.pulltorefresh.ui.PullToRefreshBase;
import com.vst.pulltorefresh.ui.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by zwy on 2017/5/12.
 * email:16681805@qq.com
 */

public class PullRefreshListViewActivity2 extends Activity {

    private PullToRefreshListView ptr_lv;
    private ArrayList<String> list = new ArrayList<>();
    private int index;
    private ListView listView;
    private Handler handler = new Handler();
    private int refreshCode = 0;
    private ArrayAdapter<String> adapter;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullrefresh_listview);
        initView();
    }

    private void initView() {
        ptr_lv = (PullToRefreshListView) findViewById(R.id.ptr_lv);
        //一定要在setAdapter前设置
        ptr_lv.setPullLoadEnabled(true);
        ptr_lv.setScrollLoadEnabled(false);
        getData();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView = ptr_lv.getRefreshableView();
        listView.setAdapter(adapter);


        ptr_lv.setHasMoreData(true);
//        ptr_lv.doPullRefreshing(true, 500);
        ptr_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 0;
                list.clear();
                refreshCode = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshCode = 1;
                getData();
            }
        });
        setLastUpdateTime();
    }


    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 16; i++) {
                    list.add("内容" + index++);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (refreshCode == 0) {
                            ptr_lv.onPullDownRefreshComplete();
                        } else if (refreshCode == 1) {
                            ptr_lv.onPullUpRefreshComplete();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        }).start();

    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        ptr_lv.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }

}
