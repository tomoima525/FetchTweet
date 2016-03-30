package com.tomoima.fetchtweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.models.TweetData;
import com.tomoima.fetchtweet.presenters.TweetShowPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TopActivity extends BaseActivity implements TweetShowPresenter.Callback {

    @Inject
    TweetShowPresenter tweetShowPresenter;

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        getAppComponent().inject(this);

        findViewById(R.id.button).setOnClickListener(
                v -> {
                    //tweetShowPresenter.getTweet(713229518278828032L);
                    tweetShowPresenter.getObservableTweet(713229518278828032L)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(tweetData -> updateView(tweetData));
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
        Timber.d("¥Initialization done:" + (tweetShowPresenter != null));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(tweetShowPresenter != null) {
//            tweetShowPresenter.setCallback(this);
//        }
    }


    @Override
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
