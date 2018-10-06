package com.canvas.test.shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.canvas.test.R;

public class LevelBar extends View {

    private int mViewWidth = 0;
    private int mViewHeight = 0;
    private int leftAndRightMargin = 10;
    private int mLevelBarHeight = 30;

    private Paint mPaint = new Paint();

    private void initPaint() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10f);
    }

    public LevelBar(Context context) {
        this(context, null);
    }

    public LevelBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        leftAndRightMargin = 5;
        mLevelBarHeight = 10;
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //默认测量模式为EXACTLY，否则请使用上面的方法并指定默认的宽度和高度
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawBackground(canvas);

        drawRiskLevelBar(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
    }

    private void drawRiskLevelBar(Canvas canvas) {

        //渐变色
        LinearGradient linearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, new int[]{getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)}, null, LinearGradient.TileMode.CLAMP);
        mPaint.setShader(linearGradient);

        //矩形左上角和右下角的点的坐标
        RectF rectF = new RectF(leftAndRightMargin, mViewHeight / 2 - mLevelBarHeight / 2, mViewWidth - leftAndRightMargin, mViewHeight / 2 + mLevelBarHeight / 2);
        canvas.drawRoundRect(rectF, 4, 4, mPaint);
    }

}
