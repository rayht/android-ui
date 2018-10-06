package com.widget.navigate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MenuDrawerLayout extends DrawerLayout implements DrawerLayout.DrawerListener {

    private MenuContentLayout menuContentLayout;

    private View contentView;

    private MenuPutLayout putLayout;

    /**
     * 手指触摸点Y坐标
     */
    private float y;

    /**
     * Drawerlayout偏移量
     */
    private float slideOffset;

    public MenuDrawerLayout(@NonNull Context context) {
        super(context);
    }

    public MenuDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuDrawerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    private void init() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child instanceof MenuContentLayout) {
                menuContentLayout = (MenuContentLayout) child;
            } else {
                contentView = child;
            }
        }

        //1. 先移除menuContentLayout
        removeView(menuContentLayout);

        //2. 构建MenuPutLayout
        putLayout = new MenuPutLayout(menuContentLayout);
        addView(putLayout);

        addDrawerListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        y = ev.getY();

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            closeDrawers();
            menuContentLayout.onMotionUp();
            return super.dispatchTouchEvent(ev);
        }

        //没有打开之前事件不拦截  打开之后拦截拦截  大于1后 内容区域不再进行偏移
        if(slideOffset < 0.8) {
            return super.dispatchTouchEvent(ev);
        }else {
            putLayout.setTouchY(y,slideOffset);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        this.slideOffset = slideOffset;

        //传递y及offset
        putLayout.setTouchY(y, slideOffset);

        float contentOffset = drawerView.getWidth() * slideOffset / 2;
        contentView.setTranslationX(contentOffset);
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
