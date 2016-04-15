package com.tomoima.fetchtweet.task;

import android.util.Pair;

import com.tomoima.fetchtweet.api.CustomTwitterApiClient;
import com.tomoima.fetchtweet.models.TweetData;
import com.tomoima.fetchtweet.rx.CrashOnError;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import io.realm.Realm;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by tomoaki on 3/30/16.
 */
public class TweetLoader implements Runnable {

    String userName;
    Long sinceId,maxId;
    static Realm realm;
    private ThreadPoolExecutor threadPoolExecutor;

    public TweetLoader(String userName, Long sinceId, Long maxId, ThreadPoolExecutor threadPoolExecutor){
        this.userName = userName;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void fetchAllTweets(long sinceId, long maxId) {
        final CustomTwitterApiClient client = CustomTwitterApiClient.getInstance();
        //maxId を emit する Subject を用意して、そのツイートを取得するたびに、その onNext を叩く
        final PublishSubject<Long> maxIdSubject = PublishSubject.create();
        maxIdSubject
                .flatMap(mid -> {
                    final Long tempMaxId = mid == -1L ? null : mid;
                    final Long tempSinceId = sinceId == -1L ? null : sinceId;
                    return client.getCustomStatusesService()
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
                            }).toList().map(list -> new Pair<>(mid, list));
                })
                .doOnError(CrashOnError.crashOnError())
                .observeOn(Schedulers.from(threadPoolExecutor))
                .subscribe(
                        p -> {
                            TweetLoader.putTweetDataList(p.second);
                            if (p.second.isEmpty()) {
                                maxIdSubject.onCompleted();
                            } else {
                                final long nextMaxId = p.second.get(p.second.size() - 1).getId();
                                if (nextMaxId == p.first) {
                                    maxIdSubject.onCompleted();
                                } else {
                                    maxIdSubject.onNext(nextMaxId);
                                }
                            }
                        },
                        e -> Timber.d("¥¥ error: " + e.getMessage()),
                        () -> Timber.d("¥¥ fetching completed")
                );
        maxIdSubject.onNext(maxId);
    }

    public void start(){
        threadPoolExecutor.execute(this);
    }
    @Override
    public void run() {
        Timber.d("¥¥ start thread: " + Thread.currentThread().getName());
        fetchAllTweets(sinceId, maxId);
    }


    private static void putTweetDataList(List<TweetData> tweetDataList){
        realm = Realm.getDefaultInstance();
        Timber.d("¥¥ input thread: " + Thread.currentThread().getName());
        realm.beginTransaction();
        for(TweetData tweetData: tweetDataList){
            realm.copyToRealm(tweetData);
        }
        realm.commitTransaction();
        realm.close();
    }
}
