package com.kosa.thirdprojectfront

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kosa.thirdprojectfront.databinding.ActivityReservationBinding

class ReservationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExpand.setOnClickListener {
            if (binding.expandLayout.visibility == View.VISIBLE) {
                binding.expandLayout.visibility = View.GONE
            } else {
                binding.expandLayout.visibility = View.VISIBLE
            }
        }
    }
}