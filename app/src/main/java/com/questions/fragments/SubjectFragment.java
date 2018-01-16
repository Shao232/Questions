package com.questions.fragments;

import android.os.Bundle;

import com.questions.R;
import com.questions.activity.ExplainActivity;
import com.questions.activity.MyCollectionsActivity;
import com.questions.activity.MyErrorSubjectActivity;
import com.questions.activity.StartExamActivity;
import com.questions.activity.SubjectActivity;
import com.questions.activity.base.BaseFragment;
import com.questions.adapter.BannerAdapter;
import com.questions.convenientbanner.ConvenientBanner;
import com.questions.databinding.FragSubjectOneBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11470 on 2017/10/18.
 */
public class SubjectFragment extends BaseFragment<FragSubjectOneBinding>{

    private int type;// 1 科目1  2 科目4

//    private SubjectFragment (int type){
//        this.type = type;
//    }

    private SubjectFragment() {
    }

    public static SubjectFragment newInstance(int type){
        SubjectFragment fragment = new SubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void init() {
        type = getArguments().getInt("type");
        List<String> imgData = new ArrayList<>();
        imgData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508243782120&di=9f458e33e61fd82f935adea402d312cc&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201112%2F15%2F20111215195115_x5HZs.thumb.700_0.jpg");
        imgData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508243782120&di=1be6f288df32ebb7fbd28405291d7e4b&imgtype=0&src=http%3A%2F%2Fm.qqzhi.com%2Fupload%2Fimg_2_1622372370D2198311274_23.jpg");
        imgData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508243782118&di=b4a265dfb48f1bcfc91c7041960e5e0a&imgtype=0&src=http%3A%2F%2Fi2.17173.itc.cn%2F2013%2Fflash%2F2013%2Flinshi%2Fheji%2Fhzw.jpg");

        mBinding.banner.setPages(BannerAdapter::new,imgData)
                .setPointViewVisible(true)
                .startTurning(3000)
                .setPageIndicator(new int[]{R.mipmap.circle_white_img,R.mipmap.circle_gray_img})
                //设置指示器的方向（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setOnItemClickListener(position -> showToast("点击的位置>>>"+position))
                //设置手动影响（设置了该项无法手动切换）
                .setManualPageable(true);

    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        //模拟考试
        mBinding.lvnExam.setOnClickListener(v -> {//模拟考试
            Bundle bundle1 = new Bundle();
            switch (type){
                case 1:
                    bundle1.putInt("type",1);
                    break;
                case 2:
                    bundle1.putInt("type",2);
                    break;
                default:
                    break;
            }
            startActivity(bundle1, StartExamActivity.class);
        });

        //章节练习
        mBinding.lvnPractice.setOnClickListener(v -> {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("questionType",2);// 1 模拟考试 2 章节练习
            bundle2.putInt("type",type);
            startActivity(bundle2, SubjectActivity.class);
        });

        mBinding.lvnCollection.setOnClickListener(v -> {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("type",type);
            startActivity(bundle2,MyCollectionsActivity.class);
        });

        mBinding.lvnError.setOnClickListener(v -> {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("type",type);
            startActivity(bundle2,MyErrorSubjectActivity.class);
        });

        mBinding.lvnExplain.setOnClickListener(v -> startActivity(ExplainActivity.class));
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_subject_one;
    }

}
