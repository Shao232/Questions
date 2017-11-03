package com.questions.widgets;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.questions.R;
import com.questions.adapter.SelectSubjectAdapter;
import com.questions.bean.SelectSubjectBean;
import com.questions.utils.MyLog;
import com.questions.utils.MyUtils;

import java.util.List;

/**
 * Created by 11470 on 2017/10/26.
 * 题目选择器
 */
public class SelectSubjectPopupWindow extends PopupWindow {

    ImageView ivSelectTouch;
    View view;
    TextView tvOkSubject;
    TextView tvErrorSubject;
    RecyclerView recyclerView;

    private Context context;
    private SelectSubjectAdapter adapter;

    private SelectSubjectOnClick selectSubjectOnClick;

    public interface SelectSubjectOnClick{
        void onSelectSubjectClick(int position);
    }

    public void setSelectSubjectOnClick(SelectSubjectOnClick selectSubjectOnClick) {
        this.selectSubjectOnClick = selectSubjectOnClick;
    }

    public SelectSubjectPopupWindow(Context context) {
        this(context,null);
    }

    public SelectSubjectPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SelectSubjectPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context){
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.view_popup_select_subject,null);
        ivSelectTouch = view.findViewById(R.id.iv_touch_select_subject);
        tvOkSubject = view.findViewById(R.id.tv_subject_ok_select);
        tvErrorSubject = view.findViewById(R.id.tv_subject_no_select);
        recyclerView = view.findViewById(R.id.recycler_select_subject);
        adapter = new SelectSubjectAdapter(context);
        initEvent((Activity) context);
    }

    public void setSelectSubjectData(Context context,String okSubject, String noSubject, List<SelectSubjectBean> dataList){
        tvOkSubject.setText(okSubject);
        tvErrorSubject.setText(noSubject);
        if (adapter.getDatas().size() >0){
            adapter.clear();
        }
        adapter.addAll(dataList);
        recyclerView.setLayoutManager(new GridLayoutManager(context,6));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((bean, position) -> {
            MyLog.i("Select","Window点击的题目>>>>"+bean.getSubject()+","+position);
            if (selectSubjectOnClick!=null){
                selectSubjectOnClick.onSelectSubjectClick(position);
            }
            dismiss();
        });
    }

    public void showAtLocation(){
        setContentView(view);
        setAnimationStyle(R.style.popupStyle);
        setHeight(MyUtils.getInstance().dp2px(context,400));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);//这里必须设置为true才能点击区域外或者消失
        setBackgroundAlpha((Activity) context,0.5f);
        showAtLocation(view, Gravity.BOTTOM,0,0);
    }

    private void initEvent(final Activity context){
        ivSelectTouch.setOnClickListener(v -> dismiss());
        setOnDismissListener(() -> setBackgroundAlpha(context,1f));
    }

    /**
     * 设置页面的透明度
     * @param bgAlpha 1表示不透明
     */
    private void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

}
