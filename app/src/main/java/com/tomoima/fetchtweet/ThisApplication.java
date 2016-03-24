package com.tomoima.fetchtweet;

import android.app.Application;

import com.tomoima.fetchtweet.modules.AppComponent;
import com.tomoima.fetchtweet.modules.AppModule;
import com.tomoima.fetchtweet.modules.DaggerAppComponent;

/**
 * Created by tomoaki on 3/23/16.
 */
public class ThisApplication extends Application {

    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector(){
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }
}
