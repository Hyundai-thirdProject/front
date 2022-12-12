package com.kosa.thirdprojectfront

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.usermgmt.response.model.Profile
import com.kakao.util.OptionalBoolean
import com.kakao.util.exception.KakaoException


class LoginActivity2 : AppCompatActivity() {

    private lateinit var loginV1: ImageView
    private lateinit var logout: ImageButton
    private val sessionCallback : SessionCallback = SessionCallback()
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginV1 = findViewById(R.id.loginV1)
        logout = findViewById(R.id.logout)

        session = Session.getCurrentSession()
        session.addCallback(sessionCallback)

            loginV1.setOnClickListener(View.OnClickListener { v: View? ->
                if (Session.getCurrentSession().checkAndImplicitOpen()) {
                    Log.d(LoginActivity2.Companion.TAG, "onClick: 로그인 세션살아있음")
                    // 카카오 로그인 시도 (창이 안뜬다.)
                    sessionCallback!!.requestMe()
                } else {
                    Log.d(LoginActivity2.Companion.TAG, "onClick: 로그인 세션끝남")
                    // 카카오 로그인 시도 (창이 뜬다.)
                    session.open(AuthType.KAKAO_LOGIN_ALL, this@LoginActivity2)
                }
            })

            //로그인을 새로할 때 로그인 시도 후 홈 화면으로 보내기 위해 한번더 해당 코드를 넣어줌 - 사용자 정보 받아와야하므로
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                Log.d(LoginActivity2.Companion.TAG, "onClick: 로그인 세션살아있음")
                // 카카오 로그인 시도 (창이 안뜬다.)
                sessionCallback!!.requestMe()
            }

        // 카카오 개발자 홈페이지에 등록할 해시키 구하기
//        getHashKey();
    }

    override fun onDestroy() {
        super.onDestroy()

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "LoginActivity2"
    }

    inner class SessionCallback : ISessionCallback {
        // 로그인에 성공한 상태
        override fun onSessionOpened() {
            requestMe()
        }

        // 로그인에 실패한 상태
        override fun onSessionOpenFailed(exception: KakaoException) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.message)
        }

        // 사용자 정보 요청
        fun requestMe() {
            UserManagement.getInstance()
                .me(object : MeV2ResponseCallback() {
                    override fun onSessionClosed(errorResult: ErrorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: $errorResult")
                    }

                    override fun onFailure(errorResult: ErrorResult) {
                        Log.e("KAKAO_API", "사용자 정보 요청 실패: $errorResult")
                    }

                    override fun onSuccess(result: MeV2Response) {
                        Log.i("KAKAO_API", "사용자 아이디: " + result.id)
                        val id = result.id.toString()
                        val kakaoAccount = result.kakaoAccount
                        if (kakaoAccount != null) {

                            // 이메일
                            val email = kakaoAccount.email
                            val profile: Profile? = kakaoAccount.profile

                            // 로그인이 성공했을 때
                            val sharedPreference = getSharedPreferences("user", MODE_PRIVATE)
                            val editor  : SharedPreferences.Editor = sharedPreference.edit()
                            editor.putString("userId", email)

                            editor.commit() // data 저장!

                            var intent = Intent(this@LoginActivity2, MainActivity::class.java)
//                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()

                            if (profile == null) {
                                Log.d("KAKAO_API", "onSuccess:profile null ")
                            } else {
                                Log.d(
                                    "KAKAO_API",
                                    "onSuccess:getProfileImageUrl " + profile.getProfileImageUrl()
                                )
                                Log.d("KAKAO_API", "onSuccess:getNickname " + profile.getNickname())
                            }
                            if (email != null) {
                                Log.d("KAKAO_API", "onSuccess:email $email")
                            } else if (kakaoAccount.emailNeedsAgreement() == OptionalBoolean.TRUE) {
                                // 동의 요청 후 이메일 획득 가능
                                // 단, 선택 동의로 설정되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야 합니다.
                                Log.d("KAKAO_API", "onSuccess: 동의 요청 후 이메일 획득 가능")
                            } else {
                                // 이메일 획득 불가
                                Log.d("KAKAO_API", "onSuccess: cannot get email")
                            }

                            // 프로필
                            val _profile: Profile? = kakaoAccount.profile
                            if (_profile != null) {
                                Log.d("KAKAO_API", "nickname: " + _profile.getNickname())
                                Log.d("KAKAO_API", "profile image: " + _profile.getProfileImageUrl())
                                Log.d(
                                    "KAKAO_API",
                                    "thumbnail image: " + _profile.getThumbnailImageUrl()
                                )
                            } else if (kakaoAccount.profileNeedsAgreement() == OptionalBoolean.TRUE) {
                                // 동의 요청 후 프로필 정보 획득 가능
                            } else {
                                // 프로필 획득 불가
                            }
                        } else {
                            Log.i("KAKAO_API", "onSuccess: kakaoAccount null")
                        }
                    }
                })
        }
    }
}