package com.example.lab2;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
* SearchAdapter 负责处理搜索结果
* 本质上是一个 RecyclerView
*
* @author 胡昊源
* @date 2021.3.7
* */
public class SearchAdapter extends RecyclerView.Adapter<TextViewHolder> {

    @NonNull
    private List<String> mItems = new ArrayList<>();

    /* 新创建一个搜索结果条目 */
    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_text, parent, false));
    }

    /* 设置搜索条目的字段 */
    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder,int position) {
        holder.bind(mItems.get(position));
        Log.i("Bind:", mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /* 告知所有 Listener 字段已经发生变化 */
    public void notifyItems(@NonNull List<String> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }
}
