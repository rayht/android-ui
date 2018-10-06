package com.canvas.test.shader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.canvas.test.R;

public class BitmapShaderView extends View {

    private Bitmap mBitmap;

    private Paint mPaint;

    private BitmapShader mBitmapShader;

    public BitmapShaderView(Context context) {
        this(context, null);
    }

    public BitmapShaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bitmap);
        mPaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
        canvas.drawCircle(mBitmap.getWidth() / 2, mBitmap.getWidth() / 2, mBitmap.getWidth() / 2, mPaint);
//        canvas.drawRect(0, 0, mBitmap.getWidth() * 2, mBitmap.getHeight() * 2, mPaint);
    }
}
