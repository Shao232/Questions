package com.questions.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.bumptech.glide.Glide;
import com.questions.R;
import com.questions.activity.MyCollectionsActivity;
import com.questions.activity.ShowBigImageActivity;
import com.questions.activity.SubjectActivity;
import com.questions.activity.base.BaseFragment;
import com.questions.bean.QuestionsBean;
import com.questions.databinding.FragJudgeBinding;
import com.questions.utils.StringUtil;
import com.questions.widgets.MyImageSpan;

/**
 * Created by 11470 on 2017/10/20.
 * 判断题
 */
public class JudgeFragment extends BaseFragment<FragJudgeBinding> {

    private QuestionsBean bean;
    private boolean isSelectorSubject;//是否已经答过题
    private boolean isCollections;//是否收藏
    public JudgeSelectorSubject listener;
    private int subjectSelectStatus = 0;// 0 未做 1 正确 2错误
    private boolean isShowErrorSubject;
    private String myAnswer;

    public void setShowErrorSubject() {
        isShowErrorSubject = true;
    }

    public interface JudgeSelectorSubject {
        void onJudgeSelectSubjectSuccess(int success);

        void onJudgeSelectSubjectFail(int fail, String myAnswer);
    }

//    public JudgeFragment(QuestionsBean bean) {
//        this.bean = bean;
//    }
//
//    public JudgeFragment(QuestionsBean bean, String myAnswer) {
//        this.bean = bean;
//        this.myAnswer = myAnswer;
//    }

    private JudgeFragment(){}

