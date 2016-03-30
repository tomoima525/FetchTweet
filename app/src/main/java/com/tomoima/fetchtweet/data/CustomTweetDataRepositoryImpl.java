package com.tomoima.fetchtweet.data;

import android.content.Context;

import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.api.CustomTwitterApiClient;
import com.tomoima.fetchtweet.models.TweetData;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

/**
 * Created by tomoaki on 3/29/16.
 */
public class CustomTweetDataRepositoryImpl extends TweetDataRepository {

    Context context;

    @Inject
    public CustomTweetDataRepositoryImpl(Context context){
        this.context = context;
    }

    @Override
    public Observable<TweetData> get(long id) {
        CustomTwitterApiClient client = CustomTwitterApiClient.getInstance();
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(
                        client.getCustomStatusesService().show(id, null, null, null)
                        .map(tweet -> {
                            TweetData data = new TweetData();
                            data.setMessage(tweet.text);
                            data.setId(tweet.id);
                            data.setName(tweet.user.screenName);
                            return data;
                        }).toBlocking().single());
                subscriber.onCompleted();
                Timber.d("¥¥completed!!");
            } catch (Exception e) {
                Timber.d("¥¥ error: " + e.getMessage());
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Observable<List<TweetData>> getAll(long maxId) {
        CustomTwitterApiClient client = CustomTwitterApiClient.getInstance();
        return Observable.create(subscriber -> {
            try{
                subscriber.onNext(client.getCustomStatusesService()
                        .userTimeline(
                                null,
                                ((ThisApplication)context.getApplicationContext()).getUserName(),
                                200,
                                1L,
                                maxId,
                                false,
                                false,
                                false,
                                true)
                        .flatMap(Observable::from)
                        .map( tweet -> {
                            TweetData data = new TweetData();
                            data.setMessage(tweet.text);
                            data.setId(tweet.id);
                            data.setName(tweet.user.screenName);
                            return data;
                        }).toList().toBlocking().single());
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    void putTweetData(TweetData data) {

    }
}
