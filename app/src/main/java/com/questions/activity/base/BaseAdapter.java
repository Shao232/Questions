package com.questions.activity.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11470 on 2017/7/4.
 */

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    protected List<T> datas;
    protected Context mContext;

    protected OnItemClickListener<T> onItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClickListener(T bean,int position);
    }

    public BaseAdapter(Context context) {
        this.mContext = context;
        datas = new ArrayList<>();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(mContext).inflate(getLayout(), parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindData(holder, getItem(position), position);
    }

    public T getItem(int position) {
        return datas.get(position) == null ? null : datas.get(position);
    }

    @Override
    public int getItemCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    public void add(T data) {
        this.datas.add(data);
        notifyDataSetChanged();
    }

    public void set(int index, T data) {
        this.datas.set(index, data);
        notifyItemChanged(index);
    }

    public void addAll(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        datas.remove(data);
        notifyDataSetChanged();
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<T> getDatas() {
        return datas;
    }

    protected abstract int getLayout();

    protected abstract VH getViewHolder(View view);

    protected abstract void onBindData(VH holder, T bean, int position);



}
