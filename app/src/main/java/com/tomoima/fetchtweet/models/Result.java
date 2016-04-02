package com.tomoima.fetchtweet.models;

import lombok.AllArgsConstructor;

/**
 * Created by tomoaki on 3/30/16.
 */

@AllArgsConstructor
public class Result {
    public ResultCode resultCode;
    public String message;
    public long sinceId;
    public long maxId;
}
