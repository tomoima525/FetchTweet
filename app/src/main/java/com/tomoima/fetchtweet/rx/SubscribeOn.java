package com.tomoima.fetchtweet.rx;

import rx.Scheduler;

/**
 * Created by tomoaki on 4/10/16.
 */
public interface SubscribeOn {
    Scheduler getScheduler();
}
