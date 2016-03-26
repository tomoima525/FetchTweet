package com.tomoima.fetchtweet.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.data.TweetDataRepositoryImpl;
import com.tomoima.fetchtweet.models.TweetData;
import com.tomoima.fetchtweet.modules.AppComponent;
import com.tomoima.fetchtweet.presenters.TweetShowPresenter;

import javax.inject.Inject;

public class TopActivity extends AppCompatActivity implements TweetShowPresenter.Callback {

    @Inject
    TweetShowPresenter tweetShowPresenter;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(tweetShowPresenter != null) {
            tweetShowPresenter.setCallback(this);
        }
    }

    @Override
    public void updateView(TweetData tweetData) {
        ((TextView)findViewById(R.id.result)).setText(tweetData.getMessage());
    }

    private AppComponent getAppComponent(){
        return ((ThisApplication)getApplicationContext()).getAppComponent();
    }
}
