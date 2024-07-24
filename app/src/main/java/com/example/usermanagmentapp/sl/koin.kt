package com.example.usermanagmentapp.sl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.usermanagmentapp.data.network.UsersNetworkService
import com.example.usermanagmentapp.data.repository.UserRepository
import com.example.usermanagmentapp.data.source.UserDataSource
import com.example.usermanagmentapp.data.source.UserPagingSource
import com.example.usermanagmentapp.ui.add.AddUserViewModel
import com.example.usermanagmentapp.ui.details.UserDetailsViewModel
import com.example.usermanagmentapp.ui.main.UserViewModel
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Module for dependency injection
 */
val appModule = module {
    val ioScheduler = Schedulers.io()
    val mainScheduler = AndroidSchedulers.mainThread()
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    single { GsonBuilder().create() }
    single {
        Retrofit.Builder()
            .baseUrl("https://gorest.co.in/public/v2/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(Interceptor {
                        val newRequest = it.request().newBuilder().addHeader(
                            "Authorization",
                            "Bearer eb56dc77ac69e3cf1bbb36a07dc0343f29507175d54d853d12ce011abde2e9ec"
                        ).build()
                        it.proceed(newRequest)
                    })
                    .addInterceptor(logging).build()
            )
            .build()
    }
    single<UsersNetworkService> {
        get<Retrofit>().create(UsersNetworkService::class.java)
    }
    factory {
        Pager(PagingConfig(pageSize = 50), 1) {
            UserPagingSource(get(), ioScheduler)
        }
    }
    single<UserRepository> {
        UserDataSource(get(), get())
    }
    viewModel {
        UserViewModel(get(), ioScheduler, mainScheduler)
    }
    viewModel {
        AddUserViewModel(get(), ioScheduler, mainScheduler)
    }
    viewModel {
        UserDetailsViewModel(get(), ioScheduler, mainScheduler)
    }
}