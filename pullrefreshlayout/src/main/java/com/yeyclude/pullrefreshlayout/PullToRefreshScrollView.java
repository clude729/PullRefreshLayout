package com.yeyclude.pullrefreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;

/**
 * 带上拉加载下拉刷新的scrollview
 * Created by clude on 17/10/15.
 */
public class PullToRefreshScrollView extends PullToRefreshBase <ScrollView>{

    /**
     * 构造方法
     *
     * @param context context
     */
    public PullToRefreshScrollView(Context context) {
//        super(context);
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context context
     * @param attrs attrs
     */
    public PullToRefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new ScrollView(context);
        return scrollView;
    }

    public void setContentView(View view){
        ScrollView scrollView = getRefreshableView();
        ViewParent viewParent = view.getParent();
        if (viewParent != null){
            ViewGroup group = (ViewGroup) viewParent;
            group.removeView(view);
            group.addView(this);
            scrollView.addView(view);
        }
    }

    @Override
    protected boolean isReadyForPullDown() {
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullUp() {
        boolean result = false;
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild){
            result = mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return result;
    }
}
