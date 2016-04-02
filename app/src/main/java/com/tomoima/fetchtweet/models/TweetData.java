package com.tomoima.fetchtweet.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tomoaki on 3/24/16.
 */
@Getter
@Setter
public class TweetData extends RealmObject {
    @Required
    private long id;
    @Required
    private String message;
    private Date date;
    private int retweetCount;
    private int favoriteCount;
    private int retweeted; //他人のRetweet
    private int favorited; //他人のfavorite
    private int type; // 0:自分のツイート, 1:Fav
    private String name; // favしたツイートをしたユーザー名
}
