package com.questions.activity;

import android.os.Bundle;
import android.view.View;

import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityMainBinding;

public class MainActivity extends MyBaseActivity<ActivityMainBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

//        QHttpManager.getInstance().getData(1, "c1", "order", new Consumer<List<QuestionsBean>>() {
//            @Override
//            public void accept(@NonNull List<QuestionsBean> dataList) throws Exception {
//                Log.i("TAG","数据数量:"+dataList.size());
//                List<QuestionsBean> beanList = new ArrayList<>();
//                for (int i = 0; i < dataList.size(); i++) {
//                    QuestionsBean bean =  dataList.get(i);
//                    // 	当四个选项都为空的时候表示判断题,item1:正确 item2:错误,请开发者自行判断!
//                    if (StringUtil.isEmpty(bean.getItem1()) && StringUtil.isEmpty(bean.getItem2())
//                            && StringUtil.isEmpty(bean.getItem3()) && StringUtil.isEmpty(bean.getItem4())){
//                        bean.setItem1("正确");
//                        bean.setItem2("错误");
//                    }
//
//                    if (StringUtil.isEqual(bean.getItem1(),"正确")
//                            || StringUtil.isEqual(bean.getItem1(),"对")){
//                        bean.setType("2");
//                    }else if (bean.getAnswer().length() >1){
//                        bean.setType("3");
//                    } else{
//                        bean.setType("1");
//                    }
//
//                    beanList.add(bean);
//                }
//
//
//                for (QuestionsBean bean : beanList){
//                    sqlBrite.insertSubject1(QuestionsBean.getContentValues(bean));
//                }
//                MyLog.i("Question>>>>科目1插入数据库完成");
//            }
//        }, new QHttpManager.OnThrowableListener() {
//            @Override
//            public void accept(Throwable throwable) {
//                MyLog.e(throwable.getMessage());
//            }
//        });
//
//        QHttpManager.getInstance().getData(4, "c1", "order", new Consumer<List<QuestionsBean>>() {
//            @Override
//            public void accept(@NonNull List<QuestionsBean> dataList) throws Exception {
//                Log.i("TAG","数据数量:"+dataList.size());
//                List<QuestionsBean> beanList = new ArrayList<>();
//                for (int i = 0; i < dataList.size(); i++) {
//                    QuestionsBean bean =  dataList.get(i);
//                    // 	当四个选项都为空的时候表示判断题,item1:正确 item2:错误,请开发者自行判断!
//                    if (StringUtil.isEmpty(bean.getItem1()) && StringUtil.isEmpty(bean.getItem2())
//                            && StringUtil.isEmpty(bean.getItem3()) && StringUtil.isEmpty(bean.getItem4())){
//                        bean.setItem1("正确");
//                        bean.setItem2("错误");
//                    }
//
//                    if (StringUtil.isEqual(bean.getItem1(),"正确")
//                            || StringUtil.isEqual(bean.getItem1(),"对")){
//                        bean.setType("2");
//                    }else if (bean.getAnswer().length() >1){
//                        bean.setType("3");
//                    } else{
//                        bean.setType("1");
//                    }
//                    beanList.add(bean);
//                }
//
//                for (QuestionsBean bean : beanList){
//                    sqlBrite.insertSubject4(QuestionsBean.getContentValues(bean));
//                }
//                MyLog.i("Question>>>>科目4插入数据库完成");
//            }
//        }, new QHttpManager.OnThrowableListener() {
//            @Override
//            public void accept(Throwable throwable) {
//                MyLog.e(throwable.getMessage());
//            }
//        });

    }

    @Override
    protected void initEvent() {
        mBinding.tvQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(QuestionsActivity.class);
            }
        });
    }



}
