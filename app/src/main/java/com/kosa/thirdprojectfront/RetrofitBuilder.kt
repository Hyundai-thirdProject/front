package com.kosa.thirdprojectfront

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    var api : API
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.9:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(API::class.java)
    }
}