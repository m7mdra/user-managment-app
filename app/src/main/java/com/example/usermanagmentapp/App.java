package com.example.usermanagmentapp;

import android.app.Application;

import com.example.usermanagmentapp.sl.AppModule;

import org.koin.android.java.KoinAndroidApplication;
import org.koin.core.KoinApplication;
import org.koin.core.context.DefaultContextExtKt;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KoinApplication application = KoinAndroidApplication.create(this).modules(AppModule.getAppModule());
        application.allowOverride(true);
        DefaultContextExtKt.startKoin(application);
    }
}
