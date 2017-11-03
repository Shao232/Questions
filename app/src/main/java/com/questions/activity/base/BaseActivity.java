package com.questions.activity.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.questions.R;
import com.questions.utils.MyUtils;
import com.questions.utils.ToastUtil;

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {

    protected V mBinding;
    protected BaseHandler handler;
    protected Toolbar toolbar;
    protected TextView tvTitle;
    private View.OnClickListener onClickListenerTopLeft;
    private View.OnClickListener onClickListenerTopRight;
    protected int menuResId;
    protected String menuStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int conViewRes = getLayout();
        if (conViewRes != 0) {
            mBinding = DataBindingUtil.setContentView(this, conViewRes);
        }
        handler = new MyHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.title);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        initData(savedInstanceState);
        initEvent();
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    public void setTitle(int drawLeft,String title){
        if (drawLeft!=0){
            Drawable leftDrawable = getResources().getDrawable(drawLeft);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(leftDrawable,null,null,null);
            tvTitle.setCompoundDrawablePadding(MyUtils.getInstance().dp2px(getApplicationContext(),8));
        }
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    public void setTitle(int drawLeft,String title,int drawRight){
        if (drawLeft!=0 || drawRight!=0){
            Drawable leftDrawable = getResources().getDrawable(drawLeft);
            Drawable rightDrawable = getResources().getDrawable(drawRight);
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(leftDrawable,null,rightDrawable,null);
            tvTitle.setCompoundDrawablePadding(MyUtils.getInstance().dp2px(getApplicationContext(),8));
        }
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }


    /**
     * 设置toolbar左边图标和点击事件
     *
     * @param iconResId            图标资源
     * @param clickListenerTopLeft 点击回调
     */
    protected void setTopLeftButton(int iconResId, View.OnClickListener clickListenerTopLeft) {
        toolbar.setNavigationIcon(iconResId);
        this.onClickListenerTopLeft = clickListenerTopLeft;
    }

    /**
     * 设置toolbar右边图标和点击事件
     *
     * @param menuStr                 按钮名称
     * @param onClickListenerTopRight 点击回调
     */
    protected void setTopRightButton(String menuStr, View.OnClickListener onClickListenerTopRight) {
        this.menuStr = menuStr;
        this.onClickListenerTopRight = onClickListenerTopRight;
    }

    /**
     * 设置toolbar右边图标和点击事件
     *
     * @param menuStr                 按钮名称
     * @param menuResId               图标资源
     * @param onClickListenerTopRight 点击回调
     */
    protected void setTopRightButton(String menuStr, int menuResId, View.OnClickListener onClickListenerTopRight) {
        this.menuStr = menuStr;
        this.menuResId = menuResId;
        this.onClickListenerTopRight = onClickListenerTopRight;
    }

    protected void setTopTitleClick(View.OnClickListener onClickListenerTopTitle){
        if (tvTitle!=null) {
            tvTitle.setOnClickListener(onClickListenerTopTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar, menu);
            MenuItem item =  menu.findItem(R.id.menu_1).setTitle(menuStr);
            SpannableString spannableString = new SpannableString((item.getTitle()));
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
            item.setTitle(spannableString);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuResId != 0) {
            menu.findItem(R.id.menu_1).setIcon(menuResId);
        }

        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_1).setTitle(menuStr);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (onClickListenerTopLeft != null) {
                onClickListenerTopLeft.onClick(toolbar);
            }
        } else if (item.getItemId() == R.id.menu_1) {
            if (onClickListenerTopRight != null) {
                onClickListenerTopRight.onClick(toolbar);
            }
        }
        return true;
    }

    /**
     * 拉起系统电话
     */
    protected void startPhone(String uri) {
        if (uri == null || "".equals(uri))
            return;
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected abstract int getLayout();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected void HandlerMessage(Context mContext,Message msg){}

    protected void showToast(String msg){
        ToastUtil.showToast(getApplicationContext(),msg);
    }

    protected void startActivity(Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    protected void startActivity(Bundle bundle,Class<?> clazz){
        Intent intent = new Intent(this,clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private static class MyHandler extends BaseHandler{

        MyHandler(Context mContext) {
            super(mContext);
        }

        @Override
        protected void handleMessage(Context mContext, Message msg) {
            BaseActivity activity = (BaseActivity) mContext;
            if (activity!=null){
                activity.HandlerMessage(mContext,msg);
            }
        }
    }

}
