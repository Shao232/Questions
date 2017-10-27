package com.slibrary.widgets.pullrecyclerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slibrary.R;
import com.slibrary.base.BaseHandler;
import com.slibrary.utils.MyUtils;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class PullToRefreshLayout extends RelativeLayout {

    private int touchSpacing;
    public static final String TAG = "PullToRefreshLayout";
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 上/下拉通知接口
    private OnPullListener l;
    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 按下Y坐标，按下X坐标，上一个事件点Y坐标
    private float downY, downX, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;


    // 释放刷新的距离
    private int refreshDist = 200;
    // 释放加载的距离
    private int loadmoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View refreshView;
    // 下拉的箭头
    private View pullView;
    // 正在刷新的图标
    private ImageView refreshingView;
    // 刷新结果图标
    private ImageView refreshStateImageView;
    // 刷新结果：成功或失败
    private TextView refreshStateTextView;
    // 最近更新
    private TextView tv_ecent_update;

    // 上拉头
    private View loadmoreView;
    // 上拉的箭头
    private View pullUpView;
    // 正在加载的图标
    private View loadingView;
    // 加载结果图标
    private View loadStateImageView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    // 是否显示最近更新
    private boolean isShowTime = false;
    // 最近更新时间
    private String time;

    // 字体颜色
    private int textColor = Color.WHITE;

    // 刷新图标
    private int refreshingImg = R.drawable.refreshing;

    // 刷新成功的图标
    private int refresh_success = R.drawable.refresh_succeed;

    // 刷新失败的图标
    private int refresh_failed = R.drawable.refresh_failed;

    // 触摸模式
    private int mode = 0;
    private final int NO_MODE = 0,// 没有任何动作
            MODE1 = 1,// 手指横向滑动
            MODE2 = 2,// 手指纵向滑动
            MODE3 = 3;// 长按

    private final int MSG_LONG_PRESS = 0x1;// 长按消息

    private boolean isRefreshFinish = true,
            isLoadmoreFinish = true;
    private int refreshResult;

    /**
     * 执行自动回滚的handler
     */
    MyHandler updateHandler = null;

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    /**
     * 设置刷新/加载更多接口
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 设置上/下拉手势监听接口
     *
     * @param l
     */
    public void setOnPullListener(OnPullListener l) {
        this.l = l;
    }

    private void initView(Context context) {
        updateHandler = new MyHandler(context);
        touchSpacing = ViewConfiguration.get(context).getScaledEdgeSlop();
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                    context, com.slibrary.R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        this.refreshResult = refreshResult;
        isRefreshFinish = false;
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_succeed);
                refreshStateImageView.setBackgroundResource(refresh_success);
                if (isShowTime) {
                    tv_ecent_update.setVisibility(View.GONE);
                }
                break;
            case FAIL:
            default:
                // 刷新失败
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_fail);
                refreshStateImageView.setBackgroundResource(refresh_failed);
                if (isShowTime) {
                    tv_ecent_update.setVisibility(View.GONE);
                }
                break;
        }
        // 刷新结果停留1秒
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeState(DONE);
                hide();
                isRefreshFinish = true;
            }
        }, 1000);
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int refreshResult) {
        this.refreshResult = refreshResult;
        isLoadmoreFinish = false;
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_succeed);
                loadStateImageView.setBackgroundResource(R.drawable.load_succeed);
                break;
            case FAIL:
            default:
                // 加载失败
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_fail);
                loadStateImageView.setBackgroundResource(R.drawable.load_failed);
                break;
        }
        // 刷新结果停留1秒
        updateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeState(DONE);
                hide();
                isLoadmoreFinish = true;
            }
        }, 1000);
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.pull_to_refresh);
                pullView.clearAnimation();
                pullView.setVisibility(View.VISIBLE);
                // 上拉布局初始状态
                loadStateImageView.setVisibility(View.GONE);
                loadStateTextView.setText(R.string.pullup_to_load);
                pullUpView.clearAnimation();
                pullUpView.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshStateTextView.setText(R.string.release_to_refresh);
                pullView.startAnimation(rotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
                pullView.clearAnimation();
                refreshingView.setVisibility(View.VISIBLE);
                pullView.setVisibility(View.INVISIBLE);
                refreshingView.startAnimation(refreshingAnimation);
                refreshStateTextView.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadStateTextView.setText(R.string.release_to_load);
                pullUpView.startAnimation(rotateAnimation);
                break;
            case LOADING:
                // 正在加载状态
                pullUpView.clearAnimation();
                loadingView.setVisibility(View.VISIBLE);
                pullUpView.setVisibility(View.INVISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mode = NO_MODE;
                downY = ev.getY();
                downX = ev.getX();
                timer.cancel();
                mEvents = 0;
                releasePull();
                if (isShowTime) {
                    tv_ecent_update.setText("最近更新:" + (time != null ? time : "未知"));
                    tv_ecent_update.setVisibility(View.VISIBLE);
                }
                if (updateHandler != null)
                    updateHandler.sendEmptyMessageDelayed(MSG_LONG_PRESS, 300);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == NO_MODE) {
                    lastY = ev.getY();
                    if (Math.abs(ev.getX() - downX) > touchSpacing
                        && Math.abs(ev.getY() - downY) < touchSpacing / 2) {
                        mode = MODE1;
                        if (updateHandler != null)
                            updateHandler.removeMessages(MSG_LONG_PRESS);
                    } else if (Math.abs(ev.getY() - downY) >= touchSpacing / 2
                               && Math.abs(ev.getX() - downX) <= touchSpacing) {
                        mode = MODE2;
                        if (updateHandler != null)
                            updateHandler.removeMessages(MSG_LONG_PRESS);
                    }
                }
                if (mode == MODE1) {
                    break;
                } else if (mode == MODE2) {
                    if (mEvents == 0) {
                        if (((Pullable) pullableView).canPullDown() && canPullDown
                            && state != LOADING) {
                            // 可以下拉，正在加载时不能下拉
                            // 对实际滑动距离做缩小，造成用力拉的感觉
                            pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                            if (pullDownY < 0) {
                                pullDownY = 0;
                                canPullDown = false;
                                canPullUp = true;
                            }
                            if (pullDownY > getMeasuredHeight())
                                pullDownY = getMeasuredHeight();
                            if (state == REFRESHING) {
                                // 正在刷新的时候触摸移动
                                isTouch = true;
                            }
                        } else if (((Pullable) pullableView).canPullUp()
                                   && canPullUp && state != REFRESHING) {
                            // 可以上拉，正在刷新时不能上拉
                            pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                            if (pullUpY > 0) {
                                pullUpY = 0;
                                canPullDown = true;
                                canPullUp = false;
                            }
                            if (pullUpY < -getMeasuredHeight())
                                pullUpY = -getMeasuredHeight();
                            if (state == LOADING) {
                                // 正在加载的时候触摸移动
                                isTouch = true;
                            }
                        } else
                            releasePull();
                    } else
                        mEvents = 0;
                    lastY = ev.getY();
                    // 根据下拉距离改变比例
                    radio = (float) (2 + 2 * Math
                            .tan(Math.PI / 2 / getMeasuredHeight()
                                 * (pullDownY + Math.abs(pullUpY))));
                    requestLayout();
                    if (pullDownY <= refreshDist
                        && (state == RELEASE_TO_REFRESH || state == DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(INIT);
                    }
                    if (pullDownY >= refreshDist
                        && (state == INIT || state == DONE)) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH);
                    }
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= loadmoreDist
                        && (state == RELEASE_TO_LOAD || state == DONE)) {
                        changeState(INIT);
                    }
                    if (-pullUpY >= loadmoreDist
                        && (state == INIT || state == DONE)) {
                        changeState(RELEASE_TO_LOAD);
                    }
                        // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                    // Math.abs(pullUpY))就可以不对当前状态作区分了
                    if ((pullDownY + Math.abs(pullUpY)) > 8) {
                        // 防止下拉过程中误触发长按事件和点击事件
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == MODE1) {
                    return super.dispatchTouchEvent(ev);
                } else if (mode == MODE2) {
                    if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                        // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                        isTouch = false;
                    if (state == RELEASE_TO_REFRESH) {
                        changeState(REFRESHING);
                        // 刷新操作
                        if (mListener != null)
                            mListener.onRefresh(this);
                    } else if (state == RELEASE_TO_LOAD) {
                        changeState(LOADING);
                        // 加载操作
                        if (mListener != null)
                            mListener.onLoadMore(this);
                    }
                    hide();
                } else if (mode == MODE3) {
                    performLongClick();
                }
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    private void initView() {
        // 初始化下拉布局
        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (TextView) refreshView
                .findViewById(R.id.state_tv);
        tv_ecent_update = (TextView) refreshView
                .findViewById(R.id.tv_ecent_update);
        refreshingView = (ImageView) refreshView
                .findViewById(R.id.refreshing_icon);
        refreshStateImageView = (ImageView) refreshView
                .findViewById(R.id.state_iv);
        // 初始化上拉布局
        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView
                .findViewById(R.id.loadstate_tv);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.loadstate_iv);
        refreshStateTextView.setTextColor(textColor);
        loadStateTextView.setTextColor(textColor);
        refreshingView.setImageResource(refreshingImg);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            loadmoreView = getChildAt(2);
            isLayout = true;
            initView();
