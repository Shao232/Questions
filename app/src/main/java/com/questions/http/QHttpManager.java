package com.questions.http;


import com.questions.QApplication;
import com.questions.bean.QuestionsBean;
import com.slibrary.retrofit_rx.HttpManager;
import com.slibrary.retrofit_rx.RxUtil;
import com.slibrary.utils.MyLog;
import com.slibrary.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 自定义网络请求管理类
 * <p>
 * 使用示例：
 * 1.先获取JYZHttpManager对象  QHttpManager httpManager = QHttpManager.getInstance();
 * 2.根据需要请求的接口调用对应的接口  比如调用登录接口：
 * httpManager.login(phoneNum, pwd, KBaseApplication.deviceId, new Consumer<UserBean>(){
 *
 * @Override public Disposable apply(@NonNull UserBean userBean) throws Exception{
 * ....
 * }
 * });
 * <p>
 * <p>
 * Created by QiQi on 2017/4/26.
 */

public class QHttpManager {

    private static class SingletonClass {

        private static QHttpManager instance = new QHttpManager();
    }

    private HttpManager<HttpApiManager> manager;

    private QHttpManager() {
        manager = new HttpManager.Builder()
                .setBaseUrl("http://v.juhe.cn/")
                .setApiService(HttpApiManager.class)
                .build();

    }

    public static QHttpManager getInstance() {
        return SingletonClass.instance;
    }

    public HttpApiManager getService() {
        return manager.getHttpService();
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Function<ResultEntity<T>, T> {
        @Override
        public T apply(@NonNull ResultEntity<T> httpResult) throws Exception {
            return httpResult.getData();
        }
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     */
    private class HttpResultSucFunc implements Function<ResultEntity, Boolean> {
        @Override
        public Boolean apply(@NonNull ResultEntity httpResult) throws Exception {
            MyLog.e(httpResult.getErrorCode());
           return false;
        }
    }


    public static class HttpResultErrorFunc implements Consumer<Throwable> {

        private final OnThrowableListener throwableListener;

        public HttpResultErrorFunc(OnThrowableListener throwableListener) {
            this.throwableListener = throwableListener;
        }

        @Override
        public void accept(@NonNull Throwable throwable) throws Exception {
//            MyLog.getInstance().dismissDialog();
            if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            ToastUtil.showToast(QApplication.getInstance(),"网络连接出错,请检查网络");
            } else if (throwable instanceof TimeoutException || throwable instanceof SocketTimeoutException) {
                ToastUtil.showToast(QApplication.getInstance(),"网络请求超时, 请稍候重试");
            } else {
                MyLog.i(throwable.getMessage());
//                ToastUtil.showToast(throwable.getMessage());
            }

            if (throwableListener != null) {
                throwableListener.accept(throwable);
            }
        }
    }

    public interface OnThrowableListener {
        void accept(Throwable throwable);
    }

//    /**
//     * 获取MD5编码后的密码
//     *
//     * @return
//     */
//    public String getMD5Pwd(String pwd) {
//        return MD5Util.getMD5Code(pwd + "JIAOYIZHAO").toUpperCase();
//    }

//    /**
//     * 通过手机密码进行登录
//     * <p>
//     * 参数定义请看
//     * {@see JYZAPIHelper#loginByPWD}
//     *
//     * @param phone
//     * @param pwd
//     * @param deviceToken
//     * @param subscriber
//     */
//    public Disposable login(String phone, String pwd, String deviceToken, Consumer<UserBean> subscriber, OnThrowableListener listener) {
//        return getService().loginByPWD(phone, getMD5Pwd(pwd), deviceToken)
//                .map(new HttpResultFunc<UserBean>())
//                .compose(RxUtil.<UserBean>applySchedulersIO())
//                .subscribe(subscriber, new HttpResultErrorFunc(listener));
//    }

    //==================================================================================================================================================

    public Disposable getData(int subject, String model, String testType, Consumer<List<QuestionsBean>> subscriber, OnThrowableListener listener){
        return getService().getData(subject,model,testType,"a02fa02e48e3e1c3fe1a9dfc793652e1")
                .map(new HttpResultFunc<List<QuestionsBean>>())
                .compose(RxUtil.applySchedulersIO())
                .subscribe(subscriber, new HttpResultErrorFunc(listener));
    }

}