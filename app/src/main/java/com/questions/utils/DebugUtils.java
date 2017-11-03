package com.questions.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by 11470 on 2017/9/21.
 */

public class DebugUtils {
    private static Boolean isDebug = null;

    public static boolean isDEBUG(){
        return isDebug ==null ? false: isDebug;
    }

    public static void syncIsDebug(Context context){
        if (isDebug == null){
            isDebug = context.getApplicationInfo()!=null &&
                    (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) !=0;
        }
    }

}
