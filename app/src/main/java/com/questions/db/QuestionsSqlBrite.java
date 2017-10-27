package com.questions.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by 11470 on 2017/10/17.
 */

public class QuestionsSqlBrite {

    private static volatile QuestionsSqlBrite singleton = null;

    private SqlBrite sqlBrite;
    private BriteDatabase db;
    private QuestionsDataBase dataBase;

    private QuestionsSqlBrite(Context context){
        sqlBrite = new SqlBrite.Builder().build();
        dataBase = new QuestionsDataBase(context);
        db = sqlBrite.wrapDatabaseHelper(dataBase, Schedulers.io());
    }

    public static QuestionsSqlBrite getSqlSingleton(Context context) {
        if (singleton == null) {
            synchronized (QuestionsSqlBrite.class) {
                if (singleton == null) {
                    singleton = new QuestionsSqlBrite(context);
                }
            }
        }
        return singleton;
    }

//    public synchronized void insertSubject1(ContentValues values) {
//        db.insert(QuestionsMetaData.MetaData.TABLE_NAME_SUBJECT1, values);
//    }
//
//    public synchronized void insertSubject4(ContentValues values) {
//        db.insert(QuestionsMetaData.MetaData.TABLE_NAME_SUBJECT4, values);
//    }

//    public QueryObservable createQuerySubject1(String sql) {
//        return db.createQuery(QuestionsMetaData.MetaData.TABLE_NAME_SUBJECT1, sql);
//    }
//
//    public QueryObservable createQuerySubject4(String sql) {
//        return db.createQuery(QuestionsMetaData.MetaData.TABLE_NAME_SUBJECT4, sql);
//    }

    public synchronized void insertCollections(ContentValues values) {
        db.insert(QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS, values);
    }

    public synchronized void insertError(ContentValues values) {
        db.insert(QuestionsMetaData.MetaData.TABLE_NAME_ERROR, values);
    }

    public Cursor rawQueryDb(String sql, String[] selectionArgs){
        return dataBase.rawQuery(sql,selectionArgs);
    }

    public QueryObservable createQueryCollections(String sql) {
        return db.createQuery(QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS, sql);
    }

    public QueryObservable createQueryError(String sql) {
        return db.createQuery(QuestionsMetaData.MetaData.TABLE_NAME_ERROR, sql);
    }

    public synchronized void deleteCollections(String whereClause,String... whereArgs){
        db.delete(QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS,whereClause,whereArgs);
    }

    public synchronized void deleteError(String whereClause,String... whereArgs){
        db.delete(QuestionsMetaData.MetaData.TABLE_NAME_ERROR,whereClause,whereArgs);
    }

}
