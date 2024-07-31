package com.example.usermanagmentapp.di;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;

import com.example.usermanagmentapp.data.model.User;
import com.example.usermanagmentapp.data.network.TokenInterceptor;
import com.example.usermanagmentapp.data.network.UsersNetworkService;
import com.example.usermanagmentapp.data.repository.UserRepository;
import com.example.usermanagmentapp.data.source.UserDataSource;
import com.example.usermanagmentapp.data.source.UserPagingSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
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
    public TokenInterceptor createTokenInterceptor() {

        return new TokenInterceptor();
    }

    @Provides
    @Singleton
    public OkHttpClient createOkHttp(HttpLoggingInterceptor httpLoggingInterceptor, TokenInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(interceptor)
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
    public Pager<Integer, User> createPager(UserRepository userRepository, @IOScheduler Scheduler ioScheduler) {
        return new Pager<>(new PagingConfig(50), 1, () -> new UserPagingSource(userRepository, ioScheduler));
    }
}
