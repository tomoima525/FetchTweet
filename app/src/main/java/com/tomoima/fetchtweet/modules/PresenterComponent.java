package com.tomoima.fetchtweet.modules;

import com.tomoima.fetchtweet.data.TweetDataRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tomoaki on 3/25/16.
 */

@Singleton
@Component(modules = PresenterModule.class, dependencies=DataModule.class)
public interface PresenterComponent {
    void inject(TweetDataRepository repository);
}
