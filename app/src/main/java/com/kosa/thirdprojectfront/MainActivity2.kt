package com.kosa.thirdprojectfront

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kosa.thirdprojectfront.databinding.ActivityMain2Binding


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val createQRBtn = binding.createQR
        val emptyQRBtn = binding.emptyQR
        val scanQRBtn = binding.scanQR

        scanQRBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@MainActivity2, ScanQR::class.java)
                startActivity(intent)
            }
        })
    }
}