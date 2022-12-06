package com.kosa.thirdprojectfront

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("android")
    fun getLoginResponse(@Body user : User) : Call<String>
}