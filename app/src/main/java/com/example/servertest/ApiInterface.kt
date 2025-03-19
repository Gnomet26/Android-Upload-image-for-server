package com.example.servertest

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody

interface ApiInterface {

    @Multipart
    @POST("api/image/upload/")
    suspend fun uploadImage(@Part image:MultipartBody.Part):Result
}