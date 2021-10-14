package com.reedtech.electronics.interfaces;

import com.reedtech.electronics.models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiQueries {

    @GET("getusers")
    Call<List<Users>> getusers();

    @GET("getuser/{id}")
    Call<Users> getuser(@Path("id") int id);

    @POST("new_user")
    Call<Users> newuser(@Body Users user);

}
