package com.questions.widgets;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.questions.R;
import com.questions.utils.StringUtil;

/**
 * TODO: document your custom view class.
 */
public class ResultDialog extends Dialog {

    private TextView tvContent;//内容
    private TextView tvContinueSubject;//继续答题
    private TextView tvToResult;//提交
    private ResultClickListener listener;
    private LeftClickListener leftClickListener;

    public interface ResultClickListener{
        void onResultClick();
    }

    public interface LeftClickListener{
        void onLeftClick();
    }

    public void setLeftClickListener(LeftClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    public void setListener(ResultClickListener listener) {
        this.listener = listener;
    }

    public ResultDialog(@NonNull Context context) {
        this(context,0);
    }


    public ResultDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
        initEvent();
    }

    private void initView(Context context) {
        View view =  LayoutInflater.from(context).inflate(R.layout.view_result_dialog,null);
        tvContent = view.findViewById(R.id.tv_content_subject);
        tvContinueSubject = view.findViewById(R.id.tv_continue_subject);
        tvToResult = view.findViewById(R.id.tv_to_result_subject);
        setContentView(view);
    }

    private void initEvent() {
        tvToResult.setOnClickListener(v -> {
            if (listener!=null){
                listener.onResultClick();
            }
            dismiss();
        });

        tvContinueSubject.setOnClickListener(v ->{
            if (leftClickListener!=null){
                leftClickListener.onLeftClick();
            }
            dismiss();
        });
    }

    public void setContent(String content){
        if (tvContent!=null && StringUtil.isNotEmpty(content)){
            tvContent.setText("您还有"+content+"道题未答题,是否提前交卷");
        }
    }

    public void setLeftContent(String content){
        if (tvContinueSubject!=null && StringUtil.isNotEmpty(content)){
            tvContinueSubject.setText(content);
        }
    }

}
