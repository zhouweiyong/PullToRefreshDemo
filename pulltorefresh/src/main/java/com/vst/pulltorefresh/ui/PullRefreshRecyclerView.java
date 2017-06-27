package com.vst.pulltorefresh.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;

import com.vst.pulltorefresh.recyclerview.wrapper.HeaderAndFooterWrapper;

/**
 * Created by zwy on 2017/5/12.
 * email:16681805@qq.com
 */

public class PullRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private RecyclerView.OnScrollListener mScrollListener;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;


    public PullRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setPullLoadEnabled(false);
    }


    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context);
        mRecyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.i("zwy","onScrollStateChanged");
                if (isScrollLoadEnabled() && hasMoreData()) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            || newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                        if (isReadyForPullUp()) {
                            Log.i("zwy","isReadyForPullUp");
//                            startLoading();
                        }
                    }
                }

                if (null != mScrollListener) {
                    mScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i("zwy","onScrollStateChanged");
                if (null != mScrollListener) {
                    mScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
//        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(recyclerView.getAdapter());
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected boolean isReadyForPullUp() {
        return true;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            Log.i("zwy","onScrollStateChanged");
//            if (isScrollLoadEnabled() && hasMoreData()) {
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
//                        || newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    if (isReadyForPullUp()) {
//                        startLoading();
//                    }
//                }
//            }

            if (null != mScrollListener) {
                mScrollListener.onScrollStateChanged(recyclerView, newState);
            }
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            Log.i("zwy","onScrollStateChanged");
            if (null != mScrollListener) {
                mScrollListener.onScrolled(recyclerView, dx, dy);
            }
            super.onScrolled(recyclerView,dx,dy);
        }
    };

    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == ILoadingLayout.State.NO_MORE_DATA)) {
            return false;
        }

        return true;
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }

            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
        }
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.REFRESHING);
        }
    }


    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

//        if (scrollLoadEnabled) {
//            // 设置Footer
//            if (null == mLoadMoreFooterLayout) {
//                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
//            }
//
//            if (null == mLoadMoreFooterLayout.getParent()) {
//                mHeaderAndFooterWrapper.addFootView(mLoadMoreFooterLayout);
//            }
//            mLoadMoreFooterLayout.show(true);
//        } else {
//            if (null != mLoadMoreFooterLayout) {
//                mLoadMoreFooterLayout.show(false);
//            }
//        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }

        return super.getFooterLoadingLayout();
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();


        if (null == adapter || adapter.getItemCount()<=0) {
            return true;
        }
        int mostTop = (adapter.getItemCount() > 0) ? mRecyclerView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }

        return false;
    }


}
