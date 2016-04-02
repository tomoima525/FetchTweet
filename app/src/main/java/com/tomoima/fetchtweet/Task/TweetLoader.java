package com.tomoima.fetchtweet.Task;

import android.app.Application;

import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.api.CustomTwitterApiClient;
import com.tomoima.fetchtweet.models.Result;
import com.tomoima.fetchtweet.models.ResultCode;
import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by tomoaki on 3/30/16.
 */
public class TweetLoader {

    Application application;

    @Inject
    public TweetLoader(Application application){
        this.application = application;
    }

    public Observable<List<TweetData>> fetchMultipleTweets(long sinceId, long maxId) {
        CustomTwitterApiClient client = CustomTwitterApiClient.getInstance();
        final Long tempMaxId = maxId == -1L ? null : maxId;
        final Long tempSinceId = sinceId == -1L ? null: sinceId;
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(client.getCustomStatusesService()
                        .userTimeline(null,
                                ((ThisApplication) application).getUserName(),
                                200,
                                tempSinceId,
                                tempMaxId,
                                false,
                                false,
                                false,
                                true)
                        .flatMap(Observable::from)
                        .map(tweet -> {
                            TweetData data = new TweetData();
                            data.setMessage(tweet.text);
                            data.setId(tweet.id);
                            data.setName(tweet.user.screenName);
                            return data;
                        }).toList().toBlocking().single());

                subscriber.onCompleted();
            } catch (Exception e){
                subscriber.onError(e);
            }
        });
    }

    public void fetchAllTweets(long sinceId, long maxId) {
        Observable<Result> observable = Observable.create(subscriber -> {
            fetchMultipleTweets(sinceId, maxId)
                    .subscribe(new Subscriber<List<TweetData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            subscriber.onNext(new Result(ResultCode.ERROR,e.toString(), -1, -1));
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onNext(List<TweetData> tweetDatas) {
                            if(tweetDatas.isEmpty()){
                                subscriber.onCompleted();
                            }
                            //Realmに格納

                            //最後の値
                            long nextSinceId = tweetDatas.get(tweetDatas.size() - 1).getId();
                            if(nextSinceId == sinceId) {
                                subscriber.onCompleted();
                                return;
                            }

                            subscriber.onNext(new Result(ResultCode.SUCCESS, "", nextSinceId, maxId));
                        }
                    });
        });
        observable.subscribe(new Subscriber<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e("¥¥ error " + e.getMessage());
            }

            @Override
            public void onNext(Result result) {
                if(ResultCode.ERROR.equals(result.resultCode)) return;
                fetchMultipleTweets(result.sinceId, result.maxId);
            }
        });
    }

}
