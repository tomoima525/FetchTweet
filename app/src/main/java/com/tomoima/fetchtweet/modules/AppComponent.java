package com.tomoima.fetchtweet.modules;

import android.support.v7.app.AppCompatActivity;

import com.tomoima.fetchtweet.data.TweetDataRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tomoaki on 3/23/16.
 */

@Singleton
@Component(
        modules = {AppModule.class, DataModule.class, PresenterModule.class}
)
public interface AppComponent {
    void inject(AppCompatActivity activity);
    void inject(TweetDataRepository repository);
}
