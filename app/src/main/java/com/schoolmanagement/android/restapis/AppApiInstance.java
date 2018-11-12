package com.schoolmanagement.android.restapis;

import android.support.annotation.NonNull;

import com.schoolmanagement.android.utils.AppConfig;
import com.schoolmanagement.android.utils.AppUtils;
import com.schoolmanagement.android.utils.DebugLog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppApiInstance {

    /**
     * Get {@link Retrofit} instance.
     *
     * @return
     */
    @NonNull
    public static Retrofit getRetrofit() {
        return getRetrofitForUrl(AppConfig.getInstance().getServerUrl());
    }

    /**
     * Get {@link Retrofit} instance.
     *
     * @return
     */
    @NonNull
    public static Retrofit getRetrofitForUrl(String baseUrl) {
        OkHttpClient client = getHttpClient();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    private static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptorLogging = new HttpLoggingInterceptor();
        interceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
        String token = AppConfig.getInstance().getJwtToken();
        Interceptor interceptorRequest = getInterceptor(token);

        return new OkHttpClient.Builder()
                .addInterceptor(interceptorLogging)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addNetworkInterceptor(interceptorRequest)
                .build();
    }

    public static AppApiService getApi() {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(AppApiService.class);
    }

    @NonNull
    private static Interceptor getInterceptor(String authHeader) {
        return chain -> {
            Request original = chain.request();

            Map<String, String> headersMap = getHeadersMap(original.headers());

            if (!AppUtils.isEmpty(authHeader)) {
                headersMap.put("Authorization", "Basic " + authHeader);
                DebugLog.w("AuthHeader found");
            } else {
                DebugLog.w("If its login or register api call Token will be null otherwise its error");
            }

            Request request = original.newBuilder()
                    .headers(toHeaders(headersMap))
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        };
    }

    private static Map<String, String> getHeadersMap(Headers headers) {
        HashMap<String, String> headersMap = new HashMap<>();

        if (headers != null) {
            Set<String> names = headers.names();
            for (String name : names) {
                headersMap.put(name, headers.get(name));
            }
        }

        return headersMap;
    }

    private static Headers toHeaders(Map<String, String> headersMap) {
        return Headers.of(headersMap);
    }
}
