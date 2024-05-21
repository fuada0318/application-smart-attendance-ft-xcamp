package com.pnj.pbl.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient {

    private val BASE_URL = "https://eyecatching-image-ghhipha43a-uc.a.run.app/"

    private fun getRetrofitClient(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getDataUser(): ApiLogin{
        return getRetrofitClient().create(ApiLogin::class.java)
    }
//    val loginService: ApiClient by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        retrofit.create(ApiClient::class.java)
//    }

//    private fun getRetrofitClient(): Retrofit{
//        return Retrofit.Builder()
//            .baseUrl("https://eyecatching-image-ghhipha43a-uc.a.run.app/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    fun getInstance(): ApiLogin{
//        return getRetrofitClient().create(ApiLogin::class.java)
//    }
}