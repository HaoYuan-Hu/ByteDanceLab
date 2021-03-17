package com.ss.android.ugc.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ss.android.ugc.demo.R;

import java.util.Calendar;
import java.util.Date;

public class Clock extends View {

    private final Handler mHandler = new Handler() {
        @Override public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            invalidate();
            mHandler.sendEmptyMessageDelayed(1, 1 * 1000);
            Log.d("Clock","handle message");
        }
    };

    private final static String TAG = Clock.class.getSimpleName(); // 类名称

    private static final int FULL_ANGLE = 360; // 角度上限

    private static final int CUSTOM_ALPHA = 140; // 自定义透明度
    private static final int FULL_ALPHA = 255; // 透明度上限

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE; // 自定义主要颜色
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY; // 自定义次要颜色

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f; // 默认描边宽度

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private float PANEL_RADIUS = 200.0f; // 表盘半径

    private float HOUR_POINTER_LENGTH; // 指针长度
    private float MINUTE_POINTER_LENGTH; // 分针长度
    private float SECOND_POINTER_LENGTH; // 秒针长度
    private float UNIT_DEGREE = (float) (6 * Math.PI / 180); // 一个小格的度数

    private int mWidth, mCenterX, mCenterY, mRadius;

    private int degreesColor;

    private Paint mNeedlePaint;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        /* 取 长 / 宽 中最短的，相当于变成个正方形 */
        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        /* 重新设置长宽 */
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.degreesColor = DEFAULT_PRIMARY_COLOR; // 设置颜色

        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿操作
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE); // 同时填充 + 描边
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND); // 笔画以半圆形式突出
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        /* 获取当前 view 较小的作为 宽 */
        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();

        int halfWidth = mWidth / 2;

        /* 设置中心点、半径 */
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;
        PANEL_RADIUS = mRadius;

        /* 设置时针、分针、秒针长度 */
        HOUR_POINTER_LENGTH = PANEL_RADIUS - 400;
        MINUTE_POINTER_LENGTH = PANEL_RADIUS - 250;
        SECOND_POINTER_LENGTH = PANEL_RADIUS - 150;

        drawDegrees(canvas);
        drawHoursValues(canvas);
        drawNeedles(canvas);

        // todo 1: 每一秒刷新一次，让指针动起来
        //首次请求
        mHandler.sendEmptyMessageDelayed(1, 1 * 1000);
    }

    /* 画刻度 */
    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0) {
                paint.setAlpha(CUSTOM_ALPHA);
            } else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * 画时间数字, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(degreesColor);
        paint.setAlpha(FULL_ALPHA);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(100f);
        paint.setStrokeWidth(5);

        int rNum = mCenterX - (int) (mWidth * 0.08f);

        for (int i = 30; i <= FULL_ANGLE; i += 30 /* Step */) {

            int X = (int) (mCenterX + rNum * Math.sin(Math.toRadians(i)));
            int Y = (int) (mCenterY + 30 - rNum * Math.cos(Math.toRadians(i)));

            String num = String.valueOf(i/30);
            canvas.drawText(num, X, Y, paint);
        }
    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int nowSeconds = now.getSeconds();
        // 画秒针
        drawPointer(canvas, 2, nowSeconds);
        // 画分针
        // todo 2: 画分针
        int secondPart = nowSeconds / 60;
        drawPointer(canvas, 1, nowMinutes + secondPart);
        // 画时针
        int minutePart = nowMinutes / 12;
        drawPointer(canvas, 0, 5 * nowHours + minutePart);
    }


    private void drawPointer(Canvas canvas, int pointerType, int value) {

        float degree;
        float[] pointerHeadXY = new float[2];

        mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        switch (pointerType) {
            // 画时针
            case 0:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
                break;
            // 画分针
            case 1:
                // todo 3: 画分针，设置分针的颜色
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.YELLOW);
                pointerHeadXY = getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
                break;
            // 画秒针
            case 2:
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.GREEN);
                pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
                break;
        }
        canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
        xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
        return xy;
    }
}