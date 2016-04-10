package com.tomoima.fetchtweet.modules;

import android.app.Application;
import android.content.Context;

import com.tomoima.fetchtweet.task.TaskRunnerThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    
}
