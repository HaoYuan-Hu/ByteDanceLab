package com.example.chapter3.homework;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class Ch3Ex2Activity extends AppCompatActivity {

    private View target;
    private View startColorPicker;
    private View endColorPicker;
    private Button durationSelector;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex2);

        target = findViewById(R.id.target);
        startColorPicker = findViewById(R.id.start_color_picker);
        endColorPicker = findViewById(R.id.end_color_picker);
        durationSelector = findViewById(R.id.duration_selector);

        startColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(Ch3Ex2Activity.this);
                picker.setColor(getBackgroundColor(startColorPicker));
                picker.enableAutoClose();
                picker.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(int color) {
                        onStartColorChanged(color);
                    }
                });
                picker.show();
            }
        });

        endColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(Ch3Ex2Activity.this);
                picker.setColor(getBackgroundColor(endColorPicker));
                picker.enableAutoClose();
                picker.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(int color) {
                        onEndColorChanged(color);
                    }
                });
                picker.show();
            }
        });

        durationSelector.setText(String.valueOf(1000));
        durationSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(Ch3Ex2Activity.this)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input(getString(R.string.duration_hint), durationSelector.getText(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                onDurationChanged(input.toString());
                            }
                        })
                        .show();
            }
        });
        resetTargetAnimation();
    }

    private void onStartColorChanged(int color) {
        startColorPicker.setBackgroundColor(color);
        resetTargetAnimation();
    }

    private void onEndColorChanged(int color) {
        endColorPicker.setBackgroundColor(color);
        resetTargetAnimation();
    }

    private void onDurationChanged(String input) {
        boolean isValid = true;
        try {
            int duration = Integer.parseInt(input);
            if (duration < 100 || duration > 10000) {
                isValid = false;
            }
        } catch (Throwable e) {
            isValid = false;
        }
        if (isValid) {
            durationSelector.setText(input);
            resetTargetAnimation();
        } else {
            Toast.makeText(Ch3Ex2Activity.this, R.string.invalid_duration, Toast.LENGTH_LONG).show();
        }
    }

    private int getBackgroundColor(View view) {
        Drawable bg = view.getBackground();
        if (bg instanceof ColorDrawable) {
            return ((ColorDrawable) bg).getColor();
        }
        return Color.WHITE;
    }

    private void resetTargetAnimation() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }

        // 在这里实现了一个 ObjectAnimator，对 target 控件的背景色进行修改
        // 可以思考下，这里为什么要使用 ofArgb，而不是 ofInt 呢？
        //
        // 我的解释：
        // 1、从编码角度：
        //   因为 Argb 虽然也是一个 int，但是和 int 的表示是有差别的，Argb 和 int 之间的转换如下：
        //   int alpha = (color & 0xff000000) >> 24 / 255.0f;
        //   int red   = (color & 0x00ff0000) >> 16 / 255.0f;
        //   int green = (color & 0x0000ff00) >> 8 / 255.0f;
        //   int blue  = (color & 0x000000ff) / 255.0f;
        //   后面还涉及到乘方、线性空间变化等一系列操作
        // 2、从代码实现角度：
        //   ofArgb 的代码实现中，调用了 ofInt，也就是说 ofArgb 做了 ofInt 应该做的事，
        //   除此意外，ofArgb 的代码实现多了这么一行：
        //   animator.setEvaluator(ArgbEvaluator.getInstance());
        //   这一步的作用正是通过一个 Evaluator，实现 Argb 编码 和 int 之间的转换。
        ObjectAnimator animator1 = ObjectAnimator.ofArgb(target,
                "backgroundColor",
                getBackgroundColor(startColorPicker),
                getBackgroundColor(endColorPicker));
        animator1.setDuration(Integer.parseInt(durationSelector.getText().toString()));
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        animator1.setRepeatMode(ObjectAnimator.REVERSE);

        // TODO ex2-1：在这里实现另一个 ObjectAnimator，对 target 控件的大小进行缩放，从 1 到 2 循环
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(target, "scaleX", 1f, 2f);
        scaleXAnimator.setDuration(Integer.parseInt(durationSelector.getText().toString()));
        scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(target, "scaleY", 1f, 2f);
        scaleYAnimator.setDuration(Integer.parseInt(durationSelector.getText().toString()));
        scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        // TODO ex2-2：在这里实现另一个 ObjectAnimator，对 target 控件的透明度进行修改，从 1 到 0.5f 循环
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target,"alpha",1f,0.5f);
        alphaAnimator.setDuration(Integer.parseInt(durationSelector.getText().toString()));
        alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        // TODO ex2-3: 将上面创建的其他 ObjectAnimator 都添加到 AnimatorSet 中
        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, scaleXAnimator, scaleYAnimator, alphaAnimator);
        animatorSet.start();
    }
}
