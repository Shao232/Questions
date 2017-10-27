package com.slibrary.retrofit_rx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.slibrary.utils.JsonObjectUtil;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class RxUtil {

    private final static ObservableTransformer schedulersTransformerIO = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable observable) {
            return observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private final static ObservableTransformer schedulersTransformerComputation = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable observable) {
            return observable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
        }
    };

    private final static ObservableTransformer<JsonArray, JsonElement> tTransformer = new ObservableTransformer<JsonArray, JsonElement>() {
        @Override
        public ObservableSource<JsonElement> apply(@NonNull Observable<JsonArray> observable) {
            return observable.filter(new Predicate<JsonArray>() {
                @Override
                public boolean test(@NonNull JsonArray array) throws Exception {
                    return JsonObjectUtil.isNotEmpty(array);
                }
            })
                    .flatMap(new Function<JsonArray, ObservableSource<JsonElement>>() {
                        @Override
                        public ObservableSource<JsonElement> apply(@NonNull JsonArray items) throws Exception {
                            return Observable.fromIterable(items);
                        }
                    });
        }
    };


    /**
     * 统一线程处理
     *
     * @return
     */
    private static FlowableTransformer rxSchedulerHelper = new FlowableTransformer() {
        @Override
        public Flowable apply(Flowable observable) {
            return observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static FlowableTransformer getRxSchedulerHelper() {
        return rxSchedulerHelper;
    }


    public static ObservableTransformer applySchedulersIO() {
        return schedulersTransformerIO;
    }

    public static ObservableTransformer applySchedulersComputation() {
        return schedulersTransformerComputation;
    }

    public static ObservableTransformer<JsonArray, JsonElement> applyJsonArrayToJsonElement() {
        return tTransformer;
    }

    public static <T> Single applyJsonArrayToClass(JsonArray array, final Class<T> clss) {
        return Observable.just(array)
                .compose(applyJsonArrayToJsonElement())
                .map(new Function<JsonElement, Object>() {
                    @Override
                    public Object apply(@NonNull JsonElement jsonElement) throws Exception {
                        return JsonObjectUtil.toClass(jsonElement, clss);
                    }
                })
                .compose(applySchedulersComputation())
                .toList();
    }


}