package com.kosa.thirdprojectfront

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kosa.thirdprojectfront.databinding.ActivityReservationBinding
import com.kosa.thirdprojectfront.databinding.ActivityReservationModifyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationModifyActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityReservationModifyBinding
    lateinit var userId : String
    lateinit var department_store : String

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        //지점 가져오기와서 textview에 넣기
        val secondIntent = intent
        val departText = secondIntent.getStringExtra("depart")
        val selecteddepart: TextView = binding.selecteddepart//지점 화면에 띄워줌
        selecteddepart.setText(departText)//지점 화면에 띄워줌

        // 시간 선택한 내용 띄우기
        for (i in 0 until numButtons.size) {
            numButtons[i] = findViewById<View>(numBtnIDs[i]) as Button

        }

        for (i in 0 until numButtons.size) {
            numButtons[i]!!.setOnClickListener {
                selectedtime.setText(
                    numButtons[i]?.text.toString()
                ) //버튼 번호를 받아와 띄움

                val time = ReservationVO()
                val btntext = numButtons[i]?.text.toString()
                time.startTime = btntext

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
        for (i in 0 until 2) {
            floorButtons[i]!!.visibility = View.VISIBLE
            // 여기에 button text의 내용을 데이터에서 꺼내와서 넣기
            // 여기에 image src을 데이터에서 꺼내와서 넣기
        }


        // 내가 클릭하지 않은 지도 다 접어
        for (i in 0 until floorButtons.size) {
            floorButtons[i]!!.setOnClickListener {
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

        val fragment_mypage = CreateQRFragment()

        binding.reservation.setOnClickListener {
            val modifyIntent = intent
            userId = intent.getStringExtra("userId").toString()
            department_store = intent.getStringExtra("depart").toString()

            val reservationVO = ReservationVO()
            reservationVO.mid = userId
            reservationVO.fno = 2
            reservationVO.startTime = selectedtime.text.toString()
            Log.d("예약 시작 시간", selectedtime.text.toString())
            reservationVO.endTime = selectedtime.text.toString()
            reservationVO.status = 0
            ReservationModify(reservationVO)

            //첫번째 방법

//            val nextIntent = Intent(this, MainActivity::class.java)
//            startActivity(nextIntent)

            //if 객체가 있으면 fragment로 가고
            // 없으면 홈으로 가세영
        }
    }

    fun ReservationModify(reservationVO: ReservationVO) {
        //url 세팅
        val call = RetrofitBuilder.api.modifyResrevation(reservationVO)
        call.enqueue(object : Callback<String> { // 비동기 방식 통신 메소드
            override fun onResponse( // 통신에 성공한 경우
                call: Call<String>, //  Call같은 경우는 명시적으로 Success / Fail을 나눠서 처리할 수 있음
                response: Response<String> //Response 같은 경우는 서버에서 Status Code를 받아서 케이스를 나눠 처리해줄 수 있음
            ) {
                if (response.isSuccessful()) { // 응답 잘 받은 경우
                    Log.d("RESPONSE: ", response.body().toString())
                    val nextIntent = Intent(this@ReservationModifyActivity2, MainActivity::class.java)
                    startActivity(nextIntent)
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