package com.questions.fragments;

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
import com.questions.activity.SubjectActivity;
import com.questions.bean.QuestionsBean;
import com.questions.databinding.FragRadioBinding;
import com.questions.widgets.MyImageSpan;
import com.questions.activity.ShowBigImageActivity;
import com.questions.activity.base.BaseFragment;
import com.questions.utils.StringUtil;

/**
 * Created by 11470 on 2017/10/19.
 * 单选题
 */
public class RadioFragment extends BaseFragment<FragRadioBinding> {

    private QuestionsBean bean;
    private boolean isSelectorSubject;//是否已经答过题
    private int subjectSelectStatus = 0;// 0 未做 1 正确 2错误
    private boolean isCollections;//是否收藏
    private boolean isShowErrorSubject;//是否是查看错题
    private String myAnswer;
    private RadioSelectorSubject listener;

    public interface RadioSelectorSubject {
        void onRadioSelectSubjectSuccess(int success);

        void onRadioSelectSubjectFail(int fail, String myAnswer);
    }

//    public RadioFragment(QuestionsBean bean) {
//        this.bean = bean;
//    }
//
//    public RadioFragment(QuestionsBean bean, String myAnswer) {
//        this.bean = bean;
//        this.myAnswer = myAnswer;
//    }

    private RadioFragment(){}

