package com.tomoima.fetchtweet.presenters;

import com.tomoima.fetchtweet.data.TweetDataRepository;
import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by tomoaki on 3/24/16.
 */
public class TweetShowPresenter {

    private TweetDataRepository repository;

    @Inject
    public TweetShowPresenter(TweetDataRepository repository){
        this.repository = repository;
    }

    public Observable<TweetData> getTweet(final long id){
        return repository.get(id);
    }

    public Observable<List<TweetData>> getTweets(final long maxId){
        return repository.getAll(maxId);
    }

}
