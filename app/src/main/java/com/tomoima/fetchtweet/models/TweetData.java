package com.tomoima.fetchtweet.models;

import java.util.Date;

import lombok.Data;

/**
 * Created by tomoaki on 3/24/16.
 */
@Data
public class TweetData {
    private long id;
    private String message;
    private Date date;
    private int retweetCount;
    private int favoriteCount;
    private int retweeted; //他人のRetweet
    private int favorited; //他人のfavorite
    private int type; // 0:自分のツイート, 1:Fav
    private String name; // favしたツイートをしたユーザー名
}
