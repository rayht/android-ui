package com.widget.navigate;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 中间工作者， 专门为了给内容提供一个背景
 * 提取，组合，转移
 * <p>
 * 提取：提取颜色值传递给MenuBgView
 * 组合: 将MenuBgView与MenuContentLayout组合在一起
 * 转移：将onTouch转移给MenuBgView与MenuContentLayout
 */
public class MenuPutLayout extends RelativeLayout {

    private MenuContentLayout contentLayout;

    private MenuBgView bgView;

    public MenuPutLayout(MenuContentLayout contentLayout) {
        this(contentLayout.getContext());
        init(contentLayout);
    }

    public MenuPutLayout(Context context) {
        super(context);
    }

    public MenuPutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuPutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(MenuContentLayout contentLayout) {
        this.contentLayout = contentLayout;

        //把contentLayout的LayoutParams设置给自己
        setLayoutParams(contentLayout.getLayoutParams());

        //添加MenuGgView
        bgView = new MenuBgView(getContext());
        addView(bgView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //把contentLayout的颜色取出，然后设置到bgView中
        bgView.setColor(contentLayout.getBackground());

        //设置contentlayout为透明色
        contentLayout.setBackgroundColor(Color.TRANSPARENT);

        addView(contentLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 设置y及滑动偏移量
     * @param y y坐标
     * @param slideOffset 滑动偏移量
     */
    public void setTouchY(float y, float slideOffset) {
        bgView.setTouchY(y, slideOffset);
        contentLayout.settouchY(y, slideOffset);
    }
}
