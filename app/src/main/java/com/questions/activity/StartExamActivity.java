package com.questions.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityStartBinding;
import com.questions.utils.SharedPreferenceUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 开始模拟考试
 */
public class StartExamActivity extends MyBaseActivity<ActivityStartBinding> {

    private int type;// 1 科目一  2 科目四
    private int num;//模拟考试的次数

    @Override
    protected int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        type = getIntent().getExtras().getInt("type");
        setTitle("模拟考试");
        String userHead = SharedPreferenceUtils.getParams("userHead",this);
        String userName = SharedPreferenceUtils.getParams("userName",this);
        Glide.with(getApplicationContext()).load(userHead)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .placeholder(R.mipmap.defult_img)
                .error(R.mipmap.defult_img)
                .into(mBinding.ivUserHeadQuestionStart);
        mBinding.tvUserNicknameQuestionStart.setText(userName);
        if (num == 0) {
            mBinding.tvExamNumQuestionStart.setText("您今天还没开始模拟考试");
        } else {
            mBinding.tvExamNumQuestionStart.setText("您今天已经模拟了" + num + "次，继续加油哦！");
        }

        if (type==1){
            mBinding.tvQuestionType.setText("科目一");
        }else{
            mBinding.tvQuestionType.setText("科目四");
        }

    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img,v -> finish());
        mBinding.btnStartQuestions.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("questionType", 1);// 1 模拟考试 2 章节练习
            bundle.putInt("type", type);
            startActivity(bundle, SubjectActivity.class);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
