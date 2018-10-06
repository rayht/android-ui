package com.widget.navigate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.widget.R;

/**
 * 线性布局，用于菜单布局摆放
 */
public class MenuContentLayout extends LinearLayout {

    private boolean opened;
    private float maxTranslationX;

    public MenuContentLayout(Context context) {
        this(context, null);
    }

    public MenuContentLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
            maxTranslationX = a.getDimension(R.styleable.SideBar_maxTranslationX, 0);
            a.recycle();
        }
    }

    public void settouchY(float y, float slideOffset) {
        //遍历全部子控件  给每一个子控件进行偏移
        //如果slideOffset =1   侧滑菜单全部出来了
        opened = slideOffset > 0.8;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            //首先全设置为没有点击
            child.setPressed(false);

            //要判断  y坐落在哪一个子控件 松手的那一刻  进行回调  跳转其他页面
            boolean isHover = opened && y > child.getTop() && y < child.getBottom();
            if (isHover) {
                child.setPressed(true);
            }

            //偏移方法
            apply(child, y, slideOffset);
        }
    }

    private void apply(View child, float y, float slideOffset) {
        float transcationX = 0;

        //当前子View的中小点Y坐标
        float centerY = getTop() + getHeight() / 2;

        //手指位置与当前子View的中小点距离
        float distance = Math.abs(y - centerY);

        //偏移系烽， *3做效果处理，使效果更明显
        float scale = distance / getHeight() * 3;

        transcationX = maxTranslationX - scale * maxTranslationX;
        Log.i("tuch","maxTranslationX  "+maxTranslationX+"   touchY  "+ y+"   slideOffset  "+ slideOffset + "   偏移量  " + transcationX * slideOffset);

        child.setTranslationX(transcationX);
    }

    /**
     * 触发回调onClick
     */
    public void onMotionUp() {
        if(opened) {
            for(int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if(child.isPressed()) {
                    child.performClick();
                }
            }
        }
    }
}
