/*
 * Copyright 2015 Soulwolf Ching
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package net.soulwolf.meetrecycle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * author: EwenQin
 * since : 16/9/1 下午1:16.
 */
public class MeetDefaultLoadView extends BaseLoadView {

    private static final String TAG = "MtrDefaultLoadView";

    private TextView mLoadText;
    private ProgressBar mLoadProgress;

    public MeetDefaultLoadView(Context context) {
        super(context);
    }

    public MeetDefaultLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeetDefaultLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MeetDefaultLoadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onCreateView(LayoutInflater inflater, ViewGroup parent, boolean attachToRoot) {
        inflater.inflate(R.layout.mtr_layout_def_load,parent,attachToRoot);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        this.mLoadText = (TextView) findViewById(R.id.text);
        this.mLoadProgress = (ProgressBar) findViewById(R.id.progress);
    }

    @Override
    protected void onStateChanged(LoadMoreState state) {
        super.onStateChanged(state);
        if(state == LoadMoreState.LOADING){
            this.setVisibility(VISIBLE);
            this.mLoadProgress.setVisibility(VISIBLE);
            this.mLoadText.setText(R.string.loading);
        }else if(state == LoadMoreState.COMPLETE) {
            this.mLoadProgress.setVisibility(GONE);
            this.mLoadText.setText(R.string.load_more_complete);
            this.setVisibility(GONE);
        }else if(state == LoadMoreState.LOAD_ERROR){
            this.setVisibility(VISIBLE);
            this.mLoadProgress.setVisibility(GONE);
            this.mLoadText.setText(R.string.load_more_error);
        }else if(state == LoadMoreState.NO_DATA){
            this.setVisibility(VISIBLE);
            this.mLoadProgress.setVisibility(GONE);
            this.mLoadText.setText(R.string.load_more_no_data);
        }
    }

}
