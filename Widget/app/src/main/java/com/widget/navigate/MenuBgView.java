package com.widget.navigate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 异形背景绘制
 */
public class MenuBgView extends View {

    private Paint mPaint;

    private Path mPath;

    public MenuBgView(Context context) {
        this(context, null);
    }

    public MenuBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }

    public void setTouchY(float y, float percent) {
        mPath.reset();

        float width = getWidth();
        float height = getHeight();

        //超出左边部分
        float offsetY = getHeight() / 8;

        float beginX = 0;
        float beginY = -offsetY;

        float endX = 0;
        float endY = height + offsetY;

        //percent * 3 / 2,一个相对好看合适的比例
        float controlX = width * percent * 3 / 2;
        float controlY = y;

        mPath.lineTo(beginX, beginY);
        mPath.quadTo(controlX, controlY, endX, endY);
        mPath.close();

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawPath(mPath, mPaint);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }
    public void setColor(Drawable color) {
        if (color instanceof ColorDrawable) {
            ColorDrawable colorDrawable= (ColorDrawable) color;
            mPaint.setColor(colorDrawable.getColor());
        }else {
            //TODO 实现背景图片的变换
        }
    }
}
