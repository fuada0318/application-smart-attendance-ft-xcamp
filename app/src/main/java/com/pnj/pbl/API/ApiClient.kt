package com.pnj.pbl.API

import com.pnj.pbl.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiLogin {
    @FormUrlEncoded
    @POST("api/login")
    fun postLogin(
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<LoginResponse>
}