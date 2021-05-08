package com.bytedance.mediademo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int PERMISSION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 和 系统拍摄按钮 绑定
    public void systemTakePicture(View view) {
        SystemCameraActivity.startUI(this);
//        TestActivity.startUI(this);
    }

    // 和 系统录制按钮 绑定
    public void systemRecord(View view) {
        SystemRecordActivity.startUI(this);
    }

    // 和 自定义相机按钮 绑定
    public void customCamera(View view) {
        requestPermission();
    }

    private void recordVideo() {
        CustomCameraActivity.startUI(this);
    }

    // 自定义相机 首先要获取权限
    private void requestPermission() {
        // 检查音视频权限
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        // 如果权限都齐全，则开始录制
        if (hasCameraPermission && hasAudioPermission) {
            recordVideo();
        } else {
            List<String> permission = new ArrayList<String>();

            // 查查成分，少啥补啥
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA);
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO);
            }
            ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission = true;
        for (int grantResult : grantResults) {
            // 如果发现未授予权限的，则直接返回错误
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                break;
            }
        }
        // 如果权限都通过了，则可以开始录制了
        if (hasPermission) {
            recordVideo();
        } else {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
        }
    }

}