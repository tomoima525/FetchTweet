package com.tomoima.fetchtweet.presenters;

import android.os.Handler;
import android.os.Looper;

import com.tomoima.fetchtweet.data.TweetDataRepository;
import com.tomoima.fetchtweet.models.TweetData;

import javax.inject.Inject;

/**
 * Created by tomoaki on 3/24/16.
 */
public class TweetShowPresenter implements TweetDataRepository.TweetDataRepositoryCallback {

    private TweetDataRepository repository;
    private Callback callback;

    @Inject
    public TweetShowPresenter(TweetDataRepository repository){
        this.repository = repository;
        this.repository.setCallback(this);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void getTweet(final long id){
        //TODO use rxjava
        new Thread(new Runnable() {
            @Override
            public void run() {
                repository.get(id);
            }
        }).start();
    }

    @Override
    public void fetchTweet(final TweetData data) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                callback.updateView(data);
            }
        });
    }

    public interface Callback {
        void updateView(TweetData tweetData);
    }
}
