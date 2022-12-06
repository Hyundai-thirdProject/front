package com.kosa.thirdprojectfront

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kosa.thirdprojectfront.databinding.FragmentHomeBinding


//신미림생성
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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

        binding.loginSignInBtn.setOnClickListener {
            (activity as MainActivity).checkDistance()
        }

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
}