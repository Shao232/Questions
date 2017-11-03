package com.questions.activity.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by dingrui on 16/7/7.
 * 所有Activity中的Handler必须继承此Handler
 */
public abstract class BaseHandler extends Handler {
    private WeakReference<Context> mContext;

    public BaseHandler(Context mContext) {
        if (mContext == null) {
            throw new NullPointerException();
        } else {
            this.mContext = new WeakReference<>(mContext);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        try {
            if (mContext != null && mContext.get() != null) {
                handleMessage(mContext.get(), msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleMessage(Context mContext, Message msg);
}
