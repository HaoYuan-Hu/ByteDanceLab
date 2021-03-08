package com.example.lab2;

import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
* TextViewHolder 对应搜索栏下的一个单个条目
* 由 SearchAdapter 管理 TextViewHolder
*
* TextViewHolder 仅由一个 TextView 组成
*
* @author 胡昊源
* @date 2021.3.7
* */
public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /* 包含一个展示 Text 的视窗 */
    private TextView mTextView;

    /* 构造函数 */
    public TextViewHolder(@NonNull View itemView) {
        super(itemView);

        /* 找到 xml 文件中对应的 text */
        mTextView = itemView.findViewById(R.id.text);

        /* 如果这个 view 不是 clickable 的，将其变成 clickable */
        /* 如果这个 view 是 clickable 的，当被点击的时候触发一个回调函数  */
        itemView.setOnClickListener(this);
    }

    public void bind(String text) {
        /* 设置 TextView 的内容 */
        mTextView.setText(text);
    }

    @Override
    public void onClick(View v) {
        /* 配置 activity 之间的通信、参数 */
        Intent intent = new Intent(v.getContext(), ContentActivity.class);
        intent.putExtra("Text", mTextView.getText().toString());

        /* 启动 activity */
        v.getContext().startActivity(intent);
    }
}
