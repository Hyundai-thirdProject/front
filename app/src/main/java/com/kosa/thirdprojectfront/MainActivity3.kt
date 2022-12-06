package com.kosa.thirdprojectfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity3 : AppCompatActivity() {

/*    var id: String = ""
    var pw: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        val button = findViewById<AppCompatButton>(R.id.login_sign_in_btn)
        val userId = findViewById<EditText>(R.id.login_id_et)
        val userPassword = findViewById<EditText>(R.id.login_password_et)

        button.setOnClickListener{
            id = userId.text.toString()
            pw = userPassword.text.toString()
            val user = User()
            user.id = userId.text.toString()
            user.password = userPassword.text.toString()

            Log.d("BUTTON CLICKED", "id: " + user.id + ", pw: " + user.password)
            Login(user)
        }
    }
    fun Login(user: User){
        val call = RetrofitBuilder.api.getLoginResponse(user)
        call.enqueue(object : Callback<String> { // 비동기 방식 통신 메소드
            override fun onResponse( // 통신에 성공한 경우
                call: Call<String>,
                response: Response<String>
            ) {
                if(response.isSuccessful()){ // 응답 잘 받은 경우
                    Log.d("RESPONSE: ", response.body().toString())

                }else{
                    // 통신 성공 but 응답 실패
                    Log.d("RESPONSE", "FAILURE")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // 통신에 실패한 경우
                Log.d("CONNECTION FAILURE: ", t.localizedMessage)
            }
        })
    }*/
}