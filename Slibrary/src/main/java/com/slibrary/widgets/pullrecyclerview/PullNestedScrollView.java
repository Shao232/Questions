package com.slibrary.widgets.pullrecyclerview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by 11470 on 2017/9/27.
 */

public class PullNestedScrollView extends NestedScrollView implements Pullable {

    private boolean isPull;// 是否支持刷新

    public PullNestedScrollView(Context context) {
        super(context);
        isPull = true;
    }

    public PullNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isPull = true;
    }

    public PullNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        isPull = true;
    }

    @Override
    public boolean canPullDown() {
        if (isPull) {
            if (getScrollY() == 0)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() -
                getMeasuredHeight()))
            return true;
        else
            return false;
    }

    /**
     * 设置是否支持刷新
     *
     * @param isPull
     */
    public void setPull(boolean isPull) {
        this.isPull = isPull;
    }

}
