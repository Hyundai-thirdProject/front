package com.kosa.thirdprojectfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//신미림 생성
class SignUpActivity : AppCompatActivity() {
      var id: String = ""
      var pw: String = ""
      var phone: String = ""
      var check : Boolean = false

      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_sign_up)

          val memberId = findViewById<EditText>(R.id.join_email)
          val memberPassword = findViewById<EditText>(R.id.join_password)
          val memberPhone = findViewById<EditText>(R.id.join_phone)
          val button = findViewById<AppCompatButton>(R.id.join_button)
          val joinpwck = findViewById<EditText>(R.id.join_pwck)
          val joincheck = findViewById<TextView>(R.id.joi_check)

          //비밀번호 일치 여부 확인하기
          joinpwck.addTextChangedListener(object : TextWatcher {
              // EditText에 문자 입력 전
              override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
              // EditText에 변화가 있을 경우
              override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

              // EditText 입력이 끝난 후
              override fun afterTextChanged(p0: Editable?) {
                  if (memberPassword.getText().toString().equals(joinpwck.getText().toString())) {
                      joincheck.setTextColor(getColor(R.color.background))
                  } else {
                      joincheck.setText("비밀번호가 일치하지 않습니다")
                      joincheck.setTextColor(getColor(R.color.red))
                  }
              }
          })

          button.setOnClickListener{
              id = memberId.text.toString()
              pw = memberPassword.text.toString()
              phone = memberPhone.text.toString()
              val member = MemberVO()
              member.mid =  memberId.text.toString()
              member.password = memberPassword.text.toString()
              member.phone = memberPhone.text.toString()

              if (memberId.text.toString().length == 0) {
                  Toast.makeText(this, "ID를 입력하세요.", Toast.LENGTH_SHORT).show()
                  return@setOnClickListener
              }
              if (memberPassword.text.toString().length == 0) {
                  Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                  return@setOnClickListener
              }
              if (memberPhone.text.toString().length == 0) {
                  Toast.makeText(this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                  return@setOnClickListener
              }
              if (!memberPassword.getText().toString().equals(joinpwck.getText().toString())) {
                  Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                  return@setOnClickListener
              }
              Log.d("BUTTON CLICKED", "id: " + member.mid + ", pw: " + member.password)
              Login(member)
          }


      }
      fun Login(member: MemberVO){
          //url 세팅
          val call = RetrofitBuilder.api.getJoinResponse(member)
          call.enqueue(object : Callback<String> { // 비동기 방식 통신 메소드
              override fun onResponse( // 통신에 성공한 경우
                  call: Call<String>, //  Call같은 경우는 명시적으로 Success / Fail을 나눠서 처리할 수 있음
                  response: Response<String> //Response 같은 경우는 서버에서 Status Code를 받아서 케이스를 나눠 처리해줄 수 있음
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