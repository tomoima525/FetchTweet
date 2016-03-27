package com.tomoima.fetchtweet.data;

import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

/**
 * Created by tomoaki on 3/24/16.
 */
public abstract class TweetDataRepository {
    TweetDataRepositoryCallback cb;
    public abstract TweetData get(long id);
    public abstract List<TweetData> getAll();
    abstract void putTweetData(TweetData data);

    public void setCallback(TweetDataRepositoryCallback cb){
        this.cb = cb;
    }

    public interface TweetDataRepositoryCallback{
        void fetchTweet(TweetData data);
    }
}
