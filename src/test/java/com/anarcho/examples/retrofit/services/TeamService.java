package com.anarcho.examples.retrofit.services;

import com.anarcho.models.TeamList;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by alpa on 7/19/15.
 */
public interface TeamService {

    @GET("/permission/{appKey}")
    TeamList getTeamList(@Path("appKey") String appKey);
}
