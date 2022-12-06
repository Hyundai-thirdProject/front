package com.kosa.thirdprojectfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//신미림 생성
class SignUpActivity : AppCompatActivity() {
      var id: String = ""
      var pw: String = ""
      var phone: String = ""
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_sign_up)

          val memberId = findViewById<EditText>(R.id.join_email)
          val memberPassword = findViewById<EditText>(R.id.join_password)
          val memberPhone = findViewById<EditText>(R.id.join_phone)
          val button = findViewById<AppCompatButton>(R.id.join_button)

          button.setOnClickListener{
              id = memberId.text.toString()
              pw = memberPassword.text.toString()
              phone = memberPhone.text.toString()
              val member = MemberVO()
              member.mid =  memberId.text.toString()
              member.password = memberPassword.text.toString()
              member.phone = memberPhone.text.toString()

              Log.d("BUTTON CLICKED", "id: " + member.mid + ", pw: " + member.password)
              Login(member)
          }
      }
      fun Login(member: MemberVO){
          val call = RetrofitBuilder.api.getJoinResponse(member)
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
      }
}