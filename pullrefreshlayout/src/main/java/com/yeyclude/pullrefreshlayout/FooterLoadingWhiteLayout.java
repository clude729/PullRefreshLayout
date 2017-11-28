package com.yeyclude.pullrefreshlayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 这个类封装了上拉加载的布局（白色图片）
 * Created by clude on 17/10/2.
 */
public class FooterLoadingWhiteLayout extends LoadingLayout{

    private static final String TAG = "FooterLoadingLayout";

    /**
     * 动画加载
     */
    private ImageView loadImagviewAnim;

    /**
     * 加载动画
     */
    private AnimationDrawable animation;

    /**
     * 状态提示文本框
     */
    private TextView mHintTextView;

    public FooterLoadingWhiteLayout(Context context) {
        super(context);
        init(context);
    }

    public FooterLoadingWhiteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FooterLoadingWhiteLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        loadImagviewAnim = (ImageView) findViewById(R.id.load_imagview_anim);
        mHintTextView = (TextView) findViewById(R.id.loading_text);
        try {
            animation = (AnimationDrawable) getResources().getDrawable(R.drawable.loadingview_anim);
        }
        catch (Resources.NotFoundException e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_load_footer_white, this, false);
        return container;
    }

    protected void onReset(){
        stop();
        mHintTextView.setVisibility(INVISIBLE);
    }

    protected void onPullToRefresh(){
        loadImagviewAnim.setVisibility(VISIBLE);
        mHintTextView.setText(R.string.m_pull_to_load);
        mHintTextView.setVisibility(VISIBLE);
    }

    protected void onReleaseToRefresh(){
        loadImagviewAnim.setVisibility(VISIBLE);
        mHintTextView.setText(R.string.m_loosen_load);
        mHintTextView.setVisibility(VISIBLE);
    }

    protected void onRefreshing(){
        start();
        mHintTextView.setText(R.string.m_loading);
        mHintTextView.setVisibility(VISIBLE);
    }

    protected void onNoMoreData(){

    }

    /**
     * 停止动画
     */
    public void stop() {
        animation.stop();
        loadImagviewAnim.setVisibility(INVISIBLE);
    }

    /**
     * 开始动画
     */
    public void start(){
        loadImagviewAnim.setVisibility(VISIBLE);
        animation.start();
    }

    @Override
    public int getContentSize() {
        View view = findViewById(R.id.pull_to_load_footer_content);
        if (null != view) {
            return view.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 40);
    }
}
