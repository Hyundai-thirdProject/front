package com.kosa.thirdprojectfront

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitBuilder
 * @author 신미림, 장주연 *
 * <pre>
수정자                      수정내용
-------------   --------------------------------------------------
신미림, 장주연              최초 생성
 **/

object RetrofitBuilder {
    var api : API
    var gson = GsonBuilder().setLenient().create()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.12:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        api = retrofit.create(API::class.java)
    }
}
