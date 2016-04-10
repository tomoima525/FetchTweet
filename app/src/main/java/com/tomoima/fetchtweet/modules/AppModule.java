package com.tomoima.fetchtweet.modules;

import android.app.Application;
import android.content.Context;

import com.tomoima.fetchtweet.rx.ObserveOn;
import com.tomoima.fetchtweet.rx.SubscribeOn;
import com.tomoima.fetchtweet.task.TaskRunnerThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tomoaki on 3/23/16.
 */

@Module
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(){
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    TaskRunnerThread provideTaskRunnerThread() {
        return new TaskRunnerThread();
    }

    @Singleton
    @Provides
    SubscribeOn provideSubscribeOn() {
        return () -> Schedulers.newThread();
    }

    @Singleton
    @Provides
    ObserveOn provideObserveOn() {
        return () -> AndroidSchedulers.mainThread();
    }
}
