package com.olegro.cristianpostcards.data.retrofit;

import android.content.Context;


import com.olegro.cristianpostcards.BuildConfig;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Web {
    private Context context;
    private UserClient userClient;

    public Web(Context context) {
        this.context = context;

        OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();
        okClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        okClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okClientBuilder.addInterceptor(interceptor);
        }

        retrofit2.Retrofit.Builder builder = new retrofit2.Retrofit.Builder()
                .baseUrl("http://2.59.40.106:8081/")
                .client(okClientBuilder.build())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create());

        retrofit2.Retrofit retrofit = builder.build();

        userClient = retrofit.create(UserClient.class);
    }

    public UserClient createRetrofit() {
        return userClient;
    }
}
