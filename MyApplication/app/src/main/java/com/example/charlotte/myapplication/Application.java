package com.example.charlotte.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by charlotte on 18.04.16.
 */
public class Application extends android.app.Application {


    private static boolean inSingleConversationActivity;

    public static Retrofit getMessageRetrofit() {
        return messageRetrofit;
    }

    private static Retrofit messageRetrofit;

    public static MessagingServerService getService() {
        return service;
    }

    private static MessagingServerService service;

    public static OpenWeatherService getWeatherService() {
        return weatherService;
    }

    private static OpenWeatherService weatherService;

    public static MySpotifyService getSpotifyService() {
        return spotifyService;
    }

    public static void setSpotifyService(MySpotifyService spotifyService) {
        Application.spotifyService = spotifyService;
    }

    private static MySpotifyService spotifyService;
    static String baseURL = "http://192.168.1.12:3000/";

    public static boolean isInSingleConversationActivity() {
        return inSingleConversationActivity;
    }

    public static void setInSingleConversationActivity(boolean inSingleConversationActivity) {
        Application.inSingleConversationActivity = inSingleConversationActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String url = sharedPref.getString(getString(R.string.server_string),baseURL);
        Log.d("Application", url);


        try {

            messageRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .build()
            ;
        }catch(IllegalArgumentException e)
        {
            messageRetrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .build();

        }

        service = messageRetrofit.create(MessagingServerService.class);

/*
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
*/


        Retrofit spotifyRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
               // .client(client)
                .build();


        spotifyService=spotifyRetrofit.create(MySpotifyService.class);

        Retrofit weatherRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                        // .client(client)
                .build();

        weatherService=weatherRetrofit.create(OpenWeatherService.class);

        SpotifyServiceSingleton.getInstance().initialize();



        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)

                .showImageOnLoading(R.drawable.music_note)
                .considerExifParams(true)
                .build();



        //Create a config with those options.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);

    }


}
