package com.kosa.thirdprojectfront

import android.app.Activity
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
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.auth.Session
import com.kakao.util.helper.Utility
import com.kosa.thirdprojectfront.SessionCallback
import com.kosa.thirdprojectfront.databinding.ActivityLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.api.NidProfileApi
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginV1: ImageView
    private lateinit var logout: Button
    private val sessionCallback : SessionCallback = SessionCallback()
    private lateinit var session: Session
    private lateinit var loginV2: ImageView
    private lateinit var btnSignIn: ImageView

    private var email: String = ""
    private var gender: String = ""
    private var name: String = ""

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResultSignUp()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()



//            btnSignOut.setOnClickListener {
//                signOut()
//            }
//            btnGetProfile.setOnClickListener {
//                GetCurrentUserProfile()
//            }

        setContentView(R.layout.activity_login)


        loginV1 = findViewById(R.id.loginV1)
        logout = findViewById(R.id.logout)
        loginV2 = findViewById(R.id.tv_naver_login)

        session = Session.getCurrentSession()
        Log.d("fkfkfkfk", session.toString())
        session.addCallback(sessionCallback)
        btnSignIn = findViewById(R.id.btnSignIn)

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignIn.setOnClickListener {
            signIn()
        }

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

        loginV2.setOnClickListener {
            val oAuthLoginCallback = object : OAuthLoginCallback {
                override fun onSuccess() {
                    // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                        override fun onSuccess(result: NidProfileResponse) {
                            name = result.profile?.name.toString()
                            email = result.profile?.email.toString()
                            gender = result.profile?.gender.toString()
                            Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                            Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                            Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
                        }
                        override fun onError(errorCode: Int, message: String) {
                            //
                        }
                        override fun onFailure(httpStatus: Int, message: String) {
                            //
                        }
                    })
                }

                override fun onError(errorCode: Int, message: String) {
                    val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                    Log.e(TAG, "naverAccessToken : $naverAccessToken")
                }
                override fun onFailure(httpStatus: Int, message: String) {
                    //
                }
            }

            NaverIdLoginSDK.initialize(this@LoginActivity, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "앱 이름")
            NaverIdLoginSDK.authenticate(this@LoginActivity, oAuthLoginCallback)
        }

        logout.setOnClickListener(View.OnClickListener { v: View? ->
            Log.d(TAG, "onCreate:click ")

            startNaverLogout()


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
    /**
     * 로그아웃
     * 애플리케이션에서 로그아웃할 때는 다음과 같이 NaverIdLoginSDK.logout() 메서드를 호출합니다. */
    private fun startNaverLogout(){
        NaverIdLoginSDK.logout()
        //setLayoutState(false)
        Toast.makeText(this@LoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        resultLauncher.launch(signInIntent)
    }


    private fun setResultSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // 정상적으로 결과가 받아와진다면 조건문 실행
                if (result.resultCode == Activity.RESULT_OK) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleSignInResult(task)

                }
            }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()
            val photoUrl = account?.photoUrl.toString()

            Log.d("로그인한 유저의 이메일", email)
            Log.d("로그인한 유저의 성", familyName)
            Log.d("로그인한 유저의 이름", givenName)
            Log.d("로그인한 유저의 전체이름", displayName)
            Log.d("로그인한 유저의 프로필 사진의 주소", photoUrl)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }



}