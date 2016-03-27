package com.tomoima.fetchtweet.data;

import android.content.Context;

import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.api.CustomTwitterApiClient;
import com.tomoima.fetchtweet.models.TweetData;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by tomoaki on 3/23/16.
 */
public class TweetDataRepositoryImpl extends TweetDataRepository{

    Context context;
    @Inject
    public TweetDataRepositoryImpl(Context context){
        this.context = context;
        ((ThisApplication)context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public TweetData get(long id) {
        final TweetData tweetData = new TweetData();
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.show(id, null, null, null, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Tweet tweet = result.data;
                if(tweet == null) return;
                tweetData.setId(tweet.id);
                tweetData.setMessage(tweet.text);
                cb.fetchTweet(tweetData);
            }

            public void failure(TwitterException exception) {
                //Do something on failure
            }
        });
        return tweetData;
    }

    @Override
    public List<TweetData> getAll() {
        final List<TweetData> tweetDataList = new ArrayList<>();
        //TODO: setup correctly
        CustomTwitterApiClient.getInstance().getCustomStatusesService().userTimeline(null, ((ThisApplication)context.getApplicationContext()).getUserName(), 200, null, null, false, false, false, true, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Tweet tweet = result.data.get(0);
                if(tweet == null) return;
                TweetData tweetData = new TweetData();
                tweetData.setId(tweet.id);
                tweetData.setMessage(tweet.text);
                tweetDataList.add(tweetData);
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
        return tweetDataList;
    }

    @Override
    public void putTweetData(TweetData data) {

    }

}
