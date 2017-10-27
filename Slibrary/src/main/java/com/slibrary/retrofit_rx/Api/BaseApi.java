package com.slibrary.retrofit_rx.Api;

import com.slibrary.retrofit_rx.exception.HttpTimeException;
import com.slibrary.retrofit_rx.listener.HttpOnNextListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;

import io.reactivex.Observable;


/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseApi<T> implements io.reactivex.functions.Function<BaseResultEntity<T>, T> {
    //rx生命周期管理
    private SoftReference<RxAppCompatActivity> rxAppCompatActivity;
    /*回调*/
    private SoftReference<HttpOnNextListener> listener;
    /*是否能取消加载框*/
    private boolean cancel;
    /*是否显示加载框*/
    private boolean showProgress;
    /*是否需要缓存处理*/
    private boolean cache;
    /*基础url*/
    private String baseUrl = "http://www.izaodao.com/Api/";
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String mothed;
    /*超时时间-默认6秒*/
    private int connectionTime = 6;
    /*有网情况下的本地缓存时间默认60秒*/
    private int cookieNetWorkTime = 60;
    /*无网络的情况下本地缓存时间默认30天*/
    private int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    /*加载框提示文字*/
    private String msg;

    public BaseApi(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        setListener(listener);
        setRxAppCompatActivity(rxAppCompatActivity);
        setShowProgress(true);
        setCache(true);
    }

    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public abstract <S> Observable getObservable(S methods);

    public void setMessage(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public void setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public void setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
    }

    public String getMothed() {
        return mothed;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
    }

    public void setMothed(String mothed) {
        this.mothed = mothed;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return baseUrl + mothed;
    }

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = new SoftReference(rxAppCompatActivity);
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public SoftReference<HttpOnNextListener> getListener() {
        return listener;
    }

    public void setListener(HttpOnNextListener listener) {
        this.listener = new SoftReference(listener);
    }

    /*
     * 获取当前rx生命周期
     * @return
     */
    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity.get();
    }


    @Override
    public T apply(BaseResultEntity<T> httpResult) throws Exception {
        if (httpResult.getCode() == 0) {
            throw new HttpTimeException(httpResult.getMsg());
        }
        return httpResult.getData();
    }

}
