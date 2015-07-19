package com.anarcho.examples.retrofit.services;

import com.squareup.okhttp.OkHttpClient;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by alpa on 7/19/15.
 */
public class BaseService {

    private static final String DEFAULT_URL = "http://anarcho.pp.ciklum.com/api";

    public static <S> S createService(Class<S> serviceClass) {
        OkHttpClient client = new OkHttpClient();
        RestAdapter builder = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(DEFAULT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Content-Type", "application/json");
                    }
                }).setLogLevel(RestAdapter.LogLevel.BASIC).build();

        return builder.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        OkHttpClient client = new OkHttpClient();
        RestAdapter builder = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(DEFAULT_URL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Content-Type", "application/json");
                        request.addHeader("x-auth-token", authToken);
                    }
                }).setLogLevel(RestAdapter.LogLevel.BASIC).build();

        return builder.create(serviceClass);
    }

}
