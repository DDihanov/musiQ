package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.models.Album;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

@Module
public class NetworkModule {
    private String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory provideRxJavaCallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
//        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {
        //need to pass the custom deserializer here
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Album.class, new Album.DataStateDeserializer())
                .create();
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();
            HttpUrl url;


            if(!originalHttpUrl.toString().equals(Config.LAST_FM_API_URL)){
                url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", Config.API_KEY)
                        .addQueryParameter("format", Config.FORMAT)
                        .build();
            } else {
                url = originalHttpUrl;
            }


            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        return httpClient
                //need these otherwise app crashes when using RxJava2
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, RxJava2CallAdapterFactory callAdapterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .client(okHttpClient)
                .build();
    }
}
