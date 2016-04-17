package com.tomoima.fetchtweet.rx;

import rx.Observable;
import rx.Observable.Transformer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tomoaki on 4/14/16.
 */
public class SchedulersTransformer {
    static final Transformer<Observable, Observable> schedulersTransformer =
            observable -> observable
                    .doOnError(CrashOnError.crashOnError())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

    @SuppressWarnings("unchecked")
    public static <T> Transformer<T, T> applySchedulers() {
        return (Transformer<T, T>) schedulersTransformer;
    }
}
