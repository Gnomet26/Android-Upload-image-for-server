package com.example.servertest

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Header
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiInterface {

    @Multipart
    @POST("api/content/create/")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part rasm_1: MultipartBody.Part?,
        @Part rasm_2: MultipartBody.Part?,
        @Part rasm_3: MultipartBody.Part?,
        @Part rasm_4: MultipartBody.Part?,
        @Part passport_old_rasm: MultipartBody.Part?,
        @Part passport_orqa_rasm: MultipartBody.Part?,
        @Part guvohnoma_rasm: MultipartBody.Part?,
        @Part("karta_raqam") kartaRaqam: RequestBody,
        @Part("elon_matni") elonMatni: RequestBody
    ):Result
}