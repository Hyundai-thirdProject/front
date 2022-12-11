package com.kosa.thirdprojectfront

import android.content.Context
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
                activity?.onFragmentChange(0)
            }
            R.id.cancel -> {
                activity?.onFragmentChange(1)
            }
        }
    }
}