package com.tomoima.fetchtweet.modules;

import com.tomoima.fetchtweet.data.TweetDataRepositoryImpl;
import com.tomoima.fetchtweet.presenters.TweetShowPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tomoaki on 3/24/16.
 */
@Module
public class PresenterModule {
    @Provides
    public TweetShowPresenter provideTweetShowPresenter(TweetDataRepositoryImpl repository){
        return new TweetShowPresenter(repository);
    }
}
