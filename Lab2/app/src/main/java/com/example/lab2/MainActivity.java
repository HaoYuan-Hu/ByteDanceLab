package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(MainActivity.this, MyActivity.class);
//                intent.putExtra("extra", "from MainActivity");
//                startActivity(intent);


//                Intent intent = new Intent("com.example.chapter2.ACTION_START");
//                startActivity(intent);


//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://www.baidu.com"));
//                startActivity(intent);

                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                intent.putExtra("userId",123456);
                startActivity(intent);
//
//                getIntent()

            }
        });
    }
}