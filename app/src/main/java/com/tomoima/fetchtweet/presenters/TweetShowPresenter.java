package com.tomoima.fetchtweet.presenters;

import com.tomoima.fetchtweet.data.TweetDataRepositoryImpl;
import com.tomoima.fetchtweet.models.TweetData;

import javax.inject.Inject;

/**
 * Created by tomoaki on 3/24/16.
 */
public class TweetShowPresenter {

    private TweetDataRepositoryImpl repository;
    private Callback callback;
    @Inject
    public TweetShowPresenter(TweetDataRepositoryImpl repository){
        this.repository = repository;
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void getTweet(final long id){
        //TODO use rxjava
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.updateView(repository.get(id));
            }
        }).start();
    }

    public interface Callback {
        void updateView(TweetData tweetData);
    }
}
