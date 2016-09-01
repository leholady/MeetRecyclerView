/*
 * Copyright (C) 2015 Leholady(乐活女人) Inc. All rights reserved.
 */
package net.soulwolf.meetrecycle.sample;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.soulwolf.meetrecycle.MeetRecycleView;
import net.soulwolf.meetrecycle.OnItemClickListener;
import net.soulwolf.meetrecycle.OnItemLongClickListener;
import net.soulwolf.meetrecycle.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author: EwenQin
 * since : 16/9/1 下午3:13.
 */
public class SimpleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private static final String TAG = "SimpleListFragment";

    private List<String> mDatas;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MeetRecycleView mMtrRecycleView;
    private SimpleTextAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        this.mMtrRecycleView = (MeetRecycleView) view.findViewById(R.id.recycler);

        this.mSwipeRefreshLayout.setOnRefreshListener(this);

        this.mDatas = new ArrayList<>();
        this.mAdapter = new SimpleTextAdapter(getContext(),mDatas);

        this.mMtrRecycleView.setOnLoadMoreListener(this);
        this.mMtrRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mMtrRecycleView.setAdapter(mAdapter);
        this.mMtrRecycleView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = 3;
                outRect.bottom = 3;
            }
        });

        this.mSwipeRefreshLayout.setRefreshing(true);
        this.onRefresh();

        this.mMtrRecycleView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View clickedView, int position) {
                Toast.makeText(getContext(), "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        this.mMtrRecycleView.addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView parent, View clickedView, int position) {
                Toast.makeText(getContext(), "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        this.mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                mDatas.addAll(DataProvider.getSimpleData(20));
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                mMtrRecycleView.setNoMore(false);
            }
        },1000);

    }

    @Override
    public void onLoadMore(MeetRecycleView view, int itemsCount, int maxLastVisiblePosition) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mDatas.size() < 100){
                    if(mDatas.size() == 40){
                        mDatas.remove(0);
                        mAdapter.notifyItemRemoved(0);
                        mMtrRecycleView.loadMoreError();
                    }else {
                        int afterPos = mDatas.size();
                        mDatas.addAll(DataProvider.getSimpleData(20));
                        mAdapter.notifyItemRangeChanged(afterPos, mDatas.size());
                        mMtrRecycleView.loadMoreComplete();
                    }
                }else {
                    mMtrRecycleView.setNoMore(true);
                }
            }
        },2000);
    }
}
