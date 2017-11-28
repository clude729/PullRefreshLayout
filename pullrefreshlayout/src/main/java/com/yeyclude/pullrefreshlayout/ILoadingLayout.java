package com.yeyclude.pullrefreshlayout;

/**
 * 下拉刷新和上拉加载更多的界面接口
 * Created by clude on 17/10/2.
 */
public interface ILoadingLayout {

    /**
     * 刷新状态
     */
    public enum State{

        /**
         * Initial state
         * 初始状态
         */
        NONE,

        /**
         * When the UI is in a state which means that user is not interacting
         * with the Pull-to-Refresh function.
         * 用户尚未进行刷新交互操作
         */
        RESET,

        /**
         * When the UI is being pulled by the user, but has not been pulled far
         * enough so that it refreshes when released.
         * 拖动中，拖动距离不足以触发刷新
         */
        PULL_TO_REFRESH,

        /**
         * When the UI is being pulled by the user, and <strong>has</strong>
         * been pulled far enough so that it will refresh when released.
         * 拖动中，拖动距离已经足够远，当释放时进行刷新
         */
        RELEASE_TO_REFRESH,

        /**
         * When the UI is currently refreshing, caused by a pull gesture.
         * 刷新中
         */
        REFRESHING,

        /**
         * When the UI is currently loading, caused by a pull gesture.
         * 加载中
         */
        @Deprecated
        LOADING,

        /**
         * No more data
         * 没有更多数据
         */
        NO_MORE_DATA,
    }

    /**
     * 设置当前状态，派生类应该根据这个状态的变化来改变View的变化
     *
     * @param state 状态
     */
    public void setState(State state);

    /**
     * 得到当前的状态
     *
     * @return 状态
     */
    public State getState();

    /**
     * 得到当前Layout的内容大小，它将作为一个刷新的临界点
     *
     * @return 高度
     */
    public int getContentSize();

    /**
     * 在拉动时调用
     *
     * @param scale 拉动的比例
     */
    public void onPull(float scale);

}
