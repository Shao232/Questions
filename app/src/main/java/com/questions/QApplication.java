package com.questions;

import com.questions.db.QuestionsSqlBrite;
import com.slibrary.base.BaseApplication;

/**
 * Created by 11470 on 2017/10/19.
 */

public class QApplication extends BaseApplication {

    public static QApplication instance;


    public static QApplication getInstance() {
        if (instance == null) {
            instance = new QApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getHost() {
        return getString(R.string.ip);
    }
}
