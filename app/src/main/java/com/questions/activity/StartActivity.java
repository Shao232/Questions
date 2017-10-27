package com.questions.activity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityStartBinding;
import com.slibrary.utils.SharedPreferenceUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 开始模拟考试
 */
public class StartActivity extends MyBaseActivity<ActivityStartBinding> {

    private int type;// 1 科目一  2 科目四
    private String todayDate;//当天时间
    private int num;//模拟考试的次数

    @Override
    protected int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        type = getIntent().getExtras().getInt("type");
        todayDate = getIntent().getExtras().getString("todayDate");
        num = SharedPreferenceUtils.getIntValue(getApplicationContext(), "QuestionNum");
        setTitle("模拟考试");
        Glide.with(getApplicationContext()).load("\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508243782118&di=b4a265dfb48f1bcfc91c7041960e5e0a&imgtype=0&src=http%3A%2F%2Fi2.17173.itc.cn%2F2013%2Fflash%2F2013%2Flinshi%2Fheji%2Fhzw.jpg")
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .placeholder(R.mipmap.defult_img)
                .error(R.mipmap.defult_img)
                .into(mBinding.ivUserHeadQuestionStart);
        mBinding.tvUserNicknameQuestionStart.setText("张三");
        if (num == 0) {
            mBinding.tvExamNumQuestionStart.setText("您今天还没开始模拟考试");
        } else {
            mBinding.tvExamNumQuestionStart.setText("您今天已经模拟了" + num + "次，继续加油哦！");
        }

    }

    @Override
    protected void initEvent() {
        mBinding.btnStartQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("questionType", 1);// 1 模拟考试 2 章节练习
                bundle.putInt("type", type);
                startActivity(bundle, SubjectActivity.class);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
