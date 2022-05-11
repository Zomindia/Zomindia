package com.zomindianew.webservice;


import android.content.Context;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Api Factory for REST Client Retrofit
 */
public class BillingFactory {

    public static String baseUrlInAPP = "https://accounts.google.com/o/oauth2/";
    private static Retrofit retrofitWithHeader = null;


    public static Retrofit getClientInAppBilling(Context context) {


        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();


                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Content-type", "application/json");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = httpClient.addInterceptor(interceptor).connectTimeout(5, TimeUnit.MINUTES).
                readTimeout(5, TimeUnit.MINUTES).
                writeTimeout(5, TimeUnit.MINUTES).build();

        if (retrofitWithHeader == null) {
            retrofitWithHeader = new Retrofit.Builder().baseUrl(baseUrlInAPP).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitWithHeader;
    }


}
