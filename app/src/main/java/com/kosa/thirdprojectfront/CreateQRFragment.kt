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

        binding = FragmentCreateQRBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val imageView = binding.qrcode
        val modifyBtn = binding.modify
        val cancelBtn = binding.cancel
        val text = "2022-12-04"

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