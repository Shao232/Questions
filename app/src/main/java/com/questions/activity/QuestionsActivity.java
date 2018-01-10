package com.questions.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;

import com.questions.R;
import com.questions.activity.base.MyBaseActivity;
import com.questions.adapter.MyFragmentPagerAdapter;
import com.questions.databinding.ActivityQuestionsBinding;
import com.questions.fragments.SubjectFragment;
import com.questions.activity.base.BaseFragment;
import com.questions.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class QuestionsActivity extends MyBaseActivity<ActivityQuestionsBinding> {

    private String[] tabTitle= {"科目一","科目四"};

    @Override
    protected int getLayout() {
        return R.layout.activity_questions;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        setTitle("驾照考题");
        setTopLeftButton(R.mipmap.back_img, v -> finish());
        List<BaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new SubjectFragment(1));
        fragmentList.add(new SubjectFragment(2));
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter
                (getSupportFragmentManager(),fragmentList);
        mBinding.viewpager.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来。
        mBinding.tab.addTab(mBinding.tab.newTab().setText(tabTitle[0]));
        mBinding.tab.addTab(mBinding.tab.newTab().setText(tabTitle[1]));
        mBinding.tab.setupWithViewPager(mBinding.viewpager);
        //设置可以滑动
        mBinding.tab.setTabMode(MODE_SCROLLABLE);
        mBinding.tab.getTabAt(0).setText(tabTitle[0]);
        mBinding.tab.getTabAt(1).setText(tabTitle[1]);
    }

    @Override
    protected void initEvent() {
        mBinding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (StringUtil.isEqual(tab.getText(),tabTitle[0])){
                    mBinding.viewpager.setCurrentItem(0,false);
                }else {
                    mBinding.viewpager.setCurrentItem(1,false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
