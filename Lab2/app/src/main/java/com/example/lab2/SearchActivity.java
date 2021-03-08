package com.example.lab2;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*
* SearchActivity 是展示搜索页面的 Activity
* 用户在输入栏输入搜索内容，在输入栏下方得到搜索结果
* 如果搜索结果为空，则显示所有内容
*
* SearchActivity 由搜索栏 SearchLayout 和搜索结果 SearchAdapter 构成
*
* @author 胡昊源
* @date 2020.3.7
* */
public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private RecyclerView mRecyclerView;

    private SearchAdapter mSearchAdapter = new SearchAdapter();

    private SearchLayout mSearchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSearchAdapter);

        final List<String> items = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            items.add("这是第" + i + "行");
        }
        mSearchAdapter.notifyItems(items);

        mSearchLayout = findViewById(R.id.search);
        mSearchLayout.setOnSearchTextChangedListener(new SearchLayout.OnSearchTextChangedListener() {
            @Override
            public void afterChanged(String text) {
                Log.i(TAG, "afterChanged" + text);
                List<String> filters = new ArrayList<>();

                for (String item : items) {
                    if (item.contains(text)) {
                        filters.add(item);
                    }
                }
                mSearchAdapter.notifyItems(filters);
            }
        });
    }
}
