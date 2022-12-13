package com.kosa.thirdprojectfront

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kosa.thirdprojectfront.databinding.FragmentEmptyQRBinding

/**
 * EmptyQRFragment
 * @author 장주연 *
 * <pre>
수정자                      수정내용
-------------   --------------------------------------------------
장주연              최초 생성
 **/

class EmptyQRFragment : Fragment(), View.OnClickListener {

    var activity: MainActivity? = null
    private lateinit var binding : FragmentEmptyQRBinding

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
        binding = FragmentEmptyQRBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val imageView = binding.empty
        imageView.setImageResource(R.drawable.empty)

        imageView.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.empty -> {
                Log.d("jjy", "EmptyQRFragment")
                activity?.onFragmentChange(2)
            }
        }
    }
}
