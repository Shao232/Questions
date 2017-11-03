package com.questions.activity;

import android.os.Bundle;

import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityExplainBinding;

public class ExplainActivity extends MyBaseActivity<ActivityExplainBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_explain;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("考试说明");
    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img,v -> finish());
    }
}
