package com.questions.activity;

import android.os.Bundle;
import android.view.View;

import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityMainBinding;

public class MainActivity extends MyBaseActivity<ActivityMainBinding> implements View.OnClickListener{

//    QuestionsSqlBrite sqlBrite;
//    Runnable runnable1 = () -> {
//        ArrayList<QuestionsBean> beenList = new ArrayList<>();
//        SQLiteDatabase database = FileUtils.openDataBase(getApplicationContext(), "questions.db", "questions.db");
////        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("data/data/"+this.getPackageName()+"/databases/questions.db",null);
//        Cursor cursor = null;
//        if (database != null) {
////            cursor = database.query("Subject1", null, null, null, null, null, null, null);
//            cursor = database.rawQuery("select * from Subject1 ",null);
//        }
//        if (cursor != null) {
//            QuestionsBean bean;
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
//                bean = new QuestionsBean();
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
//            database.close();
//        }
//        Message message = new Message();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("question", beenList);
//        message.setData(bundle);
//        message.what = 0x123;
//        handler.sendMessage(message);
//    };
//
//    Runnable runnable2 = () -> {
//        ArrayList<QuestionsBean> beenList = new ArrayList<>();
//        SQLiteDatabase database = FileUtils.openDataBase(getApplicationContext(), "questions.db", "questions.db");
////        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase("data/data/"+this.getPackageName()+"/databases/questions.db",null);
//        Cursor cursor = null;
//        if (database != null) {
//            cursor = database.rawQuery("select * from Subject4",null);
////            cursor = database.query("Subject4", null, null, null, null, null, null, null);
//        }
//        if (cursor != null) {
//            QuestionsBean bean;
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
//                bean = new QuestionsBean();
//                MyLog.i("数据>>>"+cursor.getString(idColumn));
//                bean.setId(cursor.getString(idColumn));
//                bean.setAnswer(cursor.getString(answerColumn));
//                bean.setExplains(cursor.getString(explainsColumn));
//                bean.setItem1(cursor.getString(item1Column));
//                bean.setItem2(cursor.getString(item2Column));
//                bean.setItem3(cursor.getString(item3Column));
//                bean.setItem4(cursor.getString(item4Column));
//                bean.setQuestion(cursor.getString(questionColumn));
//                bean.setType(cursor.getString(typeColumn));
////                bean.setUrl(cursor.getString(urlColumn));
////                MyLog.i("url>>>"+bean.getUrl());
//                try {
//                    if (StringUtil.isNotEmpty(bean.getUrl()) && bean.getUrl().contains(".swf")) {
//                        MyLog.i("id>>>>"+bean.getId()+",>>>>>"+bean.getUrl());
//                        switch (bean.getId()){
//                            case "34":
//                                bean.setUrl("");
//                                break;
//                            case "38":
//                                bean.setUrl("");
//                                break;
//                            case "56":
//                                bean.setUrl("");
//                                break;
//                            case "68":
//                                bean.setUrl("");
//                                break;
//                            case "70":
//                                bean.setUrl("");
//                                break;
//                            case "179":
//                                bean.setUrl("");
//                                break;
//                            case "306":
//                                bean.setUrl("");
//                                break;
//                            case "333":
//                                bean.setUrl("");
//                                break;
//                            case "453":
//                                bean.setUrl("");
//                                break;
//                            case "524":
//                                bean.setUrl("");
//                                break;
//                            case "567":
//                                bean.setUrl("");
//                                break;
//                            case "616":
//                                bean.setUrl("");
//                                break;
//                            case "648":
//                                bean.setUrl("");
//                                break;
//                            case "696":
//                                bean.setUrl("");
//                                break;
//                            case "706":
//                                bean.setUrl("");
//                                break;
//                            case "707":
//                                bean.setUrl("");
//                                break;
//                            case "822":
//                                bean.setUrl("");
//                                break;
//                            case "836":
//                                bean.setUrl("");
//                                break;
//                            case "876":
//                                bean.setUrl("");
//                                break;
//                            case "905":
//                                bean.setUrl("");
//                                break;
//                            case "971":
//                                bean.setUrl("");
//                                break;
//                            case "973":
//                                bean.setUrl("");
//                                break;
//                            case "985":
//                                bean.setUrl("");
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
//            database.close();
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
//                MyLog.i("id>>>>"+bean.getId()+",>>>>>"+bean.getUrl());
//                mBinding.tvQuestions.setText("插入成功");
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
//       sqlBrite = QuestionsSqlBrite.getSqlSingleton(this);
//        new Thread(runnable1).start();
//        new Thread(runnable2).start();
      }

    @Override
    protected void initEvent() {
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("userId","70");
        bundle.putString("userName","祸害帅");
        bundle.putString("userHead","https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508243782120&di=9f458e33e61fd82f935adea402d312cc&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201112%2F15%2F20111215195115_x5HZs.thumb.700_0.jpg");
        startActivity(bundle,QuestionsActivity.class);
    }


}
