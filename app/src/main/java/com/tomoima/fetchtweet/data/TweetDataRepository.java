package com.tomoima.fetchtweet.data;

import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

/**
 * Created by tomoaki on 3/24/16.
 */
public interface TweetDataRepository {
    TweetData get(long id);
    List<TweetData> getAll();
    void putTweetData(TweetData data);
}
