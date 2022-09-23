package com.tia_0653.mobilebanking

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("first_time", Context.MODE_PRIVATE)
        val isFirstTimeOpened = sharedPreferences.getBoolean("first_time", true)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        if(isFirstTimeOpened) {
            setContentView(R.layout.activity_splash_screen)
            sharedPreferences
                .edit()
                .putBoolean("first_time", false)
                .apply()

            Handler().postDelayed({
                val intent = Intent(this, loginView::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
        } else {
            val intent = Intent(this, loginView::class.java)
            startActivity(intent)
            finish()
        }

    }
}
