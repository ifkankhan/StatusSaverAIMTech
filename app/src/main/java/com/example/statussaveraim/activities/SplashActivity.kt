package com.example.statussaveraim.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.statussaveraim.R
import com.example.statussaveraim.databinding.ActivitySpalshBinding
import com.example.statussaveraim.utils.SharedPrefsHelper
import kotlin.getValue

class SplashActivity : AppCompatActivity() {
    val binding: ActivitySpalshBinding by lazy {
        ActivitySpalshBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        simulateLoading()
        binding.btnContinue.setOnClickListener {
            val sharedPrefs = SharedPrefsHelper(this)
            if (sharedPrefs.isFirstTimeLaunch) {
                val intent = Intent(this, LanguageActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
    }

    private fun simulateLoading() {
        binding.progressBar.progress = 0
        binding.progressBar.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.GONE

        val handler = Handler(Looper.getMainLooper())
        var progressStatus = 0

        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post {
                    binding.progressBar.progress = progressStatus
                }
                try {
                    Thread.sleep(20) // Adjust speed here, 20ms * 100 = 2 seconds
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            handler.post {
                binding.progressBar.visibility = View.GONE
                binding.btnContinue.visibility = View.VISIBLE
            }
        }.start()
    }

}