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

        //TODO: do DI with executor
        threadPoolExecutor =  new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());

        findViewById(R.id.button).setOnClickListener(
                v -> tweetShowPresenter.getTweet(20L)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::updateView, this::ToastError)
        );

        findViewById(R.id.button_2).setOnClickListener(
                v -> new Thread(new TweetLoader(ThisApplication.getUserName(),-1L,-1L, threadPoolExecutor)).start()
        );
    }
    
    public void updateView(TweetData tweetData) {
        String message = tweetData.getMessage();
        String name = tweetData.getName();
        ((TextView)findViewById(R.id.result)).setText("@" + name +": " + message);
    }

    public void ToastError(Throwable e){
        Toast.makeText(this, "Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

}
