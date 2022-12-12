package com.kosa.thirdprojectfront

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kosa.thirdprojectfront.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//신미림생성
class HomeFragment : Fragment(), View.OnClickListener {

    var activity: MainActivity? = null
    lateinit var binding: FragmentHomeBinding

    var vo: FeedingRoomVO? = null
    var userId : String?=null


    override fun onAttach(context: Context) {
        if (context != null) {
            super.onAttach(context)
            activity = getActivity() as MainActivity?
        }
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    var currentPage: Int = 0

    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
    
        // 메인 액티비티에서 넘어온 번들사용하기
        arguments?.let { 
            userId = it.getString("userId").toString()
        }
        Log.d("homefragment onCreateView", userId+": 아이디")


        // HomeFragment의 배너
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner3))
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner4))
        val viewPager: ViewPager2 = binding.homeBannerVp
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = binding.homeBannerVp.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER


        //뷰페이저 넘기는 쓰레드
        val thread = Thread(PagerRunnable())
        thread.start()

//        binding.loginSignInBtn.setOnClickListener{
//            var a = (activity as MainActivity).checkDistance()
//            Log.d("loginSignInBtn", "checkDistance")
//            activity?.onFragmentChange(4)
//        }
        binding.yeouidoBtn.setOnClickListener(this)
        binding.apgujeongBtn.setOnClickListener(this)
        binding.muyeogsenteoBtn.setOnClickListener(this)







        return binding.root
    }


    //페이지 변경하기
    fun setPage() {
        if (currentPage == 4) currentPage = 0
        binding.homeBannerVp.setCurrentItem(currentPage, true)
        currentPage += 1
    }

    //4초 마다 배너 넘기기
    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(4000)
                handler.sendEmptyMessage(0)
            }
        }
    }



    override fun onClick(v: View?) {

        when (v?.id) {

            // 여의도
            R.id.yeouido_btn -> {
                getPosition(1)
            }
            // 압구정
            R.id.apgujeong_btn -> {
                getPosition(4)
            }
            // 무역센터 코엑스
            R.id.muyeogsenteo_btn -> {
                getPosition(5)
            }
        }
    }

    fun getPosition(fno: Int) {
        val call = RetrofitBuilder.api.getPositionResponse(fno)
        call.enqueue(object : Callback<FeedingRoomVO> { // 비동기 방식 통신 메소드
            override fun onResponse( // 통신에 성공한 경우
                call: Call<FeedingRoomVO>, //  Call같은 경우는 명시적으로 Success / Fail을 나눠서 처리할 수 있음
                response: Response<FeedingRoomVO> //Response 같은 경우는 서버에서 Status Code를 받아서 케이스를 나눠 처리해줄 수 있음
            ) {
                if (response.isSuccessful()) { // 응답 잘 받은 경우
                    //db에서
                    vo=response.body()
                    Log.d("RESPONSE: ", response.body().toString())
                    Log.d("getPosition() ", "위도경도 ${vo?.latitude} + ${vo?.longitude}")
                    Log.d("getPosition() ", "방 개수 ${vo?.room_count}")
                    val check = (activity as MainActivity).checkDistance(vo?.latitude!!.toDouble(), vo?.longitude!!.toDouble());

                    val intent = Intent(context, ReservationActivity::class.java)
                    Log.d("getPosition()","아이디 intent로 넘어왔는지 확인 : "+intent.getStringExtra("userId").toString())
                    intent.putExtra("userId", userId)
                    intent.putExtra("room_count", vo?.room_count)
                    intent.putExtra("fno", vo?.fno)

                    // 일단 false로 뜨는 check 테스트를 위해 true로 처리
                    if(fno==1 && !check){
                        intent.putExtra("depart", binding.yeouidoBtn.text.toString())
                        intent.putExtra("fno2", "2")
                        intent.putExtra("fno3", "3")
                        startActivity(intent)
                    }
                    else if(fno==4 && !check){
                        intent.putExtra("depart", binding.apgujeongBtn.text.toString())
                        startActivity(intent)
                    }
                    else if(fno==5 && !check){
                        intent.putExtra("depart", binding.muyeogsenteoBtn.text.toString())
                        startActivity(intent)
                    }
                    // 거리가 안되면 예약불가능
                    else {
                        val intent = Intent(context, ReservationActivity::class.java)
                        intent.putExtra("depart", binding.muyeogsenteoBtn.text.toString())
                        startActivity(intent)
                    }


                } else {
                    // 통신 성공 but 응답 실패
                    Log.d("RESPONSE", "FAILURE")
                }
            }

            override fun onFailure(call: Call<FeedingRoomVO>, t: Throwable) {
                // 통신에 실패한 경우
                Log.d("CONNECTION FAILURE: ", t.localizedMessage)
            }
        })
    }
}
