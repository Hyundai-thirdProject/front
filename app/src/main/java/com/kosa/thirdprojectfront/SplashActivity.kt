package com.kosa.thirdprojectfront

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
/**
 * LoginActivity
 * @author 김민선*
 * <pre>
수정자                      수정내용
-------------   --------------------------------------------------
김민선           최초 생성
 **/

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this,LoginActivity2::class.java))
            finish()
        }, 3000)
    }
}