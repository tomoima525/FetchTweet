package com.tomoima.fetchtweet.Task;

import com.tomoima.fetchtweet.api.CustomTwitterApiClient;
import com.tomoima.fetchtweet.models.Result;
import com.tomoima.fetchtweet.models.ResultCode;
import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by tomoaki on 3/30/16.
 */
public class TweetLoader implements Runnable {

    String userName;
    Long sinceId,maxId;
    Realm realm;

    public TweetLoader(String userName, Long sinceId, Long maxId){
        this.userName = userName;
        this.sinceId = sinceId;
        this.maxId = maxId;
        realm = Realm.getDefaultInstance();
    }

    public Observable<Observable<List<TweetData>>> fetchMultipleTweets(long sinceId, long maxId) {
        CustomTwitterApiClient client = CustomTwitterApiClient.getInstance();
        final Long tempMaxId = maxId == -1L ? null : maxId;
        final Long tempSinceId = sinceId == -1L ? null: sinceId;
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(client.getCustomStatusesService()
                        .userTimeline(null,
                                userName,
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
                        }).toList());
                Timber.d("¥¥ network");
                subscriber.onCompleted();
            } catch (Exception e){
                subscriber.onError(e);
            }
        });
    }



    public void fetchAllTweets(long sinceId, long maxId) {
        Observable<Result> observable = Observable.create(subscriber -> {
            fetchMultipleTweets(sinceId, maxId)
                    .subscribe(new Subscriber<Observable<List<TweetData>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                            subscriber.onNext(new Result(ResultCode.ERROR, e.toString(), -1, -1));
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onNext(Observable<List<TweetData>> tweetDataListObservable) {
                            tweetDataListObservable.isEmpty().subscribe(isEmpty -> {
                                if(isEmpty){
                                    subscriber.onCompleted();
                                } else {
                                    Observable<TweetData> tweetDataObservable = tweetDataListObservable.flatMap(Observable::from);
                                    tweetDataObservable.subscribe(tweetData -> {
                                        //Realmに格納
                                        realm.beginTransaction();
                                        realm.copyToRealm(tweetData);
                                        realm.commitTransaction();
                                    });
                                    //最後のid取得
                                    tweetDataObservable.last().subscribe(tweetData -> {
                                       long nextSinceId = tweetData.getId();
                                        if(nextSinceId == sinceId){
                                            subscriber.onCompleted();
                                        } else {
                                            subscriber.onNext(new Result(ResultCode.SUCCESS, "", nextSinceId, maxId));
                                        }
                                    });
                                }
                            });
                        }
                    });
        });

        observable.subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Result>() {
            @Override
            public void onCompleted() {
                Timber.d("¥¥ fetching completed");
                realm.close();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e("¥¥ error " + e.getMessage());
            }

            @Override
            public void onNext(Result result) {
                Timber.d("¥¥ onNext since " + result.sinceId + " max " + result.maxId);
                if(ResultCode.ERROR.equals(result.resultCode)) return;
                fetchMultipleTweets(result.sinceId, result.maxId);
            }
        });
    }

    @Override
    public void run() {
        fetchAllTweets(sinceId,maxId);
    }
}
