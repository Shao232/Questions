package com.questions.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.adapter.MyFragmentPagerAdapter;
import com.questions.bean.QuestionsBean;
import com.questions.bean.SelectSubjectBean;
import com.questions.databinding.ActivitySubjectBinding;
import com.questions.db.QuestionsMetaData;
import com.questions.db.QuestionsSqlBrite;
import com.questions.fragments.JudgeFragment;
import com.questions.fragments.MultiselectFragment;
import com.questions.fragments.RadioFragment;
import com.questions.widgets.ResultDialog;
import com.questions.widgets.SelectSubjectPopupWindow;
import com.questions.activity.base.BaseFragment;
import com.questions.utils.FileUtils;
import com.questions.utils.FirstClickUtils;
import com.questions.utils.MyLog;
import com.questions.utils.MyUtils;
import com.questions.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 答题
 */
public class SubjectActivity extends MyBaseActivity<ActivitySubjectBinding> implements
        RadioFragment.RadioSelectorSubject, JudgeFragment.JudgeSelectorSubject, MultiselectFragment.MultSelectorSubject {

    private ArrayList<QuestionsBean> dataList;
    private int questionType;// 1 模拟考试 2 章节练习
    private int successNum;//对对题数
    private int failNum;//错题数
    private int noWriteNum;//未做题目
    private final int subject1Count = 100;//总题数
    private final int subject4Count = 50;//总题数
    private List<BaseFragment> fragmentList;
    private QuestionsSqlBrite sqlBrite;
    private int type;// 1 科目1 2 科目4
    private SelectSubjectPopupWindow window;

    private final long questionTime = -26100000;// 1970 45分钟

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            ArrayList<QuestionsBean> beenList = new ArrayList<>();
            SQLiteDatabase database = FileUtils.openDataBase(getApplicationContext(), "questions.db", "questions.db");
            Cursor cursor = null;
            if (type == 1) {
                if (database != null) {
                    cursor = database.query("Subject1", null, null, null, null, null, null, null);
                }
            } else if (type == 2) {
                if (database != null) {
                    cursor = database.query("Subject4", null, null, null, null, null, null, null);
                }
            }
            if (cursor != null) {
                while (cursor.getCount() > 0 && cursor.moveToNext()) {
                    QuestionsBean bean = QuestionsBean.getQuestionsBean(cursor);
                    beenList.add(bean);
                }
                cursor.close();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putSerializable("question", beenList);
            message.setData(bundle);
            message.what = 0x123;
            handler.sendMessage(message);
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            ArrayList<QuestionsBean> beenList = new ArrayList<>();
            SQLiteDatabase database = FileUtils.openDataBase(getApplicationContext(), "questions.db", "questions.db");
            Cursor cursor = null;
            if (type == 1) {
                if (database != null) {
                    cursor = database.query(QuestionsMetaData.MetaData.TABLE_NAME_SUBJECT1, null, null, null, null, null, " random() ", "100");
                }
            } else if (type == 2) {
                if (database != null) {
                    cursor = database.query(QuestionsMetaData.MetaData.TABLE_NAME_SUBJECT4, null, null, null, null, null, " random() ", "50");
                }
            }
            if (cursor != null) {
                while (cursor.getCount() > 0 && cursor.moveToNext()) {
                    QuestionsBean bean = QuestionsBean.getQuestionsBean(cursor);
                    beenList.add(bean);
                }
                cursor.close();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putSerializable("question", beenList);
            message.setData(bundle);
            message.what = 0x123;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void HandlerMessage(Context mContext, Message msg) {
        super.HandlerMessage(mContext, msg);
        if (msg.what == 0x125) {
            MyLog.i("设置为已收藏>>>>");
            mBinding.ivCollectionSubject.setImageResource(R.mipmap.collectionsed_img);
        } else if (msg.what == 0x124) {
            MyLog.i("设置为未收藏>>>>");
            mBinding.ivCollectionSubject.setImageResource(R.mipmap.my_collections_img);
        } else if (msg.what == 0x123 && msg.getData() != null) {
            dataList = (ArrayList<QuestionsBean>) msg.getData().getSerializable("question");
            if (questionType == 1) {//模拟练习
                setViewPager(dataList);
            } else if (questionType == 2) {//章节练习
                setTitle(R.mipmap.subject_manager_img, "1/" + dataList.size(), R.mipmap.button_select_subject_img);
                setViewPager(dataList);
            }
        }
    }

    private void setViewPager(List<QuestionsBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            QuestionsBean bean = dataList.get(i);
            if (StringUtil.isEqual(bean.getType(), "1")) {// 1 单选 2 判断
                RadioFragment fragment = new RadioFragment(bean);
                fragmentList.add(fragment);
            } else if (StringUtil.isEqual(bean.getType(), "2")) {
                JudgeFragment fragment = new JudgeFragment(bean);
                fragmentList.add(fragment);
            } else if (StringUtil.isEqual(bean.getType(), "3")) {
                MultiselectFragment fragment = new MultiselectFragment(bean);
                fragmentList.add(fragment);
            }
        }
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mBinding.viewpagerSubject.setAdapter(adapter);
        selectIsCollections(0);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_subject;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        questionType = getIntent().getExtras().getInt("questionType");
        type = getIntent().getExtras().getInt("type");

        sqlBrite = QuestionsSqlBrite.getSqlSingleton(this);
        fragmentList = new ArrayList<>();
        window = new SelectSubjectPopupWindow(this);

        mBinding.tvYesSubject.setText("" + successNum);
        mBinding.tvNoSubject.setText("" + failNum);

        if (questionType == 1) {
            new Thread(runnable2).start();
            if (type == 1) {
                noWriteNum = subject1Count;
                setTitle(R.mipmap.subject_manager_img, "1/" + subject1Count, R.mipmap.button_select_subject_img);
            } else if (type == 2) {
                noWriteNum = subject4Count;
                setTitle(R.mipmap.subject_manager_img, "1/" + subject4Count, R.mipmap.button_select_subject_img);
            }

            mBinding.lvnTimeSubject.setVisibility(View.VISIBLE);
            mBinding.chronometer.start();
        } else {
            MyLog.i("顺序练习>>>>线程开始");
            new Thread(runnable1).start();
            mBinding.lvnTimeSubject.setVisibility(View.GONE);
            mBinding.viewpagerSubject.setNoScroll(false);
        }
    }

    private void selectIsCollections(final int position) {
        final BaseFragment fragment = fragmentList.get(position);
        Cursor cursor = null;
        if (type == 1){
            if (fragment instanceof RadioFragment) {
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT1
                        + " where " + QuestionsMetaData.MetaData.ID + " = ?", new String[]{((RadioFragment) fragment).getBean().getId()});
            } else if (fragment instanceof JudgeFragment) {
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT1
                        + " where " + QuestionsMetaData.MetaData.ID + " = ?", new String[]{((JudgeFragment) fragment).getBean().getId()});
            } else if (fragment instanceof MultiselectFragment) {
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT1
                        + " where " + QuestionsMetaData.MetaData.ID + " = ?", new String[]{((MultiselectFragment) fragment).getBean().getId()});
            }
        }else if (type == 2){
            if (fragment instanceof RadioFragment) {
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT4
                        + " where " + QuestionsMetaData.MetaData.ID + " = ?", new String[]{((RadioFragment) fragment).getBean().getId()});
            } else if (fragment instanceof JudgeFragment) {
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT4
                        + " where " + QuestionsMetaData.MetaData.ID + " = ?", new String[]{((JudgeFragment) fragment).getBean().getId()});
            } else if (fragment instanceof MultiselectFragment) {
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT4
                        + " where " + QuestionsMetaData.MetaData.ID + " = ?", new String[]{((MultiselectFragment) fragment).getBean().getId()});
            }
        }
        MyLog.i("查询到的数据大小Cursor>>>>>>>>>>>" + cursor.getCount());
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                QuestionsBean bean = QuestionsBean.getQuestionsBean(cursor);
                if (fragment instanceof RadioFragment) {
                    if (bean != null && StringUtil.isEqual(bean.getId(), ((RadioFragment) fragment).getBean().getId())) {
                        ((RadioFragment) fragment).setCollections(true);
                        handler.sendEmptyMessage(0x125);
                    }
                } else if (fragment instanceof JudgeFragment) {
                    if (bean != null && StringUtil.isEqual(bean.getId(), ((JudgeFragment) fragment).getBean().getId())) {
                        ((JudgeFragment) fragment).setCollections(true);
                        handler.sendEmptyMessage(0x125);
                    }
                } else if (fragment instanceof MultiselectFragment) {
                    if (bean != null && StringUtil.isEqual(bean.getId(), ((MultiselectFragment) fragment).getBean().getId())) {
                        ((MultiselectFragment) fragment).setCollections(true);
                        handler.sendEmptyMessage(0x125);
                    }
                }
            }
        } else {
            if (fragment instanceof RadioFragment) {//单选
                MyLog.i("查询后没有收藏>>>>");
                ((RadioFragment) fragment).setCollections(false);
                handler.sendEmptyMessage(0x124);
            } else if (fragment instanceof JudgeFragment) {//判断
                MyLog.i("查询后没有收藏>>>>");
                ((JudgeFragment) fragment).setCollections(false);
                handler.sendEmptyMessage(0x124);
            } else if (fragment instanceof MultiselectFragment) {//多选
                MyLog.i("查询后没有收藏>>>>");
                ((MultiselectFragment) fragment).setCollections(false);
                handler.sendEmptyMessage(0x124);
            }
        }

        cursor.close();
    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img, v -> finish());
        if (questionType == 1) {
            setTopRightButton("", R.mipmap.to_result_img, v -> {
                int count = 0;
                if (type == 1) {
                    count = subject1Count - (successNum + failNum);
                } else {
                    count = subject4Count - (successNum + failNum);
                }

                ResultDialog dialog = new ResultDialog(SubjectActivity.this, R.style.myDialogStyle);
                dialog.setContent(String.valueOf(count));
                dialog.show();
                dialog.setListener(this::toResult);
            });
        }
        setTopTitleClick(v -> {
            if (!FirstClickUtils.isClickSoFast(350)) {
                if (window.isShowing()) {
                    return;
                }
                window.showAtLocation();
                List<SelectSubjectBean> subjectBeanList = new ArrayList<>();
                for (int i = 0; i < fragmentList.size(); i++) {
                    int currentSubject = i + 1;
                    BaseFragment fragment = fragmentList.get(i);
                    SelectSubjectBean bean = new SelectSubjectBean();
                    if (fragment instanceof RadioFragment) {
                        bean.setSelectStatus(((RadioFragment) fragment).getSubjectSelectStatus());
                        bean.setSubject("" + currentSubject);
                    } else if (fragment instanceof JudgeFragment) {
                        bean.setSelectStatus(((JudgeFragment) fragment).getSubjectSelectStatus());
                        bean.setSubject("" + currentSubject);
                    } else if (fragment instanceof MultiselectFragment) {
                        bean.setSelectStatus(((MultiselectFragment) fragment).getSubjectSelectStatus());
                        bean.setSubject("" + currentSubject);
                    }
                    subjectBeanList.add(bean);
                }

                window.setSelectSubjectData(SubjectActivity.this,
                        "" + successNum, "" + failNum, subjectBeanList);
                window.setSelectSubjectOnClick(position -> mBinding.viewpagerSubject.setCurrentItem(position));
            }
        });
        mBinding.viewpagerSubject.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyLog.i(">>>进入下一题:" + position);
                selectIsCollections(position);
                int newPostion = position + 1;
                if (questionType == 1) {
                    setTitle(R.mipmap.subject_manager_img, newPostion + "/100", R.mipmap.button_select_subject_img);
                } else {
                    setTitle(R.mipmap.subject_manager_img, "顺序练习 " + newPostion + "/" + dataList.size()
                            , R.mipmap.button_select_subject_img);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mBinding.chronometer.setOnChronometerTickListener(chronometer -> {
            long time1 = MyUtils.getInstance().string2Date("mm:ss", chronometer.getText().toString());
            if (time1 >= questionTime) {
                chronometer.stop();
                MyLog.i("时间到了");
                toResult();
            }
        });

        mBinding.lvnCollectionSubject.setOnClickListener(v -> {
            if (!FirstClickUtils.isClickSoFast(300)) {
                BaseFragment fragment = fragmentList.get(mBinding.viewpagerSubject.getCurrentItem());
                boolean isCollections;
                if (fragment instanceof RadioFragment) {//单选
                    isCollections = ((RadioFragment) fragment).isCollections();
                    saveCollections(fragment, isCollections, ((RadioFragment) fragment).getBean());
                } else if (fragment instanceof JudgeFragment) {//判断
                    isCollections = ((JudgeFragment) fragment).isCollections();
                    saveCollections(fragment, isCollections, ((JudgeFragment) fragment).getBean());
                } else if (fragment instanceof MultiselectFragment) {//多选
                    isCollections = ((MultiselectFragment) fragment).isCollections();
                    saveCollections(fragment, isCollections, ((MultiselectFragment) fragment).getBean());
                }
            }
        });

    }

    private void saveCollections(BaseFragment fragment, boolean isCollections, QuestionsBean bean) {
        if (!isCollections) {
            if (type == 1){
                sqlBrite.insertCollectionsSubject1(QuestionsBean.getContentValues(bean));
            }else if (type == 2){
                sqlBrite.insertCollectionsSubject4(QuestionsBean.getContentValues(bean));
            }

            if (fragment instanceof RadioFragment) {//单选
                ((RadioFragment) fragment).setCollections(true);
                handler.sendEmptyMessage(0x125);
            } else if (fragment instanceof JudgeFragment) {//判断
                ((JudgeFragment) fragment).setCollections(true);
                handler.sendEmptyMessage(0x125);
            } else if (fragment instanceof MultiselectFragment) {//多选
                ((MultiselectFragment) fragment).setCollections(true);
                handler.sendEmptyMessage(0x125);
            }
        } else {
            if (type == 1){
                sqlBrite.deleteCollectionsSubject1(" id = ? ", bean.getId());
            }else if (type == 2){
                sqlBrite.deleteCollectionsSubject4(" id = ? ", bean.getId());
            }

            if (fragment instanceof RadioFragment) {//单选
                ((RadioFragment) fragment).setCollections(false);
                handler.sendEmptyMessage(0x124);
            } else if (fragment instanceof JudgeFragment) {//判断
                ((JudgeFragment) fragment).setCollections(false);
                handler.sendEmptyMessage(0x124);
            } else if (fragment instanceof MultiselectFragment) {//多选
                ((MultiselectFragment) fragment).setCollections(false);
                handler.sendEmptyMessage(0x124);
            }
        }
    }

    private void insertError(QuestionsBean bean, String myAnswer) {
        MyLog.i("插入错题>>>>" + bean.getExplains());
        String timeDate = MyUtils.getInstance().date2String("yyyy/MM/dd HH:mm:ss", System.currentTimeMillis());
        if (type == 1){
            sqlBrite.insertErrorSubject1(QuestionsBean.getContentValues(bean, timeDate, myAnswer));
        }else if (type == 2){
            sqlBrite.insertErrorSubject4(QuestionsBean.getContentValues(bean, timeDate, myAnswer));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBinding.chronometer.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        long time1 = MyUtils.getInstance().string2Date("mm:ss", mBinding.chronometer.getText().toString());
        if (time1 >= questionTime) {
            mBinding.chronometer.stop();
            MyLog.i("时间到了");
            toResult();
        } else {
            mBinding.chronometer.start();
        }
    }

    //是否完成答题
    private void isSelectSubjectOk() {
        if (questionType == 1) {
            noWriteNum --;
            String time = "";
            int source = 0;
            boolean isOk = false;
            if (type == 1) {
                if (subject1Count == (successNum + failNum)) {
                    mBinding.chronometer.stop();
                    time = mBinding.chronometer.getText().toString();
                    source = subject1Count - failNum;
                    isOk = true;
                }
            } else if (type == 2) {
                if (subject4Count == (successNum + failNum)) {
                    mBinding.chronometer.stop();
                    time = mBinding.chronometer.getText().toString();
                    source = subject4Count - (failNum * 2);
                    isOk = true;
                }
            }
            if (isOk && StringUtil.isNotEmpty(time)) {
                Bundle bundle = new Bundle();
                bundle.putInt("subjectType", type);
                bundle.putString("time", time);
                bundle.putInt("source", source);
                startActivity(bundle, ResultActivity.class);
                finish();
            }
        }
    }

    private void toResult() {
        String time = "";
        int source = 0;
        time = mBinding.chronometer.getText().toString();
        if (type == 1) {
            source = subject1Count - (failNum + noWriteNum);
        } else if (type == 2) {
            source = subject4Count - ((failNum * 2)+ noWriteNum);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("subjectType", type);
        bundle.putString("time", time);
        bundle.putInt("source", source);
        startActivity(bundle, ResultActivity.class);
        finish();
    }

    public ViewPager getViewPager() {
        if (mBinding.viewpagerSubject != null) {
            return mBinding.viewpagerSubject;
        }
        return null;
    }

    public int getQuestionType() {
        return questionType;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    @Override
    public void onRadioSelectSubjectSuccess(int success) {
        mBinding.tvYesSubject.setText("" + success);
        isSelectSubjectOk();
    }

    @Override
    public void onRadioSelectSubjectFail(int fail, String myAnswer) {
        mBinding.tvNoSubject.setText("" + fail);
        insertError(dataList.get(mBinding.viewpagerSubject.getCurrentItem()), myAnswer);
        isSelectSubjectOk();
    }

    @Override
    public void onJudgeSelectSubjectSuccess(int success) {
        mBinding.tvYesSubject.setText("" + success);
        isSelectSubjectOk();
    }

    @Override
    public void onJudgeSelectSubjectFail(int fail, String myAnswer) {
        mBinding.tvNoSubject.setText("" + fail);
        insertError(dataList.get(mBinding.viewpagerSubject.getCurrentItem()), myAnswer);
        isSelectSubjectOk();
    }

    @Override
    public void onMultSelectSubjectSuccess(int success) {
        mBinding.tvYesSubject.setText("" + success);
        isSelectSubjectOk();
    }

    @Override
    public void onMultSelectSubjectFail(int fail, String myAnswer) {
        mBinding.tvNoSubject.setText("" + fail);
        insertError(dataList.get(mBinding.viewpagerSubject.getCurrentItem()), myAnswer);
        isSelectSubjectOk();
    }

}
