package com.bytedance.mediademo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemCameraActivity extends AppCompatActivity {
    private int REQUEST_CODE_TAKE_PHOTO = 1001;
    private int REQUEST_CODE_TAKE_PHOTO_PATH = 1002;
    private int PERMISSION_REQUEST_CAMERA_CODE = 1003;
    private int PERMISSION_REQUEST_CAMERA_PATH_CODE = 1004;

    private ImageView imageView;
    private String takeImagePath;

    public static void startUI(Context context) {
        Intent intent = new Intent(context, SystemCameraActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_camera);
        imageView = findViewById(R.id.iv_img);
    }

    // 与 拍摄按钮 绑定
    public void takePhoto(View view) {
        requestCameraPermission();
    }

    // 与 拍摄（指定路径）按钮 绑定
    public void takePhotoUsePath(View view) {
        requestCameraAndSDCardPermission();
    }

    // 请求相机和 SD 卡权限
    private void requestCameraAndSDCardPermission() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission) {
            takePhotoUsePathHasPermission();
        } else {
            String[] permissions = new String[]{Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CAMERA_PATH_CODE);
        }
    }

    // 将照片放到指定路径下
    private void takePhotoUsePathHasPermission() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeImagePath = getOutputMediaPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, PathUtils.getUriForFile(this,takeImagePath));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO_PATH);
        }
    }

    // 获取照片存储的指定路径
    private String getOutputMediaPath() {
        // 获取应用文件存储路径
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // 获取当前时间
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // 新建一个文件
        File mediaFile = new File(mediaStorageDir, "IMG_" + timeStamp + ".jpg");

        // 如果文件不存在，则创建
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }

        Toast.makeText(SystemCameraActivity.this,"mediaFile:" + mediaFile.getAbsolutePath(),Toast.LENGTH_SHORT);
        Log.d("SystemCameraActivity","mediaFile:" + mediaFile.getAbsolutePath());


        // 返回文件的绝对路径
        return mediaFile.getAbsolutePath();
    }

    // 获取相机权限，如果已有权限，则拍摄照片
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CAMERA_CODE);
        } else {
            takePhotoHasPermission();
        }
    }

    // 开启一个拍摄 Activity
    private void takePhotoHasPermission() {
        // todo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,REQUEST_CODE_TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoHasPermission();
            } else {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CAMERA_PATH_CODE) {
            boolean hasPermission = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    hasPermission = false;
                    break;
                }
            }
            if (hasPermission) {
                takePhotoUsePathHasPermission();
            } else {
                Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            // todo
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap)extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO_PATH && resultCode == RESULT_OK) {
            // todo
            // view 的宽高
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(takeImagePath, bmOptions);

            // photo 的宽高
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            Bitmap bitmap = BitmapFactory.decodeFile(takeImagePath, bmOptions);
            Bitmap rotateBitmap = PathUtils.rotateImage(bitmap,takeImagePath);
            imageView.setImageBitmap(rotateBitmap);
        }
    }
}