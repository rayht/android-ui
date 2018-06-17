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
public class FlowLayout extends ViewGroup {

    /**
     * 记录每行的View，List<View>表示每一行的集合，List<List<View>>表示所有行的集合的集合
     */
    private List<List<View>> mViewLinesList = new ArrayList<>();

    /**
     * 记录每行的高
     */
    private List<Integer> mLineHeights = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewLinesList.clear();
        mLineHeights.clear();

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

                    //加入所有行View集合
                    mViewLinesList.add(viewList);
                    //加入高度的集合中
                    mLineHeights.add(curLineH);
                    //-------------记录该子View之前行的信息End----------

                    //该childView属于下一行的第一个View,记录新信息
                    //新行的宽高等于当前行第一个View的宽高
                    curLineW = childWidth;
                    curLineH = childHeight;

                    //加入新行List的第一个View
                    viewList = new ArrayList<>();
                    viewList.add(childView);
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
                    mViewLinesList.add(viewList);
                    mLineHeights.add(curLineH);
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
        int left, right, top, bottom;

        int curLeft = 0;
        int curTop = 0;

        int lineCount = mViewLinesList.size();
        for(int i = 0 ; i < lineCount ; i++) {
            List<View> viewList = mViewLinesList.get(i);
            int lineViewSize = viewList.size();
            for(int j = 0; j < lineViewSize; j++){
                View childView = viewList.get(j);

                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();

                childView.layout(left,top,right,bottom);
                curLeft += childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            curLeft = 0;
            curTop += mLineHeights.get(i);
        }
        mViewLinesList.clear();
        mLineHeights.clear();
    }
}