    public static JudgeFragment newInstance(QuestionsBean bean,String myAnswer){
        JudgeFragment fragment = new JudgeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        bundle.putString("myAnswer",myAnswer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (JudgeSelectorSubject) activity;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void init() {
        bean = (QuestionsBean) getArguments().getSerializable("bean");
        myAnswer = getArguments().getString("myAnswer");

        SpannableString spannable = new SpannableString("   " + bean.getQuestion());
        MyImageSpan myImageSpan = new MyImageSpan(getActivity(), R.mipmap.judge_img);
        spannable.setSpan(myImageSpan, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvJudgeSubject.setText(spannable);

        if (StringUtil.isNotEmpty(bean.getUrl())) {
            if (!bean.getUrl().contains("http://")) {
                Glide.with(getContext()).load(android.util.Base64.decode(bean.getUrl(), android.util.Base64.DEFAULT))
                        .asGif().placeholder(R.mipmap.defult_img)
                        .error(R.mipmap.defult_img).into(mBinding.ivJudgeSubject);
            } else {
                Glide.with(getContext()).load(bean.getUrl()).placeholder(R.mipmap.defult_img)
                        .error(R.mipmap.defult_img).into(mBinding.ivJudgeSubject);
            }
        } else {
            mBinding.ivJudgeSubject.setVisibility(View.GONE);
        }


        mBinding.tvJudgeItem1.setText("A: " + bean.getItem1());
        mBinding.tvJudgeItem2.setText("B: " + bean.getItem2());

        if (isShowErrorSubject && StringUtil.isNotEmpty(myAnswer)) {
            showErrorSubject(myAnswer);
        }
    }

    private void showErrorSubject(String myAnswer) {
        switch (myAnswer) {
            case "正确":
                mBinding.ivJudgeItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivJudgeItem2.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "错误":
                mBinding.ivJudgeItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivJudgeItem1.setImageResource(R.mipmap.radio_yes_img);
                break;
        }
        StringBuilder builder = new StringBuilder("答案: ");
        switch (bean.getAnswer()) {
            case "1":
                builder.append("错误");
                break;
            case "2":
                builder.append("正确");
                break;
        }
        setExplain(builder);
    }


    @Override
    protected void initEvent(Bundle savedInstanceState) {

        mBinding.ivJudgeSubject.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", bean.getUrl());
            startActivity(bundle, ShowBigImageActivity.class);
        });

        if (!isShowErrorSubject) {
            mBinding.lvnJudgeItem1.setOnClickListener(v -> {
                if (!isSelectorSubject) {
                    if (isSelectorOk(caseString(bean.getItem1()))) {
                        mBinding.ivJudgeItem1.setImageResource(R.mipmap.radio_yes_img);
                        successAdd();
                        subjectSelectStatus = 1;
                    } else {
                        subjectSelectStatus = 2;
                        mBinding.ivJudgeItem1.setImageResource(R.mipmap.radio_fail_img);
                        mBinding.ivJudgeItem2.setImageResource(R.mipmap.radio_yes_img);
                        FailAdd();
                        StringBuilder builder = new StringBuilder("答案: 错误");
                        if (getActivity() instanceof SubjectActivity) {
                            if (((SubjectActivity) getActivity()).getQuestionType() == 2) {
                                setExplain(builder);
                            }
                        } else if (getActivity() instanceof MyCollectionsActivity) {
                            setExplain(builder);
                        }
                    }
                }
            });

            mBinding.lvnJudgeItem2.setOnClickListener(v -> {
                if (!isSelectorSubject) {
                    if (isSelectorOk(caseString(bean.getItem1()))) {
                        mBinding.ivJudgeItem2.setImageResource(R.mipmap.radio_yes_img);
                        successAdd();
                        subjectSelectStatus = 1;
                    } else {
                        subjectSelectStatus = 2;
                        mBinding.ivJudgeItem2.setImageResource(R.mipmap.radio_fail_img);
                        mBinding.ivJudgeItem1.setImageResource(R.mipmap.radio_yes_img);
                        FailAdd();
                        StringBuilder builder = new StringBuilder("答案: 正确");
                        if (getActivity() instanceof SubjectActivity) {
                            if (((SubjectActivity) getActivity()).getQuestionType() == 2) {
                                setExplain(builder);
                            }
                        } else if (getActivity() instanceof MyCollectionsActivity) {
                            setExplain(builder);
                        }
                    }
                }
            });
        }
    }

    private void FailAdd() {
        if (getActivity() instanceof SubjectActivity) {
            ((SubjectActivity) getActivity()).setFailNum(((SubjectActivity) getActivity()).getFailNum() + 1);
            if (((SubjectActivity) getActivity()).getQuestionType() == 1) {
                toQuestions();
            }
            if (listener != null) {
                listener.onJudgeSelectSubjectFail(((SubjectActivity) getActivity()).getFailNum(), "错误");
            }
        } else if (getActivity() instanceof MyCollectionsActivity) {
            ((MyCollectionsActivity) getActivity()).setFailNum(((MyCollectionsActivity) getActivity()).getFailNum() + 1);
            if (listener != null) {
                listener.onJudgeSelectSubjectFail(((MyCollectionsActivity) getActivity()).getFailNum(), "错误");
            }
        }
    }

    private void successAdd() {
        if (getActivity() instanceof SubjectActivity) {
            ((SubjectActivity) getActivity()).setSuccessNum(((SubjectActivity) getActivity()).getSuccessNum() + 1);
            if (((SubjectActivity) getActivity()).getQuestionType() == 1) {
                toQuestions();
            }
            if (listener != null) {
                listener.onJudgeSelectSubjectSuccess(((SubjectActivity) getActivity()).getSuccessNum());
            }
        } else if (getActivity() instanceof MyCollectionsActivity) {
            ((MyCollectionsActivity) getActivity()).setSuccessNum(((MyCollectionsActivity) getActivity()).getSuccessNum() + 1);
            if (listener != null) {
                listener.onJudgeSelectSubjectSuccess(((MyCollectionsActivity) getActivity()).getSuccessNum());
            }
        }
    }

    //当选择正确的时候，自动进入下一题
    private void toQuestions() {
        handler.postDelayed(() -> {
            SubjectActivity activity = (SubjectActivity) getActivity();
            activity.getViewPager().setCurrentItem(activity.getViewPager().getCurrentItem() + 1);
        }, 200);
    }

    private void setExplain(StringBuilder builder) {
        builder.append("\n\n");
        builder.append("试题解释:  ");
        builder.append(bean.getExplains());
        SpannableString span = new SpannableString(builder.toString());
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_048ae9)), 7, 12,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvJudgeExplain.setVisibility(View.VISIBLE);
        mBinding.tvJudgeExplain.setText(span);
    }

    private boolean isSelectorOk(String selectorItem) {
        boolean isSelectorOk = false;
        if (!isSelectorSubject) {
            isSelectorSubject = true;
            if (StringUtil.isEqual(bean.getAnswer(), selectorItem)) {
                isSelectorOk = true;
            } else {
                isSelectorOk = false;
            }
        }
        return isSelectorOk;
    }

    private String caseString(String caseStr) {
        if (StringUtil.isEqual(caseStr, "正确")) {
            return "1";
        } else if (StringUtil.isEqual(caseStr, "错误")) {
            return "2";
        }
        return "";
    }

    public int getSubjectSelectStatus() {
        return subjectSelectStatus;
    }

    public QuestionsBean getBean() {
        return bean;
    }

    public boolean isCollections() {
        return isCollections;
    }

    public void setCollections(boolean collections) {
        isCollections = collections;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_judge;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
