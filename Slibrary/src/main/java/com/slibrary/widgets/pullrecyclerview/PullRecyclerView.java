package com.slibrary.widgets.pullrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slibrary.R;


/**
 * Created by 11470 on 2017/9/27.
 */

public class PullRecyclerView extends RelativeLayout{
    PullToRefreshLayout pullView;
    LinearLayout DynamicLayout;
    PullNestedScrollView scrollView;

    public PullRecyclerView(Context context) {
        this(context,null);

    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initEvent();
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.view_pull_recyclerview,this,true);
        pullView = (PullToRefreshLayout) view.findViewById(R.id.pullView);
        DynamicLayout = (LinearLayout) view.findViewById(R.id.DynamicLayout);
        scrollView = (PullNestedScrollView) view.findViewById(R.id.NestedScrollView);
    }

    private void initEvent(){

    }

    public void addChildView(View childView){
        if (DynamicLayout!=null){
            DynamicLayout.addView(childView);
        }
    }

    public void setOnRefreshListener(PullToRefreshLayout.OnRefreshListener onRefreshListener) {
        if (pullView!=null && onRefreshListener!=null){
            pullView.setOnRefreshListener(onRefreshListener);
        }
    }

    public void setAutoRefresh(){
        if (pullView!=null){
            pullView.autoRefresh();
        }
    }

}
