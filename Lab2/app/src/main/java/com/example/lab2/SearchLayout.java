package com.example.lab2;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.ImageView;
import android.util.Log;
import android.app.Activity;

import androidx.annotation.Nullable;

/*
* SearchLayout 负责搜索页面的搜索栏
*
* 由一个 ImageView，一个 EditText，一个 TextView 组成
*
* @author 胡昊源
* @date 2020.3.7
* */
public class SearchLayout extends LinearLayout {

    /* 初始化之后不能改变的量 */
    private static final String TAG = "SearchLayout";

    private EditText mEditView;
    private TextView mCancel;

    /* 一个类中定义的的接口，用于监听搜索内容变化 */
    private OnSearchTextChangedListener mListener;

    /* 一系列构造函数 */
    public SearchLayout(Context context) {
        super(context);
        initView();
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /* 初始化 SearchActivity 的 View */
    private void initView() {
        inflate(getContext(), R.layout.layout_search, this);
        mEditView = findViewById(R.id.edit);
        mCancel = findViewById(R.id.cancel);

        /* 这里我们不把 image 作为一个类的元素的原因，是 image 只负责展示，并不负责一些点击之类的操作 */
        ImageView mImageView = findViewById(R.id.image);

        /* 这里其实也可以不设置，因为我们在 xml 文件中设置过了 */
        mImageView.setImageResource(R.drawable.icon_search);

        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });

        mEditView.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 Log.i(TAG, "beforeTextChanged" + s);
             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged" + s);
             }

             @Override
             public void afterTextChanged(Editable s) {
                 Log.i(TAG, "afterTextChanged: " + s);
                 if (mListener != null) {
                     mListener.afterChanged(s.toString());
                 }
             }
        });

    }

    public void setOnSearchTextChangedListener(OnSearchTextChangedListener listener) {
        mListener = listener;
    }

    public interface OnSearchTextChangedListener {
        void afterChanged(String text);
    }


}
