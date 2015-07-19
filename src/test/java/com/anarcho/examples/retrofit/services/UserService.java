package com.anarcho.examples.retrofit.services;

import com.anarcho.models.AuthToken;
import com.anarcho.models.User;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by alpa on 7/19/15.
 */
public interface UserService {

    @GET("/user")
    User getUser();

    @POST("/login")
    AuthToken login(@Body User user);
}
