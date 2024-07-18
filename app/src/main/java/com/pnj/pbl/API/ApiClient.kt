package com.pnj.pbl.api


import com.pnj.pbl.data.ResponseAttendanceFiltered
import com.pnj.pbl.data.ResponseAttendanceLog
import com.pnj.pbl.data.ResponseAttendanceStatus
import com.pnj.pbl.data.ResponseLogin
import com.pnj.pbl.data.ResponseUpdateProfile
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiClient {
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

    @GET("api/users/me/attendance-logs/")
    fun getAttendanceFiltered(
        @Header("Authorization") tokenAuth:String?,
        @Query("month") month : Int
    ):Call<ResponseAttendanceFiltered>

    @Multipart
    @PUT("api/users/me/profile-picture")
    fun updatePP(
        @Header("Authorization") tokenAuth: String?,
        @Part profile_pict : MultipartBody.Part
    ):Call<ResponseUpdateProfile>
}