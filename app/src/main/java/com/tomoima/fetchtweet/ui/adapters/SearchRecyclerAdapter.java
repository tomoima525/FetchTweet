package com.tomoima.fetchtweet.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tomoima.fetchtweet.BR;
import com.tomoima.fetchtweet.R;
import com.tomoima.fetchtweet.databinding.ViewTweetdataBinding;
import com.tomoima.fetchtweet.models.TweetData;

import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.Case;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by tomoaki on 4/26/16.
 */
public class SearchRecyclerAdapter extends RealmSearchAdapter<TweetData, SearchRecyclerAdapter.ViewHolder> {

    public SearchRecyclerAdapter(@NonNull Context context, @NonNull Realm realm, @NonNull String filterKey, @NonNull String sortKey) {
        super(context, realm, filterKey, true, Case.INSENSITIVE, Sort.DESCENDING, sortKey, (String) null);
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int position) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_tweetdata, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.getBinding().setVariable(BR.tweet,realmResults.get(position));
    }

    public class ViewHolder extends RealmSearchViewHolder {
        private ViewTweetdataBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewTweetdataBinding getBinding(){
            return binding;
        }
    }
}
