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
package net.soulwolf.meetrecycle.sample;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * author: EwenQin
 * since : 16/9/1 下午3:38.
 */
public class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.SimpleViewHolder>{

    private static final String TAG = "SimpleTextAdapter";

    private LayoutInflater inflater;
    private List<String> datas;

    public SimpleTextAdapter(Context context, List<String> datas) {
        this.inflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(inflater.inflate(android.R.layout.simple_list_item_1,parent,false));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.bindText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(android.R.id.text1);
            this.textView.setBackgroundColor(Color.WHITE);
        }

        public void bindText(String s){
            this.textView.setText(s);
        }
    }
}
