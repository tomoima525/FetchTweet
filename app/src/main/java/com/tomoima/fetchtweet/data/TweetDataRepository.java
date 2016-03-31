package com.tomoima.fetchtweet.data;

import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

import rx.Observable;

/**
 * Created by tomoaki on 3/24/16.
 */
public abstract class TweetDataRepository {
    public abstract Observable<TweetData> get(long id);
    public abstract Observable<List<TweetData>> getAll(long max_id);
    abstract void putTweetData(TweetData data);
}
