package com.questions.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.databinding.ActivityExplainBinding;
import com.questions.utils.SharedPreferenceUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ExplainActivity extends MyBaseActivity<ActivityExplainBinding> {

    @Override
    protected int getLayout() {
        return R.layout.activity_explain;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("考试说明");
        String userHead = SharedPreferenceUtils.getParams("userHead",this);
        String userName = SharedPreferenceUtils.getParams("userName",this);
        Glide.with(getApplicationContext()).load(userHead)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .placeholder(R.mipmap.defult_img)
                .error(R.mipmap.defult_img)
                .into(mBinding.ivUserHeadExplain);
        mBinding.tvUserNicknameExplain.setText(userName);
    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img,v -> finish());
    }
}
