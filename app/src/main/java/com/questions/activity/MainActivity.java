package com.questions.activity;

import android.os.Bundle;

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

    }

    @Override
    protected void initEvent() {
        mBinding.tvQuestions.setOnClickListener(view -> startActivity(QuestionsActivity.class));
    }



}
