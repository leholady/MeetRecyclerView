/*
 * Copyright (C) 2015 Leholady(乐活女人) Inc. All rights reserved.
 */
package net.soulwolf.meetrecycle;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * author: EwenQin
 * since : 16/9/1 下午1:05.
 */
public abstract class BaseLoadView extends FrameLayout{

    private static final String TAG = "BaseLoadView";

    private LoadMoreState mState;

    public BaseLoadView(Context context) {
        this(context,null);
    }

    public BaseLoadView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.keepAttributeSet(context, attrs, defStyleAttr, 0);
        this.initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseLoadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.keepAttributeSet(context, attrs, defStyleAttr, defStyleRes);
        this.initialize(context);
    }

    protected void initialize(Context context) {
        this.onCreateView(LayoutInflater.from(context), this, true);
        this.onViewCreated();
        this.setState(LoadMoreState.LOADING);
    }

    protected void keepAttributeSet(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        // Nothing;
    }

    protected abstract void onCreateView(LayoutInflater inflater,ViewGroup parent,boolean attachToRoot);

    protected void onViewCreated(){
    }

    public final void setState(LoadMoreState state){
        if(state != mState){
            this.mState = state;
            this.onStateChanged(mState);
        }
    }

    protected void onStateChanged(LoadMoreState state){
        // Overwrite
    }

    public LoadMoreState getState() {
        return mState;
    }
}
