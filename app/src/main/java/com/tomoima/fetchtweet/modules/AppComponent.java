package com.tomoima.fetchtweet.modules;

import com.tomoima.fetchtweet.ui.TopActivity;

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
    void inject(TopActivity activity);
}
