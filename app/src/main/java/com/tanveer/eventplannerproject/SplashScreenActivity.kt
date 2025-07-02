package com.tanveer.eventplannerproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val sharedPreferences : SharedPreferences = getSharedPreferences(resources.getString(R.string.app_name),
//            Context.MODE_PRIVATE)
//        var themeFormat = sharedPreferences.getBoolean("themeFormat",false)
//        if (themeFormat){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, StartingScreen::class.java))
            finish()
        }, 2000)
    }

}