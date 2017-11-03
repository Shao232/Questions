package com.questions.activity.base;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.questions.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;

//import com.umeng.analytics.MobclickAgent;

//import com.umeng.analytics.MobclickAgent;

/**
 * 页面碎片的基类
 *
 * @author andytang
 * @created Jun 18, 2014 12:40:58 AM
 */
public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment {
    protected String TAG = "";

    protected LayoutInflater inflater;

    protected View rootView;
    //    protected GestureDetectorLayout gestureDetectorLayout;

    protected BroadcastReceiver internalReceiver;
    private boolean isFinish = false;

    protected boolean isPrepared;

    protected V mBinding;

    private ExecutorService executorService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        TAG = ((Object) this).getClass().getName();

    }

    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(internalReceiver, intentfilter);
    }



    protected void sendBroadCast(String action) {
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(action));
    }

    // Internal calss.
    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handleReceiver(context, intent);
        }
    }

    /**
     * 如果子界面需要拦截处理注册的广播
     * 需要实现该方法
     *
     * @param context
     * @param intent
     */
    protected void handleReceiver(Context context, Intent intent) {
        // 广播处理
        if (intent == null) {
            return;
        }
    }



    protected abstract void init();

    protected Myhandler handler = new Myhandler(this);

    public static class Myhandler extends Handler {
        WeakReference<BaseFragment> fragment;

        public Myhandler(BaseFragment fragment) {
            // TODO Auto-generated constructor stub
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            BaseFragment fragment = this.fragment.get();
            if (fragment != null) {
                if (fragment.getActivity() != null)
                    fragment.handleMessages(msg);
            }
        }
    }

    protected void handleMessages(Message msg) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            this.inflater = inflater;
            if (rootView == null) {
                mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
                rootView = mBinding.getRoot();
            }

            ViewGroup pagrent = (ViewGroup) rootView.getParent();
            if (pagrent != null) {
                pagrent.removeView(rootView);
            }
            return rootView;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */

    private boolean isFirstResume = true;

    private boolean isFirstVisible = true;

    private boolean isFirstInvisible = true;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }

        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();

            } else {
                onUserInvisible();
            }
        }
    }


    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }


    /**
     * 第一次fragment可见（进行初始化工作）
     */

    public void onFirstUserVisible() {
        Log.i(TAG, "第一次可见");
        lazyLoad();
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
        Log.i(TAG, "fragment可见");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
        Log.i(TAG, "第一次fragment不可见");
    }


    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
        Log.i(TAG, "fragment不可见");

    }


    protected void lazyLoad() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
        initEvent(savedInstanceState);
        initPrepare();
    }

    protected abstract void initEvent(Bundle savedInstanceState);

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;

        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        System.gc();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(internalReceiver);
        } catch (Exception e) {

        }
    }

    protected void showToast(String msg){
        ToastUtil.showToast(getContext(),msg);
    }

    /**
     * 切换到指定的Activity， 无传递数据
     *
     * @param cls 指定的Activity
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
//		getActivity().overridePendingTransition(R.anim.push_out, R.anim.scale_out);
    }

    /**
     * 切换到指定的Activity，有数据传递
     *
     * @param bundle 传递的数据
     * @param cls    指定的Activity
     */
    public void startActivity(Bundle bundle, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
//		getActivity().overridePendingTransition(R.anim.push_out, R.anim.scale_out);
    }

    /**
     * 切换到指定的Activity，无传递数据，但需要返回结果
     *
     * @param cls         指定的Activity
     * @param requestCode 返回结果代码
     */
    public void showItemActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivityForResult(intent, requestCode);
//		getActivity().overridePendingTransition(R.anim.push_out, R.anim.scale_out);
    }

    /**
     * 切换到指定的Activity，传递数据，需要返回结果
     *
     * @param bundle      传递数据
     * @param cls         指定的Activity
     * @param requestCode 返回结果代码
     */
    public void showItemActivityForResult(Bundle bundle, Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }


    /**
     * 获取页面的布局文件ID
     */
    protected abstract int getLayout();

    /**
     * 重载页面关闭方法
     */
    public void finish() {
        if (getActivity() == null) {
            return;
        }
        if (isFinish) {
            getActivity().finish();
            return;
        }

        getActivity().getSupportFragmentManager().popBackStack();
    }

}
