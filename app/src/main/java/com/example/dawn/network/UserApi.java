package com.example.dawn.network;

import com.example.dawn.dto.Account.UserItemDTO;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApi {
    @Multipart
    @POST("api/user/register")
    Call<UserItemDTO> register(
            @Part("Email") RequestBody email,
            @Part("UserName") RequestBody userName,
            @Part("Password") RequestBody password,
            @Part MultipartBody.Part avatar
    );

}
