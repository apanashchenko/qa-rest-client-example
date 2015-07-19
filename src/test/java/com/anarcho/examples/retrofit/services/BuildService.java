package com.anarcho.examples.retrofit.services;

import com.anarcho.models.Build;
import com.anarcho.models.BuildList;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by alpa on 7/19/15.
 */
public interface BuildService {

    @GET("/apps/{appKey}/builds")
    BuildList getBuildList(@Path("appKey") String appKey);

    @GET("/apps/{appKey}/{buildId}")
    Build getBuild(@Path("appKey") String appKey, @Path("buildId") int buildId);
}
