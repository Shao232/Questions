package com.questions.activity;

import android.os.Bundle;
import android.view.View;

import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityMainBinding;

public class MainActivity extends MyBaseActivity<ActivityMainBinding> implements View.OnClickListener{

//    QuestionsSqlBrite sqlBrite;
//
//    Runnable runnable1 = () -> {
//        ArrayList<QuestionsBean> beenList = new ArrayList<>();
//        SQLiteDatabase database = FileUtils.openDataBase(getApplicationContext(), "question.db", "question.db");
//        Cursor cursor = null;
//        if (database != null) {
//            cursor = database.query("Subject1", null, null, null, null, null, null, null);
//        }
//
////                if (database != null) {
////                    cursor = database.query("Subject4", null, null, null, null, null, null, null);
////                }
//        if (cursor != null) {
//            while (cursor.getCount() > 0 && cursor.moveToNext()) {
//                int idColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.ID);
//                int answerColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.ANSWER);
//                int explainsColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.EXPLAINS);
//                int item1Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM1);
//                int item2Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM2);
//                int item3Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM3);
//                int item4Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM4);
//                int questionColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.QUESTION);
//                int urlColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.URL);
//                int typeColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.TYPE);
//                QuestionsBean bean = new QuestionsBean();
//                bean.setId(cursor.getString(idColumn));
//                bean.setAnswer(cursor.getString(answerColumn));
//                bean.setExplains(cursor.getString(explainsColumn));
//                bean.setItem1(cursor.getString(item1Column));
//                bean.setItem2(cursor.getString(item2Column));
//                bean.setItem3(cursor.getString(item3Column));
//                bean.setItem4(cursor.getString(item4Column));
//                bean.setQuestion(cursor.getString(questionColumn));
//                bean.setType(cursor.getString(typeColumn));
//                bean.setUrl(cursor.getString(urlColumn));
////                MyLog.i("url>>>"+bean.getUrl());
//                sqlBrite.insertSubject1(QuestionsBean.getContentValues(bean));
//            }
//            cursor.close();
//        }
//        Message message = new Message();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("question", beenList);
//        message.setData(bundle);
//        message.what = 0x123;
//        handler.sendMessage(message);
//    };
//
//    Runnable runnabl2 = () -> {
//        ArrayList<QuestionsBean> beenList = new ArrayList<>();
//        SQLiteDatabase database = FileUtils.openDataBase(getApplicationContext(),
//                "question.db", "question.db");
//        Cursor cursor = null;
//        if (database != null) {
//            cursor = database.query("Subject4", null, null, null, null, null, null, null);
//        }
////                if (database != null) {
////                    cursor = database.query("Subject4", null, null, null, null, null, null, null);
////                }
//        if (cursor != null) {
//            while (cursor.getCount() > 0 && cursor.moveToNext()) {
//                int idColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.ID);
//                int answerColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.ANSWER);
//                int explainsColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.EXPLAINS);
//                int item1Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM1);
//                int item2Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM2);
//                int item3Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM3);
//                int item4Column = cursor.getColumnIndex(QuestionsMetaData.MetaData.ITEM4);
//                int questionColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.QUESTION);
//                int urlColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.URL);
//                int typeColumn = cursor.getColumnIndex(QuestionsMetaData.MetaData.TYPE);
//                QuestionsBean bean = new QuestionsBean();
//                bean.setId(cursor.getString(idColumn));
//                bean.setAnswer(cursor.getString(answerColumn));
//                bean.setExplains(cursor.getString(explainsColumn));
//                bean.setItem1(cursor.getString(item1Column));
//                bean.setItem2(cursor.getString(item2Column));
//                bean.setItem3(cursor.getString(item3Column));
//                bean.setItem4(cursor.getString(item4Column));
//                bean.setQuestion(cursor.getString(questionColumn));
//                bean.setType(cursor.getString(typeColumn));
//                bean.setUrl(cursor.getString(urlColumn));
////                MyLog.i("url>>>"+bean.getUrl());
//                try {
//                    if (StringUtil.isNotEmpty(bean.getUrl()) && bean.getUrl().contains(".swf")) {
//                        MyLog.i("id>>>>"+bean.getId()+",>>>>>"+bean.getUrl());
//                        switch (bean.getId()){
//                            case "34":
//                          bean.setUrl(Base64.encodeToString(
//                                     FileUtils.InputStreamTOByte(getAssets().open("34.gif")),Base64.DEFAULT));
//                                break;
//                            case "38":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("38.gif")),Base64.DEFAULT));
//                                break;
//                            case "56":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("56.gif")),Base64.DEFAULT));
//                                break;
//                            case "68":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("68.gif")),Base64.DEFAULT));
//                                break;
//                            case "70":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("70.gif")),Base64.DEFAULT));
//                                break;
//                            case "179":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("179.gif")),Base64.DEFAULT));
//                                break;
//                            case "306":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("306.gif")),Base64.DEFAULT));
//                                break;
//                            case "333":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("333.gif")),Base64.DEFAULT));
//                                break;
//                            case "453":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("453.gif")),Base64.DEFAULT));
//                                break;
//                            case "524":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("524.gif")),Base64.DEFAULT));
//                                break;
//                            case "567":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("567.gif")),Base64.DEFAULT));
//                                break;
//                            case "616":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("616.gif")),Base64.DEFAULT));
//                                break;
//                            case "648":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("648.gif")),Base64.DEFAULT));
//                                break;
//                            case "696":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("696.gif")),Base64.DEFAULT));
//                                break;
//                            case "706":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("706.gif")),Base64.DEFAULT));
//                                break;
//                            case "707":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("707.gif")),Base64.DEFAULT));
//                                break;
//                            case "822":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("822.gif")),Base64.DEFAULT));
//                                break;
//                            case "836":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("836.gif")),Base64.DEFAULT));
//                                break;
//                            case "876":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("876.gif")),Base64.DEFAULT));
//                                break;
//                            case "905":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("905.gif")),Base64.DEFAULT));
//                                break;
//                            case "971":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("971.gif")),Base64.DEFAULT));
//                                break;
//                            case "973":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("973.gif")),Base64.DEFAULT));
//                                break;
//                            case "985":
//                                bean.setUrl(Base64.encodeToString(
//                                        FileUtils.InputStreamTOByte(getAssets().open("985.gif")),Base64.DEFAULT));
//                                break;
//                        }
//                        sqlBrite.insertSubject4(QuestionsBean.getContentValues(bean));
//                    }else{
//                        sqlBrite.insertSubject4(QuestionsBean.getContentValues(bean));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            cursor.close();
//        }
//        Message message = new Message();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("question", beenList);
//        message.setData(bundle);
//        message.what = 0x123;
//        handler.sendMessage(message);
//    };
//
//    @Override
//    protected void HandlerMessage(Context mContext, Message msg) {
//        super.HandlerMessage(mContext, msg);
//        if (msg.what == 0x123){
//            ArrayList<QuestionsBean> beanArrayList = (ArrayList<QuestionsBean>) msg.getData().getSerializable("question");
//            for (int i = 0; i < beanArrayList.size(); i++) {
//                QuestionsBean bean =  beanArrayList.get(i);
//
//                MyLog.i("id>>>>"+bean.getId()+",>>>>>"+bean.getUrl());
//            }
//        }
//    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mBinding.setOnClick(this);
//        sqlBrite = QuestionsSqlBrite.getSqlSingleton(this);
//        new Thread(runnable1).start();
//        new Thread(runnabl2).start();
    }

    @Override
    protected void initEvent() {
    }

    @Override
    public void onClick(View v) {
        startActivity(QuestionsActivity.class);
    }


}
