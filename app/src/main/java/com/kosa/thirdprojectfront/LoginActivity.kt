package com.kosa.thirdprojectfront

import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.pm.PackageInstaller
import android.os.Bundle
import com.kosa.thirdprojectfront.R
import android.view.View
import android.util.Log
import com.kakao.auth.AuthType
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.network.ErrorResult
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Base64
import com.kakao.auth.Session
import com.kakao.util.helper.Utility
import com.kosa.thirdprojectfront.SessionCallback
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    private lateinit var loginV1: Button
    private lateinit var logout: Button
    //private val sessionCallback: PackageInstaller.SessionCallback? = PackageInstaller.SessionCallback
    private val sessionCallback : SessionCallback = SessionCallback()
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginV1 = findViewById(R.id.loginV1)
        logout = findViewById(R.id.logout)

        session = Session.getCurrentSession()
        Log.d("fkfkfkfk", session.toString())
        session.addCallback(sessionCallback)

        loginV1.setOnClickListener(View.OnClickListener { v: View? ->
            Log.d("asdfasdf", session.isOpenable.toString())
            if (session.checkAndImplicitOpen()) {
                Log.d(TAG, "onClick: 로그인 세션살아있음")
                // 카카오 로그인 시도 (창이 안뜬다.)
                sessionCallback.requestMe()
            } else {
                Log.d(TAG, "onClick: 로그인 세션끝남")
                // 카카오 로그인 시도 (창이 뜬다.)

                session.open(AuthType.KAKAO_TALK, this@LoginActivity)
                Log.d("afafaf", session.toString())
            }
        })
        logout.setOnClickListener(View.OnClickListener { v: View? ->
            Log.d(TAG, "onCreate:click ")
            UserManagement.getInstance()
                .requestLogout(object : LogoutResponseCallback() {
                    override fun onSessionClosed(errorResult: ErrorResult) {
                        super.onSessionClosed(errorResult)
                        Log.d(TAG, "onSessionClosed: " + errorResult.errorMessage)
                    }

                    override fun onCompleteLogout() {
                        if (sessionCallback != null) {
                            Session.getCurrentSession().removeCallback(sessionCallback)
                        }
                        Log.d(TAG, "onCompleteLogout:logout ")
                    }
                })
        })

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

    private val hashKey: Unit
        private get() {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
            }
            if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
            for (signature in packageInfo!!.signatures) {
                try {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                } catch (e: NoSuchAlgorithmException) {
                    Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
                }
            }
        }

    companion object {
        private const val TAG = "LoginActivity"
    }
}