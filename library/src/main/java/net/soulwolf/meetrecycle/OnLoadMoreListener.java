/*
 * Copyright (C) 2015 Leholady(乐活女人) Inc. All rights reserved.
 */
package net.soulwolf.meetrecycle;

/**
 * author: EwenQin
 * since : 16/8/30 下午2:26.
 */
public interface OnLoadMoreListener {

    void onLoadMore(MeetRecycleView view,int itemsCount, final int maxLastVisiblePosition);
}
