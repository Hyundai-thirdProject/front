package com.kosa.thirdprojectfront

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kosa.thirdprojectfront.databinding.FragmentHomeBinding


//신미림생성
class HomeFragment : Fragment(), View.OnClickListener {

    var activity: MainActivity? = null
    lateinit var binding: FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(layoutInflater, container,false)

        // HomeFragment의 배너
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.logo2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.logo))
        bannerAdapter.addFragment(BannerFragment(R.drawable.logo3))
        bannerAdapter.addFragment(BannerFragment(R.drawable.logo4))
        val viewPager : ViewPager2 = binding.homeBannerVp
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
        binding.loginSignInBtn.setOnClickListener(this)


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
        when(v?.id){

            //더현대
            R.id.login_sign_in_btn ->{
                // 더현대의 좌표값 넘기기
                if((activity as MainActivity).checkDistance()){
                    Log.d("location","좌표 넘어갑니다~")
                    activity?.onFragmentChange(4)
                }else{
                    // 임시로 보내기
                    activity?.onFragmentChange(4)
                    // 모달 띄우기
//                    Toast.makeText(this,"500M 이내에서만 예약할 수 있습니다", Toast.LENGTH_SHORT).show()

                }
            }
            //코엑스
            R.id.login_sign_in_btn ->{

            }
            R.id.login_sign_in_btn ->{

            }
        }

    }

}