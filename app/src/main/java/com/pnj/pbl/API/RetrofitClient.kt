package com.pnj.pbl.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    private val BASE_URL = "https://eyecatching-image-ghhipha43a-uc.a.run.app/"

    private fun getRetrofitClient(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getDataAPI(): ApiClient{
        return getRetrofitClient().create(ApiClient::class.java)
    }

}