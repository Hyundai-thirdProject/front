package com.kosa.thirdprojectfront

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kosa.thirdprojectfront.databinding.ActivityReservationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.annotation.SuppressLint
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.properties.Delegates

/**
 * ReservationActivity
 * @author 김민선 *
 * <pre>
수정자                      수정내용
-------------   --------------------------------------------------
김민선            최초 생성, 예약 insert
신미림           예약 read
 **/
class ReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationBinding
    private lateinit var branchInfo : List<FeedingReservationVO>

    lateinit var userId : String
    lateinit var reservationVO : ReservationVO

    val numBtnIDs: Array<Int> = arrayOf(
        R.id.btnTime1,
        R.id.btnTime2,
        R.id.btnTime3,
        R.id.btnTime4,
        R.id.btnTime5,
        R.id.btnTime6,
        R.id.btnTime7,
        R.id.btnTime8,
        R.id.btnTime9,
        R.id.btnTime10,
        R.id.btnTime11,
        R.id.btnTime12,
        R.id.btnTime13,
        R.id.btnTime14,
        R.id.btnTime15,
        R.id.btnTime16,
        R.id.btnTime17,
        R.id.btnTime18,
        R.id.btnTime19,
        R.id.btnTime20
    )

    val floorBtnIDs: Array<Int> = arrayOf(
        R.id.btn_expand1,
        R.id.btn_expand2,
        R.id.btn_expand3
    )
    val expandlayoutIDs: Array<Int> = arrayOf(
        R.id.expand_layout1,
        R.id.expand_layout2,
        R.id.expand_layout3
    )


    var numButtons: Array<Button?> = arrayOfNulls<Button>(20)
    var floorButtons: Array<Button?> = arrayOfNulls<Button>(3)
    var expandlayouts: Array<LinearLayout?> = arrayOfNulls<LinearLayout>(3)
    var fnos: ArrayList<Int> = arrayListOf()
    var finalfno by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
        userId = sharedPreference.getString("userId", "").toString()


        // 안보이게 하는 버튼관련 구현
        binding.btnHidden1.setVisibility(View.INVISIBLE);
        binding.btnHidden2.setVisibility(View.INVISIBLE);
        binding.btnHidden3.setVisibility(View.INVISIBLE);
        binding.btnHidden4.setVisibility(View.INVISIBLE);
        binding.btnExpand1.setVisibility(View.INVISIBLE);
        binding.btnExpand2.setVisibility(View.INVISIBLE);
        binding.btnExpand3.setVisibility(View.INVISIBLE);

        // 클릭한 내용 들고오기
        val selectedtime: TextView = binding.selectedtime
        val selectedfloor: TextView = binding.selectedfloor

        //  지점 선택시 넘겨준 값 받아오기
        val secondIntent = intent
        val departText = secondIntent.getStringExtra("depart")
        val selecteddepart: TextView = binding.selecteddepart//지점 화면에 띄워줌
        selecteddepart.setText(departText)//지점 화면에 띄워줌
        
        val room_count = secondIntent.getStringExtra("room_count")

        val fno = secondIntent.getStringExtra("fno")
        val fno2 = secondIntent.getStringExtra("fno2")
        val fno3 = secondIntent.getStringExtra("fno3")

        val int_room_count: Int = room_count!!.toInt()

        //  fno arraylist에 추가
        if (int_room_count == 1) {
            fnos.add(fno!!.toInt())
        }else{
            fnos.add(fno!!.toInt())
            fnos.add(fno2!!.toInt())
            fnos.add(fno3!!.toInt())
        }

        // 시간 선택한 내용 띄우기
        for (i in 0 until numButtons.size) {
            numButtons[i] = findViewById<View>(numBtnIDs[i]) as Button
        }

        for (i in 0 until numButtons.size) {
            numButtons[i]!!.setOnClickListener {
                selectedtime.setText(
                    numButtons[i]?.text.toString()
                ) //버튼 번호를 받아와 띄움

                val feedingReservationVO = FeedingReservationVO()
                //선택한 시간을 보내줌
                val btntext = numButtons[i]?.text.toString()

                val depart = departText.toString()
                var branch = ""

                feedingReservationVO.start_time = btntext
                if (depart == "더현대 여의도점") {
                    branch = "yeoido"
                } else if (depart == "압구정본점") {
                    branch = "apgujeong"
                } else if (depart == "무역센터점") {
                    branch = "muyeogsenteo"
                }
                feedingReservationVO.department_store = branch
                Log.d("min2",feedingReservationVO.toString())
                getReservationSelectResponse(feedingReservationVO)
            }
        }


        // 층 선택한 내용 띄우기
        for (i in 0 until floorButtons.size) {
            floorButtons[i] = findViewById<View>(floorBtnIDs[i]) as Button
        }

        for (i in 0 until expandlayouts.size) {
            expandlayouts[i] = findViewById<View>(expandlayoutIDs[i]) as LinearLayout
            expandlayouts[i]!!.visibility = View.GONE
        }


        // 층 버튼 숫자에 만큼 만들기
        for (i in 0 until int_room_count) {
            floorButtons[i]!!.visibility = View.VISIBLE
            // 여기에 button text의 내용을 데이터에서 꺼내와서 넣기
            // 여기에 image src을 데이터에서 꺼내와서 넣기
        }

                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)



        // 내가 클릭하지 않은 지도 다 접어
        for (i in 0 until floorButtons.size) {
            floorButtons[i]!!.setOnClickListener {

                finalfno = fnos[i]
                if (expandlayouts[i]!!.visibility == View.VISIBLE) {
                    expandlayouts[i]!!.visibility = View.GONE
                } else {
                    expandlayouts[i]!!.visibility = View.VISIBLE
                    selectedfloor.setText(
                        floorButtons[i]?.text.toString()
                    ) //버튼 번호를 받아와 띄움

                    for (j in 0 until floorButtons.size) {
                        if (j != i) {
                            expandlayouts[j]!!.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.reservation.setOnClickListener {
            // 칸이 안채워져있으면 insert 못함
            if (selecteddepart.text.toString().length == 0) {
                Toast.makeText(this, "지점을 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedfloor.text.toString().length == 0) {
                Toast.makeText(this, "층을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedtime.text.toString().length == 0) {
                Toast.makeText(this, "시간을 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 예약 객체를 만들어서 필요한 정보 넘겨주기
            reservationVO = ReservationVO()
            reservationVO.mid = userId
            Log.d("예약 시 사용자 아이디", userId.toString())

            reservationVO.fno = finalfno
            reservationVO.startTime = selectedtime.text.toString()
            Log.d("예약 시작 시간", selectedtime.text.toString())

            // 예약 시작 시간 선택시 예약 끝나는 시간 자동 설정
            var strendtiem = selectedtime.text.toString() + ":00"
            var time = LocalTime.parse(strendtiem)
            time = time.plusMinutes(30)
            Log.d("endtime", time.toString())
            reservationVO.endTime =time.toString()
            reservationVO.status = 0

            // 예약중복체크
            getCheckReservation(userId)

            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        }
    }



    fun getReservationSelectResponse(feedingReservationVO: FeedingReservationVO) {
        //url 세팅
        Log.d("feedingReservationVO", feedingReservationVO.department_store.toString())
        val call = RetrofitBuilder.api.getReservationSelectResponse(
            feedingReservationVO.department_store.toString(),
            feedingReservationVO.start_time.toString()
        )

            for(k in 0 until 3){
                if(k==1){
                    floorButtons[k]?.setBackgroundColor(getColor(R.color.xmasblue))
                }else{
                    floorButtons[k]?.setBackgroundColor(getColor(R.color.xmasgreen))
                }
                floorButtons[k]?.setClickable(true);
            }



        Log.d("feedingReservationVO", feedingReservationVO.start_time.toString())
        Thread {
            call.enqueue(object : Callback<List<FeedingReservationVO>> { // 비동기 방식 통신 메소드
                @SuppressLint("ResourceAsColor")
                override fun onResponse( // 통신에 성공한 경우
                    call: Call<List<FeedingReservationVO>>, //  Call같은 경우는 명시적으로 Success / Fail을 나눠서 처리할 수 있음
                    response: Response<List<FeedingReservationVO>> //Response 같은 경우는 서버에서 Status Code를 받아서 케이스를 나눠 처리해줄 수 있음
                ) {
                    if (response.isSuccessful()) { // 응답 잘 받은 경우
                        val reserv : List<FeedingReservationVO>? = response.body()
                        var room_count :Int
                        var floor : Int
                        var user_count : Int



                        Log.d("min",reserv.toString())
                        Log.d("min","${reserv?.count()}")
                        Log.d("min","${reserv?.size}")
                        if (reserv != null) {
                            for (i in 0 until reserv.count()) {
                                Log.d("RESPONSE: ", reserv[i].toString())
                                Log.d("room_count", reserv[i]?.room_count.toString())
                                Log.d("floor", reserv[i]?.floor.toString())

                                room_count = reserv[i]?.room_count!!
                                floor = reserv[i]?.floor!!
                                user_count = reserv[i]?.use_count!!



                                var floorK =floor.toString()+"층"
                                var floorB = "지하 "+floorK
                                var idx =0
                                for(j in 0 until 3 ){
                                    if(floorK.equals(floorButtons[j]?.text)
                                        || floorB.equals(floorButtons[j]?.text)){
                                        idx=j
                                    }
                                }


//                                for ( i in 0 until floorButtons.size) {
//                                    Log.d("asdfasdf",item.use_count.toString() )
//                                    Log.d("qwerqwer",item.room_count.toString() )
//                                    Log.d("123123", item.floor.toString())
                                if(reserv[i].use_count == reserv[i].room_count){// 각층의 인덱스 값을 비교함
                                    Log.d("user_countC",reserv[i].use_count.toString() )
                                    Log.d("room_countC",reserv[i].room_count.toString() )
                                    floorButtons[idx]?.setBackgroundColor(getColor(R.color.colorGray))
                                    floorButtons[idx]?.setClickable(false);
                                    Log.d("floorButtons[0]", floorButtons[i]?.text.toString())
                                }

                                //}


                            }
                        }



                    } else {
                        // 통신 성공 but 응답 실패
                        Log.d("RESPONSE", "FAILURE")
                    }
                }

                override fun onFailure(call: Call<List<FeedingReservationVO>>, t: Throwable) {
                    // 통신에 실패한 경우
                    Log.d("CONNECTION FAILURE: ", t.localizedMessage)
                }
            })
            try {
                Thread.sleep(50)
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }.start()


    }

    fun getCheckReservation(userId: String) {
        //url 세팅
        val call = RetrofitBuilder.api.getCheckReservation(userId)
        call.enqueue(object : Callback<String> { // 비동기 방식 통신 메소드
            override fun onResponse( // 통신에 성공한 경우
                call: Call<String>, //  Call같은 경우는 명시적으로 Success / Fail을 나눠서 처리할 수 있음
                response: Response<String> //Response 같은 경우는 서버에서 Status Code를 받아서 케이스를 나눠 처리해줄 수 있음
            ) {
                if (response.isSuccessful()) { // 응답 잘 받은 경우
                    Log.d("RESPONSE: ", response.body().toString())
                    var check_result = response.body().toString()
                    if(check_result.equals("success")){
                        ReservationInsert(reservationVO)
                    }else{
                        Toast.makeText(this@ReservationActivity, "중복예약 할 수 없습니다", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // 통신 성공 but 응답 실패
                    Log.d("RESPONSE", "FAILURE")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // 통신에 실패한 경우
                Log.d("CONNECTION FAILURE: ", t.localizedMessage)
            }
        })

    }

}



fun ReservationInsert(reservationVO: ReservationVO) {
    //url 세팅
    val call = RetrofitBuilder.api.getReservationInsertResponse(reservationVO)
    call.enqueue(object : Callback<String> { // 비동기 방식 통신 메소드
        override fun onResponse( // 통신에 성공한 경우
            call: Call<String>, //  Call같은 경우는 명시적으로 Success / Fail을 나눠서 처리할 수 있음
            response: Response<String> //Response 같은 경우는 서버에서 Status Code를 받아서 케이스를 나눠 처리해줄 수 있음
        ) {
            if (response.isSuccessful()) { // 응답 잘 받은 경우
                Log.d("RESPONSE: ", response.body().toString())

            } else {
                // 통신 성공 but 응답 실패
                Log.d("RESPONSE", "FAILURE")
            }
        }

        override fun onFailure(call: Call<String>, t: Throwable) {
            // 통신에 실패한 경우
            Log.d("CONNECTION FAILURE: ", t.localizedMessage)
        }
    })
}

