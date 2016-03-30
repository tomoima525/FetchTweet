package com.tomoima.fetchtweet.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tomoima.debot.Debot;
import com.tomoima.fetchtweet.ThisApplication;
import com.tomoima.fetchtweet.modules.AppComponent;

/**
 * Created by tomoaki on 3/28/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debot.getInstance(this);
    }

    protected AppComponent getAppComponent(){
        return ((ThisApplication)getApplication()).getAppComponent();
    }
}
