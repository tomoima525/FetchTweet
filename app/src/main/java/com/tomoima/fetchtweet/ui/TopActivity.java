package com.tomoima.fetchtweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tomoima.debot.Debot;
import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.data.TweetDataRepositoryImpl;
import com.tomoima.fetchtweet.models.TweetData;
import com.tomoima.fetchtweet.modules.AppComponent;
import com.tomoima.fetchtweet.presenters.TweetShowPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import javax.inject.Inject;

import timber.log.Timber;

public class TopActivity extends AppCompatActivity implements TweetShowPresenter.Callback {

    @Inject
    TweetShowPresenter tweetShowPresenter;
    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        getAppComponent().inject(this);
        tweetShowPresenter = new TweetShowPresenter(new TweetDataRepositoryImpl(this));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetShowPresenter.getTweet(713229518278828032L);
            }
        });

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
        Timber.d("¥Initialization done");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tweetShowPresenter != null) {
            tweetShowPresenter.setCallback(this);
        }
        Debot.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Debot.onPause(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Debot.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Debot.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
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


    private AppComponent getAppComponent(){
        return ((ThisApplication)getApplicationContext()).getAppComponent();
    }
}
