package com.tomoima.fetchtweet;

import android.app.Application;

import com.tomoima.debot.DebotConfigurator;
import com.tomoima.fetchtweet.modules.AppComponent;
import com.tomoima.fetchtweet.modules.AppModule;
import com.tomoima.fetchtweet.modules.DaggerAppComponent;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
/**
 * Created by tomoaki on 3/23/16.
 */
public class ThisApplication extends Application {

    private static final String userName="tomoaki_imai"; //TODO Temporary setting
    private AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        Timber.plant(new CustomTree());
        DebotConfigurator.configureWithDefault(this);
    }

    private void initializeInjector(){
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

    public static String getUserName() {
        return userName;
    }

    private static class CustomTree extends Timber.DebugTree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            StackTraceElement trace = new Throwable().getStackTrace()[5];// something strange thing is happening with stack trace
            String className = trace.getClassName();
            String methodName = trace.getMethodName();
            String caller = trace.getFileName() + ":" + trace.getLineNumber();
            StringBuilder formatStrBldr = new StringBuilder(message);
            formatStrBldr.append(String.format(" -- at %s.%s(%s)\n", className, methodName, caller));
            super.log(priority, tag, formatStrBldr.toString(), t);
        }
    }
}
