package com.huebelancer.ccscreener;

import android.app.Application;

import com.huebelancer.ccscreener.Dependencies.DependencyRegistry;

import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by mahuebel on 8/14/17.
 */

public class CCScreenerApplication extends Application {

    DependencyRegistry registry;

    @Override
    public void onCreate() {
        super.onCreate();


        MeteorSingleton.createInstance(
                this,
                "ws://192.168.1.68:3000/websocket",
                new InMemoryDatabase()
        );

        registry = DependencyRegistry.shared;

    }
}
