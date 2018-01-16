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
import com.questions.databinding.FragMultiselectBinding;
import com.questions.widgets.MyImageSpan;
import com.questions.activity.ShowBigImageActivity;
import com.questions.activity.base.BaseFragment;
import com.questions.utils.StringUtil;

/**
 * Created by 11470 on 2017/10/23.
 * 多选
 */
public class MultiselectFragment extends BaseFragment<FragMultiselectBinding> {

    private QuestionsBean bean;
    private boolean isSelectorSubjectA;//是否已经选择
    private boolean isSelectorSubjectB;//是否已经选择
    private boolean isSelectorSubjectC;//是否已经选择
    private boolean isSelectorSubjectD;//是否已经选择
    private boolean isCollections;//是否收藏
    private boolean isCommit;//是否答题
    private int subjectSelectStatus = 0;// 0 未做 1 正确 2错误
    private boolean isSelector;
    private MultSelectorSubject listener;
    private byte[] answer = new byte[4];
    private StringBuilder result = new StringBuilder();
    private boolean isShowErrorSubject;
    private String myAnswer;

    public void setShowErrorSubject(boolean showErrorSubject) {
        isShowErrorSubject = showErrorSubject;
    }

    public interface MultSelectorSubject {
        void onMultSelectSubjectSuccess(int success);

        void onMultSelectSubjectFail(int fail, String myAnswer);
    }

//    public MultiselectFragment(QuestionsBean bean) {
//        this.bean = bean;
//    }
//
//    public MultiselectFragment(QuestionsBean bean, String myAnswer) {
//        this.bean = bean;
//        this.myAnswer = myAnswer;
//    }

    private MultiselectFragment(){}

    public static MultiselectFragment newInstance(QuestionsBean bean,String myAnswer){
        MultiselectFragment fragment = new MultiselectFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        bundle.putString("myAnswer",myAnswer);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (MultSelectorSubject) activity;
    }

    @Override
    protected void init() {
        bean = (QuestionsBean) getArguments().getSerializable("bean");
        myAnswer = getArguments().getString("myAnswer");

        SpannableString spannable = new SpannableString("   " + bean.getQuestion());
        MyImageSpan myImageSpan = new MyImageSpan(getActivity(), R.mipmap.subject_check_img);
        spannable.setSpan(myImageSpan, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvMultSubject.setText(spannable);

        if (StringUtil.isNotEmpty(bean.getUrl())) {
            if (!bean.getUrl().contains("http://")) {
                Glide.with(getContext()).load(android.util.Base64.decode(bean.getUrl(), android.util.Base64.DEFAULT))
                        .asGif().placeholder(R.mipmap.defult_img)
                        .error(R.mipmap.defult_img).into(mBinding.ivMultSubject);
            } else {
                Glide.with(getContext()).load(bean.getUrl()).placeholder(R.mipmap.defult_img)
                        .error(R.mipmap.defult_img).into(mBinding.ivMultSubject);
            }
        } else {
            mBinding.ivMultSubject.setVisibility(View.GONE);
        }

        mBinding.tvMultItem1.setText("A: " + bean.getItem1());
        mBinding.tvMultItem2.setText("B: " + bean.getItem2());
        mBinding.tvMultItem3.setText("C: " + bean.getItem3());
        mBinding.tvMultItem4.setText("D: " + bean.getItem4());

        if (isShowErrorSubject && StringUtil.isNotEmpty(myAnswer)) {
            showErrorSubject(myAnswer);
        }
    }

    private void showErrorSubject(String myAnswer) {
        switch (myAnswer) {
            case "7":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "8":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "9":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "10":
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "11":
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "12":
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "13":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "14":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "15":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "16":
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
            case "17":
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                break;
        }

        StringBuilder builder = new StringBuilder("答案: ");
        switch (bean.getAnswer()) {
            case "7":
                builder.append("AB");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "8":
                builder.append("AC");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "9":
                builder.append("AD");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "10":
                builder.append("BC");
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "11":
                builder.append("BD");
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "12":
                builder.append("CD");
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "13":
                builder.append("ABC");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "14":
                builder.append("ABD");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "15":
                builder.append("ACD");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "16":
                builder.append("BCD");
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
            case "17":
                builder.append("ABCD");
                mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                break;
        }
        setExplain(builder);
    }


    @Override
    protected void initEvent(Bundle savedInstanceState) {
        mBinding.ivMultSubject.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("url", bean.getUrl());
            startActivity(bundle, ShowBigImageActivity.class);
        });

