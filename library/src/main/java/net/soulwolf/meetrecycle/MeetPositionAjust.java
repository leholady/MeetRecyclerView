/*
 * Copyright (C) 2015 Leholady(乐活女人) Inc. All rights reserved.
 */
package net.soulwolf.meetrecycle;

/**
 * author: EwenQin
 * since : 2016/11/23 上午10:18.
 */
public interface MeetPositionAjust {

    int adjustMeetPosition(int position);

    int getHeaderCount();

    boolean isHeader(int position);

    boolean isLoadMore(int position);

}
