package com.questions.http;

/**
 * Created by 11470 on 2017/10/23.
 */

public class ResultEntity<T> {

    T result;//返回数据

    String error_code;

    public void setResult(T result) {
        this.result = result;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public T getData() {
        return result;
    }

    public String getErrorCode() {
        return error_code;
    }
}
