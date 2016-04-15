package com.tomoima.fetchtweet.rx;

import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;

/**
 * Created by tomoaki on 4/11/16.
 */
public class CrashOnError {
    public static Action1<Throwable> crashOnError() {
        final Throwable checkpoint = new Throwable();
        return throwable -> {
            StackTraceElement[] stackTrace = checkpoint.getStackTrace();
            StackTraceElement element = stackTrace[1]; // First element after `crashOnError()`
            String msg = String.format("¥¥ onError() crash from subscribe() in %s.%s(%s:%s)",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber());
            throw new OnErrorNotImplementedException(msg, throwable);
        };
    }
}
