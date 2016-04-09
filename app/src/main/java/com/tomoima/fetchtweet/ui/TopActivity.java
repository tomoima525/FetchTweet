package com.tomoima.fetchtweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.Task.TweetLoader;
import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.models.TweetData;
import com.tomoima.fetchtweet.presenters.TweetShowPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopActivity extends BaseActivity {

    @Inject
    TweetShowPresenter tweetShowPresenter;
    ThreadPoolExecutor threadPoolExecutor;
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        getAppComponent().inject(this);

        //TODO: do DI
        threadPoolExecutor =  new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        findViewById(R.id.button).setOnClickListener(
                v -> {
                    tweetShowPresenter.getTweet(713229518278828032L)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(tweetData -> updateView(tweetData));
                }
        );

        TextView dataCountTv = (TextView)findViewById(R.id.data_count);

        findViewById(R.id.button_2).setOnClickListener(
                v -> {
                    new Thread(new TweetLoader(ThisApplication.getUserName(),-1L,-1L, threadPoolExecutor)).start();
                }
        );

        findViewById(R.id.button_clear).setOnClickListener(
                v-> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<TweetData> results = realm.where(TweetData.class).findAll();
                    realm.beginTransaction();
                    results.clear();
                    realm.commitTransaction();
                    realm.close();
                }
        );

        findViewById(R.id.button_3).setOnClickListener(
                v-> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<TweetData> results = realm.where(TweetData.class).findAll();
                    dataCountTv.setText("size:" + results.size());
                    realm.close();
                }
        );

        loginButton =(TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Login with Twitter failure", e);
            }
        });
    }
    
    public void updateView(TweetData tweetData) {
        String message = tweetData.getMessage();
        ((TextView)findViewById(R.id.result)).setText(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
