package com.tomoima.fetchtweet.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by tomoaki on 3/24/16.
 */
public class TweetData extends RealmObject {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(int retweeted) {
        this.retweeted = retweeted;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
