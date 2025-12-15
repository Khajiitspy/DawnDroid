package com.example.dawn.network;

import com.example.dawn.dto.Task.TaskItemDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TaskApi {
    @GET("api/tasks")
    public Call<List<TaskItemDTO>> list();
    @Multipart
    @POST("api/tasks")
    Call<TaskItemDTO> create(
            @Part("Name") RequestBody name,
            @Part MultipartBody.Part image
    );
}