//            refreshDist = ((ViewGroup) refreshView).getChildAt(0)
//                    .getMeasuredHeight();
//            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
//                    .getMeasuredHeight();
        }
        if (refreshDist != ((ViewGroup) refreshView).getChildAt(0)
                .getMeasuredHeight()) {
            refreshDist = ((ViewGroup) refreshView).getChildAt(0)
                    .getMeasuredHeight();
        }
        if (loadmoreDist != ((ViewGroup) loadmoreView).getChildAt(0)
                .getMeasuredHeight()) {
            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
                    .getMeasuredHeight();
        }

        if (pullDownY == 0) {
            if (PullToRefreshLayout.this.l != null)
                PullToRefreshLayout.this.l.isPullDown(false);
        } else {
            if (PullToRefreshLayout.this.l != null)
                PullToRefreshLayout.this.l.isPullDown(true);
        }

        if (pullUpY == 0) {
            if (PullToRefreshLayout.this.l != null) {
                PullToRefreshLayout.this.l.isPullUp(false);
            }
        } else {
            if (PullToRefreshLayout.this.l != null) {
                PullToRefreshLayout.this.l.isPullUp(true);
            }
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        refreshView.layout(0,
                (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        pullableView.layout(0, (int) (pullDownY + pullUpY),
                pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                                                 + pullableView.getMeasuredHeight());
        loadmoreView.layout(0,
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
                loadmoreView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
                + loadmoreView.getMeasuredHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (updateHandler == null) {
            updateHandler = new MyHandler(getContext());
        }
        if (timer == null) {
            timer = new MyTimer(updateHandler);
        }
        if (!isRefreshFinish) {
            refreshFinish(refreshResult);
        } else if (!isLoadmoreFinish) {
            loadmoreFinish(refreshResult);
        } else if (state == REFRESHING) {
            changeState(state);
        } else if (state == LOADING) {
            changeState(state);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
            updateHandler = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        pullDownY = refreshDist;
        pullUpY = 0;
        requestLayout();
        changeState(REFRESHING);
        if (mListener != null)
            mListener.onRefresh(this);
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout view);

        /**
         * 加载操作
         */
        void onLoadMore(PullToRefreshLayout view);
    }

    /**
     * 上/下拉手势监听
     */
    public interface OnPullListener {
        /**
         * @param isPullDown true下拉中,false结束下拉
         */
         void isPullDown(boolean isPullDown);

        /**
         * @param isPullUp true 上拉中,false结束上拉
         */
         void isPullUp(boolean isPullUp);
    }

    /**
     * 是否显示最近更新
     *
     * @return
     */
    public boolean isShowTime() {
        return isShowTime;
    }

    /**
     * 设置是否显示最近更新
     *
     * @param isShowTime
     */
    public void setShowTime(boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    /**
     * 获取当前最近更新时间
     *
     * @return
     */
    public String getTime() {
        return time;
    }

    /**
     * 设置最近更新时间
     *
     * @param time
     */
    public void setTime(String time) {
        if (time == null)
            time = MyUtils.getInstance().getTime24("yyyy-MM-dd HH:mm:ss",
                    Locale.CHINESE);
        this.time = time;
    }

    /**
     * 设置字体颜色
     */
    public void setblack(int textColor) {
        this.textColor = textColor;
        if (refreshStateTextView != null && loadStateTextView != null) {
            refreshStateTextView.setTextColor(textColor);
            loadStateTextView.setTextColor(textColor);
        }
    }

    /**
     * 设置正在刷新图标
     */
    public void setRefreshing(int refreshingImg) {
        this.refreshingImg = refreshingImg;
        if (refreshingView != null) {
            refreshingView.setImageResource(refreshingImg);
        }
    }

    /**
     * 设置刷新成功图标
     */
    public void setRefreshSuccess(int refresh_success) {
        this.refresh_success = refresh_success;
    }

    /**
     * 设置刷新失败图标
     */
    public void setRefresh_Failed(int refresh_failed) {
        this.refresh_failed = refresh_failed;
    }

    /**
     * Handler
     */
    class MyHandler extends BaseHandler {

        public MyHandler(Context mContext) {
            super(mContext);
        }

        @Override
        protected void handleMessage(Context mContext, Message msg) {
            switch (msg.what) {
                case MSG_LONG_PRESS:
                    mode = MODE3;
                    break;

                default:
                    // 回弹速度随下拉距离moveDeltaY增大而增大
                    MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                                                           / getMeasuredHeight() *
                                                           (pullDownY + Math.abs(pullUpY))));
                    if (!isTouch) {
                        // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                        if (state == REFRESHING && pullDownY <= refreshDist) {
                            pullDownY = refreshDist;
                            timer.cancel();
                        } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                            pullUpY = -loadmoreDist;
                            timer.cancel();
                        } else if (state == INIT && state == DONE) {
                            /** add by zoubangyue 2015-08-27 优化视图初始化状态的时候，取消定时器 */
                            // 已完成回弹
                            pullDownY = 0;
                            pullUpY = 0;
                            pullView.clearAnimation();
                            pullUpView.clearAnimation();
                            // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                            if (state != REFRESHING && state != LOADING)
                                changeState(INIT);
                            timer.cancel();
                        }

                    }
                    if (pullDownY > 0)
                        pullDownY -= MOVE_SPEED;
                    else if (pullUpY < 0)
                        pullUpY += MOVE_SPEED;
                    if (pullDownY < 0) {
                        // 已完成回弹
                        pullDownY = 0;
                        pullView.clearAnimation();
                        // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                        if (state != REFRESHING && state != LOADING)
                            changeState(INIT);
                        timer.cancel();
                    }
                    if (pullUpY > 0) {
                        // 已完成回弹
                        pullUpY = 0;
                        pullUpView.clearAnimation();
                        // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                        if (state != REFRESHING && state != LOADING)
                            changeState(INIT);
                        timer.cancel();
                    }
                    // 刷新布局,会自动调用onLayout
                    requestLayout();
                    break;
            }
        }
    }

}
