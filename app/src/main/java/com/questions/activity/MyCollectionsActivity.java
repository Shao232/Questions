package com.questions.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.questions.R;
import com.questions.adapter.MyFragmentPagerAdapter;
import com.questions.bean.QuestionsBean;
import com.questions.bean.SelectSubjectBean;
import com.questions.databinding.ActivityMyCollectionsBinding;
import com.questions.db.QuestionsMetaData;
import com.questions.db.QuestionsSqlBrite;
import com.questions.fragments.JudgeFragment;
import com.questions.fragments.MultiselectFragment;
import com.questions.fragments.RadioFragment;
import com.questions.widgets.SelectSubjectPopupWindow;
import com.questions.activity.base.BaseActivity;
import com.questions.activity.base.BaseFragment;
import com.questions.utils.FirstClickUtils;
import com.questions.utils.MyLog;
import com.questions.utils.MyUtils;
import com.questions.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionsActivity extends BaseActivity<ActivityMyCollectionsBinding> implements
        RadioFragment.RadioSelectorSubject, JudgeFragment.JudgeSelectorSubject, MultiselectFragment.MultSelectorSubject {

    private ArrayList<QuestionsBean> dataList;
    private int successNum;//对对题数
    private int failNum;//错题数
    private List<BaseFragment> fragmentList;
    private QuestionsSqlBrite sqlBrite;
    private SelectSubjectPopupWindow window;
    private MyFragmentPagerAdapter adapter;
    private int type;// 1 科目1 2科目4

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<QuestionsBean> beenList = new ArrayList<>();
            Cursor cursor = null;
            if (type == 1){
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT1, null);
            }else if (type == 2){
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_COLLECTIONS_SUBJECT4, null);
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
        if (msg.what == 0x123 && msg.getData() != null) {
            dataList = (ArrayList<QuestionsBean>) msg.getData().getSerializable("question");
            if (dataList.size() >0) {
                setTitle(R.mipmap.subject_manager_img, "1/" + dataList.size(), R.mipmap.button_select_subject_img);
                setViewPager(dataList);
            }else {
                setTitle("我的收藏");
                mBinding.tvEmptyData.setVisibility(View.VISIBLE);
                mBinding.tvEmptyData.setText("您目前没有收藏的题目");
            }
        } else if (msg.what == 0x124) {
            MyLog.i("设置为未收藏>>>>");
            mBinding.ivCollectionSubject.setImageResource(R.mipmap.my_collections_img);
        }  if (msg.what == 0x125) {
            MyLog.i("设置为已收藏>>>>");
            mBinding.ivCollectionSubject.setImageResource(R.mipmap.collectionsed_img);
        }
    }

    private void setViewPager(List<QuestionsBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            QuestionsBean bean = dataList.get(i);
            if (StringUtil.isEqual(bean.getType(), "1")) {// 1 单选 2 判断
                RadioFragment fragment = RadioFragment.newInstance(bean,bean.getMyAnswer());
                fragment.setCollections(true);
                fragmentList.add(fragment);
            } else if (StringUtil.isEqual(bean.getType(), "2")) {
                JudgeFragment fragment = JudgeFragment.newInstance(bean,bean.getMyAnswer());
                fragment.setCollections(true);
                fragmentList.add(fragment);
            } else if (StringUtil.isEqual(bean.getType(), "3")) {
                MultiselectFragment fragment = MultiselectFragment.newInstance(bean,bean.getMyAnswer());
                fragment.setCollections(true);
                fragmentList.add(fragment);
            }
        }
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mBinding.viewpagerCollectionsSubject.setAdapter(adapter);
        mBinding.viewpagerCollectionsSubject.setNoScroll(false);
        mBinding.ivCollectionSubject.setImageResource(R.mipmap.collectionsed_img);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_collections;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        type =  getIntent().getExtras().getInt("type");
        sqlBrite = QuestionsSqlBrite.getSqlSingleton(this);
        fragmentList = new ArrayList<>();
        window = new SelectSubjectPopupWindow(MyCollectionsActivity.this);
        mBinding.tvYesSubjectCollections.setText("" + successNum);
        mBinding.tvNoSubjectCollections.setText("" + failNum);
        new Thread(runnable).start();

    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img, v -> finish());
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

                window.setSelectSubjectData(MyCollectionsActivity.this,
                        "" + successNum, "" + failNum, subjectBeanList);
                window.setSelectSubjectOnClick(position -> {
                    MyLog.i("Select", "Activity点击的题目>>>>" + position);
                    mBinding.viewpagerCollectionsSubject.setCurrentItem(position);
                });
            }
        });
        mBinding.viewpagerCollectionsSubject.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyLog.i(">>>进入下一题:" + position);
                int newPostion = position + 1;
                selectIsCollections(position);
                setTitle(R.mipmap.subject_manager_img, newPostion + "/" + dataList.size()
                        , R.mipmap.button_select_subject_img);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.lvnCollectionSubject.setOnClickListener(v -> {
            if (!FirstClickUtils.isClickSoFast(300)) {
                BaseFragment fragment = fragmentList.get(mBinding.viewpagerCollectionsSubject.getCurrentItem());
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

    private void saveCollections(BaseFragment fragment, boolean isCollections, QuestionsBean bean) {
        if (isCollections) {
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
    protected void onStop() {
        super.onStop();
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
    public void onJudgeSelectSubjectSuccess(int success) {
        mBinding.tvYesSubjectCollections.setText("" + success);

    }

    @Override
    public void onJudgeSelectSubjectFail(int fail, String myAnswer) {
        mBinding.tvNoSubjectCollections.setText("" + fail);
        insertError(dataList.get(mBinding.viewpagerCollectionsSubject.getCurrentItem()), myAnswer);
    }

    @Override
    public void onRadioSelectSubjectSuccess(int success) {
        mBinding.tvYesSubjectCollections.setText("" + success);

    }

    @Override
    public void onRadioSelectSubjectFail(int fail, String myAnswer) {
        mBinding.tvNoSubjectCollections.setText("" + fail);
        insertError(dataList.get(mBinding.viewpagerCollectionsSubject.getCurrentItem()), myAnswer);
    }

    @Override
    public void onMultSelectSubjectSuccess(int success) {
        mBinding.tvYesSubjectCollections.setText("" + success);
    }

    @Override
    public void onMultSelectSubjectFail(int fail, String myAnswer) {
        mBinding.tvNoSubjectCollections.setText("" + fail);
        insertError(dataList.get(mBinding.viewpagerCollectionsSubject.getCurrentItem()), myAnswer);
    }
}