    public static RadioFragment newInstance(QuestionsBean bean,String myAnswer){
        RadioFragment fragment = new RadioFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        bundle.putString("myAnswer",myAnswer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (RadioSelectorSubject) activity;
    }

    @Override
    protected void init() {
        bean = (QuestionsBean) getArguments().getSerializable("bean");
        myAnswer = getArguments().getString("myAnswer");

        SpannableString spannable = new SpannableString("   " + bean.getQuestion());
        MyImageSpan myImageSpan = new MyImageSpan(getActivity(), R.mipmap.radio_img);
        spannable.setSpan(myImageSpan, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvRadioSubject.setText(spannable);

        if (StringUtil.isNotEmpty(bean.getUrl())) {
            if (!bean.getUrl().contains("http://")) {
                Glide.with(getContext()).load(android.util.Base64.decode(bean.getUrl(), android.util.Base64.DEFAULT))
                        .asGif().placeholder(R.mipmap.defult_img)
                        .error(R.mipmap.defult_img).into(mBinding.ivRadioSubject);
            } else {
                Glide.with(getContext()).load(bean.getUrl()).placeholder(R.mipmap.defult_img)
                        .error(R.mipmap.defult_img).into(mBinding.ivRadioSubject);
            }
        } else {
            mBinding.ivRadioSubject.setVisibility(View.GONE);
        }

        mBinding.tvRadioItem1.setText("A: " + bean.getItem1());
        mBinding.tvRadioItem2.setText("B: " + bean.getItem2());
        mBinding.tvRadioItem3.setText("C: " + bean.getItem3());
        mBinding.tvRadioItem4.setText("D: " + bean.getItem4());

        if (isShowErrorSubject && StringUtil.isNotEmpty(myAnswer)) {
            showErrorSubject(myAnswer);
        }
    }

    private void showErrorSubject(String myAnswer) {
        switch (myAnswer) {
            case "A":
                mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "B":
                mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "C":
                mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "D":
                mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
        }
        StringBuilder builder = new StringBuilder("答案: ");
        switch (bean.getAnswer()) {
            case "1":
                mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_yes_img);
                builder.append("A");
                break;
            case "2":
                mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_yes_img);
                builder.append("B");
                break;
            case "3":
                mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_yes_img);
                builder.append("C");
                break;
            case "4":
                mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_yes_img);
                builder.append("D");
                break;
        }
        setExplain(builder);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        mBinding.ivRadioSubject.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", bean.getUrl());
            startActivity(bundle, ShowBigImageActivity.class);
        });

        if (!isShowErrorSubject) {//如果是查看错题就不能点击
            mBinding.lvnRadioItem1.setOnClickListener(v -> {
                if (!isSelectorSubject) {
                    if (isSelectorOk("1")) {
                        mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_yes_img);
                        successAdd();
                        subjectSelectStatus = 1;
                    } else {
                        subjectSelectStatus = 2;
                        FailAdd();
                        mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_fail_img);
                        StringBuilder builder = new StringBuilder("答案: ");
                        switch (bean.getAnswer()) {
                            case "2":
                                mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_yes_img);
                                builder.append("B");
                                break;
                            case "3":
                                mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_yes_img);
                                builder.append("C");
                                break;
                            case "4":
                                mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_yes_img);
                                builder.append("D");
                                break;
                        }
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

            mBinding.lvnRadioItem2.setOnClickListener(v -> {
                if (!isSelectorSubject) {
                    if (isSelectorOk("2")) {
                        mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_yes_img);
                        successAdd();
                        subjectSelectStatus = 1;
                    } else {
                        subjectSelectStatus = 2;
                        FailAdd();
                        mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_fail_img);
                        StringBuilder builder = new StringBuilder("答案:");
                        switch (bean.getAnswer()) {
                            case "1":
                                builder.append("A");
                                mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_yes_img);
                                break;
                            case "3":
                                builder.append("C");
                                mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_yes_img);
                                break;
                            case "4":
                                builder.append("D");
                                mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_yes_img);
                                break;
                        }
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

            mBinding.lvnRadioItem3.setOnClickListener(v -> {
                if (!isSelectorSubject) {
                    if (isSelectorOk("3")) {
                        mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_yes_img);
                        successAdd();
                        subjectSelectStatus = 1;
                    } else {
                        subjectSelectStatus = 2;
                        FailAdd();
                        mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_fail_img);
                        StringBuilder builder = new StringBuilder("答案:");
                        switch (bean.getAnswer()) {
                            case "1":
                                builder.append("A");
                                mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_yes_img);
                                break;
                            case "2":
                                builder.append("B");
                                mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_yes_img);
                                break;
                            case "4":
                                builder.append("D");
                                mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_yes_img);
                                break;
                        }
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

            mBinding.lvnRadioItem4.setOnClickListener(v -> {
                if (!isSelectorSubject) {
                    if (isSelectorOk("4")) {
                        mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_yes_img);
                        successAdd();
                        subjectSelectStatus = 1;
                    } else {
                        subjectSelectStatus = 2;
                        FailAdd();
                        mBinding.ivRadioItem4.setImageResource(R.mipmap.radio_fail_img);
                        StringBuilder builder = new StringBuilder("答案:");
                        switch (bean.getAnswer()) {
                            case "1":
                                builder.append("A");
                                mBinding.ivRadioItem1.setImageResource(R.mipmap.radio_yes_img);
                                break;
                            case "2":
                                builder.append("B");
                                mBinding.ivRadioItem2.setImageResource(R.mipmap.radio_yes_img);
                                break;
                            case "3":
                                builder.append("C");
                                mBinding.ivRadioItem3.setImageResource(R.mipmap.radio_yes_img);
                                break;
                        }
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
                listener.onRadioSelectSubjectFail(((SubjectActivity) getActivity()).getFailNum(), "A");
            }
        } else if (getActivity() instanceof MyCollectionsActivity) {
            ((MyCollectionsActivity) getActivity()).setFailNum(((MyCollectionsActivity) getActivity()).getFailNum() + 1);
            if (listener != null) {
                listener.onRadioSelectSubjectFail(((MyCollectionsActivity) getActivity()).getFailNum(), "A");
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
                listener.onRadioSelectSubjectSuccess(((SubjectActivity) getActivity()).getSuccessNum());
            }
        } else if (getActivity() instanceof MyCollectionsActivity) {
            ((MyCollectionsActivity) getActivity()).setSuccessNum(((MyCollectionsActivity) getActivity()).getSuccessNum() + 1);
            if (listener != null) {
                listener.onRadioSelectSubjectSuccess(((MyCollectionsActivity) getActivity()).getSuccessNum());
            }
        }
    }

    //当选择正确的时候，自动进入下一题
    private void toQuestions() {
        handler.postDelayed(() -> {
            SubjectActivity activity = (SubjectActivity) getActivity();
            activity.getViewPager().setCurrentItem(activity.getViewPager().getCurrentItem() + 1);
        }, 300);
    }

    private void setExplain(StringBuilder builder) {
        builder.append("\n\n");
        builder.append("试题解释:  ");
        builder.append(bean.getExplains());
        SpannableString span = new SpannableString(builder.toString());
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_048ae9)), 5, 12,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvRadioExplain.setVisibility(View.VISIBLE);
        mBinding.tvRadioExplain.setText(span);
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

    public int getSubjectSelectStatus() {
        return subjectSelectStatus;
    }

    public QuestionsBean getBean() {
        return bean;
    }

    public void setShowErrorSubject(boolean showErrorSubject) {
        isShowErrorSubject = showErrorSubject;
    }

    public boolean isCollections() {
        return isCollections;
    }

    public void setCollections(boolean collections) {
        isCollections = collections;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_radio;
    }

}
