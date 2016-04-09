package com.tomoima.fetchtweet;

import com.tomoima.debot.DebotConfigurator;
import com.tomoima.debot.DebotStrategyBuilder;
import com.tomoima.fetchtweet.debug.ClearData;
import com.tomoima.fetchtweet.debug.CountData;

/**
 * Created by tomoaki on 4/8/16.
 */
public class DebugApplication extends ThisApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DebotStrategyBuilder builder = new DebotStrategyBuilder.Builder(this)
                .registerMenu("clear data", new ClearData())
                .registerMenu("count data", new CountData())
                .build();
        DebotConfigurator.configureWithCustomizedMenu(this,builder.getStrategyList());
    }
}
