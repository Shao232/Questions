package com.questions.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.questions.R;
import com.questions.bean.SelectSubjectBean;
import com.questions.activity.base.BaseAdapter;
import com.questions.utils.MyLog;

/**
 * Created by 11470 on 2017/10/26.
 */

public class SelectSubjectAdapter extends BaseAdapter<SelectSubjectAdapter.SelectSubjectViewHolder,SelectSubjectBean> {


    public SelectSubjectAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_select_subject;
    }

    @Override
    protected SelectSubjectViewHolder getViewHolder(View view) {
        return new SelectSubjectViewHolder(view);
    }

    @Override
    protected void onBindData(SelectSubjectViewHolder holder, SelectSubjectBean bean, int position) {
        holder.tvSubject.setText(bean.getSubject());
        switch (bean.getSelectStatus()){
            case 1:
                holder.tvSubject.setTextColor(mContext.getResources().getColor(R.color.color_048ae9));
                holder.tvSubject.setBackgroundResource(R.drawable.ok_select_img);
                break;
            case 2:
                holder.tvSubject.setTextColor(mContext.getResources().getColor(R.color.color_f4011f));
                holder.tvSubject.setBackgroundResource(R.drawable.error_select_img);
                break;
            case 0:
                holder.tvSubject.setTextColor(mContext.getResources().getColor(R.color.color_9d9d9d));
                holder.tvSubject.setBackgroundResource(R.drawable.default_select_img);
                break;
        }
        holder.listener.setData(bean,position);
    }

     class SelectSubjectViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubject;
        MyClickListener listener;

        SelectSubjectViewHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tv_subject_item);
            listener = new MyClickListener();
            itemView.setOnClickListener(listener);
        }
    }

    private class MyClickListener implements View.OnClickListener{

        private SelectSubjectBean data;
        private int position;

        private void setData(SelectSubjectBean data,int position){
            this.data = data;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener!=null){
                MyLog.i("Select","BaseAdapter点击的题目>>>>"+position);
                onItemClickListener.onItemClickListener(data,position);
            }
        }
    }

}
