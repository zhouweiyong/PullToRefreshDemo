package com.vst.pulltorefresh.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vst.pulltorefresh.recyclerview.base.ItemViewDelegate;
import com.vst.pulltorefresh.recyclerview.base.ViewHolder;
import com.vst.pulltorefresh.recyclerview.widget.FooterView;

import java.util.List;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/12/28
 * class description:请输入类描述
 */
public abstract class PullUpAdapter<T> extends MultiItemTypeAdapter<T> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public static final int TYPE_FOOTER = 0;
    public static final int TYPE_ITEM = 1;

    public static final int STATE_INVISIBLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_NO_MORE = 2;
    public static final int STATE_NO_DATA = 3;

    protected int state = STATE_LOADING;
    private FooterView footerView;
    private OnPullUpListener pullUpListener;
    /**
     * 滚动到底部时的监听器
     */
    public interface OnPullUpListener {
        void onBottom(int state);
    }

    public PullUpAdapter(RecyclerView recyclerView, final int layoutId, List<T> datas) {
        super(recyclerView.getContext(), datas);
        mContext = recyclerView.getContext();
        mInflater = LayoutInflater.from(mContext);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(TYPE_ITEM,new ItemViewDelegate<T>()
        {
            @Override
            public int getItemViewLayoutId()
            {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position)
            {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position)
            {
                PullUpAdapter.this.convert(holder, t, position);
            }
        });
        recyclerView.addOnScrollListener(new BasePullUpScrollListener());
        footerView = new FooterView(mContext);
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return super.onCreateViewHolder(parent, viewType);
        } else if (viewType == TYPE_FOOTER) {
            refreshFooterView();
            return new ViewHolder(mContext, footerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            if (position == 0) {
                footerView.setVisibility(View.GONE);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public FooterView getFooterView() {
        return footerView;
    }

    public void setPullUpListener(OnPullUpListener pullUpListener) {
        this.pullUpListener = pullUpListener;
    }

    private void refreshFooterView() {
        switch (state) {
            case STATE_INVISIBLE:
                footerView.setInVisibleState();
                break;
            case STATE_LOADING:
                footerView.setLoadingState();
                break;
            case STATE_NO_MORE:
                footerView.setNoMoreState();
                break;
            case STATE_NO_DATA:
                footerView.setNoDataState();
                break;
        }
    }


    /**
     * 监听，判断RecyclerView滚动到底部时，加载onBottom方法
     */
    public class BasePullUpScrollListener extends RecyclerView.OnScrollListener {
        public static final int LINEAR = 0;
        public static final int GRID = 1;
        public static final int STAGGERED_GRID = 2;

        //标识RecyclerView的LayoutManager是哪种
        protected int layoutManagerType;
        // 瀑布流的最后一个的位置
        protected int[] lastPositions;
        // 最后一个的位置
        protected int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==
                    getItemCount() && pullUpListener != null) {
                pullUpListener.onBottom(state);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LINEAR;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = STAGGERED_GRID;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are " +
                                "LinearLayoutManager, GridLayoutManager and " +
                                "StaggeredGridLayoutManager");
            }

            switch (layoutManagerType) {
                case LINEAR:
                    lastVisibleItem = ((LinearLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    break;
                case GRID:
                    lastVisibleItem = ((GridLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager
                            = (StaggeredGridLayoutManager) layoutManager;
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    lastVisibleItem = findMax(lastPositions);
                    break;
            }
        }

        private int findMax(int[] lastPositions) {
            int max = lastPositions[0];
            for (int value : lastPositions) {
                if (value > max) {
                    max = value;
                }
            }
            return max;
        }
    }
}
