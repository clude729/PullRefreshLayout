package com.yeyclude.pullrefreshlayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 这个类封装了下拉刷新的布局（白色动画）
 * Created by clude on 17/10/2.
 */
public class HeaderLoadingWhiteLayout extends LoadingLayout{

    private static final String TAG = "HeaderLoadingLayout";

    private static final int DELAY_RESET = 1;

    /**
     * Header的容器
     */
    private RelativeLayout mHeaderContainer;

    /**
     * 状态提示TextView
     */
    private TextView mHintTextView;

    /**
     * 最近刷新时间
     */
    private TextView mTimeTextView;

    /**
     * 最后刷新日期格式
     */
    private String mode = "dd/MM/yyyy hh:mm";

    /**
     * 动画ImageView
     */
    private ImageView loadImagviewAnim;

    /**
     * 加载动画
     */
    private AnimationDrawable animation;

    /**
     * 高度
     */
    private int height;

    /**
     * 页数（刷新时间缓存标记）
     */
    private int page = -1;

    private View inHeadView;

    private boolean isAddOtherView = false;

    public HeaderLoadingWhiteLayout(Context context) {
        super(context);
        init(context);
    }

    public HeaderLoadingWhiteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderLoadingWhiteLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context context
     */
    private void init(Context context) {
        mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mTimeTextView = (TextView) findViewById(R.id.loading_last_time_textview);
        loadImagviewAnim = (ImageView) findViewById(R.id.load_imagview_anim);
        mode = getContext().getResources().getString(R.string.m_refresh_lasttime2).replace("%","");
        try {
            animation = (AnimationDrawable) getResources().getDrawable(R.drawable.loadingview_anim);
            mTimeTextView.setText(getLastTimeString());
        }
        catch (Resources.NotFoundException e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_white, null);
        return container;
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }
        return (int) (getResources().getDisplayMetrics().density * 60);
    }

    /**
     * 延迟重置文字，让用户看不到最后重置的文字
     * onReset（）方法是在下拉刷新完成时候调用的
     */
    private Handler resetHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY_RESET:
                    animation.stop();
                    loadImagviewAnim.clearAnimation();
                    loadImagviewAnim.setImageResource(R.drawable.loading_pulldown);
                    mHintTextView.setVisibility(View.VISIBLE);
                    mHintTextView.setText(R.string.m_pull_to_refresh);
                    mTimeTextView.setText(getLastTimeString());
                    break;
            }
        }
    };

    protected void onReset() {
        resetHandler.removeMessages(DELAY_RESET);
        resetHandler.sendEmptyMessageDelayed(DELAY_RESET, 500);
    }

    protected void onPullToRefresh() {
        loadImagviewAnim.setImageResource(R.drawable.loading_pulldown);
        loadImagviewAnim.setVisibility(View.VISIBLE);
        mHintTextView.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.m_pull_to_refresh);
    }

    protected void onReleaseToRefresh() {
        loadImagviewAnim.setImageResource(R.drawable.loading_relase);
        loadImagviewAnim.setVisibility(View.VISIBLE);
        mHintTextView.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.m_refreshing);
    }

    protected void onRefreshing() {
        loadImagviewAnim.setVisibility(View.VISIBLE);
        loadImagviewAnim.setImageDrawable(animation);
        animation.start();
        mHintTextView.setVisibility(View.GONE);
    }

    public void onPull(float scale){
        if (height == 0) {
            height = getHeight();
        }
        if (!isAddOtherView){
            if (scale < 0){
                return;
            }
            else if (scale > 1){
                return;
            }
            else {
                int padBottom = (int)((1 - scale) * height);
                MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
                params.height = height;
                params.topMargin = 0;
                this.setLayoutParams(params);
                View view = findViewById(R.id.pull_to_refresh_header_content);
                params = (MarginLayoutParams) view.getLayoutParams();
                params.bottomMargin = padBottom;
                view.setLayoutParams(params);
            }
        }
        else {
            if (scale >= 0 || scale <= 1){
                MarginLayoutParams params = (MarginLayoutParams) inHeadView.getLayoutParams();
                params.height = (int) (height * scale);
                inHeadView.setLayoutParams(params);
            }
        }
        super.onPull(scale);
    }

    /**
     * 设置刷新页标识（页面初始化时设置，可设置第一次刷新时间）
     * @param page 页面标记
     */
    public void setPage(int page) {
        this.page = page;
    }

    public void setHeadView(View view) {
        inHeadView = view;
        this.setVisibility(View.GONE);
        mHeaderContainer = (RelativeLayout) view.findViewById(R.id.pull_to_refresh_header_content);
        mHintTextView = (TextView) view.findViewById(R.id.pull_to_refresh_header_hint_textview);
        mTimeTextView = (TextView) view.findViewById(R.id.loading_last_time_textview);
        loadImagviewAnim = (ImageView) view.findViewById(R.id.load_imagview_anim);
        try {
            animation = (AnimationDrawable) getResources().getDrawable(R.drawable.loadingview_anim);
            mTimeTextView.setText(getLastTimeString());
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        isAddOtherView = true;
    }

    /**
     * 刷新成功记录刷新时间
     */
    public void refreshingSuccess(CharSequence charSequence) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (null != activeNetInfo) {
            String spName = "refresh-time" + page;
            StaticClass.saveSPData(getContext(), spName, System.currentTimeMillis());
        }
    }

    /**
     * 获取最后刷新时间的字符串（日期格式）
     * @return 返回最后刷新时间的字符串
     */
    private String getLastTimeString() {
        // 获取SharedPrefrences中上次刷新时间的key字符串
        String spName = "refresh-time" + page;
        // 获取上次刷新时间
        Long time = (Long) StaticClass.getSPData(getContext(), spName, null);
        // 如果上次刷新时间为null，则最近没有刷新
        if (time == null || time == 0) {
            return getContext().getResources().getString(R.string.no_refresh);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        String dayString;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(mode);
        dayString = simpleDateFormat.format(date);
        return getContext().getResources().getString(R.string.m_refresh_lasttime1) + " " + dayString;
    }
}
