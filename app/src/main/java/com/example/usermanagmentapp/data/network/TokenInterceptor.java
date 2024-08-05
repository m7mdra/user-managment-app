package com.example.usermanagmentapp.data.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization",
                        "Bearer eb56dc77ac69e3cf1bbb36a07dc0343f29507175d54d853d12ce011abde2e9ec")
                .build();
        return chain.proceed(request);
    }
}
