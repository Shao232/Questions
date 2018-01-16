package com.questions.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.questions.R;
import com.questions.databinding.ActivityResultBinding;
import com.questions.activity.base.BaseActivity;

public class ResultActivity extends BaseActivity<ActivityResultBinding> {

    private String time;
    private int type;
    private int source;

    @Override
    protected int getLayout() {
        return R.layout.activity_result;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        time = bundle.getString("time");
        type = bundle.getInt("subjectType");
        source = bundle.getInt("source");
        setTitle("考试结果");
        if (type == 1){
            mBinding.tvQuestionTypeResult.setText("科目1");
        }else if (type == 2){
            mBinding.tvQuestionTypeResult.setText("科目4");
        }
        mBinding.tvTimeResult.setText(time);
        mBinding.tvResultSource.setText(""+source);
        if (source < 90){
            mBinding.tvResultSource.setTextColor(getResources().getColor(R.color.color_f4011f));
            mBinding.lvnResultBack.setImageResource(R.mipmap.result_no_img);
            mBinding.tvResultText1.setText("马路杀手");
            mBinding.tvResultText1.setTextColor(getResources().getColor(R.color.color_f4011f));
            mBinding.tvResultText2.setText("不要灰心，下次继续加油啦！");
        }
    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img, v -> finish());
        mBinding.tvMyErrorResult.setOnClickListener(v ->{
            Bundle bundle = new Bundle();
            bundle.putInt("type",type);
            startActivity(bundle,MyErrorSubjectActivity.class);
            finish();
        });
        mBinding.tvToSubject.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("questionType", 1);// 1 模拟考试 2 章节练习
            bundle.putInt("type", type);
            startActivity(bundle, SubjectActivity.class);
            finish();
        });
    }
}
