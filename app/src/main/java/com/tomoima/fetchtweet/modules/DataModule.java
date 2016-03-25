package com.tomoima.fetchtweet.modules;

import android.content.Context;

import com.tomoima.fetchtweet.data.TweetDataRepository;
import com.tomoima.fetchtweet.data.TweetDataRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tomoaki on 3/23/16.
 */

@Module
public class DataModule {
    @Provides
    public TweetDataRepository provideTweetDataRepository(Context context){
        return new TweetDataRepositoryImpl(context);
    }
}
