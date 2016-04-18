package com.tomoima.fetchtweet.events;

/**
 * Created by tomoaki on 4/17/16.
 */
public class ProcessingEvent {
    public final Long id;
    public final int fetchedDataNum;
    public final String threadName;

    public ProcessingEvent(Long id, int fetchedDataNum, String threadName) {
        this.id = id;
        this.fetchedDataNum = fetchedDataNum;
        this.threadName = threadName;
    }
}
