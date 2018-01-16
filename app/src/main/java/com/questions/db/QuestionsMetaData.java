package com.questions.db;

import android.provider.BaseColumns;

/**
 * Created by 11470 on 2017/10/17.
 */

public class QuestionsMetaData {

    // 数据库名称常量
    public static final String DATABASE_NAME = "question.db";
    // 数据库版本常量
    public static final int DATABASE_VERSION = 1;

    public interface MetaData extends BaseColumns{
        String TABLE_NAME_SUBJECT1 = "Subject1";//科目1 表

        String TABLE_NAME_SUBJECT4 = "Subject4";//科目4 表

        String TABLE_NAME_ERROR_SUBJECT1= "ErrorSubject1";//错题表 科目1

        String TABLE_NAME_ERROR_SUBJECT4= "ErrorSubject4";//错题表 科目4

        String TABLE_NAME_COLLECTIONS_SUBJECT1 = "collectionsSubject1";//收藏表 科目1

        String TABLE_NAME_COLLECTIONS_SUBJECT4 = "collectionsSubject4";//收藏表 科目4

        String ID = "id";

        String ANSWER = "answer";

        String EXPLAINS = "explains";

        String ITEM1 = "item1";

        String ITEM2 = "item2";

        String ITEM3 = "item3";

        String ITEM4 = "item4";

        String QUESTION = "question";

        String TYPE= "type";// 1 单选 2 判断

        String TIME = "time";//时间

        String MYANSWER ="myAnswer";//我选择的答案

        String URL = "url";

    }

}
