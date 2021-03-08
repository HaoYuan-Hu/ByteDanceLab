package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


/*
* ContentActivity 是显示搜索结果内容的 Activity
* 在用户点击搜索结果跳转之后，新建一个 ContentActivity 并显示相关内容
*
* ContentActivity 仅由一个 TextView 组成
*
* @author 胡昊源
* @date 2020.3.7
* */
public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* 设置 Activity 要展示的页面 */
        setContentView(R.layout.activity_content);

        /* 获得传递过来的参数 */
        String content = getIntent().getStringExtra("Text");

        /* 找到自己需要的 View */
        TextView contentView = findViewById(R.id.content);

        /* 在这个 View 上设置文本 */
        contentView.setText(content);
    }
}
