package com.example.imagevideoapp;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VideoActivity extends AppCompatActivity {
    private String TAG = "VideoActivity";

    private TextView timeMsg; // 进度条后的时间信息
    private VideoView videoView; // 视频播放视窗
    private ImageView restartBtn; // 重新播放按钮
    private ImageView controlBtn; // 开始/暂停按钮
    private SeekBar seekBar; // 进度条

    private String totalTime; // 视频总时长
    private String currentTime; // 视频当前时长

    // 进度条跟随视频进度的线程
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            if (videoView.isPlaying()) {
                int current = videoView.getCurrentPosition();
                seekBar.setProgress(current);

                Log.d(TAG,"seekBar progress:" + seekBar.getProgress());
                Log.d(TAG,"video progress:" + current);

                currentTime = getFormalTime(current);
                timeMsg.setText(currentTime + " / " + totalTime);
            }
            handler.postDelayed(runnable, 500);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        timeMsg = findViewById(R.id.video_time);

        // 设置重启按钮
        restartBtn = findViewById(R.id.restart_btn);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView != null){
                    videoView.resume();
                    videoView.start();
                    controlBtn.setImageResource(R.drawable.pause);
                }
            }
        });

        // 设置开关按钮
        controlBtn = findViewById(R.id.control_btn);
        controlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果正在播放，则暂停，并把图片修改成开始按钮
                if(videoView != null && videoView.isPlaying()){
                    videoView.pause();
                    controlBtn.setImageResource(R.drawable.start);
                    Toast.makeText(v.getContext(),"视频暂停",Toast.LENGTH_SHORT).show();
                }
                // 如果正在暂停，则开始播放，并把图片修改成开始按钮
                else if(videoView != null && !videoView.isPlaying()){
                    videoView.start();
                    controlBtn.setImageResource(R.drawable.pause);
                    Toast.makeText(v.getContext(),"视频开始",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 设置进度条
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        // 设置视频内容
        videoView = findViewById(R.id.videoView);
        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        videoView.setZOrderOnTop(true);
        videoView.setVideoPath(getVideoPath(R.raw.big_buck_bunny));

        // 监听准备状态
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 获取视频数据
                totalTime = getFormalTime(videoView.getDuration());
                currentTime = getFormalTime(videoView.getCurrentPosition());

                timeMsg.setText(currentTime + " / " + totalTime);
                seekBar.setMax(videoView.getDuration());

                Toast.makeText(VideoActivity.this, "视频加载完毕", Toast.LENGTH_SHORT).show();
            }
        });

        // 播放完毕后回调
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
            }
        });

        // 播放视频
        handler.postDelayed(runnable, 0);
        videoView.start();
    }

    // 设置 seekBar 的变化监听
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // 当进度条停止修改的时候触发
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 取得当前进度条的刻度
            int progress = seekBar.getProgress();
            if (videoView.isPlaying()) {
                // 设置当前播放的位置
                videoView.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {

        }
    };


    // 标准化时间，把秒转化成分秒组合
    private String getFormalTime(long millionSeconds) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return simpleDateFormat.format(c.getTime());
    }

    // 补全视频路径
    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    // 销毁函数
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

}
