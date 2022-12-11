package com.kosa.thirdprojectfront

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface API {
    @POST("android")
    fun getLoginResponse(@Body user : User) : Call<String>

    @POST("member/join")
    fun getJoinResponse(@Body memberVO: MemberVO ) : Call<String>

    @POST("reservation/insert")
    fun getReservationInsertResponse(@Body reservationVO: ReservationVO) : Call<String>

    @POST("reservation/search")
    fun searchMyReservation(@Body userId : String) : Call<MyReservationVO>

    @POST("reservation/modify")
    fun modifyResrevation(@Body reservationVO: ReservationVO) : Call<String>


    @GET("reservation/select")// 쿼리 혹은 path
    fun getReservationSelectResponse(@Query("department_store") department_store : String
                                     , @Query("start_time") start_time: String)
            : Call<List<FeedingReservationVO>>

    @POST("reservation/cancel")
    fun cancelMyResrvation(@Body userId : String) : Call<String>
}