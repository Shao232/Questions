package com.slibrary.retrofit_rx;

import android.util.Log;

import com.slibrary.retrofit_rx.Api.BaseApi;
import com.slibrary.retrofit_rx.exception.RetryWhenNetworkException;
import com.slibrary.retrofit_rx.subscribers.ProgressSubscriber;
import com.slibrary.utils.DebugUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * http交互处理类
 * 在具体的项目中，继承实现此类
 * gradle 差异化构建此类，此类在debug模式使用
 * <p>
 * <p>
 * <p>
 * Created by WZG on 2016/7/16.
 */
public class HttpManager<T> {
    private T httpService;

    //构造方法私有
    private HttpManager(String baseUrl, Class<T> cls) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (DebugUtils.isDEBUG()){
            System.out.printf("-------debug----------");
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    Log.i("RetrofitLog", "retrofitBack = " + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        builder.connectTimeout(5000, TimeUnit.MILLISECONDS);
        /*缓存位置和大小*/
//        builder.cache(new Cache(MyApplication.getInstance().getCacheDir(), 10 * 1024 * 1024));

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        httpService = retrofit.create(cls);
    }

    public static class Builder<T> {
        private Class<T> cls;
        private String baseUrl;

        public Builder setApiService(Class<T> cls) {
            this.cls = cls;
            return this;
        }

        public Builder setBaseUrl(String url) {
            this.baseUrl = url;
            return this;
        }

        public HttpManager build() {
            return new HttpManager<>(baseUrl, cls);
        }
    }


    public T getHttpService() {
        return httpService;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseApi basePar) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        /*创建retrofit对象*/

        ProgressSubscriber subscriber = new ProgressSubscriber(basePar);

        /*rx处理*/
        Observable observable = basePar.getObservable(httpService)
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*生命周期管理*/
                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);

        /*数据回调*/
        observable.subscribe(subscriber);
    }
}