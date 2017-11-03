package com.questions.activity.base;


import android.support.multidex.MultiDexApplication;

import com.questions.utils.DebugUtils;



/**
 * Created by 11470 on 2017/9/21.
 */

public abstract class BaseApplication extends MultiDexApplication{

    public static String IP;

    @Override
    public void onCreate() {
        super.onCreate();
        DebugUtils.syncIsDebug(getApplicationContext());
        IP = getHost();
    }

    protected abstract String getHost();

}
