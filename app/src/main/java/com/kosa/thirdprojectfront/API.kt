package com.kosa.thirdprojectfront

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * API
 * @author 신미림, 장주연 *
 * <pre>
수정자                      수정내용
--------------   --------------------------------------------------
 신미림, 장주연              최초 생성
 신미림, 장주연              android, member/join
 신미림                     reservation/select, reservation/cancel
 장주연                     reservation/search, reservation/modify
 김민찬                     feedingroom/position
 **/

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

    @POST("feedingroom/position")
    fun getPositionResponse(@Body fno : Int) : Call<FeedingRoomVO>

    @POST("reservation/check")
    fun getCheckReservation(@Body userId : String) : Call<String>



}