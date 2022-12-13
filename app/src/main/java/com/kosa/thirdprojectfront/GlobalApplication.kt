package com.kosa.thirdprojectfront

import android.app.Application
import androidx.annotation.Nullable
import com.kakao.auth.*

/**
 * GlobalApplication
 * @author 신미림, 장주연 *
 * <pre>
수정자                      수정내용
-------------   --------------------------------------------------
신미린, 장주연              최초 생성
 **/

//Kakao SDK를 사용하기 위해서 초기화하는 클래스. GlobalApplication 공유 클래스를 통해 앱 수준에서 관리한다.
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        // Kakao Sdk 초기화
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    // kakaoSDKAdapter 클래스는 KakaoAdapter를 상속
    inner class KakaoSDKAdapter : KakaoAdapter() {
        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun getAuthTypes(): Array<AuthType> {
                    // Kakao SDK로그인을 하는 방식에 대한 Enum class (카카오톡 앱 + 카카오 스토리 + 웹뷰 다이어로그 포함)
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                @Nullable
                override fun getApprovalType(): ApprovalType? {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isSaveFormData(): Boolean {
                    return true
                }
            }
        }

        override fun getApplicationConfig(): IApplicationConfig {
            return IApplicationConfig { getInstance() }
        }
    }

    companion object {
        private var instance: GlobalApplication? = null
        fun getInstance(): Application? {
            checkNotNull(instance) { "this app illegal state" }
            return instance
        }
    }
}