package com.happy.wallpapersapp

import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*


class SplashScreenActivity : AppCompatActivity() {

    private var animationView: LottieAnimationView? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground))
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        animationView = findViewById(R.id.animationView)
        setAnimation()
        Handler().postDelayed({
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fragment_open_enter,R.anim.fragment_close_exit)
            finish()
        },SPLASH_TIME_OUT.toLong())
    }

    fun setAnimation(){
        val c = Calendar.getInstance()
        var currentHour = c.get(Calendar.HOUR_OF_DAY)
        var animJson: String? = ""

        when (currentHour) {
            in 8..14 -> {
                animJson = "morning.json"
            }
            in 15..19 -> {
                animJson = "evening.json"
            }
            else -> {
                animJson = "night.json"
            }
        }

        animationView?.setAnimation(animJson)
        animationView?.playAnimation()

    }

    companion object {
        var SPLASH_TIME_OUT = 4000
    }
}