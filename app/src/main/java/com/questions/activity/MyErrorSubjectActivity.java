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
import com.questions.databinding.ActivityMyErrorBinding;
import com.questions.db.QuestionsMetaData;
import com.questions.db.QuestionsSqlBrite;
import com.questions.fragments.JudgeFragment;
import com.questions.fragments.MultiselectFragment;
import com.questions.fragments.RadioFragment;
import com.questions.activity.base.BaseActivity;
import com.questions.activity.base.BaseFragment;
import com.questions.utils.FirstClickUtils;
import com.questions.utils.MyLog;
import com.questions.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11470 on 2017/10/25.
 */
public class MyErrorSubjectActivity extends BaseActivity<ActivityMyErrorBinding> implements
        RadioFragment.RadioSelectorSubject, JudgeFragment.JudgeSelectorSubject, MultiselectFragment.MultSelectorSubject {

    private ArrayList<QuestionsBean> dataList;
    private List<BaseFragment> fragmentList;
    private QuestionsSqlBrite sqlBrite;
    private MyFragmentPagerAdapter adapter;
    private int type;// 1 科目1 2科目4

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<QuestionsBean> beenList = new ArrayList<>();
            Cursor cursor = null;
            if (type == 1){
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_ERROR_SUBJECT1 + " order by " + QuestionsMetaData.MetaData.TIME + " DESC ", null);
            }else if (type == 2){
                cursor = sqlBrite.rawQueryDb("select * from " + QuestionsMetaData.MetaData.TABLE_NAME_ERROR_SUBJECT4 + " order by " + QuestionsMetaData.MetaData.TIME + " DESC ", null);
            }

            if (cursor != null) {
                while (cursor.getCount() > 0 && cursor.moveToNext()) {
                    QuestionsBean bean = QuestionsBean.getErrorQuestionsBean(cursor);
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
            mBinding.ivErrorCollectionSubject.setImageResource(R.mipmap.collectionsed_img);
        } else if (msg.what == 0x124) {
            MyLog.i("设置为未收藏>>>>");
            mBinding.ivErrorCollectionSubject.setImageResource(R.mipmap.my_collections_img);
        } else if (msg.what == 0x123 && msg.getData() != null) {
            dataList = (ArrayList<QuestionsBean>) msg.getData().getSerializable("question");
            if (dataList.size() >0){
                setTitle(R.mipmap.subject_manager_img, "1/" + dataList.size());
                setViewPager(dataList);
            }else {
                setTitle("我的错题");
                mBinding.tvEmptyData.setVisibility(View.VISIBLE);
                mBinding.tvEmptyData.setText("您目前没有错题");
            }

        }
    }

    private void setViewPager(List<QuestionsBean> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            QuestionsBean bean = dataList.get(i);
            if (StringUtil.isEqual(bean.getType(), "1")) {// 1 单选 2 判断
                RadioFragment fragment = RadioFragment.newInstance(bean,bean.getMyAnswer());
                fragment.setShowErrorSubject(true);
                fragmentList.add(fragment);
            } else if (StringUtil.isEqual(bean.getType(), "2")) {
                JudgeFragment fragment = JudgeFragment.newInstance(bean,bean.getMyAnswer());
                fragment.setShowErrorSubject();
                fragmentList.add(fragment);
            } else if (StringUtil.isEqual(bean.getType(), "3")) {
                MultiselectFragment fragment = MultiselectFragment.newInstance(bean,bean.getMyAnswer());
                fragment.setShowErrorSubject(true);
                fragmentList.add(fragment);
            }
        }
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mBinding.viewpagerErrorSubject.setAdapter(adapter);
        mBinding.viewpagerErrorSubject.setNoScroll(false);
        selectIsCollections(0);
    }

    private void selectIsCollections(final int position) {
        final BaseFragment fragment = fragmentList.get(position);
        Cursor cursor = null;
        if (type == 1){
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
    protected int getLayout() {
        return R.layout.activity_my_error;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        type =  getIntent().getExtras().getInt("type");
        sqlBrite = QuestionsSqlBrite.getSqlSingleton(this);
        fragmentList = new ArrayList<>();
        new Thread(runnable).start();
    }

    @Override
    protected void initEvent() {
        setTopLeftButton(R.mipmap.back_img, v -> finish());
        setTopRightButton("", R.mipmap.delete_error_img, v -> {
            if (!(fragmentList!=null && fragmentList.size() >0)){
                return;
            }
            QuestionsBean bean = dataList.get(mBinding.viewpagerErrorSubject.getCurrentItem());
            if (type == 1){
                sqlBrite.deleteErrorSubject1(" id = ? ",bean.getId());
            }else if (type == 2){
                sqlBrite.deleteErrorSubject4(" id = ? ",bean.getId());
            }

            fragmentList.remove(mBinding.viewpagerErrorSubject.getCurrentItem());
            dataList.remove(bean);
            adapter.updateData(fragmentList);
            setTitle(R.mipmap.subject_manager_img, "1/" + dataList.size());
            mBinding.viewpagerErrorSubject.setCurrentItem(0);
            selectIsCollections(0);

        });

        mBinding.viewpagerErrorSubject.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        mBinding.lvnErrorCollectionSubject.setOnClickListener(v -> {
            if (!FirstClickUtils.isClickSoFast(300)) {
                BaseFragment fragment = fragmentList.get(mBinding.viewpagerErrorSubject.getCurrentItem());
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

    @Override
    public void onJudgeSelectSubjectSuccess(int success) {

    }

    @Override
    public void onJudgeSelectSubjectFail(int fail, String myAnswer) {

    }

    @Override
    public void onRadioSelectSubjectSuccess(int success) {

    }

    @Override
    public void onRadioSelectSubjectFail(int fail, String myAnswer) {

    }

    @Override
    public void onMultSelectSubjectSuccess(int success) {

    }

    @Override
    public void onMultSelectSubjectFail(int fail, String myAnswer) {

    }
}
