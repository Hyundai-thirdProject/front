package com.kosa.thirdprojectfront

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface API {
    @POST("android")
    fun getLoginResponse(@Body user : User) : Call<String>

    @POST("member/join")
    fun getJoinResponse(@Body memberVO: MemberVO ) : Call<String>

    @POST("reservation/insert")
    fun getReservationInsertResponse(@Body reservationVO: ReservationVO) : Call<String>

    @POST("reservation/search")
    fun searchMyReservation(@Body userId : String) : Call<MyReservationVO>

}