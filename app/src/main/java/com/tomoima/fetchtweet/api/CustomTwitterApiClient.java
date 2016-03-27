package com.tomoima.fetchtweet.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.AuthenticatedClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.models.SafeListAdapter;
import com.twitter.sdk.android.core.models.SafeMapAdapter;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by tomoaki on 3/24/16.
 */
public class CustomTwitterApiClient extends TwitterApiClient {

    private static CustomTwitterApiClient customTwitterApiClient;
    private CustomFavoriteService customFavoriteService;
    private CustomStatusesService customStatusesService;

    public static CustomTwitterApiClient getInstance(){
        if(customTwitterApiClient == null){
            customTwitterApiClient = new CustomTwitterApiClient(TwitterCore.getInstance());
        }
        return customTwitterApiClient;
    }

    private CustomTwitterApiClient(TwitterCore twitterCore){
        super(twitterCore.getSessionManager().getActiveSession());
        Gson gson = (new GsonBuilder()).registerTypeAdapterFactory(new SafeListAdapter()).registerTypeAdapterFactory(new SafeMapAdapter()).create();
        RestAdapter adapter = (new RestAdapter.Builder())
                .setClient(new AuthenticatedClient(twitterCore.getAuthConfig(), twitterCore.getSessionManager().getActiveSession(), twitterCore.getSSLSocketFactory()))
                .setEndpoint(new TwitterApi().getBaseHostUrl())
                .setConverter(new GsonConverter(gson))
                .setExecutors(TwitterCore.getInstance().getFabric().getExecutorService(), Executors.newFixedThreadPool(4)).build();
        customFavoriteService = adapter.create(CustomFavoriteService.class);
        customStatusesService = adapter.create(CustomStatusesService.class);
    }

    public CustomFavoriteService getCustomFavoriteService(){
        return customFavoriteService;
    }

    public CustomStatusesService getCustomStatusesService(){
        return  customStatusesService;
    }

    public interface CustomFavoriteService {
        @GET("/1.1/favorites/list.json")
        void list(@Query("user_id") Long var1,
                  @Query("screen_name") String var2,
                  @Query("count") Integer var3,
                  @Query("since_id") String var4,
                  @Query("max_id") String var5,
                  Callback<List<Tweet>> var7);

    }

    public interface CustomStatusesService {
        @GET("/1.1/statuses/user_timeline.json")
        void userTimeline(@Query("user_id") Long var1,
                          @Query("screen_name") String var2,
                          @Query("count") Integer var3,
                          @Query("since_id") Long var4,
                          @Query("max_id") Long var5,
                          @Query("trim_user") Boolean var6,
                          @Query("exclude_replies") Boolean var7,
                          @Query("contributor_details") Boolean var8,
                          @Query("include_rts") Boolean var9,
                          Callback<List<Tweet>> var10);

    }
}