        if (!isShowErrorSubject) {
            mBinding.lvnMultItem1.setOnClickListener(v -> {
                if (!isCommit) {
                    if (isSelectorSubjectA) {
                        mBinding.ivMultItem1.setImageResource(R.mipmap.radio_defult_img);
                        isSelectorSubjectA = false;
                        answer[0] = 0;

                    } else {
                        mBinding.ivMultItem1.setImageResource(R.mipmap.radio_bg_img);
                        isSelectorSubjectA = true;
                        answer[0] = 1;
                    }
                    updateBtnColor();
                }
            });

            mBinding.lvnMultItem2.setOnClickListener(v -> {
                if (!isCommit) {
                    if (isSelectorSubjectB) {
                        mBinding.ivMultItem2.setImageResource(R.mipmap.radio_defult_img);
                        isSelectorSubjectB = false;
                        answer[1] = 0;
                    } else {
                        mBinding.ivMultItem2.setImageResource(R.mipmap.radio_bg_img);
                        isSelectorSubjectB = true;
                        answer[1] = 1;
                    }
                    updateBtnColor();
                }
            });

            mBinding.lvnMultItem3.setOnClickListener(v -> {
                if (!isCommit) {
                    if (isSelectorSubjectC) {
                        mBinding.ivMultItem3.setImageResource(R.mipmap.radio_defult_img);
                        isSelectorSubjectC = false;
                        answer[2] = 0;
                    } else {
                        mBinding.ivMultItem3.setImageResource(R.mipmap.radio_bg_img);
                        isSelectorSubjectC = true;
                        answer[2] = 1;
                    }
                    updateBtnColor();
                }
            });

            mBinding.lvnMultItem4.setOnClickListener(v -> {
                if (!isCommit) {
                    if (isSelectorSubjectD) {
                        mBinding.ivMultItem4.setImageResource(R.mipmap.radio_defult_img);
                        isSelectorSubjectD = false;
                        answer[3] = 0;
                    } else {
                        mBinding.ivMultItem4.setImageResource(R.mipmap.radio_bg_img);
                        isSelectorSubjectD = true;
                        answer[3] = 1;
                    }
                    updateBtnColor();
                }
            });

            mBinding.btnCommitMult.setOnClickListener(v -> {
                if (!isCommit) {
                    if (isSelector) {
                        isCommit = true;
                        for (byte anAnswer : answer) {
                            result.append(Long.toString(anAnswer & 0xff, 2));
                        }
                        if (StringUtil.isEqual(toCaseAnswer(result.toString()), bean.getAnswer())) {
                            for (int i = 0; i < answer.length; i++) {
                                byte num = answer[i];
                                if (i == 0 && num > 0) {
                                    mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                                } else if (i == 1 && num > 0) {
                                    mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                                } else if (i == 2 && num > 0) {
                                    mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                                } else if (i == 3 && num > 0) {
                                    mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                                }
                            }
                            successAdd();
                            subjectSelectStatus = 1;
                        } else {
                            subjectSelectStatus = 2;
                            for (int i = 0; i < answer.length; i++) {
                                byte num = answer[i];
                                if (i == 0 && num > 0) {
                                    mBinding.ivMultItem1.setImageResource(R.mipmap.radio_fail_img);
                                } else if (i == 1 && num > 0) {
                                    mBinding.ivMultItem2.setImageResource(R.mipmap.radio_fail_img);
                                } else if (i == 2 && num > 0) {
                                    mBinding.ivMultItem3.setImageResource(R.mipmap.radio_fail_img);
                                } else if (i == 3 && num > 0) {
                                    mBinding.ivMultItem4.setImageResource(R.mipmap.radio_fail_img);
                                }
                            }
                            byte[] answerNum = toCaseByte(bean.getAnswer());
                            for (int i = 0; i < answerNum.length; i++) {
                                byte num = answerNum[i];
                                if (i == 0 && num > 0) {
                                    mBinding.ivMultItem1.setImageResource(R.mipmap.radio_yes_img);
                                } else if (i == 1 && num > 0) {
                                    mBinding.ivMultItem2.setImageResource(R.mipmap.radio_yes_img);
                                } else if (i == 2 && num > 0) {
                                    mBinding.ivMultItem3.setImageResource(R.mipmap.radio_yes_img);
                                } else if (i == 3 && num > 0) {
                                    mBinding.ivMultItem4.setImageResource(R.mipmap.radio_yes_img);
                                }
                            }
                            FailAdd();
                            StringBuilder builder = new StringBuilder("答案:");
                            switch (bean.getAnswer()) {
                                case "7":
                                    builder.append("AB");
                                    break;
                                case "8":
                                    builder.append("AC");
                                    break;
                                case "9":
                                    builder.append("AD");
                                    break;
                                case "10":
                                    builder.append("BC");
                                    break;
                                case "11":
                                    builder.append("BD");
                                    break;
                                case "12":
                                    builder.append("CD");
                                    break;
                                case "13":
                                    builder.append("ABC");
                                    break;
                                case "14":
                                    builder.append("ABD");
                                    break;
                                case "15":
                                    builder.append("ACD");
                                    break;
                                case "16":
                                    builder.append("BCD");
                                    break;
                                case "17":
                                    builder.append("ABCD");
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
                listener.onMultSelectSubjectFail(((SubjectActivity) getActivity()).getFailNum(), toCaseAnswer(result.toString()));
            }
        } else if (getActivity() instanceof MyCollectionsActivity) {
            ((MyCollectionsActivity) getActivity()).setFailNum(((MyCollectionsActivity) getActivity()).getFailNum() + 1);
            if (listener != null) {
                listener.onMultSelectSubjectFail(((MyCollectionsActivity) getActivity()).getFailNum(), toCaseAnswer(result.toString()));
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
                listener.onMultSelectSubjectSuccess(((SubjectActivity) getActivity()).getSuccessNum());
            }
        } else if (getActivity() instanceof MyCollectionsActivity) {
            ((MyCollectionsActivity) getActivity()).setSuccessNum(((MyCollectionsActivity) getActivity()).getSuccessNum() + 1);
            if (listener != null) {
                listener.onMultSelectSubjectSuccess(((MyCollectionsActivity) getActivity()).getSuccessNum());
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
        builder.append("试题解释: ");
        builder.append(bean.getExplains());

        SpannableString span = new SpannableString(builder.toString());
        int start = builder.indexOf("试");
        int end = start + 5;
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_048ae9)), start, end
                , Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mBinding.tvMultExplain.setVisibility(View.VISIBLE);
        mBinding.tvMultExplain.setText(span);
    }

    private void updateBtnColor() {
        int isSelectorTwo = 0;//判断两个或以上
        for (byte anAnswer : answer) {
            if (anAnswer > 0) {
                isSelectorTwo++;
            }
            if (isSelectorTwo >= 2) {
                break;
            }
        }
        if (isSelectorTwo >= 2) {
            mBinding.btnCommitMult.setBackgroundResource(R.drawable.selector_r8_1c79d0);
            isSelector = true;
        } else {
            mBinding.btnCommitMult.setBackgroundResource(R.drawable.shape_r8_gray);
            isSelector = false;
        }

    }

    private String toCaseAnswer(String result) {
        if (StringUtil.isEqual(result, "1100")) {
            return "7";
        } else if (StringUtil.isEqual(result, "1010")) {
            return "8";
        } else if (StringUtil.isEqual(result, "1001")) {
            return "9";
        } else if (StringUtil.isEqual(result, "0110")) {
            return "10";
        } else if (StringUtil.isEqual(result, "0101")) {
            return "11";
        } else if (StringUtil.isEqual(result, "0011")) {
            return "12";
        } else if (StringUtil.isEqual(result, "1110")) {
            return "13";
        } else if (StringUtil.isEqual(result, "1101")) {
            return "14";
        } else if (StringUtil.isEqual(result, "1011")) {
            return "15";
        } else if (StringUtil.isEqual(result, "0111")) {
            return "16";
        } else if (StringUtil.isEqual(result, "1111")) {
            return "17";
        }
        return "";
    }

    private byte[] toCaseByte(String answer) {
        if (StringUtil.isEqual(answer, "7")) {
            return new byte[]{1, 1, 0, 0};
        }
        if (StringUtil.isEqual(answer, "8")) {
            return new byte[]{1, 0, 1, 0};
        }
        if (StringUtil.isEqual(answer, "9")) {
            return new byte[]{1, 0, 0, 1};
        }
        if (StringUtil.isEqual(answer, "10")) {
            return new byte[]{0, 1, 1, 0};
        }
        if (StringUtil.isEqual(answer, "11")) {
            return new byte[]{0, 1, 0, 1};
        }
        if (StringUtil.isEqual(answer, "12")) {
            return new byte[]{0, 0, 1, 1};
        }
        if (StringUtil.isEqual(answer, "13")) {
            return new byte[]{1, 1, 1, 0};
        }
        if (StringUtil.isEqual(answer, "14")) {
            return new byte[]{1, 1, 0, 1};
        }
        if (StringUtil.isEqual(answer, "15")) {
            return new byte[]{1, 0, 1, 1};
        }
        if (StringUtil.isEqual(answer, "16")) {
            return new byte[]{0, 1, 1, 1};
        }
        if (StringUtil.isEqual(answer, "17")) {
            return new byte[]{1, 1, 1, 1};
        }
        return null;
    }

    public int getSubjectSelectStatus() {
        return subjectSelectStatus;
    }

    public void setCommit(boolean commit) {
        isCommit = commit;
    }

    public boolean isCommit() {
        return isCommit;
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
        return R.layout.frag_multiselect;
    }
}
