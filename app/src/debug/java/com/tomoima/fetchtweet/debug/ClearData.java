package com.tomoima.fetchtweet.debug;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tomoima.debot.strategy.DebotStrategy;
import com.tomoima.fetchtweet.models.TweetData;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by tomoaki on 4/8/16.
 */
public class ClearData extends DebotStrategy {
    @Override
    public void startAction(@NonNull Activity activity) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<TweetData> results = realm.where(TweetData.class).findAll();
        realm.beginTransaction();
        results.clear();
        realm.commitTransaction();
        realm.close();
    }
}
