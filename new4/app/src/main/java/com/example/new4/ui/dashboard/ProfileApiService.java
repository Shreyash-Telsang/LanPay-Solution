package com.example.new4.ui.dashboard;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ProfileApiService {
    @POST("Python/get_profile")
    Call<UserProfile> getProfile(@Body Map<String, String> body);

    @POST("Python/update_profile")
    Call<UserProfile> updateProfile(@Body UserProfile profile);

    @POST("Python/delete_account")
    Call<Void> deleteAccount(@Body Map<String, String> body);
}