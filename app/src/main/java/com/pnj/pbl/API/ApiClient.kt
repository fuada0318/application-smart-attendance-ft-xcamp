package com.pnj.pbl.api


import com.pnj.pbl.data.ResponseAttendanceLog
import com.pnj.pbl.data.ResponseAttendanceStatus
import com.pnj.pbl.data.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("api/login")
    fun postLogin(
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<ResponseLogin>

    @GET("api/users/me/attendance-status")
    fun getAttendanceStatus(
        @Header("Authorization") tokenAuth:String?
    ):Call<ResponseAttendanceStatus>

    @GET("api/users/me/attendance-logs")
    fun getAttendanceLogs(
        @Header("Authorization") tokenAuth:String?
    ):Call<ResponseAttendanceLog>
}