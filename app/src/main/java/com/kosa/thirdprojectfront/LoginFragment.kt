package com.kosa.thirdprojectfront

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.kosa.thirdprojectfront.databinding.FragmentCreateQRBinding
import com.kosa.thirdprojectfront.databinding.FragmentHomeBinding
import com.kosa.thirdprojectfront.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//신미림생성
class LoginFragment : Fragment(), View.OnClickListener {

    var activity: MainActivity? = null
    private lateinit var binding : FragmentLoginBinding
    var id: String = ""
    var pw: String = ""

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val button = binding.loginSignInBtn
        val userId = binding.loginIdEt
        val userPassword = binding.loginPasswordEt

        button.setOnClickListener(this)

//        id = userId.text.toString()
//        pw = userPassword.text.toString()
//        val user = User()
//        user.id = userId.text.toString()
//        user.password = userPassword.text.toString()

//        Log.d("BUTTON CLICKED", "id: " + user.id + ", pw: " + user.password)
//        Login(user)
        // Inflate the layout for this fragment
        return view

    }

    fun Login(user: User) {
        val call = RetrofitBuilder.api.getLoginResponse(user)
        call.enqueue(object : Callback<String> { // 비동기 방식 통신 메소드
            override fun onResponse( // 통신에 성공한 경우
                call: Call<String>,
                response: Response<String>
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_sign_in_btn -> {
                id = binding.loginIdEt.text.toString()
                pw = binding.loginPasswordEt.text.toString()
                val user = User()
                user.id = id
                user.password = pw
                activity?.onFragmentChange(3)

                Log.d("BUTTON CLICKED", "id: " + user.id + ", pw: " + user.password)
                Login(user)
            }
        }
    }


}