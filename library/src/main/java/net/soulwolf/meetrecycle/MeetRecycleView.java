/*
 * Copyright (C) 2015 Leholady(乐活女人) Inc. All rights reserved.
 */
package net.soulwolf.meetrecycle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: EwenQin
 * since : 16/8/30 上午10:59.
 */
public class MeetRecycleView extends RecyclerView {

    private static final String TAG = "MeetRecycleView";

    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    private SparseArray<View> mHeaderViews = new SparseArray<>();

    private WrapAdapter mWrapAdapter;

    private boolean mLoadingData = false;
    private boolean mNoMore = false;
    private boolean mLoadMoreEnabled = true;

    private View mEmptyView;
    private BaseLoadView mLoadView;

    private OnLoadMoreListener mLoadMoreListener;

    private final Object mLock = new Object();

    public MeetRecycleView(Context context) {
        this(context, null);
    }

    public MeetRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeetRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initialize(context);
    }

    protected void initialize(Context context) {
        this.setLoadView(new MeetDefaultLoadView(context));
    }

    public void setLoadView(BaseLoadView loadView){
        if(loadView == null){
            return;
        }
        this.mLoadView = checkParams(loadView);
        this.mLoadView.setState(LoadMoreState.COMPLETE);
        if(mWrapAdapter != null){
            this.mWrapAdapter.getAdapter().notifyDataSetChanged();
        }
    }

    private BaseLoadView checkParams(BaseLoadView loadView){
        if(loadView.getLayoutParams() == null){
            loadView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return loadView;
    }

    public void addHeaderView(View view) {
        synchronized (mLock){
            final int size = mHeaderViews.size();
            this.mHeaderViews.put(VIEW_TYPES.TYPE_HEADER + size, view);
            if (mWrapAdapter != null && mWrapAdapter.getAdapter() != null) {
                this.mWrapAdapter.getAdapter().notifyDataSetChanged();
            }
        }
    }

    public void removeHeaderView(View view){
       synchronized (mLock){
           int index = mHeaderViews.indexOfValue(view);
           if(index != -1){
               int key = mHeaderViews.keyAt(index);
               this.mHeaderViews.remove(key);
               if (mWrapAdapter != null && mWrapAdapter.getAdapter() != null) {
                   this.mWrapAdapter.getAdapter().notifyDataSetChanged();
               }
           }
       }
    }

    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType);
    }

    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && mHeaderViews.indexOfKey(itemViewType) != -1;
    }

    public void loadMoreComplete() {
        this.mLoadingData = false;
        this.mLoadView.setState(LoadMoreState.COMPLETE);
    }

    public void setNoMore(boolean mNoMore) {
        this.mLoadingData = false;
        this.mNoMore = mNoMore;
        this.mLoadView.setState(LoadMoreState.NO_DATA);
    }

    public void reset() {
        this.setNoMore(false);
        this.loadMoreComplete();
    }

    public void setLoadMoreEnabled(boolean enabled) {
        this.mLoadMoreEnabled = enabled;
        if (!enabled) {
            this.mLoadView.setState(LoadMoreState.COMPLETE);
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        this.mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(mWrapAdapter != null){
            this.mWrapAdapter.getAdapter().unregisterAdapterDataObserver(mDataObserver);
            this.mWrapAdapter = null;
        }
        if(adapter != null){
            this.mWrapAdapter = new WrapAdapter(adapter);
            this.mWrapAdapter.getAdapter().registerAdapterDataObserver(mDataObserver);
        }
        super.setAdapter(mWrapAdapter);
        if(mWrapAdapter != null){
            this.mWrapAdapter.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        if(mWrapAdapter != null){
            this.mWrapAdapter.getAdapter().unregisterAdapterDataObserver(mDataObserver);
            this.mWrapAdapter = null;
        }
        if(adapter != null){
            this.mWrapAdapter = new WrapAdapter(adapter);
            this.mWrapAdapter.getAdapter().registerAdapterDataObserver(mDataObserver);
        }
        super.swapAdapter(mWrapAdapter, removeAndRecycleExistingViews);
        if(mWrapAdapter != null){
            this.mWrapAdapter.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null && !mLoadingData && mLoadMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount() && !mNoMore) {
                this.mLoadingData = true;
                this.mLoadView.setState(LoadMoreState.LOADING);
                if (mLoadMoreListener != null) {
                    this.mLoadMoreListener.onLoadMore(this, layoutManager.getItemCount(), lastVisibleItemPosition);
                }
            }
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

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        this.mLoadMoreListener = l;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {

        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (mLoadMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    MeetRecycleView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    MeetRecycleView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter<ViewHolder> adapter;

        public WrapAdapter(RecyclerView.Adapter<ViewHolder> adapter) {
            this.adapter = adapter;
        }

        public boolean isHeader(int position) {
            return position >= 0 && position < mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return mLoadMoreEnabled && position == getItemCount() - 1;
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == VIEW_TYPES.TYPE_FOOTER) {
                return new SimpleViewHolder(mLoadView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            int adjPosition = position - (getHeadersCount());
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mLoadMoreEnabled) {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 1;
                } else {
                    return getHeadersCount() + 1;
                }
            } else {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount();
                } else {
                    return getHeadersCount();
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - getHeadersCount();
            if (isHeader(position)) {
                return mHeaderViews.keyAt(position);
            }
            if (isFooter(position)) {
                return VIEW_TYPES.TYPE_FOOTER;
            }
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        public Adapter<ViewHolder> getAdapter() {
            return adapter;
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}
