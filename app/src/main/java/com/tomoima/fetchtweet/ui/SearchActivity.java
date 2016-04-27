package com.tomoima.fetchtweet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.ui.adapters.SearchRecyclerAdapter;

import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import io.realm.Realm;

/**
 * Created by tomoaki on 4/26/16.
 */
public class SearchActivity extends BaseActivity {

    Realm realm;

    public static Intent createIntent(Context context){
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        RealmSearchView realmSearchView = (RealmSearchView) findViewById(R.id.search_view);
        realm = Realm.getDefaultInstance();
        RealmSearchAdapter adapter = new SearchRecyclerAdapter(this, realm, "message");
        realmSearchView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }
}
