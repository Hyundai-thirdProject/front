package com.kosa.thirdprojectfront

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kosa.thirdprojectfront.databinding.FragmentCreateQRBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateQRFragment : Fragment(), View.OnClickListener {

    var activity: MainActivity? = null
    private lateinit var binding : FragmentCreateQRBinding

    private lateinit var mid : String
    private lateinit var start_time : String
    private lateinit var floor : String
    private lateinit var department_store : String

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            mid = it.getString("mid").toString()
            start_time = it.getString("start_time").toString()
            floor = it.getString("floor").toString()
            department_store = it.getString("department_store").toString()
        }

        binding = FragmentCreateQRBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val b_branch = binding.branchInfo
        val b_floor = binding.floorInfo
        val b_time = binding.timeInfo

        val imageView = binding.qrcode
        val modifyBtn = binding.modify
        val cancelBtn = binding.cancel

//        b_branch.setText(String(department_store.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8))
        b_branch.setText(department_store)
        if (floor.startsWith("-")) {
            b_floor.setText("지하 " + floor.substring(1) + " 층")
        } else {
            b_floor.setText(floor + " 층")
        }
        b_time.setText(start_time)

        val text = "branch: ${department_store}\n" +
                "start_time: ${start_time}\n" +
                "floor: ${floor}"

        modifyBtn.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imageView.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.modify -> {
                Log.d("ChangeFragment", "FragmentChange 2")
                activity?.onFragmentChangeWithDepartmentStore(0, department_store)
            }
            R.id.cancel -> {
                cancelMyResrvation(mid)
                activity?.onFragmentChange(1)



            }
        }
    }

    fun cancelMyResrvation(mid : String){
        //url 세팅
        val call = RetrofitBuilder.api.cancelMyResrvation(mid)
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


}