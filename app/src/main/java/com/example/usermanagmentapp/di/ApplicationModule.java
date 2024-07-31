package com.example.usermanagmentapp.di;

import androidx.annotation.NonNull;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;

import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.network.UsersNetworkService;
import com.example.usermanagmentapp.data.repository.UserRepository;
import com.example.usermanagmentapp.data.source.UserDataSource;
import com.example.usermanagmentapp.data.source.UserPagingSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(value = SingletonComponent.class)
public class ApplicationModule {
    @Provides
    @Singleton
    public Gson createGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor createLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    public OkHttpClient createOkHttp(HttpLoggingInterceptor httpLoggingInterceptor) {

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
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
                })
                .build();
    }

    @Provides
    @Singleton
    public Retrofit createRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://gorest.co.in/public/v2/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    public UsersNetworkService createUsersNetworkService(Retrofit retrofit) {
        return retrofit.create(UsersNetworkService.class);
    }

    @Provides
    @Singleton
    public UserRepository createUserRepository(UsersNetworkService usersNetworkService, Gson gson) {
        return new UserDataSource(usersNetworkService, gson);
    }

    @IOScheduler
    @Provides
    public Scheduler provideIoScheduler() {
        return Schedulers.io();
    }

    @MainScheduler
    @Provides
    public Scheduler provideMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    public UserPagingSource createUserPagingSource(UserRepository userRepository, @IOScheduler Scheduler ioScheduler) {
        return new UserPagingSource(userRepository, ioScheduler);
    }

    @Provides
    public Pager<Integer, User> createPager(UserPagingSource pagingSource) {
        return new Pager<>(new PagingConfig(50), 1, () -> pagingSource);
    }
}
