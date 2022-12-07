package com.kosa.thirdprojectfront

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kosa.thirdprojectfront.databinding.FragmentCreateQRBinding
import com.kosa.thirdprojectfront.databinding.FragmentReservationBinding


class ReservationFragment : Fragment(), View.OnClickListener {

    var activity : MainActivity?=null
    private  lateinit var binding : FragmentReservationBinding

    //onCreate이전에 실행되는 Attach 와 Detach
    override fun onAttach(context: Context) {
        if (context != null){
            super.onAttach(context)
            activity = getActivity() as MainActivity?
        }
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentReservationBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        // 안보이게 하는 버튼관련 구현
        binding.btnHidden1.setVisibility(View.INVISIBLE);
        binding.btnHidden2.setVisibility(View.INVISIBLE);
        binding.btnHidden3.setVisibility(View.INVISIBLE);
        binding.btnHidden4.setVisibility(View.INVISIBLE);

        // 층 지도 관련 구현


        // 클릭한 내용 들고오기
        val selecteddepart: TextView = binding.selecteddepart
        val selectedtime: TextView = binding.selectedtime
        val selectedfloor: TextView = binding.selectedfloor


        // 시간 선택한 내용 띄우기
        for (i in 0 until numButtons.size) {
            numButtons[i] = view.findViewById<View>(numBtnIDs[i]) as Button
        }

        for (i in 0 until numButtons.size) {
            numButtons[i]!!.setOnClickListener {
                selectedtime.setText(
                    numButtons[i]?.text.toString()
                ) //버튼 번호를 받아와 띄움


            }
        }

        // 층 선택한 내용 띄우기
        for (i in 0 until floorButtons.size) {
            floorButtons[i] = view.findViewById<View>(floorBtnIDs[i]) as Button
        }

        for (i in 0 until expandlayouts.size) {
            expandlayouts[i] = view.findViewById<View>(expandlayoutIDs[i]) as LinearLayout
        }


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

    return view
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}

