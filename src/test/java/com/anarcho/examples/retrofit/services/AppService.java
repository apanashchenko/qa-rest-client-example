package com.anarcho.examples.retrofit.services;

import com.anarcho.models.App;
import com.anarcho.models.AppList;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by alpa on 7/19/15.
 */
public interface AppService {

    @GET("/apps")
    AppList getAppList();

    @GET("/apps/{appKey}")
    App getApp(@Path("appKey") String appKey);
}
