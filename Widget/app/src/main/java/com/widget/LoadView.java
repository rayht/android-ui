package com.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class LoadView extends View {

    /**
     * 画外圆的Paint
     */
    private Paint mCircularPaint = null;

    /**
     * 画图片的Paint
     */
    private Paint mBitmapPaint = null;

    /**
     * 图片矩阵，用于操作图片
     */
    private Matrix mMatrix = null;

    /**
     * View宽
     */
    private int mViewWidth;

    /**
     * View高
     */
    private int mViewHeight;

    /**
     * 箭头上的图片
     */
    private Bitmap mBitmap;

    /**
     * 圆上的某点的pos
     */
    private float[] pos;

    /**
     * 圆上的某点的tan[y,x]
     */
    private float[] tan;

    /**
     * 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
     */
    private float currentValue = 0;

    public LoadView(Context context) {
        this(context, null);
    }

    public LoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;
    }

    private void init(Context context) {

        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();

        mCircularPaint = new Paint();
        mCircularPaint.setColor(Color.RED);
        mCircularPaint.setStyle(Paint.Style.STROKE);
        mCircularPaint.setStrokeWidth(5);

        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.DKGRAY);
        mBitmapPaint.setStrokeWidth(2);
        mBitmapPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* 每一次重绘时增加0.05,以增加圆上的距离，当大于等于1时重置为0，从起点开始 */
        currentValue += 0.005;
        if(currentValue >= 1) {
            currentValue = 0;
        }

        /* 画背景 */
        canvas.drawColor(Color.WHITE);

        /* 移动画面到中间 */
        canvas.translate(mViewWidth / 2, mViewHeight / 2);

        Path path = new Path();
        path.addCircle(0, 0, 200, Path.Direction.CCW);

        /* 画图片时箭头的角度需要移动的角度 */
        PathMeasure measure = new PathMeasure(path, false);
        measure.getPosTan(currentValue * measure.getLength(), pos, tan);
        float degress = (float) (Math.atan2(tan[1], tan[0]) * 180 / Math.PI);
        mMatrix.reset();
        /* Bitmap以自己中心旋转degress */
        mMatrix.postRotate(degress,mBitmap.getWidth() / 2,mBitmap.getHeight() / 2);
        /* 移动到指定的位置 */
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2,pos[1] - mBitmap.getHeight() / 2);

        /* 画圆 */
        canvas.drawPath(path, mCircularPaint);

        /* 画圆上的小图片 */
        canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);

        /* 不停重绘 */
        invalidate();
    }
}
