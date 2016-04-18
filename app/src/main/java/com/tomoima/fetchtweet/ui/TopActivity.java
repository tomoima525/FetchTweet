package com.tomoima.fetchtweet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.events.ProcessingEvent;
import com.tomoima.fetchtweet.models.TweetData;
import com.tomoima.fetchtweet.presenters.TweetShowPresenter;
import com.tomoima.fetchtweet.rx.SchedulersTransformer;
import com.tomoima.fetchtweet.task.TaskRunnerThread;
import com.tomoima.fetchtweet.task.TweetLoader;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.realm.Realm;

public class TopActivity extends BaseActivity {

    @Inject
    TweetShowPresenter tweetShowPresenter;
    @Inject
    TaskRunnerThread taskRunnerThread;

    private TwitterLoginButton loginButton;
    private LinearLayout processView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        getAppComponent().inject(this);
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        setUpLoginButton(session != null);

        findViewById(R.id.button).setOnClickListener(
                v -> tweetShowPresenter.getTweet(20L)
                        .compose(SchedulersTransformer.applySchedulers())
                        .subscribe(this::updateView, this::ToastError)
        );

        findViewById(R.id.button_2).setOnClickListener(
                v -> new TweetLoader(ThisApplication.getUserName(),-1L,-1L, taskRunnerThread.getThreadPoolExecutor()).start()
        );

        processView = (LinearLayout) findViewById(R.id.process_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void setUpLoginButton(boolean isSessionExists){
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        if(isSessionExists) {
            loginButton.setVisibility(View.GONE);
            return;
        }
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                loginButton.setVisibility(View.GONE);
            }

            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Login with Twitter failure", e);
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingEvent(ProcessingEvent event){
        updateProcessView(event);
    }

    private void updateProcessView(ProcessingEvent event) {
        Realm realm = Realm.getDefaultInstance();
        Long totalCount = realm.where(TweetData.class).count();
        View view = LayoutInflater.from(this).inflate(R.layout.row_process, null);
        TextView twCount = (TextView) view.findViewById(R.id.tw_count);
        twCount.setText(event.fetchedDataNum + " tweets fetched. Total: " + totalCount);

//        TextView idName = (TextView) view.findViewById(R.id.id_name);
//        idName.setText("id: " + event.id);

        TextView threadName = (TextView) view.findViewById(R.id.thread_name);
        threadName.setText("Thread:" + event.threadName);

        processView.addView(view);
        realm.close();
    }

}
