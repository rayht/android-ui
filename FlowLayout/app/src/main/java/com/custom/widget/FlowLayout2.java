package com.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leiht
 * @time 2018-6-14
 */
public class FlowLayout2 extends ViewGroup {

    public FlowLayout2(Context context) {
        super(context);
    }

    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.i("Leiht","测量布局...");

        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //最终测量出的宽度
        int measuredWith = 0;
        //最终测量出的高度
        int measuredHeight = 0;

        //测量行的宽
        int curLineW = 0;
        //测量行的高
        int curLineH = 0;

        //父容器的宽高测量模式都是EXACTLY，即'match_parent',测量宽度和高度就等于父容器的宽高
        if(widthModel == MeasureSpec.EXACTLY && heightModel == MeasureSpec.EXACTLY) {
            measuredWith = widthSpecSize;
            measuredHeight = heightSpecSize;
        }else {
            //当前被测量的子View的宽
            int childWidth;
            //当前被测量的子View的高
            int childHeight;

            int count = getChildCount();
            //保存每行的View
            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                View childView = getChildAt(i);

                //测量子View
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin +
                        layoutParams.rightMargin;
                childHeight = childView.getMeasuredHeight() + layoutParams.topMargin +
                        layoutParams.bottomMargin;

                //该行的宽度大于measuredWith，换行
                if(curLineW + childWidth > widthSpecSize) {
                    //-------------记录该子View之前行的信息Start----------
                    //宽度取各行中最宽的为准
                    measuredWith = Math.max(measuredWith, curLineW);
                    //高度为所有行的高度之和
                    measuredHeight = measuredHeight + curLineH;
                    //-------------记录该子View之前行的信息End----------

                    //该childView属于下一行的第一个View,记录新信息
                    //新行的宽高等于当前行第一个View的宽高
                    curLineW = childWidth;
                    curLineH = childHeight;
                }else {
                    curLineW += childWidth;
                    curLineH = Math.max(curLineH, childHeight);

                    //该子View不需要换行，记录在List中
                    viewList.add(childView);
                }

                //最后一个，需要手动换行
                if(i == count - 1) {
                    measuredWith = Math.max(measuredWith, childWidth);
                    measuredHeight = measuredHeight + curLineH;
                }
             }
        }

        if(widthModel == MeasureSpec.EXACTLY) {
            measuredWith = Math.max(measuredWith, widthSpecSize);
        }
        //设置最终测量宽高
        setMeasuredDimension(measuredWith, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("Leiht","摆放布局...");

        //每个子View的布局的X,减去左外边距
        int startX = getPaddingLeft();
        //每个子View的布局的X
        int startY = getPaddingTop();

        //onMeasure的宽度
        int measuredWidth = getMeasuredWidth();
        //onMeasure的高度
        int measureddHeight = getMeasuredHeight();

        int childUsedWidth = 0;
        int childUsedLineHeight = 0;
        int childCount = getChildCount();

        for(int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if(childView.getVisibility() == View.GONE) {
                continue;
            }

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            childUsedWidth = childMeasuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            //startX已减去左外边距，这里需要减去右外边距
            if(startX + childUsedWidth > measuredWidth - getPaddingRight()) {
                //恢复左起点
                startX = getPaddingLeft();
                //累加行高
                startY += childUsedLineHeight;
            }
            //加上自己的边距
            int leftChildView = startX + layoutParams.leftMargin;
            int topChildView = startY + layoutParams.topMargin;
            int rightChildView = leftChildView + childMeasuredWidth;
            int bottomChildView = topChildView + childMeasuredHeight;

            childView.layout(leftChildView, topChildView, rightChildView, bottomChildView);

            startX += childMeasuredWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            //计算每一行使用的高度
            childUsedLineHeight = Math.max(childUsedLineHeight, childMeasuredHeight + layoutParams.topMargin + layoutParams.bottomMargin);
        }
    }
}
