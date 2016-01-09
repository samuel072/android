package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView
{
    private View inner;

    private float y;

    private Rect normal = new Rect();;

    public MyScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    @Override
    protected void onFinishInflate()
    {
        if (getChildCount() > 0)
        {
            inner = getChildAt(0);

        }
    }



    @Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
//		super.scrollTo(x, y);
		inner.scrollTo(x, y);
		this.invalidate();
	}

	// 是否需要开启动画
    public boolean isNeedAnimation()
    {
        return !normal.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove()
    {

        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset)
        {
            return true;
        }
        return false;
    }
}
